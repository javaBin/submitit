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

package no.java.submitit.app.pages.panels

import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.panel._
import org.apache.wicket.model._
import no.java.submitit.app.pages.LoginHandler

class LoginPanel(id: String, handler: LoginHandler) extends Panel(id) {
  
  override def isVersioned = false
  
  add(new FeedbackPanel("feedback"))

  add(new Form("loginForm") {
    
    override def isVersioned = false

    val passModel = new Model[String]
    add(new PasswordTextField("passPhrase", passModel))
    
    override def onSubmit {
      handler.onLogin(passModel.getObject)
    }
  })
  
}
