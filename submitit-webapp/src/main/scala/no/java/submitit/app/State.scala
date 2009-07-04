/*
 * Copyright 2009 JavaBin
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

package no.java.submitit.app

import org.apache.wicket.Request
import org.apache.wicket.protocol.http.WebSession
import org.apache.wicket.Session
import no.java.submitit.common._
import no.java.submitit.model._
import DefaultConfigValues._

class State(request: Request, val backendClient: BackendClient) extends WebSession(request) {
  
  var captcha: Captcha = _
  var verifiedWithCaptha = false
  
  var invitation = false
  
  private var f = false
  
  def fromServer = f
  
  def fromServer_=(from: Boolean) {
    f = from
  }
  
  def isNew = submitAllowed && !fromServer
  
  private def notNewModifyAllowed = !isNew && SubmititApp.boolSetting(globalEditAllowedBoolean)
  
  def submitAllowed = invitation || SubmititApp.boolSetting(submitAllowedBoolean)

  def notNewModifyNotAllowedNewAllowed = !isNew && !notNewModifyAllowed && SubmititApp.boolSetting(submitAllowedBoolean)
  
  private var presentation: Presentation = _
  
  def currentPresentation = {
    if (presentation != null) presentation
    else {
      presentation = new Presentation
      presentation.init
      presentation
    }
   }

  def currentPresentation_=(currentPresentation: Presentation) {
    presentation = currentPresentation
  }
  
  def createNewPresentation = {
    if (presentation != null) {
      // Reset state in case of a new registration, but preserve speakers
      val p = new Presentation
      p.speakers = presentation.speakers
      presentation = p
      fromServer = false
      presentation
    }
    else {
      presentation = new Presentation
      presentation.init
      presentation
    }
    
  }

  def setCaptchaIfNotSet() {
    if (captcha == null) captcha = new Captcha
  }
  
  def resetCaptcha() {
    captcha = new Captcha
  }
  
}

object State {
  
  def apply() = Session.get().asInstanceOf[State]
  
}
