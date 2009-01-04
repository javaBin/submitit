package no.java.submitit.ems

import no.java.ems._
import no.java.ems.domain.{Event,Session,Person,EmailAddress,Binary,ByteArrayBinary}
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
    session.setBody(presentation.abstr)

    def notes = <notes>
    <summary>{presentation.summary}</summary>
    <outline>{presentation.outline}</outline>
    <equipment>{presentation.equipment}</equipment>
    <audience>{presentation.expectedAudience}</audience>
    <feedback>{presentation.feedback}</feedback>
    </notes>
    
    session.setNotes(notes.toString)
    
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
    session.setLanguage(language)
    session.setLevel(level)
  }
  
  def toPresentation(session: Session): Presentation = {
    val pres = new Presentation()
    pres.sessionId = session.getId
    pres.title = session.getTitle
    pres.abstr = session.getBody
    pres.speakers = session.getSpeakers.toList.map(speaker => fromEmsSpeaker(speaker))
    
    /* Handle any text before and after XML in notes-field */
    val notes = XML.loadString("<wrap>" + session.getNotes + "</wrap>") \ "notes"
    
    pres.summary = (notes \ "summary").text
    pres.outline = (notes \ "outline").text
    pres.equipment = (notes \ "equipment").text
    pres.expectedAudience = (notes \ "audience").text
    pres.feedback = (notes \ "feedback").text
    
    pres.language = session.getLanguage.getIsoCode match {
      case "no" => Language.Norwegian
      case "en" => Language.English
    }
    pres.level = session.getLevel match {
      case Session.Level.Introductory => Level.Beginner
      case Session.Level.Intermediate => Level.Intermediate
      case Session.Level.Advanced => Level.Advanced
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
      photo.getDataStream.read(content)
      new Picture(photo.getId, content, photo.getFileName, photo.getMimeType)
    } else {
      null
    }
  }
  
}
