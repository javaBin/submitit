package no.java.submitit.app.pages

import org.apache.wicket.markup.html.form._
import org.apache.wicket.model._
import _root_.no.java.submitit.model._

class SubmitPage extends LayoutPage {
  
  val pres = new Presentation
  
  add(new InputForm)
  
  
  class InputForm extends Form("inputForm") {
    
    add(new TextField("title",  new PropertyModel(pres, "title")))
    
    	override def onSubmit() {
    	  println(pres.title)
		}
    
  }
  
}
