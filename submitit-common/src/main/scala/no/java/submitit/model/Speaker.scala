/*
 * Copyright 2011 javaBin
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

class Speaker extends Serializable {
  
  var personId: String = _
  var name: String = _
  var email: String = _
  var bio: String = _
  var nationality: String = _
  var zipcode: String = _
  var picture: Option[Binary] = None 
  
  def hasNewPicture = picture.map(_.isNew).getOrElse(false)
    
  override def toString =
    "Name: " + name +
    "\nE-mail: " + email +
    "\nSpeaker's profile:\n" + bio +
    "\nSpeaker's nationality:\n" + nationality +
    "\nSpeaker's zipcode:\n" + zipcode +
    "\n\nPicture name: " + (picture.map(_.name).getOrElse(""))

  override def equals(other: Any): Boolean = {
    other match {
      case that: Speaker =>
        (that canEqual this) && 
          personId == that.personId && 
          name == that.name && 
          nationality == that.nationality && 
          zipcode == that.zipcode && 
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
  
  def apply(name: String, email: String, bio: String, nationality: String, zipcode: String, picture: Option[Binary]): Speaker = {
    val s = new Speaker()
    s.name = name
    s.email = email
    s.bio = bio
    s.nationality = nationality
    s.zipcode = zipcode
    s.picture = picture
    s
  }
}