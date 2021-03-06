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

package no.java.submitit.app.pages.widgets

import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model._
import org.apache.wicket.util.string.Strings.escapeMarkup
import scala.util.matching._
import _root_.no.java.ems.wiki._

class WikiMarkupText (id: String, label: String) extends Label(id) {

  def result = 
    if(label != null) {
      val wikiEngine = new DefaultWikiEngine(new DefaultHtmlWikiSink())
      wikiEngine.transform(escapeMarkup(label).toString)
      wikiEngine.getSink.toString
    }	
    else null

  setEscapeModelStrings(false)
  setDefaultModel(new Model(result))

}