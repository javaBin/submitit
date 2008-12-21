package no.java.submitit.app.pages

import org.apache.wicket.markup.html.panel._
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator
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

class SpeakerPanel(val pres: Presentation) extends Panel("speakers") {
  val that = this
  
  val supportedExtensions = List("jpg", "jpeg", "png", "gif")
  val extensionRegex = ("""(?i)\.""" + supportedExtensions.mkString("(?:", "|", ")") + "$").r 
  

  val model = new IModel {
    def getObject():_root_.java.util.List[_] = pres.speakers.reverse
    def setObject(obj: Object) {}
    def detach() {}
  }

  val newSpeakerForm = new Form("newSpeakerForm")
  newSpeakerForm.setOutputMarkupId(true)
  add(newSpeakerForm)

  
  add(new AjaxSubmitLink("newSpeaker", newSpeakerForm) {
    override def onSubmit(target: AjaxRequestTarget, form: Form) {
      State.get.currentPresentation.addSpeaker()
      target.addComponent(that)
      target.appendJavascript("new Effect.Highlight($('" + that.getMarkupId() + "'));");
    }
  })
  

  
  class FileUploadForm(speaker: Speaker) extends Form("ajax-simpleUpload") {
    private val fileUploadField : FileUploadField = new FileUploadField("fileInput")
    
    add(new Label("extension", supportedExtensions.mkString(", ")))
    
    // set this form to multipart mode (allways needed for uploads!)
    setMultiPart(true);

    // Add one file input field
    add(fileUploadField);

    override def onSubmit {
      val upload = fileUploadField.getFileUpload();
      if (upload != null) {
          // Create a new file
          val fileName = upload.getClientFileName
          val bytes = upload.getBytes
          println(extensionRegex)
          extensionRegex.findFirstIn(fileName) match {
            case Some(n) => speaker.picture = new Picture(bytes, fileName, upload.getContentType)
            case None => error(fileName + " has an unsupported file type")
          }
      }
    }
  }
  
  val speakersList = new ListView("speakerList", model) {
    override def populateItem(item: ListItem) {
      val speakersForm = new Form("speakersForm") with EasyForm
      speakersForm.setOutputMarkupId(true)
      item.setOutputMarkupId(true)
      item.add(speakersForm)
      
      item.add(new Label("speakerNumber", (item.getIndex + 1).toString))
      
      val speaker = item.getModelObject.asInstanceOf[Speaker]
      speakersForm.add(new TextField("speakerName", new PropertyModel(speaker, "name")))
      
      val email = new TextField("email", new PropertyModel(speaker, "email"))
      email.add(RfcCompliantEmailAddressValidator.getInstance())
      
      speakersForm.add(email)
      speakersForm.addPropTA("bio", speaker, "bio")
      speakersForm.addHelpLink("speakersProfileHelp", true)
      
      item.add(new AjaxSubmitLink("remove", speakersForm) {
        override def onSubmit(target: AjaxRequestTarget, form: Form) {
          State.get.currentPresentation.removeSpeaker(speaker) 
          target.addComponent(item)
          target.appendJavascript("new Effect.Fade($('" + item.getMarkupId() + "'));");
        }	
      })
      
      
      // Add upload form with ajax progress bar
      val ajaxSimpleUploadForm = new FileUploadForm(speaker) 
      item.add(ajaxSimpleUploadForm);
      
      
      item.add(new Label("fileName", if (speaker.picture != null) speaker.picture.name else null) {
        override def isVisible = speaker.picture != null
      })	
    }
  }	

  setOutputMarkupId(true)

  add(speakersList)
  
}
