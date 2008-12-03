package no.java.submitit.ems

import no.java.ems._
import no.java.ems.domain.{Event,Session,Person,EmailAddress}
import _root_.scala.collection.jcl.Conversions._
import common.Implicits._
import model._

class EmsConverter {

  def toPerson(speaker: Speaker): Person = {
    val person = new Person(speaker.name)
    person.setDescription(speaker.bio)
    person.setEmailAddresses(new EmailAddress(speaker.email) :: Nil)
    person
  }
  
  def toSession(presentation: Presentation): Session = {
    val session = new Session(presentation.title)
    session.setId(presentation.sessionId)
    session.setBody(presentation.abstr)

    session.setNotes(encodeSessionData(presentation.outline,
                                       presentation.equipment, 
                                       presentation.requiredExperience, 
                                       presentation.expectedAudience))
    
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
    session
  }
  
  def toPresentation(session: Session): Presentation = {
    val pres = new Presentation()
    pres.title = session.getTitle
    pres.abstr = session.getBody
    pres.speakers = session.getSpeakers.toList.map(speaker => fromEmsSpeaker(speaker))
    
    val decodeSessionData(outline, equipment, experience, audience) = session.getNotes
    pres.outline = outline
    pres.equipment = equipment
    pres.requiredExperience = experience
    pres.expectedAudience = audience
    
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
    // TODO set picture
    result
  }
  
  def fromEmsSpeaker(speaker: no.java.ems.domain.Speaker): Speaker = {
    val result = new Speaker
    result.personId = speaker.getPersonId
    result.name = speaker.getName
    result.bio = speaker.getDescription
    result
  }

  val decodeSessionData = """(?sm)(?:^h2\. Outline
\s*(.*?))?\s*(?:^h2\. Equipment
\s*(.*?))?\s*(?:^h2\. Required experience
\s*(.*?))?\s*(?:^h2\. Expected audience
\s*(.*?))?""".r

  def encodeSessionData(outline: String, equipment: String, experience: String, audience: String): String = 
    "h2. Outline\n\n" + outline + "\n\n" +
    "h2. Equipment\n\n" + equipment + "\n\n" +
    "h2. Required experience\n\n" + experience + "\n\n" +
    "h2. Expected audience\n\n" + audience
    
}
