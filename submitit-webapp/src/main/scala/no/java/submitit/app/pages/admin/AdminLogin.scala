package no.java.submitit.app.pages.admin

import org.apache.wicket.markup.html.form._
import org.apache.wicket.model._

class AdminLogin extends LayoutPage {

  add(new panels.LoginPanel("login", new LoginHandler {
    def onLogin(pwd: String) {
      val authenticated = SubmititApp.authenticates(pwd)
      if (authenticated) {
        setResponsePage(new PropertyModificationPage(true))
      }
    }
  }))

}
