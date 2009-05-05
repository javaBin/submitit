package no.java.submitit.app.pages

import org.apache.wicket.markup.html.WebPage
import no.java.submitit.common._
import no.java.submitit.model._
import no.java.submitit.app.State
import org.apache.wicket.markup.html.basic._

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class IdResolverPage extends LayoutPage {
  
  def logger = LoggerFactory.getLogger(classOf[IdResolverPage])

  val id = getRequest.getParameter("id")

  val presentation: Presentation =
    try {
      val backendClient = State().backendClient
      backendClient.loadPresentation(id)
    }
  catch {
    case x => {
      logger.warn("Got exception when trying to retrieve session with id: '" + id + "' from EMS", x)
      null
    }
    
  }
  
  if(presentation != null) {
    State()fromServer = true 
    State().currentPresentation = presentation
  }
  
  val (text, doRedirect) = if(id == null) ("you must supply and id", false) else if (presentation == null) ("not a valid key", false) else ("Redirecting", true) 
  add(new Label("identified", text))
  if(doRedirect) setResponsePage(new ReviewPage(presentation))
  
}
