package no.java.submitit.app

import no.java.submitit.ems._
import no.java.submitit.app.pages._
import org.apache.wicket.util.lang.Bytes
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadWebRequest
import org.apache.wicket.protocol.http.WebRequest
import javax.servlet.http.HttpServletRequest
import org.apache.wicket.Request
import org.apache.wicket.Response
import org.apache.wicket.Session
import _root_.java.util.Properties

class SubmititApp extends WebApplication {

  //def backendClient = new EmsClient("JavaZone 2009", "http://localhost:3000/ems")
  val backendClient = new submitit.common.BackendClientMock
  
  
  
  	override def init() {
        mountBookmarkablePage("/lookupPresentation", classOf[IdResolverPage]);
        mountBookmarkablePage("/helpIt", classOf[HelpPage]);
        getApplicationSettings.setDefaultMaximumUploadSize(Bytes.kilobytes(500))
        SubmititApp.props = utils.PropertyLoader.loadRessource("submitit.properties")
	}
   
    override def newWebRequest(servletRequest: HttpServletRequest) = new UploadWebRequest(servletRequest)
  
  	override def newSession(request: Request, response: Response):State = new State(request, backendClient)
  
    def getHomePage() = classOf[StartPage]

}

object SubmititApp {
  
  private var props: Properties = _
  
  def getSetting(key: String) = props getProperty key
  
}
