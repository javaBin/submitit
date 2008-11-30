package no.java.submitit.app

import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource
import org.apache.wicket.markup.html.image.Image

class Captcha {

  def randomString(min: Int, max: Int) = {
    def randomInt(min: Int, max: Int):Int = (Math.random * (max - min)).asInstanceOf[Int] + min
    
    val num = randomInt(min, max)
    val res = for(i <- 0 to 5) yield randomInt('a', 'z').asInstanceOf[Byte]
    new String(res.toArray)
  }
  
  val imagePass = randomString(0, 100)
  
  val image = new Image("captchaImage", new CaptchaImageResource(imagePass))
  
}
