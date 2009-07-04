/*
 * Copyright 2009 JavaBin
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