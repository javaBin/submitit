package no.java.submitit.app.pages

import org.apache.wicket.markup.html.form._
import org.apache.wicket.model.PropertyModel

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
  
  private def pm(any: Any, propertyName: String) = new PropertyModel(any, propertyName)
  
}
