package no.java.submitit.app.pages

import org.apache.wicket.markup.html.form.HiddenField
import org.apache.wicket.markup.html.image._
import org.apache.wicket.resource._
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.markup.html.list._
import org.apache.wicket.model._
import org.apache.wicket.markup.html.link._
import model._
import widgets._
import common.Implicits._
import app.State

class ReviewPage(p: Presentation) extends LayoutPage {
  
  val editAllowed = SubmititApp.boolSetting("globalEditAllowedBoolean")
  add(new HiddenField("locked") {
    override def isVisible = !State().lockPresentation
  })
  add(new HiddenField("unlocked") {
    override def isVisible = State().lockPresentation
  })
  
  add(new NewPresentationLink("newPresentation"))
  
  add(new HtmlLabel("reviewBeforeSubmitMsg", SubmititApp.getSetting("reviewPageBeforeSubmitHtml")))
  add(new HtmlLabel("viewSubmittedMsg", SubmititApp.getSetting("reviewPageViewSubmittedHthml")))
  
  add(new Label("title", p.title))
  add(new WikiMarkupText("summary", p.summary))
  add(new WikiMarkupText("abstract", p.abstr))
  add(new Label("language", p.language.toString))
  add(new Label("level", p.level.toString))
  add(new Label("duration", p.duration.toString))
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
  
  add(new PageLink("submitLink", new LazyPageLink(new ConfirmPage(p), classOf[ConfirmPage])))
  add(new PageLink("editLink",  new LazyPageLink(new SubmitPage(p), classOf[SubmitPage])))
  
}
