package no.java.submitit.app

import no.java.submitit.app.pages._
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.Request
import org.apache.wicket.Response
import org.apache.wicket.Session

class SubmititApp extends WebApplication {


  	override def init() {
        mountBookmarkablePage("/lookupPresentation", classOf[IdResolverPage]);
	}
  
  	override def newSession(request: Request, response: Response):State = new State(request)
  
    def getHomePage() = classOf[SubmitPage]

}
