package no.java.submitit.app.pages

import org.apache.wicket.markup.html.basic._

class StartPage extends LayoutPage {
  
  if (SubmititApp.boolSetting("submitAllowedBoolean")) {
    
    if(State().isNew) {
      setResponsePage(new SubmitPage(State().currentPresentation))
    }
    else {
      setResponsePage(new ReviewPage(State().currentPresentation))
    }
  }
  else {
    val res = new Label("info", SubmititApp.getSetting("submitNotAllowedHtml"))
    res.setEscapeModelStrings(false)
    add(res)
  }

}
