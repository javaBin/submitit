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

  val emsUrl = SubmititApp.getSetting("emsUrl")
  val username = SubmititApp.getSetting("emsUser")
  val password = SubmititApp.getSetting("emsPwd")
  
  val backendClient = 
    if (emsUrl != "") new EmsClient("JavaZone 2009", emsUrl, username, password)
    else new submitit.common.BackendClientMock
  
  override def init() {
    mountBookmarkablePage("/lookupPresentation", classOf[IdResolverPage]);
    mountBookmarkablePage("/helpIt", classOf[HelpPage]);
    mountBookmarkablePage("99792226-admin-login-99792226", classOf[admin.AdminLogin])
    getApplicationSettings.setDefaultMaximumUploadSize(Bytes.kilobytes(500))
    
    val props = utils.PropertyLoader.loadRessource("submitit.properties")
    SubmititApp.adminPass = props.remove("adminPassPhrase").asInstanceOf[String]
    
    val elems = props.keys
    var theMap = Map[String, String]()
    for (i <- 0 to props.size() - 1) {
      val e = elems.nextElement.asInstanceOf[String]
      theMap = theMap + (e -> props.getProperty(e).asInstanceOf[String])
    }
    SubmititApp.props = theMap
  }
  
  override def newWebRequest(servletRequest: HttpServletRequest) = new UploadWebRequest(servletRequest)
  
  override def newSession(request: Request, response: Response):State = new State(request, backendClient)
  
  def getHomePage() = classOf[StartPage]

}

object SubmititApp {
  
  var props: Map[String, String] = _
  private var adminPass: String = _
  
  def getSetting(key: String) = props get key match {
    case Some(s) => s
    case None => null
  }
  
  def boolSetting(key: String) = _root_.java.lang.Boolean.parseBoolean(getSetting(key))
  
  def authenticates(password: Object) = adminPass == password
  
}
