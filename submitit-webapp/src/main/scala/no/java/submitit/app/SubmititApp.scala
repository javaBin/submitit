/*
 * Copyright 2009 JavaBin
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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
import org.apache.wicket.settings.IExceptionSettings
import DefaultConfigValues._

class SubmititApp extends WebApplication with LoggHandling {

  override def init() {
    SubmititApp.propertyFileName = super.getServletContext.getInitParameter("submitit.properties")
    if (SubmititApp.propertyFileName == null) throw new Exception("""You must specify "submitit.properties" as a init parameter.""")
    val props = utils.PropertyIOUtils.loadRessource(SubmititApp.propertyFileName)
    SubmititApp.adminPass = this nullOnEmptyString props.remove(SubmititApp.adminPassPhrase).asInstanceOf[String]
    SubmititApp.emsUrl = this nullOnEmptyString props.remove(SubmititApp.emsUrlKey).asInstanceOf[String]
    SubmititApp.emsUsername = this nullOnEmptyString props.remove(SubmititApp.emsUsernameKey).asInstanceOf[String]
    SubmititApp.emsPwd = this nullOnEmptyString props.remove(SubmititApp.emsPwdKey).asInstanceOf[String]

    val elems = props.keys
    var theMap = DefaultConfigValues.configValues
    
    for (i <- 0 to props.size() - 1) {
      val e = elems.nextElement.asInstanceOf[String]
      DefaultConfigValues getKey(e) match {
        case Some(key) if theMap.contains(key) => theMap = theMap + (key -> props.getProperty(e).asInstanceOf[String])
        case _ => logger.info("Removing log value no longer in use:  " + e)
      }
    }
    
    SubmititApp.properties = theMap

    mountBookmarkablePage("/lookupPresentation", classOf[IdResolverPage]);
    mountBookmarkablePage("/helpit", classOf[HelpPage]);
    mountBookmarkablePage("/admin-login", classOf[admin.AdminLogin])
    mountBookmarkablePage("/invitation", classOf[InvitationPage])
    getApplicationSettings.setDefaultMaximumUploadSize(Bytes.kilobytes(500))
  }

  private def backendClient: BackendClient = {
    if (SubmititApp.emsUrl != null) new EmsClient(SubmititApp.getSetting(eventName), SubmititApp.emsUrl, SubmititApp.emsUsername, SubmititApp.emsPwd)
    else submitit.common.BackendClientMock
  }

  override def newWebRequest(servletRequest: HttpServletRequest) = new UploadWebRequest(servletRequest)
  
  override def newSession(request: Request, response: Response):State = new State(request, backendClient)
  
  def getHomePage() = classOf[StartPage]
  
  private def nullOnEmptyString(s: String) = if (s.trim != "") s else null
  
  override def newRequestCycle(request: Request, response: Response) = new MyRequestCycle(this, request.asInstanceOf[WebRequest], response)

}

class MyRequestCycle(application: WebApplication, request: WebRequest, response: Response) extends org.apache.wicket.protocol.http.WebRequestCycle(application, request, response) {
  override def onRuntimeException(cause: org.apache.wicket.Page, e: RuntimeException) = {
    if ("deployment" == getApplication.getConfigurationType)
      new ErrorPage(State().currentPresentation, e)
    else super.onRuntimeException(cause, e)
  }
}

object SubmititApp {
  
  private val adminPassPhrase = "adminPassPhrase"
  private val emsUrlKey = "emsUrl"
  private val emsUsernameKey = "emsUser"
  private val emsPwdKey = "emsPwd"
  
  private var properties: Map[ConfigKey, String] = _
  private var adminPass: String = _
  private var emsUrl: String = _
  private var emsUsername : String = _
  private var emsPwd: String = _
  private var propertyFileName: String = _

  def props = properties
  
  def props_=(props: Map[ConfigKey, String]) {
    this.properties = props
    val stringProps = props.foldLeft(Map[String, String]()){(m, tuple) => m + (tuple._1.toString -> tuple._2)}
    val map = stringProps + 
      (adminPassPhrase -> adminPass) + 
      (emsUrlKey -> emsUrl) + 
      (emsUsernameKey -> emsUsername) +
      (emsPwdKey -> emsPwd)
    utils.PropertyIOUtils.writeResource(propertyFileName, map)
  }
  
  def getSetting(key: ConfigKey) = props get key match {
    case Some(s) if s != "" => s
    case None => null
    case _ => null
  }
  
  def getBccEmailList = {
    getSetting(emailBccCommaSeparatedList) match {
      case null => new Array[String](0)
      case email => email.split(",")
    }
  }
  
  def getOfficialEmail = {
    getSetting(officialEmailReplyTo)
  }
  
  def intSetting(key: ConfigKey) = getSetting(key).toInt
  
  def boolSetting(key: ConfigKey) = _root_.java.lang.Boolean.parseBoolean(getSetting(key))
  
  def getListSetting(key: ConfigKey, separator: Char) = getSetting(key) match {
    case s: String => s.split(separator).toList.map(_.trim)
    case null => Nil
  }

  def getListSetting(key: ConfigKey): List[String] = getListSetting(key, ',')
  
  def authenticates(password: Object) = adminPass == password
  
}
