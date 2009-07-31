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

import org.apache.wicket.markup.html.form.HiddenField
import org.apache.wicket.markup.html.image._
import org.apache.wicket.resource._
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.markup.html.list._
import org.apache.wicket.model._
import org.apache.wicket.markup.html.link._
import org.apache.wicket.util.lang.Bytes
import model._
import widgets._
import common.Implicits._
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.markup.html.form.Form
import app.State
import org.apache.wicket.markup.html.panel.FeedbackPanel
import DefaultConfigValues._
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar
import Functions._

class ReviewPage(p: Presentation, notAdminView: Boolean) extends LayoutPage with common.LoggHandling {
  
  val supportedExtensions = SubmititApp.getListSetting(presentationAllowedExtendsionFileTypes)
  val editAllowed = SubmititApp.boolSetting(globalEditAllowedBoolean) || (p.status == Status.Approved && SubmititApp.boolSetting(globalEditAllowedForAcceptedBoolean))
  
  private def showLink(shouldShow: Boolean) = notAdminView && shouldShow 
  
  
  add(new FeedbackPanel("systemFeedback"))
  
  add(new HiddenField("showEditLink") {
	  override def isVisible = showLink(p.isNew || editAllowed)
  })
  
  add(new HiddenField("showSubmitLink") {
    override def isVisible = showLink(p.isNew || editAllowed)
  })
  
  add(new HiddenField("showNewLink") {
	  override def isVisible = showLink(!p.isNew && State().submitAllowed)
  })
  
  add(new HiddenField("showRoom") {
  	override def isVisible = SubmititApp.boolSetting(showRoomWhenApprovedBoolean) && p.status == Status.Approved && p.room != null
  })
  
  add(new HiddenField("showTimeslot") {
  	override def isVisible = SubmititApp.boolSetting(showTimeslotWhenApprovedBoolean) && p.status == Status.Approved && p.timeslot != null
  })
  
  val showTags = (SubmititApp.boolSetting(showUserSelectedKeywordsInReviewPageWhenEditNotAllowedBoolean) && 
                 !SubmititApp.boolSetting(globalEditAllowedBoolean) && 
                 !p.isNew && 
                 p.status != Status.NotApproved) &&
                 !(p.status == Status.Approved && 
                 SubmititApp.boolSetting(globalEditAllowedForAcceptedBoolean))
  
  add(new HiddenField("showTags") {
  	override def isVisible = showLink(showTags)
  })
  
  add(new HiddenField("viewTags") {
  	override def isVisible = showLink(!showTags)
  })

  add(new Form("saveTagsForm"){
    add(new panels.TagsPanel("tags", p, true))
    override def onSubmit {
    	if(p.isNew) error("Not allowed to update an abstracts keywords, when abstract has not yet been saved. This should never be possible bacause of an invariant.")
    	else try {
    	  	State().backendClient.savePresentation(p)
    	  	info("Updated the tags on your submission")
        }
    	  catch {
    	    case e => {
    	      logger.warn("Unable to save keywords on abstract", e)
    	      info("Could not save specified tags. Failure has been logged. Please send your keywords to " + SubmititApp.getOfficialEmail)
           }
         }
    }
  })
  
  val statusMsg = if (p.isNew) "Not submitted"
                  else if (notAdminView && !SubmititApp.boolSetting(showActualStatusInReviewPageBoolean)) Status.Pending.toString
                  else p.status.toString
  
  add(new Label("status", statusMsg))
  add(new Label("room", p.room))
  add(new Label("timeslot", p.timeslot))
  add(new NewPresentationLink("newPresentation"))

  val msg = if (p.isNew) SubmititApp.getSetting(reviewPageBeforeSubmitHtml).getOrElse("")
  	else if (!p.isNew && !editAllowed) SubmititApp.getSetting(reviewPageViewSubmittedHthml).getOrElse("")
  	else if (!p.isNew && editAllowed) SubmititApp.getSetting(reviewPageViewSubmittedChangeAllowedHthml).getOrElse("")
  	else SubmititApp.getSetting(reviewPageViewSubmittedHthml).getOrElse("")

  add(new HtmlLabel("viewMessage", msg))

  val feedback = if(p.status == Status.NotApproved) {
    						   if(SubmititApp.boolSetting(allowIndidualFeedbackOnRejectBoolean) && p.hasFeedback) Some(p.feedback)
                   else if(SubmititApp.boolSetting(showSpecialMessageOnRejectBoolean)) Some(SubmititApp.getSetting(feedbackRejected).getOrElse(""))
                 	 else None
                 }
                 else if (SubmititApp.boolSetting(showFeedbackBoolean) && p.hasFeedback) Some(p.feedback)
                 else None
  
  add(new MultiLineLabel("feedback", feedback.getOrElse("")) {
    override def isVisible = feedback.isDefined
  })
  
  
  add(createUploadForm("pdfForm", "uploadSlideText", "You must upload pdf for publishing online. (max " + SubmititApp.intSetting(presentationUploadSizeInMBInt) + " MB). Supported file types: "+ supportedExtensions.mkString(", "),
                       SubmititApp.intSetting(presentationUploadPdfSizeInMBInt),
                       hasExtension(_, extensionRegex(supportedExtensions)),
                       p.pdfSlideset = _
  ))
  add(createUploadForm("slideForm", "uploadSlideText", "You can upload slides as backup for your presentation. This will be available for you at the venue (max " + SubmititApp.intSetting(presentationUploadPdfSizeInMBInt) + " MB)", 
                       SubmititApp.intSetting(presentationUploadPdfSizeInMBInt),
                       hasntExtension(_, extensionRegex(List("pdf"))),
                       p.slideset = _
  ))
  
  def createUploadForm(formId: String, titleId: String, titleText: String, maxFileSize: Int, fileNameValidator: String => Boolean, assign: Some[Binary] => Unit) = new FileUploadForm(formId) {
    override def onSubmit {
      val uploadRes = getFileContents(fileUploadField.getFileUpload)
    	if (uploadRes.isDefined) {
    		val (fileName, bytes, contentType) = uploadRes.get
    		if(fileNameValidator(fileName)) {
    			assign(Some(Binary(fileName, contentType, bytes)))
    			State().backendClient.savePresentation(p)
    			setResponsePage(new ReviewPage(p, true))
    			info(fileName + " uploaded successfully")
    		}
    		else {
    			error("Upload does not have correct file type")
    		}
      }
    }
    override def isVisible = SubmititApp.boolSetting(allowSlideUploadBoolen) && p.status == Status.Approved
    add(new UploadProgressBar("progress", this));
    setMaxSize(Bytes.megabytes(maxFileSize))
    add(new Label(titleId, titleText))
  }
  
  
  add(new Label("title", p.title))
  add(new WikiMarkupText("summary", p.summary))
  add(new WikiMarkupText("abstract", p.abstr))
  add(new Label("language", p.language.toString))
  add(new Label("level", p.level.toString))
  add(new Label("format", p.format.toString))
  add(new WikiMarkupText("outline", p.outline))
  add(new WikiMarkupText("equipment", p.equipment))
  add(new WikiMarkupText("expectedAudience", p.expectedAudience))
  add(new panels.TagsPanel("unmodifiableTags", p, false))
  
  add(createFileLabel("pdfName", p.pdfSlideset))
  add(createFileLabel("slideName", p.slideset))
  
  private def createFileLabel(id: String, binary: Option[Binary]) = new Label(id) {
  	setModel(if (binary.isDefined) new Model(binary.get.name) else new Model(""))
  	override def isVisible = binary.isDefined
  }
  
  add(new ListView("speakers", p.speakers.reverse) {
    override def populateItem(item: ListItem) {
      val speaker = item.getModelObject.asInstanceOf[Speaker]
      item.add(new Label("name", speaker.name))
      item.add(new Label("email", speaker.email))
      item.add(new WikiMarkupText("bio", speaker.bio))
      
      if (speaker.picture.isDefined) {
        val picture = speaker.picture.get
        item add (new NonCachingImage("image", new ByteArrayResource(picture.contentType, picture.content.get)))
      }
      else {
       item add new Image("image", new ContextRelativeResource("images/question.jpeg"))
      }
    }
  })
  
  
  val submitLink = new PageLink("submitLink", new IPageLink {
    def getPage = new ConfirmPage(p)
    def getPageIdentity = classOf[ConfirmPage]
  })
  
  submitLink.add(new Label("submitLinkMessage", if(p.isNew) "Submit presentation" else "Submit updated presentation"))
  add(submitLink)
      
  add(new PageLink("editLink",new IPageLink {
    def getPage = new EditPage(p)
    def getPageIdentity = classOf[EditPage]
  }))
  
}
