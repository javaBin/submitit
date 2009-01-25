package no.java.submitit.app.pages.widgets

import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model._
import org.apache.wicket.util.string.Strings.escapeMarkup
import scala.util.matching._
import _root_.no.java.ems.wiki._

class WikiMarkupText (id: String, label: String) extends Label(id) {
  
  def wikiEngine = new DefaultWikiEngine(new DefaultHtmlWikiSink())
  
  def result = 
    if(label != null) {
      wikiEngine.transform(escapeMarkup(label).toString)
      wikiEngine.getSink.toString
    }	
    else null

  setEscapeModelStrings(false)
  setModel(new Model(result))

}