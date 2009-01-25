package no.java.submitit.app.pages

import org.apache.wicket.markup.html.link._
import no.java.submitit.model._

class NewPresentationLink(id: String) extends Link(id) {
  
  override def onClick {
    val state = State.get
  
    // Reset state in case of a new registration, but preserve speakers
    val p = new Presentation
    p.speakers = state.currentPresentation.speakers
    state.currentPresentation = p
    state.fromServer = false
    setResponsePage(new SubmitPage(p))
  }
  
}
