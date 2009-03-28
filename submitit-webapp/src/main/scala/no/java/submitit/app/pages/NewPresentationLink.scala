package no.java.submitit.app.pages

import org.apache.wicket.markup.html.link._
import no.java.submitit.model._

class NewPresentationLink(id: String) extends Link(id) {
  
  override def onClick {
    val state = State.get
    setResponsePage(new SubmitPage(state.createNewPresentation))
  }
  
}