/*
 * Copyright 2011 javaBin
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

package no.java.submitit.app.pages

import borders.ContentBorder
import no.java.submitit.app.{SubmititApp, State}
import org.apache.wicket.markup.html.WebPage
import no.java.submitit.common._
import no.java.submitit.model._
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model.Model
import no.java.submitit.encrypt.EncryptionUtils
import no.java.submitit.config.Keys._
import java.security.GeneralSecurityException

class IdResolverPage extends LayoutPage with LoggHandling {

  val id = getRequest.getParameter("id")

  val (presentation, infoMessage) =
    try {
      if(id == null || id == "") (None, "Not a valid url. This has been logged.")
      else if (id == Presentation.testPresentationURL) (Some(State().currentPresentation), "")
      else fetchPresentation(id) match {
        case Some(pres) => (Some(pres), "")
        case None => (None, "Not a valid key! Attempt to fetch a presentation with this key has been logged.")
      }
    }
    catch {
      case x => {
        logger.warn("Got exception when trying to retrieve session with id: '" + id + "' from EMS", x)
        (None, "Got an exception while trying to get your presentation. This may be because of maintenance. " +
               "Please try again later.\n" +
               "You should not worry, we have backup of your presentation in several different forms." +
               "\nIf the problem continues you should notify the programme committee at " + SubmititApp.getOfficialEmail + ".")
    }
  }

  presentation match {
    case Some(pres) => {
      State().clearBinaries()
      State().currentPresentation = pres
      State().updateBinaries()
      setResponsePage(new ReviewPage(pres, true))
    }
    case None => {
      contentBorder.add(new Label("identified", new Model(infoMessage)))
    }
  }

  private def fetchPresentation(id: String) = {
    try {
      val decryptedId = EncryptionUtils.decrypt(SubmititApp.setting(encryptionKey), id)
      State().backendClient.loadPresentation(decryptedId)
    }
    catch {
      case e: GeneralSecurityException =>  {
        logger.warn("Exception when decrypting key, id tampering? Key: '" + id + "'. Exception message: " + e.getMessage)
        None
      }
    }
  }

}
