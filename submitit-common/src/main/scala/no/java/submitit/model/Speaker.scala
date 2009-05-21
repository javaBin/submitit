package no.java.submitit.model

import _root_.java.io.Serializable

class Speaker extends Serializable with EmsId {
  
  var personId: String = _
  var name: String = _
  var email: String = _
  var bio: String = _
  var picture: Picture = _
  
  override def toString =
    "Name: " + name +
    "\nE-mail: " + email +
    "\nSpeaker's profile:\n" + bio +
    "\n\nPicture name: " + (if (picture != null) picture.name else "")

  def toPersonXml(language: Language.Value) =
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

  def toSessionSpeakerXML = 
      <speakers>
      <name>{this.name}</name>
      <person-uuid>{this.originalId}</person-uuid>
      <description>{this.bio}</description>
      </speakers>

}

import xml._ 

object Speaker {
  
	def fromPersonXML(personXML: Node) = {
		val speaker = new Speaker
	  personXML.child foreach {_ match {
	  	case <uuid>{originalId}</uuid> => speaker.originalId = originalId.text
	  	case <name>{name}</name> => speaker.name = name.text
	  	case <description>{bio}</description> => speaker.bio = bio.text
	  	case <email-addresses>{addresses @ _*}</email-addresses> => speaker.email = extractFirstEmail(addresses)
	  	case _ =>  
	   }
		}
   speaker
   }

	private def extractFirstEmail(xml: NodeSeq) = (xml \\ "email-address").firstOption.getOrElse(<n></n>).text
 
}
  

