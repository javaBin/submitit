package no.java.submitit.app.pages

import org.apache.wicket.markup.html.form.upload._
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.basic.Label

abstract class FileUploadForm(extensionWicketId: String, supportedExtensions: List[String]) extends Form("simpleUpload") {
  protected val fileUploadField : FileUploadField = new FileUploadField("fileInput")
  
  add(new Label(extensionWicketId, supportedExtensions.mkString(", ")))
  
  // set this form to multipart mode (allways needed for uploads!)
  setMultiPart(true);
  
  // Add one file input field
  add(fileUploadField);
  
  protected def onSubmit: Unit
}