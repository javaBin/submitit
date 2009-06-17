package no.java.submitit.model

import Language._
import Level._

import _root_.java.io.Serializable
import no.java.submitit.model._
import scala.xml.NodeSeq
import common.Implicits._

class Presentation extends Serializable {
  
  var sessionId: String = _
  var title: String = ""
  var summary: String = ""
  var abstr: String = ""
  var speakers: List[Speaker] = Nil
  var keywords: List[String] = Nil
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
  
  // Kind of a hack here, so that we may use scala.List in the model object. Need to convert to ArrayList through getters and setters.
  def getKeywords: _root_.java.util.ArrayList[String] = keywords
  def setKeywords(list: _root_.java.util.ArrayList[String]) = keywords = list.toArray.foldLeft(List[String]())((l, e) => e.toString :: l)
  
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
  
}

object Presentation {

  def apply(title: String, 
            speakers: List[Speaker],
            summary: String,
            abstr: String, 
            outline: String, 
            language: Language.Value,
            level: Level.Value,
            format: PresentationFormat.Value,
            equipment: String,
            expectedAudience: String): Presentation = {
			    val p = new Presentation
			    p.title = title
			    p.summary = summary
			    p.speakers = speakers
			    p.abstr = abstr
			    p.outline = outline
			    p.language = language
			    p.level = level
			    p.format = format
			    p.equipment = equipment
			    p.expectedAudience = expectedAudience
			    p
            }
}
                   
