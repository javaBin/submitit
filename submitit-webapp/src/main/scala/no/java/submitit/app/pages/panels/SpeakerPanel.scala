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
import org.apache.wicket.MarkupContainer
import no.java.submitit.model._
import no.java.submitit.common.Implicits._
import no.java.submitit.app.Functions._
import no.java.submitit.app.pages.{FileUploadForm, EasyForm}
import no.java.submitit.app.State

class SpeakerPanel(val pres: Presentation, enclosingForm: Form[_]) extends Panel("speakers") {
  
  val supportedExtensions = List("jpg", "jpeg", "png", "gif")
  def extensionRegex = ("""(?i)\.""" + supportedExtensions.mkString("(?:", "|", ")") + "$").r

  def model = {
    type T = _root_.java.util.List[Speaker]
    new IModel[T] {
      def getObject(): T = pres.speakers.reverse
      def setObject(obj: T) {}
      def detach() {}
    }
  }

  add(new AjaxSubmitLink("newSpeaker", enclosingForm) {
    override def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
      pres.addSpeaker()
      target.addComponent(SpeakerPanel.this)
      target.appendJavascript("new Effect.Highlight($('" + SpeakerPanel.this.getMarkupId() + "'));");
    }
  })
  

  
  val speakersList = new ListView("speakerList", model) {
    override def populateItem(item: ListItem[Speaker]) {
      val speakersForm = new MarkupContainer("speakersForm") with EasyForm
      speakersForm.setOutputMarkupId(true)
      item.setOutputMarkupId(true)
      item.add(speakersForm)
      
      val speaker = item.getModelObject
      speakersForm.add(new TextField("speakerName", new PropertyModel[Speaker](speaker, "name")))
      
      val email = new TextField("email", new PropertyModel[Speaker](speaker, "email"))
      val nationality = new TextField("nationality", new PropertyModel[Speaker](speaker, "nationality"))
      val zipcode = new TextField("zipcode", new PropertyModel[Speaker](speaker, "zipcode"))
      
      speakersForm.add(email)
      speakersForm.add(nationality)
      speakersForm.add(zipcode)
      speakersForm.addPropTA("bio", speaker, "bio")
      speakersForm.addHelpLink("speakersProfileHelp", true)
      
      item.add(new AjaxSubmitLink("remove", enclosingForm) {
        override def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
          pres.removeSpeaker(speaker) 
          target.addComponent(item)
          target.appendJavascript("new Effect.Fade($('" + item.getMarkupId() + "'));");
        }	
      })
      
      
      // Add upload form with ajax progress bar
      val uploadForm = new FileUploadForm("simpleUpload") {
      
        override def onSubmit {
          val uploadRes = getFileContents(fileUploadField.getFileUpload())
          if (uploadRes.isDefined) {
            val (fileName, stream, contentType) = uploadRes.get
            // Create a new file
            if(hasExtension(fileName, supportedImageExtensions)){
                speaker.picture = Some(Binary(fileName, contentType, Some(stream)))
                State().addBinary(speaker.picture.get)
              }
            else {
            	error(fileName + " has an unsupported file type")
            }
          }
        }
         add(new Label("extension", supportedImages.mkString(", ")))
      }
      speakersForm.add(uploadForm);
      
      item.add(new Label("fileName", if (speaker.picture.isDefined) speaker.picture.get.name else null) {
        override def isVisible = speaker.picture != null
      })	
    }
  }	

  setOutputMarkupId(true)

  add(speakersList)
  
}
