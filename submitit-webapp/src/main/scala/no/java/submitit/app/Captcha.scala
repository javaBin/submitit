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

package no.java.submitit.app

import _root_.java.io.Serializable

import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource
import org.apache.wicket.markup.html.image.Image
import DefaultConfigValues._

class Captcha extends Serializable {

  private def randomString() = {
    def randomInt(min: Int, max: Int):Int = (Math.random * (max - min)).asInstanceOf[Int] + min
    
    val captchaLength = SubmititApp.intSetting(captchaLengthInt) - 1
    val res = for(i <- 0 to captchaLength) yield randomInt('a', 'z').asInstanceOf[Byte]
    new String(res.toArray)
  }
  
  val imagePass = randomString()
  
  val image = new Image("captchaImage", new CaptchaImageResource(imagePass))
  
  var password: String = _
  
}
