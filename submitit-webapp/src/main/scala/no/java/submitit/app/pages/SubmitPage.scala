/*
 * Copyright 2009 JavaBin
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

package no.java.submitit.app.pages

import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator
import org.apache.wicket.markup.html.basic._
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
import DefaultConfigValues._


class SubmitPage(pres: Presentation, specialInvite: Boolean) extends LayoutPage {
  
  def this(pres: Presentation) {
    this(pres, false)
  }

  def redirectToReview() {
    setResponsePage(new ReviewPage(State().currentPresentation))
  }
  
  State() setCaptchaIfNotSet
  def captcha = State().captcha

  def password = getRequest.getParameter("password");
  
  add(new Form("inputForm") with EasyForm {
    
    val verified = State().verifiedWithCaptha
    
    add(new FeedbackPanel("feedback"))
    add(new widgets.HtmlLabel("infoText", SubmititApp.getSetting(editPageInfoTextHtml)))
    addPropTF("title", pres, "title")
    addPropTA("summary", pres, "summary")
    addPropTA("theabstract", pres, "abstr")
    addPropTA("outline", pres, "outline")
    addPropTA("equipment", pres, "equipment")
    addPropTA("expectedAudience", pres, "expectedAudience")
    addPropRC("level", pres, "level", Level.elements.toList)
    addPropRC("language", pres, "language", Language.elements.toList)
    addPropRC("format", pres, "format", PresentationFormat.elements.toList)
    add(new panels.TagsPanel("tags", pres, true))
    
    if (!verified) add(captcha.image)
    
    add(new TextField("password", new PropertyModel(captcha, "password")){
      override def isVisible = !verified
    })
    
    private class ReviewLink(id: String, form: Form) extends SubmitLink(id, form){
      override def onSubmit() { 
          handleSubmit
      }
    }
    
    addHelpLink("outlineHelp", true)
    addHelpLink("expectedAudienceHelp", true)
    addHelpLink("formatHelp", false)
    addHelpLink("titleHelp", false)
    addHelpLink("abstractHelp", true)
    addHelpLink("levelHelp", false)
    addHelpLink("languageHelp", false)
    addHelpLink("speakersHelp", false)
    addHelpLink("highlightHelp", true)
    
    add(new ReviewLink("reviewButtonTop", this))
    
    add(new SubmitLink("captchaButton", this){
      override def onSubmit()  {
        State().resetCaptcha()
        setResponsePage(new SubmitPage(pres))
      }
    })
    
    add(new panels.SpeakerPanel(pres))
    
    def handleSubmit() {
      // Some form validation
      if (!State().verifiedWithCaptha && captcha.imagePass != password) error("Wrong captcha password")
      
      val emailValidator = RfcCompliantEmailAddressValidator.getInstance().getPattern.matcher("")
      required(pres.speakers, "You must specify at least one speaker")
      required(pres.title, "You must specify a title")
      required(pres.abstr, "You must specify an abstract")
      required(pres.summary, "You must specify highlights")
      
      pres.speakers.foreach(sp => {
        required(sp.name, "You must specify speaker name")
        required(sp.email, "You must specify an email")
        required(sp.bio, "You must specify speaker's profile")
        if (sp.email != null && !emailValidator.reset(sp.email).matches) error("'" + sp.email + "' is not valid email")
      })
      
      if(!hasErrorMessage) {
        State().verifiedWithCaptha = true
        redirectToReview()
      }
    }
  })
    
}
