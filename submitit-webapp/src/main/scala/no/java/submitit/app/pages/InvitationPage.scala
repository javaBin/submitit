package no.java.submitit.app.pages

import org.apache.wicket.markup.html.form._
import org.apache.wicket.model._
import no.java.submitit.model.Presentation

class InvitationPage extends LayoutPage {
  
  setVersioned(false)
  
   add(new LoginPanel("login", handleLogin))
   
   def handleLogin(passPhrase: String) {
     if(passPhrase != SubmititApp.getSetting("passPhraseSubmitSpecualURL")) {
       error("Incorrect password")
     }
     else {
       State().verifiedWithCaptha = true
       setResponsePage(new SubmitPage(State().createNewPresentation, true))
     }
   }

}
