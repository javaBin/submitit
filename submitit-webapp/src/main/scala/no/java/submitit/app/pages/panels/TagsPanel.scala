package no.java.submitit.app.pages.panels

import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.panel._
import org.apache.wicket.model._
import org.apache.wicket.markup.html.list._
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import common.Implicits._
import model.Presentation
import _root_.java.io.Serializable

class TagsPanel(id: String, presentation: Presentation, checksEnabled: Boolean) extends Panel(id) {
  
  val text = if(checksEnabled) "Please check topics which relate to your presentation (will be presented as tags in the programme)"
             else "Presentation topics or tags"
  add(new Label("tagstext", text))
  
  val presentationWrapper = new Serializable {
      // Kind of a hack here, so that we may use scala.List in the model object. Need to convert to ArrayList through getters and setters.
      def getKeywords: _root_.java.util.ArrayList[String] = presentation.keywords
      def setKeywords(list: _root_.java.util.ArrayList[String]) = presentation.keywords = list.toArray.foldLeft(List[String]())((l, e) => e.toString :: l)
  }
  
  val multipleCheckBox = new CheckBoxMultipleChoice("tagElement", new PropertyModel(presentationWrapper, "keywords"), SubmititApp.getListSetting("userSelectedKeywords", '|')) {
    override def onComponentTag(tag: ComponentTag) {
      super.onComponentTag(tag)
      setEnabled(checksEnabled)
    }
  }
  
  multipleCheckBox.setPrefix("""<div class="tagsDivs">""")
  multipleCheckBox.setSuffix("</div>")

  
    
    
  add(multipleCheckBox)
  
}