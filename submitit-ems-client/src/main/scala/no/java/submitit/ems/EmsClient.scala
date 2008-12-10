package no.java.submitit.ems

import no.java.ems.client._
import no.java.ems.domain.{Event,Session,Person,EmailAddress}
import _root_.scala.collection.jcl.Conversions._
import common.Implicits._
import common._
import model._

class EmsClient(eventName: String, serverUrl: String) extends BackendClient {
  
  val emsService = new RestEmsService(serverUrl)
  
  val converter = new EmsConverter
  
  val event = findOrCreateEvent("JavaZone 2009", emsService.getEvents().toList)
  
  def savePresentation(presentation: Presentation): String = {
    presentation.speakers.foreach(speaker => 
      if (speaker.personId == null) findOrCreateContact(speaker))
    
    val session = converter.toSession(presentation)
    session.setEventId(event.getId())
    emsService.saveSession(session)
    presentation.sessionId = session.getId()
    session.getId()
  }
  
  def loadPresentation(id: String): Presentation = {
    val session = emsService.getSession(id)
    converter.toPresentation(session)
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
        adr.getEmailAddress() == email))
  }
  
  private def createContact(speaker: Speaker): Person = {
    val person = converter.toPerson(speaker)
    emsService.saveContact(person)
    person
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