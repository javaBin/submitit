package no.java.submitit.app.pages.widgets

class HtmlLabel(id: String, value:String) extends org.apache.wicket.markup.html.basic.Label(id){
  setEscapeModelStrings(false)
  setModel(new org.apache.wicket.model.Model(value))
}
