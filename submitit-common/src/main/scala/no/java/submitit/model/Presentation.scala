package no.java.submitit.model

import Language._
import Level._

import _root_.java.io.Serializable
import no.java.submitit.model._
import scala.xml.NodeSeq

class Presentation extends Serializable {
  
  var sessionId: String = _
  var title: String = ""
  var abstr: String = ""
  var speakers: List[Speaker] = Nil
  var outline: String = ""
  var language: Language.Value = Norwegian
  var level: Level.Value = Beginner
  var duration: Int = 60
  var equipment: String = ""
  var requiredExperience: String = ""
  var expectedAudience: String = ""
 
  def init() {
    addSpeaker
  }
  
  def addSpeaker() {
    speakers = new Speaker :: speakers
  }
  
  def removeSpeaker(s: Speaker) {
    speakers = speakers.remove(_ == s)
  }
  
  override def toString =
    "title: " + title +
      "\nspeakers: " + speakers.mkString("(\n", "\n\t", "\n)") +
      "\nabstract: " + abstr +
      "\noutline: " + outline +
      "\nlanguage: " + language +
      "\nlevel: " + level +
      "\nduration: " + duration +
      "\nequipment: " + equipment +
      "\nrequiredExperience: " + requiredExperience +
      "\nexpectedAudience: " + expectedAudience
   
}

object Presentation {

  def apply(title: String, 
            speakers: List[Speaker], 
            abstr: String, 
            outline: String, 
            language: Language.Value,
            level: Level.Value,
            duration: Int,
            equipment: String,
            requiredExperience: String,
            expectedAudience: String): Presentation = {
			    val p = new Presentation
			    p.title = title
			    p.speakers = speakers
			    p.abstr = abstr
			    p.outline = outline
			    p.language = language
			    p.level = level
			    p.duration = duration
			    p.equipment = equipment
			    p.requiredExperience = requiredExperience
			    p.expectedAudience = expectedAudience
			    p
            }
}
                   
