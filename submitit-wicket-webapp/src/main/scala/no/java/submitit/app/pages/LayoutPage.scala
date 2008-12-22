package no.java.submitit.app.pages

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label

class LayoutPage extends WebPage {
  
  add(new Label("headerText", SubmititApp.getSetting("headerText")))

}
