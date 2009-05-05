package no.java.submitit.app.pages

import common.LoggHandling

class ErrorPage(pres: model.Presentation, e: Exception) extends LayoutPage with LoggHandling {
  
  logger.error("Exception has occured", e)
  
  OfficialEmailLink.addLink(this)
  add(new org.apache.wicket.markup.html.basic.MultiLineLabel("pres", new org.apache.wicket.model.Model(pres)))
  
}
