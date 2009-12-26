/*
 * Copyright 2009 javaBin
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package no.java.submitit.app.pages.admin

import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model._
import org.apache.wicket.markup.html.list._
import org.apache.wicket.markup.html.link._
import no.java.submitit.common.Implicits._
import no.java.submitit.model._
import scala.collection.jcl.Conversions._
import org.apache.wicket.markup.html.panel.FeedbackPanel
import _root_.java.io.Serializable
import collection.mutable.LinkedHashMap
import no.java.submitit.app.pages.{ReviewPage, LayoutPage}
import no.java.submitit.app.{SubmititApp, State, DefaultConfigValues}
import no.java.submitit.app.DefaultConfigValues.ConfigKey
import no.java.submitit.app.Functions._
class PropertyModificationPage(it: Boolean) extends LayoutPage {
  
  class Element(var key: String, var value: String) extends Serializable {
    override def toString = key + " " + value
  }
  
  val whenClicked = (s: Status.Value) => {
    val p = new Presentation {
      
    	override val testPresentation = true
      title = "Test presentation with actual status " + s.toString
    	abstr = "This is the abstract. This presentation will not be submitted! You should use this to test that the admin settings are correct"
    	summary = "This is the summary. This presentation will not be submitted! You should use this to test that the admin settings are correct"
      status = s
      timeslot = "10:00 - 11:00"
      room = "Lab 1"
      feedback = "This is bogus feedback on a test presentation"
      val speaker = new Speaker {
    		name = "Donald Duck"
        email = "somethingbogus@java.no"
        bio = "Quack quack"
      }
    	speakers = speaker :: Nil
    	setResponsePage(new ReviewPage(this, true))
    }
    State().currentPresentation = p
  }
  
  add(new Link("approvedLink") {
  	def onClick{whenClicked(Status.Approved)}
  })
  add(new Link("pendingLink") {
  	def onClick{whenClicked(Status.Pending)}
  })
  add(new Link("rejectedLink") {
  	def onClick{whenClicked(Status.NotApproved)}
  })
  
  
  def createForm(props: collection.Map[ConfigKey, Option[String]]): Form= {
    new Form("propertyForm") {
      val filteredProps = props.filter{case(key, _) => key.visible}
      var list: List[Element] = Nil

      val listView = new ListView("props", filteredProps.toList) {
        override def populateItem(item: ListItem) {
          val (key, value) = item.getModelObject.asInstanceOf[(ConfigKey, Option[String])]
          val element = new Element(key.toString, value.getOrElse(""))
          val field = new TextField("value", new PropertyModel(element, "value"))
          if(!key.editable) field.setEnabled(false)

          item.add(new Label("key", new PropertyModel(element, "key")))
          item.add(new Label("description", new Model(key.description)))
          item.add(field)
          
          list = element :: list
        }
      }
      add(listView)
    
      override def onSubmit {
        val newValues: Map[ConfigKey, String] = list.foldLeft(Map[ConfigKey, String]())((m, elem) => m + (DefaultConfigValues.key(elem.key) -> elem.value))
        
        val errors = newValues.foldLeft(LinkedHashMap[String, String]()){
          case (e, (key, value)) => { 
          try {
          	key.parser(value)
          	e
          } catch {
          	case ex => e + (key.toString -> ex.getMessage)
          }
        }}
        
      val parsed: Map[ConfigKey, Option[String]] = list.foldLeft(Map[ConfigKey, Option[String]]())((m, elem) => m + (DefaultConfigValues.key(elem.key) -> stringToOption(elem.value)))
      // Must use this strange thing to make the new Map. Hopefully 2.8 with more consistent collections will fix this
      val newProps: collection.Map[ConfigKey, Option[String]] = new LinkedHashMap[ConfigKey, Option[String]]() ++ props ++ parsed

      if(errors.isEmpty) {
        	SubmititApp.props = newProps
        	info("Updated and stored new properties")
         }
        else {
          errors.foreach { el =>
          	error(el._1 + " gave parse errors " + el._2)
          }
        }
        
      	PropertyModificationPage.this.replace(createForm(newProps))
      }
    }
  }
  
  add(createForm(SubmititApp.props))  
  add(new FeedbackPanel("feedback"))
  
}


