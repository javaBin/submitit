package no.java.submitit.app.pages

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.link._
import no.java.submitit.model._
import no.java.submitit.app.State
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.markup.html.link._
import org.apache.wicket.protocol.http.servlet.ServletWebRequest
import javax.servlet.http.HttpServletRequest

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConfirmPage extends LayoutPage {
  
  val state = State.get
  state.lockPresentation = true

  val pres = state.currentPresentation
  val logger = LoggerFactory.getLogger(classOf[ConfirmPage])
  
  val presentation = pres.toString
  logger.info(presentation)
  
  add(new MultiLineLabel("pres", presentation))
  
  // TODO actually post
  val request = getRequest.asInstanceOf[ServletWebRequest].getHttpServletRequest
  val url = "http://" + request.getServerName + ":" + request.getServerPort + request.getContextPath + "/"
  val uniqueId = 10
  add(new ExternalLink("confirmUrl", url + "lookupPresentation?id=" + uniqueId, url + "lookupPresentation?id=" + uniqueId))
  
  add(new NewPresentationLink("newPresentation"))

}
