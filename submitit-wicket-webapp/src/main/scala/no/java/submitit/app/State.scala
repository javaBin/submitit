package no.java.submitit.app

import org.apache.wicket.Request
import org.apache.wicket.protocol.http.WebSession
import org.apache.wicket.Session
import no.java.submitit.model._

class State(request: Request) extends WebSession(request) {
  
  var currentPresentation: Presentation = _
  
  var verifiedWithCaptha = false
  
  var captcha: Captcha = _

  def setCaptchaIfNotSet() {
    if (captcha == null) captcha = new Captcha
  }
  
  def resetCaptcha() {
    captcha = new Captcha
  }

}

object State{
  
  def get = Session.get().asInstanceOf[State]
  
}
