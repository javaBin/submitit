package no.java.submitit.app.pages

import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model._
import _root_.no.java.submitit.model._
import no.java.submitit.app.State
import org.apache.wicket.markup.html.link._

class ReviewPage extends LayoutPage {
  
  val p = State.get.currentPresentation
  add(new Label("title", p.title))
  add(new Label("theabstract", p.abstr))
  add(new PageLink("submitLink", classOf[ConfirmPage]))
  
}
