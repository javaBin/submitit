package no.java.submitit.app

import no.java.submitit.app.pages._
import org.apache.wicket.util.lang.Bytes
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadWebRequest
import org.apache.wicket.protocol.http.WebRequest
import javax.servlet.http.HttpServletRequest
import org.apache.wicket.Request
import org.apache.wicket.Response
import org.apache.wicket.Session

class SubmititApp extends WebApplication {


  	override def init() {
        mountBookmarkablePage("/lookupPresentation", classOf[IdResolverPage]);
        mountBookmarkablePage("/helpIt", classOf[HelpPage]);
        getApplicationSettings.setDefaultMaximumUploadSize(Bytes.kilobytes(500))
	}
   
    override def newWebRequest(servletRequest: HttpServletRequest) = new UploadWebRequest(servletRequest)
  
  	override def newSession(request: Request, response: Response):State = new State(request)
  
    def getHomePage() = classOf[StartPage]

}
