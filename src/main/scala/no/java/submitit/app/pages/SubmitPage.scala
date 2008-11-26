package no.java.submitit.app.pages

import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.model._
import no.java.submitit.model._
import org.apache.wicket.PageParameters
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.RequiredTextField
import org.apache.wicket.markup.html.image.Image
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.util.value.ValueMap
import no.java.submitit.app._


class SubmitPage extends LayoutPage {
  
  val state = State.get

  def randomString(min: Int, max: Int) = {
    def randomInt(min: Int, max: Int):Int = {
      (Math.random * (max - min)).asInstanceOf[Int] + min
    }
    
    val num = randomInt(min, max)
    val res = for(i <- 0 to 8) yield randomInt('a', 'z').asInstanceOf[Byte]
    new String(res.toArray)
  }
  
  /** Random captcha password to match against. */
  val imagePass = randomString(6, 8)
  
  val (pres, isNew) = 
    if (state.currentPresentation == null) {
      state.currentPresentation = new Presentation
      (state.currentPresentation, true)
    } else {
      (state.currentPresentation, false)
    } 
    
  val properties = new ValueMap();
  
  def password = properties.getString("password");
  
  add(new Form("inputForm") {
    
    add(new TextField("title",  new PropertyModel(pres, "title")))
    add(new TextArea("theabstract",  new PropertyModel(pres, "abstr")))
    add(new FeedbackPanel("feedback"))
    
    add(new Image("captchaImage", new CaptchaImageResource(imagePass)))
    
    add(new RequiredTextField("password", new PropertyModel(properties, "password")) {
      override def onComponentTag(tag: ComponentTag)
      {
        super.onComponentTag(tag)
        // clear the field after each render
        tag.put("value", "")
      }
    })
    
    override def onSubmit() {
      if (imagePass != password) {
        error("Wrong captcha password");
      }
      else {
        setResponsePage(classOf[ReviewPage])
      }
    }
    
  })
  
}
