package no.java.submitit.app.pages

import org.apache.wicket.markup.html.WebPage
import no.java.submitit.model._
import no.java.submitit.app.State
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.markup.html.link._

class ConfirmPage extends LayoutPage {
  
  add(new ExternalLink("confirmUrl", "http://localhost:8040/submitit/lookupPresentation?id=1", "http://localhost:8040/submitit/lookupPresentation?id=1"))

}
