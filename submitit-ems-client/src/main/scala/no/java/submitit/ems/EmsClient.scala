/*
 * Copyright 2009 JavaBin
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

import _root_.java.io.Serializable
import _root_.scala.collection.jcl.Conversions._
import common.Implicits._
import common._
import model._
import xml._
import _root_.no.scala.scalanet.http._
import _root_.no.scala.scalanet.http.Implicits._

class EmsClient(eventName: String, serverUrl: String, username: String, password: String) extends BackendClient with Serializable {
  
  def isSet(s: String) = s != null && !s.trim.isEmpty
      
  def converter = new EmsConverter
  
  lazy val event = findOrCreateEvent
  
  def findOrCreateEvent = {
    
    HTTP.start(serverUrl) { (http: HTTP) =>
      findEventInXML(XML.load(http.get("1/events").body.stream))
    }
  }
  
  def findEventInXML(doc: Node) =
    (doc \\"event").find(event => event \ "name" == eventName) match {
      case Some(event) => (event \\ "id").text
      case None => ""
    }
  
  
  def savePresentation(presentation: Presentation): String = {
    ""
  }
  
  def loadPresentation(id: String): Option[Presentation] = {
    error("Not implemented yet")
  }
  
   /*
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
  


  def loadPresentation(id: String): Option[Presentation] = {
    getSession(id) match {
      case Some(session) => {
        val presentation = converter.toPresentation(session)
        presentation.speakers.foreach(speaker => {
          val person = emsService.getContact(speaker.personId)
          speaker.email = person.getEmailAddresses.toList.head.getEmailAddress 
        })
        Some(presentation)
      }
      case None => None
    }
  }
  
  private def getSession(id: String): Option[Session] = {
    // Workaround for authorization problems...
    val sessions = emsService.getSessions(event.getId)
    sessions.find(session => session.getId == id)
  }
  
  private def updateOrCreateSession(presentation: Presentation): Presentation = {
    val session = 
      if (presentation.sessionId == null) {	
        val s = new Session()
        s.addTags("fra_submitit" :: Nil)
        s
      }
      else getSession(presentation.sessionId) match {
        case Some(session) => session
        case None => error("Unknown session " + presentation.sessionId)
      } 
    
    session.setEventId(event.getId)
    converter.updateSession(presentation, session)
    emsService.saveSession(session)
    
    presentation.sessionId = session.getId
    presentation
  }
  
  private def findOrCreateContact(speaker: Speaker): Person = {
    if (speaker.personId != null) findContactById(speaker)
    else findContactByEmail(speaker.email) match {
      case Some(person) => person
      case None => findContactByName(speaker.name) match {
        case Some(person) => person
        case None => createContact(speaker)
      }
    }
  }

  /**
   * User may have modified speaker form (renamed speaker and given a new email addresse, which would be a new speaker.
   * However the speaker would have the same id because of the way the form is created. Must handle this and
   * create a new speaker if speaker email and name does not match.
   */
  private def findContactById(speaker: Speaker): Person = {
    val contact = emsService.getContact(speaker.personId)
    if(emailMatches(contact, speaker.email) || contact.getName == speaker.name) contact
    else createContact(speaker)
  }

  private def emailMatches(contact: Person, email: String) = {
      contact.getEmailAddresses.exists(adr =>
        adr.getEmailAddress() == email)
  }
  
  private def findContactByEmail(email: String): Option[Person] = {
    emsService.getContacts().find(emailMatches(_, email))
  }
  
  private def findContactByName(name: String): Option[Person] = {
    // TODO
    None
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
  */
  
}