package no.java.submitit.model

import Language._
import Level._

import _root_.java.io.Serializable
import no.java.submitit.model._
import scala.xml.NodeSeq

class Presentation extends Serializable {
  
  var sessionId: String = _
  var title: String = ""
  var summary: String = ""
  var abstr: String = ""
  var speakers: List[Speaker] = Nil
  var outline: String = ""
  var language: Language.Value = Norwegian
  var level: Level.Value = Beginner
  var duration: Int = 60
  var equipment: String = ""
  var expectedAudience: String = ""
  var feedback: String = _
 
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
    "title: " + title +
    "\nabstract: " + abstr +
    "\nsummary: " + summary +
    "\noutline: " + outline +
    "\nlanguage: " + language +
    "\nlevel: " + level +
    "\nduration: " + duration +
    "\nequipment: " + equipment +
    "\nexpectedAudience: " + expectedAudience +
    "\nspeakers: " + speakers.mkString("(\n", "\n\t", "\n)")
   
}

object Presentation {

  def apply(title: String, 
            speakers: List[Speaker],
            summary: String,
            abstr: String, 
            outline: String, 
            language: Language.Value,
            level: Level.Value,
            duration: Int,
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
			    p.duration = duration
			    p.equipment = equipment
			    p.expectedAudience = expectedAudience
			    p
            }
}
                   
