package no.java.submitit.app

import no.java.submitit.common._
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
import org.apache.wicket.Application._

class SubmititApp extends WebApplication {

  override def init() {
    SubmititApp.propertyFileName = super.getInitParameter("submitit.properties")
    if (SubmititApp.propertyFileName == null) throw new Exception("""You must specify "submitit.properties" as a init parameter.""")
    val props = utils.PropertyIOUtils.loadRessource(SubmititApp.propertyFileName)
    SubmititApp.adminPass = props.remove(SubmititApp.adminPassPhrase).asInstanceOf[String]

    val elems = props.keys
    var theMap = Map[String, String]()
    for (i <- 0 to props.size() - 1) {
      val e = elems.nextElement.asInstanceOf[String]
      theMap = theMap + (e -> props.getProperty(e).asInstanceOf[String])
    }
    SubmititApp.properties = theMap

    mountBookmarkablePage("/lookupPresentation", classOf[IdResolverPage]);
    mountBookmarkablePage("/helpIt", classOf[HelpPage]);
    mountBookmarkablePage("/admin-login", classOf[admin.AdminLogin])
    getApplicationSettings.setDefaultMaximumUploadSize(Bytes.kilobytes(500))
  }

  private def backendClient: BackendClient = {
    val emsUrl = SubmititApp.getSetting("emsUrl")
    val username = SubmititApp.getSetting("emsUser")
    val password = SubmititApp.getSetting("emsPwd")
  
    if (emsUrl != "") new EmsClient("JavaZone 2009", emsUrl, username, password)
    else new submitit.common.BackendClientMock
  }

  override def newWebRequest(servletRequest: HttpServletRequest) = new UploadWebRequest(servletRequest)
  
  override def newSession(request: Request, response: Response):State = new State(request, backendClient)
  
  def getHomePage() = classOf[StartPage]

}

object SubmititApp {
  
  val adminPassPhrase = "adminPassPhrase"
  
  private var properties: Map[String, String] = _
  private var adminPass: String = _
  private var propertyFileName: String = _

  def props = properties
  
  def props_=(props: Map[String, String]) {
    this.properties = props
    utils.PropertyIOUtils.writeResource(adminPass, propertyFileName, properties)
  }
  
  def getSetting(key: String) = props get key match {
    case Some(s) => s
    case None => null
  }
  
  def intSetting(key: String) = getSetting(key).toInt
  
  def boolSetting(key: String) = _root_.java.lang.Boolean.parseBoolean(getSetting(key))
  
  def authenticates(password: Object) = adminPass == password
  
}
