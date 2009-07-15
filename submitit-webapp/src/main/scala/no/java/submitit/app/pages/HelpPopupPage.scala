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

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model._
import org.apache.wicket.ajax.markup.html._
import org.apache.wicket.ajax._
import org.apache.wicket.extensions.ajax.markup.html.modal._
import org.apache.wicket.markup.html.panel._
import org.apache.wicket.markup.html.form.HiddenField
  

class HelpPopupPanel(resourceKey: String, id: String, supportsMarkup: Boolean) extends Panel(id) {
  
  
  add(new HiddenField("showWikiMarkup") {
    override def isVisible = supportsMarkup
  })
  
  val htmlContent = getLocalizer.getString(resourceKey, this)

  val res = new Label("content")
  res.setEscapeModelStrings(false)
  res.setModel(new Model(htmlContent))
  
  add(res)
  
}
