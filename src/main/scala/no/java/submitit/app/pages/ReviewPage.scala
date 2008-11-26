package no.java.submitit.app.pages

import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model._
import _root_.no.java.submitit.model._

class ReviewPage(p: Presentation) extends LayoutPage {

      add(new Label("title", p.title))
      add(new Label("theabstract", p.abstr))
  
}
