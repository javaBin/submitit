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
import common.Implicits._
import collection.jcl.Conversions._
import org.apache.wicket.markup.html.panel.FeedbackPanel
import _root_.java.io.Serializable
import DefaultConfigValues._
import collection.mutable.LinkedHashMap

class PropertyModificationPage(it: Boolean) extends LayoutPage {
  
  class Element(var key: String, var value: String) extends Serializable {
    override def toString = key + " " + value
  }
  
  def createForm: Form= {
    new Form("propertyForm") {
      val props = SubmititApp.props
      val filteredProps = props.filter{case(key, _) => key.visible}
      var list: List[Element] = Nil

      val listView = new ListView("props", filteredProps.toList) {
        override def populateItem(item: ListItem) {
          val (key, value) = item.getModelObject.asInstanceOf[(ConfigKey, String)]
          val element = new Element(key.toString, value)
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
          	case ex => e + (key.name -> ex.getMessage)
          }
        }}
        
        if(errors.isEmpty) {
          // Must use this strange thing to make the new Map. Hopefully 2.8 with more consistent collections will fix this
          val newProps: collection.Map[ConfigKey, String] = new LinkedHashMap[ConfigKey, String]() ++ props ++ newValues
        	SubmititApp.props = newProps
        	info("Updated and stored new properties")
        	PropertyModificationPage.this.replace(createForm)
         }
        else {
          errors.foreach { el =>
          	error(el._1 + " gave parse errors " + el._2)
          }
        }
      }
    }
  }
  
  add(createForm)  
  add(new FeedbackPanel("feedback"))
  
}


