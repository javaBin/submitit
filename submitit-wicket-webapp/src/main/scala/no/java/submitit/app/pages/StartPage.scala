package no.java.submitit.app.pages

import org.apache.wicket.markup.html.basic._

class StartPage extends LayoutPage {
  
  val submitAllowed = SubmititApp.getSetting("submitAllowed")
  if (_root_.java.lang.Boolean.parseBoolean(submitAllowed)) {
    
    if(State.get.lockPresentation) {
      setResponsePage(classOf[ReviewPage])
    }
    else {
      setResponsePage(classOf[SubmitPage])
    }
  }
  else {
    add(new Label("info", SubmititApp.getSetting("submitNotAllowedMsg")))
  }

}
