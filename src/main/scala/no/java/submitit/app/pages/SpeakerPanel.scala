package no.java.submitit.app.pages

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.form._
import model._
import org.apache.wicket.markup.html.list._
import Implicits._
import org.apache.wicket.model.PropertyModel

class SpeakerPanel(speakers: List[Speaker]) extends Panel("speakers") {

  add(new Button("newSpeaker") {
    override def onSubmit {
      State.get.currentPresentation.addSpeaker()
      setResponsePage(classOf[SubmitPage])
    }
  })
  
  add(new ListView("speakerList",  speakers.reverse) {
    override def populateItem(item: ListItem) {
      val speaker = item.getModelObject.asInstanceOf[Speaker]
      item.add(new TextField("speakerName", new PropertyModel(speaker, "name")))
      
      item.add(new Button("remove") {
        override def onSubmit {
          State.get.currentPresentation.removeSpeaker(speaker)
          setResponsePage(classOf[SubmitPage])
        }	
      })	
    }
  })
}
