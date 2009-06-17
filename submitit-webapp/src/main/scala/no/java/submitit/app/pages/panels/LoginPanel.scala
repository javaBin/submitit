package no.java.submitit.app.pages.panels

import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.panel._
import org.apache.wicket.model._

class LoginPanel(id: String, handler: LoginHandler) extends Panel(id) {
  
  override def isVersioned = false
  
  add(new FeedbackPanel("feedback"))

  add(new Form("loginForm") {
    
    override def isVersioned = false

    val passModel = new Model
    add(new PasswordTextField("passPhrase", passModel))
    
    override def onSubmit {
      handler.onLogin(passModel.getObject.asInstanceOf[String])
    }
  })
  
}
