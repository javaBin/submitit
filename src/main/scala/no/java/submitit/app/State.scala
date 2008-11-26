package no.java.submitit.app

import org.apache.wicket.Request
import org.apache.wicket.protocol.http.WebSession
import org.apache.wicket.Session
import no.java.submitit.model._

class State(request: Request) extends WebSession(request) {
  
  var currentPresentation: Presentation = _

}

object State{
  
  def get = Session.get().asInstanceOf[State]
  
}
