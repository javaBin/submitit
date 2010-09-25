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

package no.java.submitit.app

import org.apache.wicket.Request
import org.apache.wicket.protocol.http.WebSession
import org.apache.wicket.Session
import no.java.submitit.common._
import no.java.submitit.model._
import DefaultConfigValues._
import _root_.java.io.File
import Functions._
import no.java.submitit.config.Keys._

class State(request: Request, val backendClient: BackendClient) extends WebSession(request) with LoggHandling {
  
  var captcha: Captcha = _
  var verifiedWithCaptha = false
  
  var invitation = false
  
  var binaries = List[Binary]()
  
  def addBinary(binary: Binary) {
    logger.info("Adding binary with temp file " + binary.tmpFileName.getOrElse(""))
    binaries = binary :: binaries
  }
  
  private def fromServer = currentPresentation.sessionId != null
  
  private def notNewModifyAllowed = fromServer && SubmititApp.boolSetting(globalEditAllowedBoolean)
  
  def submitAllowed = invitation || SubmititApp.boolSetting(submitAllowedBoolean)

  def notNewModifyNotAllowedNewAllowed = fromServer && !notNewModifyAllowed && SubmititApp.boolSetting(submitAllowedBoolean)
  
  private var presentation: Presentation = _
  
  def currentPresentation = {
    if (presentation != null) presentation
    else {
      presentation = new Presentation
      presentation.init
      presentation
    }
   }

  def clearBinaries() {
     removeBinaries(binaries)
  }

  def updateBinaries() {
    if (presentation != null) {
    	binaries = currentPresentation.pdfSlideset.toList ::: currentPresentation.slideset.toList ::: currentPresentation.speakers.filter(_.picture.isDefined).map(_.picture.get)
    	if(binaries != Nil) logger.info("Adding binary with temp file " + binaries.map(_.tmpFileName.getOrElse("")).mkString(", "))
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
      presentation
    }
    else {
      clearPresentation()
    }
    
  }

  def clearPresentation() = {
      presentation = new Presentation
      presentation.init
      presentation
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
