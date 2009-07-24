/*
 * Copyright 2009 javaBin
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

    val elems = props.keys
    var theMap = DefaultConfigValues.configValues

    DefaultConfigValues.configKeyList.filter(_.mandatoryInFile).foreach(e => if(!props.containsKey(e.name)) throw new Exception("You must specify " + e.name + " in property file"))

    for (i <- 0 to props.size() - 1) {
      val e = elems.nextElement.asInstanceOf[String]
      DefaultConfigValues getKey(e) match {
        case Some(key) if theMap.contains(key) => theMap = theMap + (key -> props.getProperty(e).asInstanceOf[String])
        case _ => logger.info("Removing property value no longer in use:  " + e)
      }
    }
    
    SubmititApp.properties = theMap

    mountBookmarkablePage("/lookupPresentation", classOf[IdResolverPage]);
    mountBookmarkablePage("/helpit", classOf[HelpPage]);
    mountBookmarkablePage("/admin-login", classOf[admin.AdminLogin])
    mountBookmarkablePage("/list-submissions", classOf[admin.ListPresentationLoginPage])
    mountBookmarkablePage("/invitation", classOf[InvitationPage])
    getApplicationSettings.setDefaultMaximumUploadSize(Bytes.kilobytes(500))
  }

  private def backendClient: BackendClient = {
    if (SubmititApp.getSetting(emsUrl).isDefined) new EmsClient(SubmititApp.getSetting(eventName).get, 
                                                                SubmititApp.getSetting(emsUrl).get, 
                                                                SubmititApp.getSetting(emsUser), SubmititApp.getSetting(emsPwd), 
                                                                SubmititApp.getListSetting(commaSeparatedListOfTagsForNewSubmissions))
    else submitit.common.BackendClientMock
  }

  override def newWebRequest(servletRequest: HttpServletRequest) = new UploadWebRequest(servletRequest)
  
  override def newSession(request: Request, response: Response):State = new State(request, backendClient)
  
  def getHomePage() = classOf[StartPage]
  
  private def nullOnEmptyString(s: String) = if (s.trim != "") s.trim else null
  
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
  
  private var properties: collection.Map[ConfigKey, String] = _
  private var propertyFileName: String = _

  def props: collection.Map[ConfigKey, String] = properties
  
  def props_=(props: collection.Map[ConfigKey, String]) {
    this.properties = props
    utils.PropertyIOUtils.writeResource(propertyFileName, props)
  }
  
  def getSetting(key: ConfigKey) = props.get(key).get match {
    case s if s != null && s != "" => Some(s.trim)
    case _ => None
  }
  
  def getBccEmailList = {
    getSetting(emailBccCommaSeparatedList) match {
      case Some(email) => email.split(",")
      case None => new Array[String](0)
    }
  }
  
  def getOfficialEmail = {
    getSetting(officialEmailReplyTo).get
  }
  
  def intSetting(key: ConfigKey) = DefaultConfigValues.intParse(getSetting(key).get)
  
  def boolSetting(key: ConfigKey) = DefaultConfigValues.booleanParse(getSetting(key).get)
  
  def getListSetting(key: ConfigKey, separator: Char) = getSetting(key) match {
    case Some(s) => s.split(separator).toList.map(_.trim)
    case None => Nil
  }

  def getListSetting(key: ConfigKey): List[String] = getListSetting(key, ',')
  
  def authenticates(password: Object) = getSetting(adminPassPhrase).get == password
  
}
