package no.java.submitit.app.pages

import org.apache.wicket.markup.html.link.ExternalLink

class HelpPage extends LayoutPage {

  add(new ExternalLink("officialEmail", "mailto:" + SubmititApp.getOfficialEmail, SubmititApp.getOfficialEmail))
  
}
