/*
 * Copyright 2009 javaBin
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package no.java.submitit.app.pages

import borders.ContentBorder
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.link._
import no.java.submitit.common._
import no.java.submitit.model._
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.markup.html.link._
import org.apache.wicket.protocol.http.servlet.ServletWebRequest
import _root_.java.util.{Date,Properties}
import javax.servlet.http.HttpServletRequest
import javax.mail.{Message,Session,Transport}
import javax.mail.internet.MimeMessage
import org.apache.wicket.markup.html.form.HiddenField
import no.java.submitit.app.DefaultConfigValues._
import no.java.submitit.app.{SubmititApp, State}

class ConfirmPage(pres: Presentation) extends LayoutPage with LoggHandling {

  val isTest = pres.testPresentation
  
  val presentation = pres.toString
  
  if(!isTest) logger.info(presentation)
  
  add(new HiddenField("showNewLink") {
	  override def isVisible = State().invitation || State().submitAllowed
  })

  add(new NewPresentationLink("newPresentation"))

  add(new Link("toJavaZone"){
    override def onClick {
      setRedirect(false)
      getResponse.redirect("http://www.javazone.no")
      State().invalidateNow
    }
  })
  
  val contentBorder = new ContentBorder("contentBorder")
  add(contentBorder)

  contentBorder.add(new Label("extraInfo", if(isTest) "Test submission, has not been submitted and no email has been sent" else ""))
  
  contentBorder.add(new MultiLineLabel("pres", presentation))

  val url = {
    val backendClient = State().backendClient
    val uniqueId = if(!isTest) backendClient.savePresentation(pres) else Presentation.testPresentationURL
    pres.sessionId = uniqueId
    SubmititApp.getSetting(submititBaseUrl).get + "/lookupPresentation?id=" + uniqueId
  }
  
  contentBorder.add(new ExternalLink("confirmUrl", url, url))
  

  if (!isTest && SubmititApp.getSetting(smtpHost).isDefined) {
    val props = new Properties
    props.put("mail.smtp.host", SubmititApp.getSetting(smtpHost).get)
    
    props.put("mail.from", SubmititApp.getOfficialEmail)
    val session = Session.getInstance(props, null)
    
    val msg = new MimeMessage(session)
    msg.setFrom
    pres.speakers.foreach(speaker => 
      msg.addRecipients(Message.RecipientType.TO, speaker.email)
    )
    
    val (greetmessage, subjectmessage) = if(pres.isNew) ("submitting", "of") else ("updating", "on updating")
    
    SubmititApp.getBccEmailList.foreach(msg.addRecipients(Message.RecipientType.BCC, _))
    msg.setSubject("Confirmation " + subjectmessage + " your JavaZone 2009 submission \"" + pres.title + "\"")
    msg.setSentDate(new Date)
    msg.setText("Thank you for " + greetmessage + " your presentation titled \"" + pres.title + "\".\n\n" +
                "You can access the submitted presentation at " + url + "\n\n\n" +
                "Best regards,\n\n" +
                "JavaZone Programme Committee\n" +
                SubmititApp.getOfficialEmail + "\n\n\n\n" +
                "The following has been registered:\n\n" + pres)
    
    Transport.send(msg)
  }
  
}
