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

import _root_.java.io.Serializable
import xml.Utility.trim

class Speaker extends Serializable with EmsId {
  
  var personId: String = _
  var name: String = _
  var email: String = _
  var bio: String = _
  var picture: Option[Binary] = None 
  
  def hasNewPicture = picture.map(_.isNew).getOrElse(false)
    
  override def toString =
    "Name: " + name +
    "\nE-mail: " + email +
    "\nSpeaker's profile:\n" + bio +
    "\n\nPicture name: " + (picture.map(_.name).getOrElse(""))

  def toPersonXml(language: Language.Value) = trim {
      <person>
			<uuid>{originalId}</uuid>
			<url/>
			<name>{name}</name>
			<description>{bio}</description>
			<language>{Language.toEmsValue(language)}</language>
			<email-addresses>
			<email-address>{this.email}</email-address>
			</email-addresses>
			<tags />
			</person>
   }

  def toSessionSpeakerXML = trim { 
      <speakers>
      <name>{this.name}</name>
      <person-uuid>{this.originalId}</person-uuid>
      <description>{this.bio}</description>
      </speakers>
  }

}

import xml._ 

object Speaker {
  
	def fromPersonXML(personXML: Node): Speaker = {
		fromPersonXML(personXML.child)
   }

 def fromPersonXML(personXML: NodeSeq): Speaker = {
		val speaker = new Speaker
		personXML foreach {_ match {
			case <uuid>{originalId}</uuid> => speaker.originalId = originalId.text
			case <name>{name}</name> => speaker.name = name.text
			case <description>{bio}</description> => speaker.bio = bio.text
			case <email-addresses>{addresses @ _*}</email-addresses> => speaker.email = extractFirstEmail(addresses)
			case _ =>  
			}
		}
		speaker
	}
	def fromSpeakerXML(personXML: NodeSeq): Speaker = {
			val speaker = new Speaker
			personXML foreach {_ match {
			case <person-uuid>{originalId}</person-uuid> => speaker.originalId = originalId.text
			case <name>{name}</name> => speaker.name = name.text
			case <description>{bio}</description> => speaker.bio = bio.text
			case _ =>  
			}
			}
			speaker
	}

	private def extractFirstEmail(xml: NodeSeq) = (xml \\ "email-address").firstOption.getOrElse(<n></n>).text
 
  def apply(name: String, email: String, bio: String, picture: Option[Binary]): Speaker = {
    val s = new Speaker()
    s.name = name
    s.email = email
    s.bio = bio
    s.picture = picture
    s
  }
}
