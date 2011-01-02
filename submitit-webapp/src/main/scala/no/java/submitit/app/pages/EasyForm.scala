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

import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.extensions.ajax.markup.html.modal._
import org.apache.wicket.ajax.markup.html.form._
import org.apache.wicket.ajax.markup.html._
import org.apache.wicket.ajax._
import org.apache.wicket.ajax.form._
import org.apache.wicket.markup.html.form._

trait EasyForm extends org.apache.wicket.MarkupContainer  {

  def requiredText = """\s*\S{2}""".r
  
  def required(value: String, errorMsg: String) {
    if (value == null || requiredText.findFirstIn(value) == None) error(errorMsg)
  }
  
  def required(list: List[_], errorMsg: String) {
    if(list == null || list.isEmpty) error(errorMsg)
  }
  
  def addPropTF[T](id: String, any: T, propertyName: String, visible: Boolean = true) {
    val tf = new TextField(id,  pm(any, propertyName))
    tf.setVisible(visible)
    add(tf)
  }
  
  def addPropTA[T](id: String, any: T, propertyName: String) {
    add(new TextArea(id, pm(any, propertyName)))
  }
    
  def addPropRC[T](id: String, any: Any, propertyName: String, choices: _root_.java.util.List[T]) {
    add(new RadioChoice[T](id, new PropertyModel[T](any, propertyName), choices))
  }
  
  def addPropLabel(id: String, any: Any, propertyName: String) {
    add(new Label(id, pm(any, propertyName)))
  }
  
  private def pm[T](any: T, propertyName: String) = new PropertyModel[T](any, propertyName)
  
  private var modalWindow: ModalWindow = _
  
  def addHelpLink(id: String, wikiMarkup: Boolean) {
    add(new HelpLink(id, wikiMarkup))
  }
  
  
  private class HelpLink(id: String, wikiMarkup: Boolean) extends AjaxLink(id){
    if(modalWindow == null) {
      modalWindow = new ModalWindow("modal")
      EasyForm.this.add(modalWindow);
    }
    override def onClick(target: AjaxRequestTarget) {
      modalWindow.setContent(new HelpPopupPanel(id, modalWindow.getContentId(), wikiMarkup))
      modalWindow.setTitle("Help");
      modalWindow.show(target)
    }
  }
  
}
