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

import _root_.java.io.InputStream
import _root_.scala.collection.jcl.Conversions._
import common.Implicits._
import common.{IOUtils, LoggHandling}
import model._
import common.IOUtils._
import org.joda.time.Interval
import org.joda.time.format.DateTimeFormat

import xml._

class EmsConverter extends LoggHandling {

  def toEmsSession(presentation: Presentation, orgXml: Node): Node = {
			val g = orgXml.child.map { element => element match {
        case <title>{_*}</title> => <title>{presentation.title}</title>
        case <format>{_*}</format> => <format>{convertFormat(presentation.format)}</format>
        case <level>{_*}</level> => <level>{convertLevel(presentation.level)}</level>
        case <language>{_*}</language> => <language>{convertLanguage(presentation.language)}</language>
        case <body>{_*}</body> => <body>{presentation.abstr}</body>
        case <expected-audience>{_*}</expected-audience> => <expected-audience>{presentation.expectedAudience}</expected-audience>
        case <equipment>{_*}</equipment> => <equipment>{presentation.equipment}</equipment>
        case <speakers>{speakers @ _*}</speakers> => <speakers>{convertSpeakers(presentation.speakers, speakers)}</speakers>
				case n => n
				}
			}
			<ns2:session xmlns:ns2="http://xmlns.java.no/ems/external/1">{g}</ns2:session>
  }

  private def convertSpeakers(speakers: List[Speaker], nodeSeq: NodeSeq) = {

    //val speaker = speakers.find(_.originalId)
  }

    
	def toEmsPerson(speaker: Speaker, orgXml: Node): Node = {
			val g = orgXml.child.map { element => element match {
				case <name>{_*}</name> => <name>{speaker.name}</name>
				case <description>{_*}</description> => <description>{speaker.bio}</description>
				case <email-addresses>{addresses @ _*}</email-addresses> => <email-addresses>{handleAdresses(speaker, addresses)}</email-addresses>
				case n => n
				}
			}
			<person>{g}</person>
		} 
 
	
	private def handleAdresses(speaker: Speaker, nodeSeq: NodeSeq) =
	   <email-address>{speaker.email}</email-address> ++ nodeSeq.map { _ match {
      case <email-address>{email}</email-address> if email != speaker.email => <email-address>{email}</email-address>         
      case s => null
	  	}	
	  }


  private def convertLevel(level: Level.Value) = level match {
    case Level.Advanced => "Advanced"
    case Level.Intermediate => "Intermediate"
    case Level.Beginner => "Introductory"
  }

  private def convertFormat(format: PresentationFormat.Value) = format match {
    case PresentationFormat.Presentation => "Presentation"
    case PresentationFormat.LightningTalk => "Quickie"
  }

  private def convertLanguage(format: Language.Value) = format match {
    case Language.Norwegian => "no"
    case Language.English => "en"
  }

  /*

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
    pres.status = session.getState match {
      case Session.State.Approved => Status.Approved
      case Session.State.Pending => Status.Pending
      case Session.State.Rejected => Status.NotApproved
      case l => unknownEnumValue(l, Status.Pending)
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

      usingIS(photo.getDataStream) {
        stream => read(0, stream, content)
      }

      new Picture(photo.getId, content, photo.getFileName, photo.getMimeType)
    } else {
      null
    }
  }

  private def read(ofs: Int, is: InputStream, content: Array[Byte]): Unit = {
    val len = is.read(content, ofs, content.length - ofs)
    if (len != -1)
      read(ofs + len, is, content)
  }

  private def unknownEnumValue[T](v: Any, r: T): T = {
    logger.error("Unknown enum value '" + v + "', defaulting to '" + r + "'")
    r
  }

  */
}

