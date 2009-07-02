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
