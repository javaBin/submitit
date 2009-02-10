package no.java.submitit.model

import _root_.java.io.Serializable

class Speaker extends Serializable {
  
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

  override def equals(other: Any): Boolean = {
    other match {
      case that: Speaker =>
        (that canEqual this) && 
          personId == that.personId && 
          name == that.name && 
          bio == that.bio
      case _ => false
    }
  }
  
  def canEqual(other: Any): Boolean = {
    other.isInstanceOf[Speaker]
  }
  
  override def hashCode(): Int =
    name.hashCode + 41 * bio.hashCode
  
}

object Speaker {
  
  def apply(name: String, email: String, bio: String, picture: Picture): Speaker = {
    val s = new Speaker()
    s.name = name
    s.email = email
    s.bio = bio
    s.picture = picture
    s
  }
}