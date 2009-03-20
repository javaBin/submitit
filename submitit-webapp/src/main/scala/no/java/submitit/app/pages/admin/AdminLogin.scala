package no.java.submitit.app.pages.admin

import org.apache.wicket.markup.html.form._
import org.apache.wicket.model._

class AdminLogin extends LayoutPage {
  
  add(new Form("loginForm") {
    
    val passModel = new Model
    add(new PasswordTextField("passPhrase", passModel))
    
    override def onSubmit {
      val authenticated = SubmititApp.authenticates(passModel.getObject)
      if (authenticated) {
        setResponsePage(new PropertyModificationPage(true))
      }
    }
  })
  

}
