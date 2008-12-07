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
import org.apache.wicket.markup.html.link._
import no.java.submitit.app._
import common.Implicits._


class SubmitPage extends LayoutPage {
  
  val state = State.get
  state setCaptchaIfNotSet
  val captcha = State.get.captcha

  /** Random captcha password to match against. */
  val (pres, isNew) = 
    if (state.currentPresentation == null) {
      val p = new Presentation
      p.init
      state.currentPresentation = p
      (state.currentPresentation, true)
    } else {
      (state.currentPresentation, false)
    } 
    
  def password = getRequest.getParameter("password");
  
  add(new Form("inputForm") {
    
    val verified = state.verifiedWithCaptha
    add(new TextField("title",  new PropertyModel(pres, "title")))
    add(new TextArea("theabstract",  new PropertyModel(pres, "abstr")))

    add(new TextField("duration",  new PropertyModel(pres, "duration")))
    add(new TextArea("equipment",  new PropertyModel(pres, "equipment")))
    add(new TextArea("requiredExperience",  new PropertyModel(pres, "requiredExperience")))
    add(new TextArea("expectedAudience",  new PropertyModel(pres, "expectedAudience")))
    
    add(new FeedbackPanel("feedback"))
    
    val level = new RadioChoice("level", new PropertyModel(pres, "level"), Level.elements.toList)
    val language = new RadioChoice("language", new PropertyModel(pres, "language"), Language.elements.toList)
    
    add(language)
    add(level)
    
    add(new SpeakerPanel(pres))
    
    if (!verified) add(captcha.image)
    
    add(new TextField("password", new PropertyModel(captcha, "password")){
      override def isVisible = !verified
    })
    
    add(new SubmitLink("reviewButton", this){
      override def onSubmit() { 
        if (!state.verifiedWithCaptha &&captcha.imagePass != password) error("Wrong captcha password")
        else  {
          handleSubmit
        }
      }
    })
    
    val newCapButton = new SubmitLink("captchaButton", this){
      override def onSubmit()  {
        setupCatcha
      }
    }
    add(newCapButton)
    
    def handleSubmit {
      println(pres)
      state.verifiedWithCaptha = true
      setResponsePage(classOf[ReviewPage])
    }
    
  })
  
  
  def setupCatcha {
    state.resetCaptcha
    setResponsePage(classOf[SubmitPage])
  }
    
}
