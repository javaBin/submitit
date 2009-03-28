package no.java.submitit.app.pages

import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.panel._
import org.apache.wicket.model._

class LoginPanel(id: String, afterSubmit: String => Unit) extends Panel(id) {
  
  override def isVersioned = false
  
  add(new FeedbackPanel("feedback"))

  add(new Form("loginForm") {
    
    override def isVersioned = false

    val passModel = new Model
    add(new PasswordTextField("passPhrase", passModel))
    
    override def onSubmit {
      afterSubmit(passModel.getObject.asInstanceOf[String])
    }
  })
  
}
