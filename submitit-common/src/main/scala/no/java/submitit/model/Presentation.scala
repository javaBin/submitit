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

package no.java.submitit.model

import Language._
import Level._

import _root_.java.io.Serializable
import no.java.submitit.model._
import scala.xml.NodeSeq
import common.Implicits._

class Presentation extends Serializable {
  
  val testPresentation = false
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
  var timeslot: String = _
  var room: String = _
  
  def isNew = sessionId == null

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
  
  def hasFeedback = feedback != null && feedback.trim != ""
  
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
    "\n\nTags:\n" + keywords.mkString(", ") +
    "\n\nSpeakers:\n\n" + speakers.mkString("", "\n\n", "")

  def toNonNullString(s: String) = if (s != null) s else ""
  
}

object Presentation {
  
  val testPresentationURL = "TEST-PRESENTATION-WHICH-WILL-NOT-BE-SUBMITTED"

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

case class PresentationInfo(id: String, title: String, speakerNames: List[String], status: Status.Value) 
