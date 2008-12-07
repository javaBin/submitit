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
    
    // set this form to multipart mode (allways needed for uploads!)
    setMultiPart(true);

    // Add one file input field
    add(fileUploadField);

    // Set maximum size to 100K for demo purposes
    setMaxSize(Bytes.kilobytes(500));

    override def onSubmit {
      val upload = fileUploadField.getFileUpload();
      if (upload != null) {
          // Create a new file
          val fileName = upload.getClientFileName
          val bytes = upload.getBytes
          speaker.picture = new Picture(bytes, fileName)
      }
    }
    override def isVisible = speaker.picture == null
    
  }
  
  val speakersList = new ListView("speakerList", model) {
    override def populateItem(item: ListItem) {
      val speakersForm = new Form("speakersForm")
      speakersForm.setOutputMarkupId(true)
      item.setOutputMarkupId(true)
      item.add(speakersForm)
      
      val speaker = item.getModelObject.asInstanceOf[Speaker]
      speakersForm.add(new TextField("speakerName", new PropertyModel(speaker, "name")))
      
      val email = new TextField("email", new PropertyModel(speaker, "email"))
      email.add(RfcCompliantEmailAddressValidator.getInstance())
      
      speakersForm.add(email)
      speakersForm.add(new TextArea("bio", new PropertyModel(speaker, "bio")))
      item.add(new AjaxSubmitLink("remove", speakersForm) {
        override def onSubmit(target: AjaxRequestTarget, form: Form) {
          State.get.currentPresentation.removeSpeaker(speaker) 
          target.addComponent(item)
          target.appendJavascript("new Effect.Fade($('" + item.getMarkupId() + "'));");
        }	
      })
      
      
      // Add upload form with ajax progress bar
      val ajaxSimpleUploadForm = new FileUploadForm(speaker) 
      ajaxSimpleUploadForm.add(new UploadProgressBar("progress", ajaxSimpleUploadForm));
      item.add(ajaxSimpleUploadForm);
      val uploadFeedback = new FeedbackPanel("uploadFeedback");
      ajaxSimpleUploadForm.add(uploadFeedback);
      
      
      item.add(new Label("fileName", if (speaker.picture != null) speaker.picture.name else null) {
        override def isVisible = speaker.picture != null
      })	
    }
  }	
  

  

  
  setOutputMarkupId(true)

  add(speakersList)
  
  
}
