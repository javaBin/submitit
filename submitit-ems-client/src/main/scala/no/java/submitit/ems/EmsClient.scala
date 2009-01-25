package no.java.submitit.ems

import no.java.ems.client._
import no.java.ems.domain.{Event,Session,Person,EmailAddress,Binary}
import _root_.java.io.Serializable
import _root_.scala.collection.jcl.Conversions._
import common.Implicits._
import common._
import model._

class EmsClient(eventName: String, serverUrl: String, username: String, password: String) extends BackendClient with Serializable {
  
  def isSet(s: String) = s != null && !s.trim.isEmpty
    
  def emsService = new RestEmsService(serverUrl)
  
  if (isSet(username) || isSet(password)) {
    emsService.setCredentials(username, password)
  }
  
  def converter = new EmsConverter
  
  val event = findOrCreateEvent("JavaZone 2009", emsService.getEvents().toList)
  
  def savePresentation(presentation: Presentation): String = {
    presentation.speakers.foreach(speaker => {
      val person = findOrCreateContact(speaker)
      updateDefaultEmail(person, speaker.email)
      speaker.personId = person.getId
      
      val picture = speaker.picture
      if (picture != null && picture.id == null) savePicture(picture)
    })

    updateOrCreateSession(presentation).sessionId
  }
  
  def loadPresentation(id: String): Presentation = {
    val session = emsService.getSession(id)
    val presentation = converter.toPresentation(session)
    presentation.speakers.foreach(speaker => {
      val person = emsService.getContact(speaker.personId)
      speaker.email = person.getEmailAddresses.toList.head.getEmailAddress 
    })
    presentation
  }
  
  private def updateOrCreateSession(presentation: Presentation): Presentation = {
    val session = 
      if (presentation.sessionId == null) new Session()
      else emsService.getSession(presentation.sessionId) 

    session.setEventId(event.getId)
    converter.updateSession(presentation, session)
    emsService.saveSession(session)
    
    presentation.sessionId = session.getId
    presentation
  }
  
  private def findOrCreateContact(speaker: Speaker): Person = {
    if (speaker.personId != null) emsService.getContact(speaker.personId)
    else findContactByEmail(speaker.email) match {
      case Some(person) => person
      case None => createContact(speaker)
    }
  }
  
  private def findContactByEmail(email: String): Option[Person] = {
    emsService.getContacts().find(contact => 
      contact.getEmailAddresses.exists(adr => 
        adr.getEmailAddress() == email))
  }

  private def updateDefaultEmail(person: Person, email: String) {
    val emailAddresses = person.getEmailAddresses.toList
    if (emailAddresses.head.getEmailAddress != email) {
      person.setEmailAddresses(new EmailAddress(email) :: 
                                 emailAddresses.filter(_.getEmailAddress != email))
      emsService.saveContact(person)
    }
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