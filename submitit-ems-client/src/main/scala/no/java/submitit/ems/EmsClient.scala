package no.java.submitit.ems

import no.java.ems.client._
import no.java.ems.domain.{Event,Session,Person,EmailAddress,Binary}
import _root_.scala.collection.jcl.Conversions._
import common.Implicits._
import common._
import model._

class EmsClient(eventName: String, serverUrl: String, username: String, password: String) extends BackendClient {
  
  def isSet(s: String) = s != null && !s.trim.isEmpty
    
  val emsService = new RestEmsService(serverUrl)
  if (isSet(username) || isSet(password)) {
    emsService.setCredentials(username, password)
  }
  
  val converter = new EmsConverter
  
  val event = findOrCreateEvent("JavaZone 2009", emsService.getEvents().toList)
  
  def savePresentation(presentation: Presentation): String = {
    presentation.speakers.foreach(speaker => {
      if (speaker.personId == null) findOrCreateContact(speaker)
      
      val picture = speaker.picture
      if (picture != null && picture.id == null) savePicture(picture)
    })
    
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
  
  private def savePicture(picture: Picture) {
    val photo = converter.toPhoto(picture)
    val result = emsService.saveBinary(photo)
    picture.id = result.getId
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