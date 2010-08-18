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

package no.java.submitit.app.pages.panels

import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.panel._
import org.apache.wicket.model._
import org.apache.wicket.markup.html.list._
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import no.java.submitit.common.Implicits._
import no.java.submitit.model.Presentation
import _root_.java.io.Serializable
import no.java.submitit.app.DefaultConfigValues._
import no.java.submitit.app.SubmititApp
import scala.collection.JavaConversions._

class TagsPanel(id: String, presentation: Presentation, checksEnabled: Boolean) extends Panel(id) {
  
  val text = if(checksEnabled) "Please check topics which relate to your presentation (will be presented as tags in the programme)"
             else "Presentation topics or tags"
  add(new Label("tagstext", text))
  
  val presentationWrapper = new Serializable {
      // Kind of a hack here, so that we may use scala.List in the model object. Need to convert to ArrayList through getters and setters.
      val keywords: _root_.java.util.ArrayList[String] = presentation.keywords
      def getKeywords = keywords
      def setKeywords(list: _root_.java.util.ArrayList[String]) = presentation.keywords = list.toArray.foldLeft(List[String]())((l, e) => e.toString :: l)
  }

  val selection = SubmititApp.getListSetting(userSelectedKeywords, '|')
  val multipleCheckBox = new CheckBoxMultipleChoice("tagElement", new PropertyModel(presentationWrapper, "keywords"), selection) {
    override def isEnabled = checksEnabled
  }
  
  multipleCheckBox.setPrefix("""<div class="tagsDivs">""")
  multipleCheckBox.setSuffix("</div>")

  
    
    
  add(multipleCheckBox)
  
}