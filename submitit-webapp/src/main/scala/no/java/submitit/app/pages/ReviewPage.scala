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

class ReviewPage(p: Presentation) extends LayoutPage with common.LoggHandling {
  
  val supportedExtensions = SubmititApp.getListSetting(presentationAllowedExtendsionFileTypes)
  val editAllowed = SubmititApp.boolSetting(globalEditAllowedBoolean) || (p.status == Status.Approved && SubmititApp.boolSetting(globalEditAllowedForAcceptedBoolean))
  
  add(new FeedbackPanel("systemFeedback"))
  
  add(new HiddenField("showEditLink") {
	  override def isVisible = State().isNew || editAllowed
  })
  
  add(new HiddenField("showSubmitLink") {
    override def isVisible = State().isNew
  })

  add(new HiddenField("showSubmitUpdatedLink") {
	  override def isVisible = !State().isNew && editAllowed
  })

  add(new HiddenField("showNewLink") {
	  override def isVisible = !State().isNew && State().submitAllowed
  })
  
  add(new HiddenField("showRoom") {
  	override def isVisible = SubmititApp.boolSetting(showRoomWhenApprovedBoolean) && p.status == Status.Approved && p.room != null
  })
  
  add(new HiddenField("showTimeslot") {
  	override def isVisible = SubmititApp.boolSetting(showTimeslotWhenApprovedBoolean) && p.status == Status.Approved && p.timeslot != null
  })
  
  val showTags = (SubmititApp.boolSetting(showUserSelectedKeywordsInReviewPageWhenEditNotAllowedBoolean) && 
                 !SubmititApp.boolSetting(globalEditAllowedBoolean) && 
                 !State().isNew && 
                 p.status != Status.NotApproved) &&
                 !(p.status == Status.Approved && 
                 SubmititApp.boolSetting(globalEditAllowedForAcceptedBoolean))
  
  add(new HiddenField("showTags") {
  	override def isVisible = showTags
  })
  
  add(new HiddenField("viewTags") {
  	override def isVisible = !showTags
  })

  add(new Form("saveTagsForm"){
    add(new panels.TagsPanel("tags", p, true))
    override def onSubmit {
    	if(State().isNew) error("Not allowed to update an abstracts keywords, when abstract has not yet been saved. This should never be possible bacause of an invariant.")
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
  
  val statusMsg = if (!State().fromServer) "Not submitted"
                  else if (!SubmititApp.boolSetting(showActualStatusInReviewPageBoolean)) Status.Pending.toString
                  else p.status.toString
  
  add(new Label("status", statusMsg))
  add(new Label("room", p.room))
  add(new Label("timeslot", p.timeslot))
  add(new NewPresentationLink("newPresentation"))

  val msg = if (State().isNew) SubmititApp.getSetting(reviewPageBeforeSubmitHtml)
  	else if (!State().isNew && !editAllowed) SubmititApp.getSetting(reviewPageViewSubmittedHthml)
  	else if (!State().isNew && editAllowed) SubmititApp.getSetting(reviewPageViewSubmittedChangeAllowedHthml)
  	else SubmititApp.getSetting(reviewPageViewSubmittedHthml)

  add(new HtmlLabel("viewMessage", msg))

  val feedback = if(p.status == Status.NotApproved) {
    						   if(SubmititApp.boolSetting(allowIndidualFeedbackOnRejectBoolean) && p.hasFeedback) Some(p.feedback)
                   else if(SubmititApp.boolSetting(showSpecialMessageOnRejectBoolean)) Some(SubmititApp.getSetting(feedbackRejected))
                 	 else None
                 }
                 else if (SubmititApp.boolSetting(showFeedbackBoolean) && p.hasFeedback) Some(p.feedback)
                 else None
  
  add(new MultiLineLabel("feedback", feedback.getOrElse("")) {
    override def isVisible = feedback.isDefined
  })
  

  val uploadForm = new FileUploadForm("extension", supportedExtensions) {
    
    override def onSubmit {
      // what to do??
      info("Thank you for uploading your slides")
    }
    override def isVisible = SubmititApp.boolSetting(allowSlideUploadBoolen) && p.status == Status.Approved
  }
  uploadForm.setMaxSize(Bytes.megabytes(SubmititApp.intSetting(presentationUploadSizeInMBInt)))
  uploadForm.add(new Label("uploadSlideText", "Upload your slides (max " + SubmititApp.intSetting(presentationUploadSizeInMBInt) + " MB). Supported file types: "))
  add(uploadForm)
  
  
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
  
  add(new ListView("speakers", p.speakers.reverse) {
    override def populateItem(item: ListItem) {
      val speaker = item.getModelObject.asInstanceOf[Speaker]
      item.add(new Label("name", speaker.name))
      item.add(new Label("email", speaker.email))
      item.add(new WikiMarkupText("bio", speaker.bio))
      item add (if (speaker.picture != null) new NonCachingImage("image", new ByteArrayResource(speaker.picture.contentType, speaker.picture.content))
                  else new Image("image", new ContextRelativeResource("images/question.jpeg")))
    }
  })
  
  
  add(new PageLink("submitLink", new IPageLink {
    def getPage = new ConfirmPage(p)
    def getPageIdentity = classOf[ConfirmPage]
  }))
      
  add(new PageLink("editLink",new IPageLink {
    def getPage = new SubmitPage(p)
    def getPageIdentity = classOf[SubmitPage]
  }))
  
}
