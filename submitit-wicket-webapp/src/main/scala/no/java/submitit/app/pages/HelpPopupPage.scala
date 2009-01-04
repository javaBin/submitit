package no.java.submitit.app.pages

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model._
import org.apache.wicket.ajax.markup.html._
import org.apache.wicket.ajax._
import org.apache.wicket.extensions.ajax.markup.html.modal._
import org.apache.wicket.markup.html.panel._
import org.apache.wicket.markup.html.form.HiddenField
  

class HelpPopupPanel(resourceKey: String, id: String, supportsMarkup: Boolean) extends Panel(id) {
  
  
  add(new HiddenField("showWikiMarkup") {
    override def isVisible = supportsMarkup
  })
  
  val htmlContent = getLocalizer.getString(resourceKey, this)

  val res = new Label("content")
  res.setEscapeModelStrings(false)
  res.setModel(new Model(htmlContent))
  
  add(res)
  
}
