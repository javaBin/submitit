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

import org.apache.wicket.markup.html.panel._
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.markup.html.form.upload._
import org.apache.wicket.util.lang._
import org.apache.wicket.util.file._
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar
import org.apache.wicket.ajax.markup.html.form._
import org.apache.wicket.ajax.markup.html._
import org.apache.wicket.ajax._
import org.apache.wicket.ajax.form._
import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.list._
import org.apache.wicket.model._
import model._
import common.Implicits._
import Functions._

class SpeakerPanel(val pres: Presentation) extends Panel("speakers") {
  
  val supportedExtensions = List("jpg", "jpeg", "png", "gif")
  def extensionRegex = ("""(?i)\.""" + supportedExtensions.mkString("(?:", "|", ")") + "$").r 
  

  def model = new IModel {
    def getObject():_root_.java.util.List[_] = pres.speakers.reverse
    def setObject(obj: Object) {}
    def detach() {}
  }

  val newSpeakerForm = new Form("newSpeakerForm")
  newSpeakerForm.setOutputMarkupId(true)
  add(newSpeakerForm)

  
  add(new AjaxSubmitLink("newSpeaker", newSpeakerForm) {
    override def onSubmit(target: AjaxRequestTarget, form: Form) {
      State().currentPresentation.addSpeaker()
      target.addComponent(SpeakerPanel.this)
      target.appendJavascript("new Effect.Highlight($('" + SpeakerPanel.this.getMarkupId() + "'));");
    }
  })
  

  
  val speakersList = new ListView("speakerList", model) {
    override def populateItem(item: ListItem) {
      val speakersForm = new Form("speakersForm") with EasyForm
      speakersForm.setOutputMarkupId(true)
      item.setOutputMarkupId(true)
      item.add(speakersForm)
      
      val speaker = item.getModelObject.asInstanceOf[Speaker]
      speakersForm.add(new TextField("speakerName", new PropertyModel(speaker, "name")))
      
      val email = new TextField("email", new PropertyModel(speaker, "email"))
      
      speakersForm.add(email)
      speakersForm.addPropTA("bio", speaker, "bio")
      speakersForm.addHelpLink("speakersProfileHelp", true)
      
      item.add(new AjaxSubmitLink("remove", speakersForm) {
        override def onSubmit(target: AjaxRequestTarget, form: Form) {
          State().currentPresentation.removeSpeaker(speaker) 
          target.addComponent(item)
          target.appendJavascript("new Effect.Fade($('" + item.getMarkupId() + "'));");
        }	
      })
      
      
      // Add upload form with ajax progress bar
      val uploadForm = new FileUploadForm("simpleUpload") {
      
        override def onSubmit {
          val uploadRes = getFileContents(fileUploadField.getFileUpload())
          if (uploadRes.isDefined) {
            val (fileName, bytes, contentType) = uploadRes.get
            // Create a new file
            hasExtension(fileName) match {
              case Some(n) => {
                speaker.picture = Binary(fileName, contentType, bytes)
                State().addBinary(speaker.picture)
              }
              case None => error(fileName + " has an unsupported file type")
            }
          }
        }
         add(new Label("extension", supportedImages.mkString(", ")))
      }
      item.add(uploadForm);
      
      item.add(new Label("fileName", if (speaker.picture != null) speaker.picture.name else null) {
        override def isVisible = speaker.picture != null
      })	
    }
  }	

  setOutputMarkupId(true)

  add(speakersList)
  
}
