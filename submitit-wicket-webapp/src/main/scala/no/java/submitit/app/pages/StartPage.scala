package no.java.submitit.app.pages

import org.apache.wicket.markup.html.basic._

class StartPage extends LayoutPage {
  
  if (SubmititApp.boolSetting("submitAllowed")) {
    
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
