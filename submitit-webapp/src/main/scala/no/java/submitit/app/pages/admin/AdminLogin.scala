package no.java.submitit.app.pages.admin

import org.apache.wicket.markup.html.form._
import org.apache.wicket.model._

class AdminLogin extends LayoutPage {

  add(new LoginPanel("login", { passPhrase =>
    val authenticated = SubmititApp.authenticates(passPhrase)
    if (authenticated) {
      setResponsePage(new PropertyModificationPage(true))
    }
  }))

}
