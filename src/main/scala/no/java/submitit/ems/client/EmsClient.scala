package no.java.submitit.ems.client

import no.java.ems.client._
import no.java.ems.domain.{Event,Session,Person,EmailAddress}
import no.java.submitit.model._
import _root_.scala.collection.jcl.Conversions._

class EmsClient(eventName: String, emsService: RestEmsService) {
  
  private val event = findOrCreateEvent("JavaZone 2009", emsService.getEvents().toList)
  
  implicit def listToJavaList[T](l: Seq[T]) = 
    l.foldLeft(new _root_.java.util.ArrayList[T](l.size))((al, e) => {al.add(e); al})
  
  def savePresentation(presentation: Presentation) {
    presentation.speakers.foreach(speaker => 
      if (speaker.personId == null) findOrCreateContact(speaker))
    
    val session = convertToSession(presentation)
    session.setEventId(event.getId())
    emsService.saveSession(session)
    presentation.sessionId = session.getId()
  }
  
  private def findOrCreateContact(speaker: Speaker) {
    val person = findContactByEmail(speaker.email) match {
      case Some(person) => person
      case None => createContact(speaker)
    }
    speaker.personId = person.getId()
  }
  
  private def findContactByEmail(email: String): Option[Person] = {
    emsService.getContacts().find(contact => 
      contact.getEmailAddresses.exists(adr => 
        adr.getEmailAddress().equals(email)))
  }
  
  private def createContact(speaker: Speaker): Person = {
    val person = new Person(speaker.name)
    person.setDescription(speaker.bio)
    person.setEmailAddresses(new EmailAddress(speaker.email) :: Nil)
    emsService.saveContact(person)
    person
  }
  
  private def convertToSession(presentation: Presentation): Session = {
    val session = new Session(presentation.title)
    session.setId(presentation.sessionId)
    session.setLead(presentation.abstr)
    
    // TODO encode rest of presentation fields into body
    session.setBody(presentation.description)
    
    session.setSpeakers(presentation.speakers.map(speaker => convertSpeaker(speaker)))
    
    val language = presentation.language match {
      case Language.Norwegian => new no.java.ems.domain.Language("no")
      case Language.English => new no.java.ems.domain.Language("en")
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
  
  private def convertSpeaker(speaker: Speaker): no.java.ems.domain.Speaker = {
    val result = new no.java.ems.domain.Speaker(speaker.personId, speaker.name)
    result.setDescription(speaker.bio)
    // TODO set picture
    result
  }
  
  private def findOrCreateEvent(name: String, events: List[Event]): Event = {
    events match {
      case event :: events => 
        if (event.getName() == name) event else findOrCreateEvent(name, events)
      case Nil => createEvent(name)
    }
  }
  
  private def createEvent(name: String): Event = {
    val event = new Event()
    event.setName(name)
    emsService.saveEvent(event)
    event
  }
}