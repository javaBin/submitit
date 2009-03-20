package no.java.submitit.app.pages

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ErrorPage(pres: model.Presentation, e: Exception) extends LayoutPage {
  
  def logger = LoggerFactory.getLogger(classOf[ConfirmPage])
  
  logger.error("Exception has occured", e)
  
  OfficialEmailLink.addLink(this)
  add(new org.apache.wicket.markup.html.basic.MultiLineLabel("pres", new org.apache.wicket.model.Model(pres)))
  
}
