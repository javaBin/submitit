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
    SubmititApp.propertyFileName = super.getServletContext.getInitParameter("submitit.properties")
    if (SubmititApp.propertyFileName == null) throw new Exception("""You must specify "submitit.properties" as a init parameter.""")
    val props = utils.PropertyIOUtils.loadRessource(SubmititApp.propertyFileName)
    SubmititApp.adminPass = this nullOnEmptyString props.remove(SubmititApp.adminPassPhrase).asInstanceOf[String]
    SubmititApp.emsUrl = this nullOnEmptyString props.remove(SubmititApp.emsUrlKey).asInstanceOf[String]
    SubmititApp.emsUsername = this nullOnEmptyString props.remove(SubmititApp.emsUsernameKey).asInstanceOf[String]
    SubmititApp.emsPwd = this nullOnEmptyString props.remove(SubmititApp.emsPwdKey).asInstanceOf[String]

    val elems = props.keys
    var theMap = Map[String, String]()
    for (i <- 0 to props.size() - 1) {
      val e = elems.nextElement.asInstanceOf[String]
      theMap = theMap + (e -> props.getProperty(e).asInstanceOf[String])
    }
    SubmititApp.properties = theMap

    mountBookmarkablePage("/lookupPresentation", classOf[IdResolverPage]);
    mountBookmarkablePage("/helpit", classOf[HelpPage]);
    mountBookmarkablePage("/admin-login", classOf[admin.AdminLogin])
    getApplicationSettings.setDefaultMaximumUploadSize(Bytes.kilobytes(500))
  }

  private def backendClient: BackendClient = {
    if (SubmititApp.emsUrl != null) new EmsClient(SubmititApp.getSetting("eventName"), SubmititApp.emsUrl, SubmititApp.emsUsername, SubmititApp.emsPwd)
    else submitit.common.BackendClientMock
  }

  override def newWebRequest(servletRequest: HttpServletRequest) = new UploadWebRequest(servletRequest)
  
  override def newSession(request: Request, response: Response):State = new State(request, backendClient)
  
  def getHomePage() = classOf[StartPage]
  
  private def nullOnEmptyString(s: String) = if (s.trim != "") s else null

}

object SubmititApp {
  
  private val adminPassPhrase = "adminPassPhrase"
  private val emsUrlKey = "emsUrl"
  private val emsUsernameKey = "emsUser"
  private val emsPwdKey = "emsPwd"
  
  private var properties: Map[String, String] = _
  private var adminPass: String = _
  private var emsUrl: String = _
  private var emsUsername : String = _
  private var emsPwd: String = _
  private var propertyFileName: String = _

  def props = properties
  
  def props_=(props: Map[String, String]) {
    this.properties = props
    val map = props + 
      (adminPassPhrase -> adminPass) + 
      (emsUrlKey -> emsUrl) + 
      (emsUsernameKey -> emsUsername) +
      (emsPwdKey -> emsPwd)
    utils.PropertyIOUtils.writeResource(propertyFileName, map)
  }
  
  def getSetting(key: String) = props get key match {
    case Some(s) if s != "" => s
    case None => null
    case _ => null
  }
  
  def getBccEmailList = {
    getSetting("emailBccCommaSeparatedList") match {
      case null => new Array[String](0)
      case email => email.split(",")
    }
  }
  
  def getOfficialEmail = {
    getSetting("officialEmailReplyTo")
  }
  
  def intSetting(key: String) = getSetting(key).toInt
  
  def boolSetting(key: String) = _root_.java.lang.Boolean.parseBoolean(getSetting(key))
  
  def authenticates(password: Object) = adminPass == password
  
}
