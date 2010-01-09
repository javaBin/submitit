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

package no.java.submitit.ems

import no.java.ems._
import no.java.ems.domain.{Event,Session,Person,Speaker => EmsSpeaker,EmailAddress,Binary => EmsBinary,ByteArrayBinary}
import _root_.java.io.InputStream
import _root_.scala.collection.jcl.Conversions._
import no.java.submitit.common.Implicits._
import no.java.submitit.common.{IOUtils, LoggHandling}
import no.java.submitit.model._
import no.java.submitit.common.IOUtils._
import org.joda.time.Interval
import org.joda.time.format.DateTimeFormat

import scala.xml.XML

class EmsConverter extends LoggHandling {
  
  private def getStatus(state: Session.State) = state match {
      case Session.State.Approved => Status.Approved
      case Session.State.Pending => Status.Pending
      case Session.State.Rejected => Status.NotApproved
      case l => unknownEnumValue(l, Status.Pending)
  }
  
  def toPerson(speaker: Speaker): Person = {
    val person = new Person(speaker.name)
    person.setDescription(speaker.bio)
    person.setEmailAddresses(new EmailAddress(speaker.email) :: Nil)
    person
  }
  
  def updateSession(presentation: Presentation, session: Session) {
    session.setTitle(presentation.title)
    session.setLead(presentation.summary)
    session.setBody(presentation.abstr)

    session.setOutline(presentation.outline)
    session.setEquipment(presentation.equipment)
    session.setExpectedAudience(presentation.expectedAudience)
    session.setFeedback(presentation.feedback)
    session.setKeywords(presentation.keywords)
    
    session.setSpeakers(presentation.speakers.map(speaker => toEmsSpeaker(speaker)))
    
    val language = presentation.language match {
      case Language.Norwegian => new domain.Language("no")
      case Language.English => new domain.Language("en")
      case l => unknownEnumValue(l, new domain.Language("no"))
    }
    val level = presentation.level match {
      case Level.Beginner => Session.Level.Introductory
      case Level.Intermediate => Session.Level.Intermediate
      case Level.Advanced => Session.Level.Advanced
      case l => unknownEnumValue(l, Session.Level.Introductory)
    }
    val format = presentation.format match {
      case PresentationFormat.Presentation => Session.Format.Presentation
      case PresentationFormat.LightningTalk => Session.Format.Quickie
      case l => unknownEnumValue(l, Session.Format.Presentation)
    }
    
    session.setLanguage(language)
    session.setLevel(level)
    session.setFormat(format)

    val attachments = presentation.slideset.toList ::: presentation.pdfSlideset.toList
    session.setAttachements(attachments.map(toEmsBinary))
  }
  
  def toPresentation(session: Session): Presentation = {
    val pres = new Presentation()
    pres.sessionId = session.getId
    pres.title = session.getTitle
    pres.abstr = session.getBody
    pres.speakers = session.getSpeakers.toList.map(speaker => fromEmsSpeaker(speaker))
    
    pres.summary = session.getLead
    pres.outline = session.getOutline
    pres.equipment = session.getEquipment
    pres.expectedAudience = session.getExpectedAudience
    pres.feedback = session.getFeedback
    pres.keywords = session.getKeywords.toList
    pres.room = if (session.getRoom != null) session.getRoom.getName else null
    pres.timeslot = if (session.getTimeslot != null) formatInterval(session.getTimeslot) else null
    
    def formatInterval(interval: Interval) = {
    	val dateFormatter = DateTimeFormat.shortDate();
    	val timeFormatter = DateTimeFormat.shortTime();
    	dateFormatter.print(interval.getStart()) + " " + timeFormatter.print(interval.getStart()) + " - " + timeFormatter.print(interval.getEnd());    
    }
    
    pres.language = session.getLanguage.getIsoCode match {
      case "no" => Language.Norwegian
      case "en" => Language.English
      case l => unknownEnumValue(l, Language.Norwegian)
    }
    pres.level = session.getLevel match {
      case Session.Level.Introductory => Level.Beginner
      case Session.Level.Intermediate => Level.Intermediate
      case Session.Level.Advanced => Level.Advanced
      case l => unknownEnumValue(l, Level.Intermediate)
    }
    pres.format = session.getFormat match {
      case Session.Format.Presentation => PresentationFormat.Presentation
      case Session.Format.Quickie => PresentationFormat.LightningTalk
      case l => unknownEnumValue(l, PresentationFormat.Presentation)
    }
    pres.status = getStatus(session.getState)
    
    val attachments = session.getAttachements.toList.map(toBinary(_, false))
    attachments.foreach(_ match {
      case Some(b) if b.name.toLowerCase.endsWith(".pdf") => pres.pdfSlideset = Some(b)
      case Some(b) => pres.slideset = Some(b)
      case None =>
    })
    pres
  }
  
  def toEmsSpeaker(speaker: Speaker): EmsSpeaker = {
    val result = new EmsSpeaker(speaker.personId, speaker.name)
    result.setDescription(speaker.bio)
    if (speaker.picture.isDefined) result.setPhoto(toEmsBinary(speaker.picture.get))
    result
  }
  
  def fromEmsSpeaker(speaker: EmsSpeaker): Speaker = {
    val result = new Speaker
    result.personId = speaker.getPersonId
    result.name = speaker.getName
    result.bio = speaker.getDescription
    result.picture = toBinary(speaker.getPhoto, true)
    result
  }

  def toEmsBinary(binary: Binary): EmsBinary = {
    // EMS requires a non-empty array here.
    val content = if(binary.isNew) binary.content else new Array[Byte](0)
    new ByteArrayBinary(binary.id, binary.name, binary.contentType, content)
  }
  
  def toBinary(binary: EmsBinary, fetchData: Boolean): Option[Binary] = {
    if (binary != null) {
    	var content: Option[(InputStream)] = None
      
      if(fetchData) {
        content = Some(binary.getDataStream)
      }
      Some(Binary(binary.getId, binary.getFileName, binary.getMimeType, content))
    } else {
      None
    }
  }

  private def unknownEnumValue[T](v: Any, r: T): T = {
    logger.error("Unknown enum value '" + v + "', defaulting to '" + r + "'")  
    r
  }

}

