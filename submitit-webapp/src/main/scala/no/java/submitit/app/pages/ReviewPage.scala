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
  add(new HiddenField("new") {
    override def isVisible = State().isNew
  })
  add(new HiddenField("notNewModifyAllowed") {
    override def isVisible = State().notNewModifyAllowed
  })
  add(new HiddenField("submitNotAllowed") {
    override def isVisible = !State().submitAllowed
  })
  add(new HiddenField("notNewModifyNotAllowedNewAllowed") {
    override def isVisible = State().notNewModifyNotAllowedNewAllowed
  })
  
  val presentationMsg = if (State().isNew) "Not submitted"
                        else p.status.toString
  
  add(new Label("status", presentationMsg))
  add(new NewPresentationLink("newPresentation"))
  add(new FeedbackPanel("feedbackPanel"))

  
  add(new HtmlLabel("reviewBeforeSubmitMsg", SubmititApp.getSetting("reviewPageBeforeSubmitHtml")))
  add(new HtmlLabel("viewSubmittedMsg1", SubmititApp.getSetting("reviewPageViewSubmittedHthml")))
  add(new HtmlLabel("viewSubmittedMsg2", SubmititApp.getSetting("reviewPageViewSubmittedChangeAllowedHthml")))
  add(new HtmlLabel("viewSubmittedMsg3", SubmititApp.getSetting("reviewPageViewSubmittedHthml")))
  
  val feedback = if(p.status != Status.NotApproved) p.feedback else SubmititApp.getSetting("feedbackRejected")
  
  add(new MultiLineLabel("feedback", feedback) {
    override def isVisible = feedback != null && feedback != ""
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
