package no.java.submitit.ems

import no.java.ems._
import no.java.ems.domain.{Event,Session,Person,EmailAddress,Binary,ByteArrayBinary}
import _root_.java.io.InputStream
import _root_.scala.collection.jcl.Conversions._
import common.Implicits._
import model._

import scala.xml.XML

class EmsConverter {

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
    
    session.setSpeakers(presentation.speakers.map(speaker => toEmsSpeaker(speaker)))
    
    val language = presentation.language match {
      case Language.Norwegian => new domain.Language("no")
      case Language.English => new domain.Language("en")
    }
    val level = presentation.level match {
      case Level.Beginner => Session.Level.Introductory
      case Level.Intermediate => Session.Level.Intermediate
      case Level.Advanced => Session.Level.Advanced
    }
    val format = presentation.format match {
      case PresentationFormat.Presentation => Session.Format.Presentation
      case PresentationFormat.LightningTalk => Session.Format.Quickie
    }
    session.setLanguage(language)
    session.setLevel(level)
    session.setFormat(format)
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
    
    pres.language = session.getLanguage.getIsoCode match {
      case "no" => Language.Norwegian
      case "en" => Language.English
    }
    pres.level = session.getLevel match {
      case Session.Level.Introductory => Level.Beginner
      case Session.Level.Intermediate => Level.Intermediate
      case Session.Level.Advanced => Level.Advanced
    }
    pres.format = session.getFormat match {
      case Session.Format.Presentation => PresentationFormat.Presentation
      case Session.Format.Quickie => PresentationFormat.LightningTalk
      case _ => error("Unhandled session format: " + session.getFormat)
    }
    pres
  }
  
  def toEmsSpeaker(speaker: Speaker): no.java.ems.domain.Speaker = {
    val result = new no.java.ems.domain.Speaker(speaker.personId, speaker.name)
    result.setDescription(speaker.bio)
    result.setPhoto(toPhoto(speaker.picture))
    result
  }
  
  def fromEmsSpeaker(speaker: no.java.ems.domain.Speaker): Speaker = {
    val result = new Speaker
    result.personId = speaker.getPersonId
    result.name = speaker.getName
    result.bio = speaker.getDescription
    result.picture = toPicture(speaker.getPhoto)
    result
  }

  def toPhoto(picture: Picture): Binary = {
    if (picture != null) {
      new ByteArrayBinary(picture.id, picture.name, picture.contentType, picture.content)
    } else {
      null
    }
  }
  
  def toPicture(photo: Binary): Picture = {
    if (photo != null) {
      val content = new Array[Byte](photo.getSize.toInt)
      read(0, photo.getDataStream, content)
      new Picture(photo.getId, content, photo.getFileName, photo.getMimeType)
    } else {
      null
    }
  }

  def read(ofs: Int, is: InputStream, content: Array[Byte]): Unit = {
    val len = is.read(content, ofs, content.length - ofs)
    if (len != -1)
      read(ofs + len, is, content)
  }
}

