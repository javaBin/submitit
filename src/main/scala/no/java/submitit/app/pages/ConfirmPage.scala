package no.java.submitit.app.pages

import org.apache.wicket.markup.html.WebPage
import no.java.submitit.model._
import no.java.submitit.app.State
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.markup.html.link._
import org.apache.wicket.protocol.http.servlet.ServletWebRequest
import javax.servlet.http.HttpServletRequest

class ConfirmPage extends LayoutPage {

  // TODO actually post
  val request = getRequest.asInstanceOf[ServletWebRequest].getHttpServletRequest
  val url = "http://" + request.getServerName + ":" + request.getServerPort + request.getContextPath + "/"
  val uniqueId = 10
  add(new ExternalLink("confirmUrl", url + "lookupPresentation?id=" + uniqueId, url + "lookupPresentation?id=" + uniqueId))

}
