package no.java.submitit.app.pages

import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.model._
import no.java.submitit.model._
import org.apache.wicket.PageParameters
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.util.value.ValueMap
import org.apache.wicket.ajax.markup.html._
import org.apache.wicket.ajax._
import org.apache.wicket.extensions.ajax.markup.html.modal._
import org.apache.wicket.markup.html.link._
import no.java.submitit.app._
import common.Implicits._


class SubmitPage extends LayoutPage {
  
  val state = State()
  if (state.lockPresentation) throw new SecurityException("Not allowed to get to this point when submission is locked")
  
  def redirectToReview() {
    setResponsePage(classOf[ReviewPage])
  }
  
  state setCaptchaIfNotSet
  val captcha = State.get.captcha

  val pres = state.currentPresentation
  
  def password = getRequest.getParameter("password");
  
  add(new Form("inputForm") with EasyForm {
    
    val verified = state.verifiedWithCaptha
    
    add(new FeedbackPanel("feedback"))
    addPropTF("title", pres, "title")
    addPropTA("summary", pres, "summary")
    addPropTA("theabstract", pres, "abstr")
    addPropLabel("duration", pres, "duration")
    addPropTA("outline", pres, "outline")
    addPropTA("equipment", pres, "equipment")
    addPropTA("expectedAudience", pres, "expectedAudience")
    addPropRC("level", pres, "level", Level.elements.toList)
    addPropRC("language", pres, "language", Language.elements.toList)
    
    if (!verified) add(captcha.image)
    
    add(new TextField("password", new PropertyModel(captcha, "password")){
      override def isVisible = !verified
    })
    
    private class ReviewLink(id: String, form: Form) extends SubmitLink(id, form){
      override def onSubmit() { 
          handleSubmit
      }
    }
    
    addHelpLink("outlineHelp")
    addHelpLink("expectedAudienceHelp")
    addHelpLink("durationHelp")
    addHelpLink("titleHelp")
    addHelpLink("abstractHelp")
    addHelpLink("levelHelp")
    addHelpLink("languageHelp")
    addHelpLink("speakersHelp")
    addHelpLink("highlightHelp")
    
    add(new ReviewLink("reviewButtonTop", this))
    add(new ReviewLink("reviewButtonBottom", this))
    
    add(new SubmitLink("captchaButton", this){
      override def onSubmit()  {
        state.resetCaptcha()
        setResponsePage(classOf[SubmitPage])
      }
    })
    
    add(new SpeakerPanel(pres))
    
    def handleSubmit() {
      // Some form validation
      if (!state.verifiedWithCaptha && captcha.imagePass != password) error("Wrong captcha password")
      
      required(pres.speakers, "You must specify at least one speaker")
      required(pres.title, "You must specify a title")
      required(pres.abstr, "You must specify an abstract")
      if(pres.duration > 60) error("Max duration is 60 minutes")
      
      pres.speakers.foreach(sp => {
        required(sp.name, "You must specify an a name")
        required(sp.email, "You must specify an email")
        required(sp.bio, "You must specify an bio")
      })
      
      if(!hasErrorMessage) {
        state.verifiedWithCaptha = true
        redirectToReview()
      }
    }
  })
    
}
