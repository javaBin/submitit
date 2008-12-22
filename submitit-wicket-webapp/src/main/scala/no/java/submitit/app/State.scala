package no.java.submitit.app

import org.apache.wicket.Request
import org.apache.wicket.protocol.http.WebSession
import org.apache.wicket.Session
import no.java.submitit.common._
import no.java.submitit.model._

class State(request: Request, val backendClient: BackendClient) extends WebSession(request) {
  
  var captcha: Captcha = _
  var verifiedWithCaptha = false
  
  private var f = false
  
  def fromServer = f
  
  def fromServer_=(from: Boolean) {
    f = from
  }
  
  def lockPresentation = f && !SubmititApp.boolSetting("globalEditAllowedBoolean")
  
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

  def setCaptchaIfNotSet() {
    if (captcha == null) captcha = new Captcha
  }
  
  def resetCaptcha() {
    captcha = new Captcha
  }
  
}

object State{
  
  def get = Session.get().asInstanceOf[State]
  
  def apply() = get
  
}
