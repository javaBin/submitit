package no.java.submitit.app.pages.admin

import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model._
import org.apache.wicket.markup.html.list._
import common.Implicits._
import scala.collection.jcl.Conversions._
import org.apache.wicket.markup.html.panel.FeedbackPanel

class PropertyModificationPage(it: Boolean) extends LayoutPage {
  
  class Element(var key: String, var value: String) {
    override def toString = key + " " + value
  }
  
  def createForm: Form= {
    new Form("propertyForm") {
      val props = SubmititApp.props
        
      var list = List[Element]()
      val listView = new ListView("props", props.toList) {
        override def populateItem(item: ListItem) {
          val values = item.getModelObject.asInstanceOf[(String, String)]
          val element = new Element(values._1, values._2)
          item.add(new Label("key", new PropertyModel(element, "key")))
          item.add(new TextArea("value", new PropertyModel(element, "value")))
          list = element :: list
        }
      }
      add(listView)
    
      override def onSubmit {
        val newValues = list.foldRight(Map[String, String]())((e, m) => m + (e.key -> e.value))
        SubmititApp.props = newValues
        info("Updated")
        PropertyModificationPage.this.replace(createForm)
      }
    }
  }
  
  add(createForm)  
  add(new FeedbackPanel("feedback"))
  
}


