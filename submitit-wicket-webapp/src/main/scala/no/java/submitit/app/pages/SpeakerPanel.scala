package no.java.submitit.app.pages

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator
import org.apache.wicket.ajax.markup.html.form._
import org.apache.wicket.ajax.markup.html._
import org.apache.wicket.ajax._
import org.apache.wicket.ajax.form._
import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.list._
import org.apache.wicket.model.IModel
import org.apache.wicket.model.PropertyModel
import model._
import common.Implicits._

class SpeakerPanel(val pres: Presentation) extends Panel("speakers") {

  val that = this

  setOutputMarkupId(true)

  val model = new IModel {
    def getObject():_root_.java.util.List[_] = pres.speakers.reverse
    def setObject(obj: Object) {}
    def detach() {}
  }

  val speakersForm = new Form("speakersForm")
  speakersForm setOutputMarkupId true
  add(speakersForm)

  val speakersList = new ListView("speakerList", model) {
    override def populateItem(item: ListItem) {
      val speaker = item.getModelObject.asInstanceOf[Speaker]
      item.add(new TextField("speakerName", new PropertyModel(speaker, "name")))
      val email = new TextField("email", new PropertyModel(speaker, "email"))
      email.add(RfcCompliantEmailAddressValidator.getInstance())
      item.add(email)
      item.add(new TextArea("bio", new PropertyModel(speaker, "bio")))
      item.add(new AjaxSubmitLink("remove", speakersForm) {
        override def onSubmit(target: AjaxRequestTarget, form: Form) {
          State.get.currentPresentation.removeSpeaker(speaker) 
          target.addComponent(that)
          target.appendJavascript("new Effect.Pulsate($('" + speakersForm.getMarkupId() + "'));");
        }	
      })
    }
  }
  
  add(new AjaxSubmitLink("newSpeaker", speakersForm) {
    override def onSubmit(target: AjaxRequestTarget, form: Form) {
      State.get.currentPresentation.addSpeaker()
      target.addComponent(that)
      target.appendJavascript("new Effect.Pulsate($('" + speakersForm.getMarkupId() + "'));");
    }
  })
  

  speakersForm.add(speakersList)
  
}
