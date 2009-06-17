package no.java.submitit.app.pages.panels

import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.panel._
import org.apache.wicket.model._
import org.apache.wicket.markup.html.list._
import common.Implicits._
import model.Presentation

class TagsPanel(id: String, presentation: Presentation) extends Panel(id) {
  
  val multipleCheckBox = new CheckBoxMultipleChoice("tagElement", new PropertyModel(presentation, "keywords"), SubmititApp.getListSetting("userSelectedKeywords"))
  multipleCheckBox.setPrefix("<div class=\"tagsDivs\">")
  multipleCheckBox.setSuffix("</div>")
  
  add(multipleCheckBox)
  
}
