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
import no.java.submitit.app._


class SubmitPage extends LayoutPage {
  
  val state = State.get
  state setCaptchaIfNotSet

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
    add(new FeedbackPanel("feedback"))
    
    add(new SpeakerPanel(pres.speakers, this))
    
    if (!verified) add(State.get.captcha.image)
    
    add(new TextField("password", new Model()){
      override def isVisible = !verified
    })
    
    add(new Button("reviewButton"){
      override def onSubmit() { 
        if (!state.verifiedWithCaptha && State.get.captcha.imagePass != password) error("Wrong captcha password")
        else  {
          state.verifiedWithCaptha = true
          setResponsePage(classOf[ReviewPage])
        }
      }
    })
    
    val newCapButton = new Button("captchaButton"){
      override def onSubmit()  {
        setupCatcha
      }
    }
    add(newCapButton)
  })
  
  def setupCatcha {
    state.resetCaptcha
    setResponsePage(classOf[SubmitPage])
  }
    
}
