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
import app.State

class ReviewPage(p: Presentation) extends LayoutPage {
  
  val supportedExtensions = List("pdf, ppt, key, odp")
  val editAllowed = SubmititApp.boolSetting("globalEditAllowedBoolean")
  
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

  
  val presentationMsg = if (!State().fromServer) "Not submitted"
                        else p.status.toString
  
  add(new Label("status", presentationMsg))
  add(new NewPresentationLink("newPresentation"))

  val msg = if (State().isNew) SubmititApp.getSetting("reviewPageBeforeSubmitHtml")
  	else if (!State().isNew && !editAllowed) SubmititApp.getSetting("reviewPageViewSubmittedHthml")
  	else if (!State().isNew && editAllowed) SubmititApp.getSetting("reviewPageViewSubmittedChangeAllowedHthml")
  	else SubmititApp.getSetting("reviewPageViewSubmittedHthml")

  add(new HtmlLabel("viewMessage", msg))

  val feedback = if(p.status == Status.NotApproved && 
                   SubmititApp.boolSetting("showSpecialMessageOnRejectBoolean") &&
  								 !(SubmititApp.boolSetting("allowIndidualFeedbackOnRejectBoolean") && p.feedback != null)) { 
    							    SubmititApp.getSetting("feedbackRejected")
               	 }
                 else p.feedback
  
  add(new MultiLineLabel("feedback", feedback) {
    override def isVisible = SubmititApp.boolSetting("showFeedbackBoolean") && feedback != null && feedback != ""
  })
  

  val uploadForm = new FileUploadForm("extension", supportedExtensions) {
    override def onSubmit {
      // what to do??
      info("Thank you for uploading your slides")
    }
    override def isVisible = SubmititApp.boolSetting("allowSlideUploadBoolen") && p.status == Status.Approved
  }
  uploadForm.setMaxSize(Bytes.megabytes(2))
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
