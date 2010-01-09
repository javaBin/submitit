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

package no.java.submitit.app.pages

import org.apache.wicket.markup.html.basic._
import no.java.submitit.app.DefaultConfigValues._
import no.java.submitit.app.{State, SubmititApp}

class StartPage extends LayoutPage {
  
  if (SubmititApp.boolSetting(submitAllowedBoolean)) {
    State().currentPresentation = null
    getRequestCycle().setRedirect(true);
    setResponsePage(new EditPage(State().currentPresentation))
  }
  else {
    val res = new Label("info", SubmititApp.getSetting(submitNotAllowedHtml).getOrElse(""))
    res.setEscapeModelStrings(false)
    add(res)
  }

}
