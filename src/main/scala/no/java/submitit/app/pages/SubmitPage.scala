package no.java.submitit.app.pages

import org.apache.wicket.markup.html.form._
import org.apache.wicket.model._
import _root_.no.java.submitit.model._
import org.apache.wicket.PageParameters
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.RequiredTextField
import org.apache.wicket.markup.html.image.Image
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.util.value.ValueMap

class SubmitPage extends LayoutPage {
  val pres = new Presentation
  
  
  
  val properties = new ValueMap();

   def randomString(min: Int, max: Int) = {
        def randomInt(min: Int, max: Int):Int = {
          (Math.random * (max - min)).asInstanceOf[Int] + min
        }
        
        val num = randomInt(min, max)
        
        val res = for(i <- 0 to 8) yield randomInt('a', 'z').asInstanceOf[Byte]
        new String(res.toArray)
    }
   
   def password = properties.getString("password");

    /** Random captcha password to match against. */
    val imagePass = randomString(6, 8)
    println(imagePass)
  
  
    add(new InputForm) 
    
  class InputForm extends Form("inputForm") {
    
    add(new TextField("title",  new PropertyModel(pres, "title")))
    add(new TextArea("theabstract",  new PropertyModel(pres, "abstr")))
    
    val captchaImageResource = new CaptchaImageResource(imagePass)
    add(new Image("captchaImage", captchaImageResource))
    
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
        val reviewPage = new ReviewPage(pres)
        setResponsePage(reviewPage)
      }
    }
    
  }
  
}
