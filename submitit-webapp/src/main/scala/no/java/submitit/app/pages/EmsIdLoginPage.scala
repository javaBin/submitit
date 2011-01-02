package no.java.submitit.app.pages

import panels.LoginPanel
import no.java.submitit.config.Keys.pwdForEmsIdPage
import no.java.submitit.app. {State, SubmititApp}

class EmsIdLoginPage extends LayoutPage {

	contentBorder.add(new LoginPanel("login", new LoginHandler {
    def onLogin(pwd: String) {
      if (SubmititApp.getSetting(pwdForEmsIdPage).map(_.equals(pwd)).getOrElse(false)) {
        State().emsIdAuthorized = true
        continueToOriginalDestination()
      }
      else {
        warn("Incorrect password")
      }
    }
  }))
}