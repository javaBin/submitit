package no.java.submitit.model

import Language._
import Level._

import _root_.java.io.Serializable
import no.java.submitit.model._
import xml._
import xml.Utility.trim

class Presentation extends Serializable with EmsId {
  
  var sessionId: String = _
  var title: String = ""
  var summary: String = ""
  var abstr: String = ""
  var speakers: List[Speaker] = Nil
  var outline: String = ""
  var language: Language.Value = Language.Norwegian
  var level: Level.Value = Level.Beginner
  var format: PresentationFormat.Value = PresentationFormat.Presentation
  var equipment: String = ""
  var expectedAudience: String = ""
  var feedback: String = _
  var status: Status.Value = Status.Pending

  def duration: Int = format match {
    case PresentationFormat.Presentation => 60
    case PresentationFormat.LightningTalk => 15
  }
  
  def init() {
    addSpeaker
  }

  def addSpeaker() {
    speakers = new Speaker :: speakers
  }
  
  def removeSpeaker(s: Speaker) {
    speakers = removeSpeaker(s, speakers)
  }
  
  private def removeSpeaker(s: Speaker, speakers: List[Speaker]): List[Speaker] = {
    speakers match {
      case speaker :: xs => if (s == speaker) xs else speaker :: removeSpeaker(s, xs)
      case Nil => Nil
    }
  }

  override def toString =
    "Format: " + format +
    "\nTitle: " + title +
    "\n\nAbstract:\n" + abstr +
    "\n\nSummary:\n" + summary +
    "\n\nOutline:\n" + toNonNullString(outline) +
    "\n\nLanguage: " + language +
    "\nLevel: " + level +
    "\nEquipment:\n" + toNonNullString(equipment) +
    "\n\nExpected audience:\n" + toNonNullString(expectedAudience) +
    "\n\nSpeakers:\n\n" + speakers.mkString("", "\n\n", "")

  def toNonNullString(s: String) = if (s != null) s else ""


  def toXML(eventId: String) = trim {
  	<ns2:session xmlns:ns2="http://xmlns.java.no/ems/external/1">
    <uuid>{this.originalId}</uuid>
    <event-id>{eventId}</event-id>
	  <title>{title}</title>
	  <format>{PresentationFormat.toEmsValue(format)}</format>
	  <level>{Level.toEmsValue(level)}</level>
	  <language>{Language.toEmsValue(language)}</language>
	  <body>{abstr}</body>
    <tags />
    <keywords/>
	  <expected-audience>{expectedAudience}</expected-audience>
	  <equipment>{equipment}</equipment>
	  speakers.map(x => x.toSessionSpeakerXML)
    <published />
    <expected-audience>{expectedAudience}</expected-audience>
	  </ns2:session>
  }

}
                   
