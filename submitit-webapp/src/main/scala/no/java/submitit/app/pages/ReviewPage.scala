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

import borders.ContentBorder
import org.apache.wicket.markup.html.form.HiddenField
import org.apache.wicket.markup.html.image._
import org.apache.wicket.resource._
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.markup.html.list._
import org.apache.wicket.model._
import org.apache.wicket.markup.html.link._
import org.apache.wicket.util.lang.Bytes
import no.java.submitit.model._
import widgets._
import no.java.submitit.common.Implicits._
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.panel.FeedbackPanel
import no.java.submitit.app.DefaultConfigValues._
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar
import org.apache.wicket.util.resource.{IResourceStream, FileResourceStream}
import no.java.submitit.app.Functions._
import no.java.submitit.app.{SubmititApp, State}
import no.java.submitit.common.LoggHandling

class ReviewPage(val pres: Presentation, notAdminView: Boolean) extends LayoutPage with LoggHandling with UpdateSessionHandling {
  
  val editAllowed = SubmititApp.boolSetting(globalEditAllowedBoolean) || (pres.status == Status.Approved && SubmititApp.boolSetting(globalEditAllowedForAcceptedBoolean))
  
  private def show(shouldShow: Boolean) = notAdminView && shouldShow

  private def submitLink(name: String) = new PageLink(name, new IPageLink {
    def getPage = new ConfirmPage(pres)
    def getPageIdentity = classOf[ConfirmPage]
  }) {
    // Add this message to to PageLink for proper nesting
    add(new Label("submitLinkMessage", if(pres.isNew) "Submit presentation" else "Submit updated presentation"))
  }


  private def editLink(name: String) = new PageLink(name, new IPageLink {
    def getPage = new EditPage(pres)
    def getPageIdentity = classOf[EditPage]
  })

  menuLinks = submitLink("submitLinkTop") ::
              submitLink("submitLinkBottom") ::
              editLink("editLinkTop") ::
              editLink("editLinkBottom") ::
              new NewPresentationLink("newPresentationTop") ::
              new NewPresentationLink("newPresentationBottom") :: Nil

  add(new HiddenField("showEditLink") {
	  override def isVisible = show(pres.isNew || editAllowed)
  })

  add(new HiddenField("showSubmitLink") {
    override def isVisible = show(pres.isNew || editAllowed)
  })

  add(new HiddenField("showNewLink") {
	  override def isVisible = show(!pres.isNew && State().submitAllowed)
  })

  contentBorder.add(new FeedbackPanel("systemFeedback"))
  
  contentBorder.add(new HiddenField("showRoom") {
  	override def isVisible = SubmititApp.boolSetting(showRoomWhenApprovedBoolean) && pres.status == Status.Approved && pres.room != null
  })
  
  contentBorder.add(new HiddenField("showTimeslot") {
  	override def isVisible = SubmititApp.boolSetting(showTimeslotWhenApprovedBoolean) && pres.status == Status.Approved && pres.timeslot != null
  })
  
  val statusMsg = if (pres.isNew) "Not submitted"
                  else if (notAdminView && !SubmititApp.boolSetting(showActualStatusInReviewPageBoolean)) Status.Pending.toString
                  else pres.status.toString

  contentBorder.add(new panels.LegendPanel)
  contentBorder.add(new Label("status", statusMsg))
  contentBorder.add(new Label("room", pres.room))
  contentBorder.add(new Label("timeslot", pres.timeslot))


  val msg = if (pres.isNew) SubmititApp.getSetting(reviewPageBeforeSubmitHtml).getOrElse("")
  	else if (!pres.isNew && !editAllowed) SubmititApp.getSetting(reviewPageViewSubmittedHthml).getOrElse("")
  	else if (!pres.isNew && editAllowed) SubmititApp.getSetting(reviewPageViewSubmittedChangeAllowedHthml).getOrElse("")
  	else SubmititApp.getSetting(reviewPageViewSubmittedHthml).getOrElse("")

  contentBorder.add(new HtmlLabel("viewMessage", msg))

  val feedback = if(pres.status == Status.NotApproved) {
    						   if(SubmititApp.boolSetting(allowIndidualFeedbackOnRejectBoolean) && pres.hasFeedback) Some(pres.feedback)
                   else if(SubmititApp.boolSetting(showSpecialMessageOnRejectBoolean)) Some(SubmititApp.getSetting(feedbackRejected).getOrElse(""))
                 	 else None
                 }
                 else if (SubmititApp.boolSetting(showFeedbackBoolean) && pres.hasFeedback) Some(pres.feedback)
                 else None
  
  contentBorder.add(new MultiLineLabel("feedback", feedback.getOrElse("")) {
    override def isVisible = feedback.isDefined
  })
  
  
  contentBorder.add(createUploadForm("pdfForm", "uploadSlideText", "You must upload pdf for publishing online. Max file size is " + SubmititApp.intSetting(presentationUploadPdfSizeInMBInt) + " MB.",
                       SubmititApp.intSetting(presentationUploadPdfSizeInMBInt),
                       hasExtension(_, extensionRegex(List("pdf"))),
                       pres.pdfSlideset = _
  ))
  contentBorder.add(createUploadForm("slideForm", "uploadSlideText", "You can upload slides as backup for your presentation. This will be available for you at the venue. Max file size is " + SubmititApp.intSetting(presentationUploadSizeInMBInt) + " MB",
                       SubmititApp.intSetting(presentationUploadSizeInMBInt),
                       hasntExtension(_, extensionRegex(List("pdf"))),
                       pres.slideset = _
  ))
  
  def createUploadForm(formId: String, titleId: String, titleText: String, maxFileSize: Int, fileNameValidator: String => Boolean, assign: Some[Binary] => Unit) = new FileUploadForm(formId) {
    override def onSubmit {
      val uploadRes = getFileContents(fileUploadField.getFileUpload)
    	if (uploadRes.isDefined) {
    		val (fileName, stream, contentType) = uploadRes.get
    		if(fileNameValidator(fileName)) {
    			assign(Some(Binary(fileName, contentType, Some(stream))))
    			State().backendClient.savePresentation(pres)
    			setResponsePage(new ReviewPage(pres, true))
    			info(fileName + " uploaded successfully")
    		}
    		else {
    			error("Upload does not have correct file type")
    		}
      }
    }
    override def isVisible = show(SubmititApp.boolSetting(allowSlideUploadBoolen) && pres.status == Status.Approved)
    add(new UploadProgressBar("progress", this));
    setMaxSize(Bytes.megabytes(maxFileSize))
    add(new Label(titleId, titleText))
  }
  
  
  contentBorder.add(new Label("title", pres.title))
  contentBorder.add(new WikiMarkupText("summary", pres.summary))
  contentBorder.add(new WikiMarkupText("abstract", pres.abstr))
  contentBorder.add(new Label("language", pres.language.toString))
  contentBorder.add(new Label("level", pres.level.toString))
  contentBorder.add(new Label("format", pres.format.toString))
  contentBorder.add(new WikiMarkupText("outline", pres.outline))
  contentBorder.add(new WikiMarkupText("equipment", pres.equipment))
  contentBorder.add(new WikiMarkupText("expectedAudience", pres.expectedAudience))
  contentBorder.add(new panels.TagsPanel("unmodifiableTags", pres, false))
  
  contentBorder.add(createFileLabel("pdfName", pres.pdfSlideset))
  contentBorder.add(createFileLabel("slideName", pres.slideset))
  
  private def createFileLabel(id: String, binary: Option[Binary]) = new Label(id) {
  	setModel(if (binary.isDefined) new Model(binary.get.name) else new Model(""))
  	override def isVisible = binary.isDefined
  }
  
  contentBorder.add(new ListView("speakers", pres.speakers.reverse) {
    override def populateItem(item: ListItem) {
      val speaker = item.getModelObject.asInstanceOf[Speaker]
      item.add(new Label("name", speaker.name))
      item.add(new Label("email", speaker.email))
      item.add(new WikiMarkupText("bio", speaker.bio))
      
      if (speaker.picture.isDefined) {
        val picture = speaker.picture.get
        item add (new NonCachingImage("image", new ByteArrayResource(picture.contentType, null) {
         override def getResourceStream: IResourceStream = new FileResourceStream(picture.getTmpFile)
        }))
      }
      else {
       item add new Image("image", new ContextRelativeResource("images/question.jpeg"))
      }
    }
  })
  
}
