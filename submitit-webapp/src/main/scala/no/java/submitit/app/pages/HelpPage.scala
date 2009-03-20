package no.java.submitit.app.pages

import org.apache.wicket.markup.html.link.ExternalLink

class HelpPage extends LayoutPage {

  OfficialEmailLink.addLink(this)
  
}

object OfficialEmailLink {
  
  def addLink(page: org.apache.wicket.Page) {
    page.add(new ExternalLink("officialEmail", "mailto:" + SubmititApp.getOfficialEmail, SubmititApp.getOfficialEmail))
  }
  
}
