package no.java.submitit.app.pages

import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.extensions.ajax.markup.html.modal._
import org.apache.wicket.ajax.markup.html.form._
import org.apache.wicket.ajax.markup.html._
import org.apache.wicket.ajax._
import org.apache.wicket.ajax.form._
import org.apache.wicket.markup.html.form._

trait EasyForm extends org.apache.wicket.MarkupContainer  {
  
  val requiredText = """\s*\S{2}""".r
  
  def required(value: String, errorMsg: String) {
    if (value == null || requiredText.findFirstIn(value) == None) error(errorMsg)
  }
  
  def required(list: List[_], errorMsg: String) {
    if(list == null || list.isEmpty) error(errorMsg)
  }
  
  def addPropTF(id: String, any: Any, propertyName: String) {
    add(new TextField(id,  pm(any, propertyName)))
  }
  
  def addPropTA(id: String, any: Any, propertyName: String) {
    add(new TextArea(id, pm(any, propertyName)))
  }
    
  def addPropRC(id: String, any: Any, propertyName: String, choices: _root_.java.util.List[_]) {
    add(new RadioChoice(id, pm(any, propertyName), choices))
  }
  
  def addPropLabel(id: String, any: Any, propertyName: String) {
    add(new Label(id, pm(any, propertyName)))
  }
  
  private def pm(any: Any, propertyName: String) = new PropertyModel(any, propertyName)
  
  
  private var modalWindow: ModalWindow = _
  
  def addHelpLink(id: String, wikiMarkup: Boolean) {
    add(new HelpLink(id, wikiMarkup))
  }
  
  
  private class HelpLink(id: String, wikiMarkup: Boolean) extends AjaxLink(id){
    if(modalWindow == null) {
      modalWindow = new ModalWindow("modal")
      EasyForm.this.add(modalWindow);
    }
    override def onClick(target: AjaxRequestTarget) {
      modalWindow.setContent(new HelpPopupPanel(id, modalWindow.getContentId(), wikiMarkup))
      modalWindow.setTitle("Help");
      modalWindow.show(target)
    }
  }
  
}
