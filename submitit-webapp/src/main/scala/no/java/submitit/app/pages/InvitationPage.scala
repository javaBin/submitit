package no.java.submitit.app.pages

import org.apache.wicket.markup.html.form._
import org.apache.wicket.model._
import no.java.submitit.model.Presentation

class InvitationPage extends LayoutPage {
  
  add(new LoginPanel("login", new LoginHandler {
    
    def onLogin(pwd: String) {
      if(pwd != SubmititApp.getSetting("passPhraseSubmitSpecualURL")) {
        error("Incorrect password")
      }	
      else {
        State().verifiedWithCaptha = true
        setResponsePage(new SubmitPage(State().createNewPresentation, true))
      }
       
    }
    
  }))
  

}
