package no.java.submitit.app.pages

import org.apache.wicket.markup.html.WebPage
import no.java.submitit.common._
import no.java.submitit.model._
import no.java.submitit.app.State
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model.Model

class IdResolverPage extends LayoutPage with LoggHandling {
  
  val id = getRequest.getParameter("id")

  val (presentation, infoMessage) =
    try {
      if(id == null || id == "") (None, "Not a valid url. This has been logged.")
      else State().backendClient.loadPresentation(id) match {
        case Some(pres) => (Some(pres), "")
        case None => (None, "Not a valid key! Attempt to fetch a presentation with this key has been logged.")
      }
    }
    catch {
      case x => {
        logger.warn("Got exception when trying to retrieve session with id: '" + id + "' from EMS", x)
        (None, "Got an exception while trying to get your presentation. This may be because of maintenance. " +
               "Please try again later.\n" +
               "You should not worry, we have backup of your presentation in several different forms." +
               "\nIf the problem continues you should notify the programme committee at " + SubmititApp.getOfficialEmail + ".")
    }
  }

  presentation match {
    case Some(pres) => {
      State().fromServer = true
      State().currentPresentation = pres
      setResponsePage(new ReviewPage(pres))
    }
    case None => {
      add(new Label("identified", new Model(infoMessage)))
    }
  }

}
