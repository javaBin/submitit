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

package no.java.submitit.app.pages.admin

import no.java.submitit.app.SubmititApp
import no.java.submitit.app.DefaultConfigValues._
import no.java.submitit.app.pages.{LoginHandler, LayoutPage}
import no.java.submitit.app.pages.panels.LoginPanel

class ListPresentationLoginPage extends LayoutPage {

	add(new LoginPanel("login", new LoginHandler {
		def onLogin(pwd: String) {
			val authenticated = SubmititApp.getSetting(listAllSubmissionsPassword).isDefined && SubmititApp.getSetting(listAllSubmissionsPassword).get == pwd
      	if (authenticated) {
      		setResponsePage(new ListPresentationsPage)
      	}
      	else {
      		 error("Incorrect password")
      	}
			}
  }))
  
}
