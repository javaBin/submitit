package no.java.submitit.app

import javax.servlet.http._
import Functions._
import no.java.submitit.model.Binary

class SessionListener extends javax.servlet.http.HttpSessionListener {
  
	def sessionCreated(event: HttpSessionEvent) {
		event.getSession.setAttribute(binariesInSession, List[Binary]())
	}

	def sessionDestroyed(event: HttpSessionEvent) {
	  val binaries = event.getSession.getAttribute(binariesInSession).asInstanceOf[List[Binary]]
    removeBinaries(binaries)
	}

}
