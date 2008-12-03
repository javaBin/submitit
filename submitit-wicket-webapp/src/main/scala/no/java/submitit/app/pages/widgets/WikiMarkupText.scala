package no.java.submitit.app.pages.widgets

import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model._
import org.apache.wicket.util.string.Strings.escapeMarkup
import scala.util.matching._
import WikiMarkupText._

class WikiMarkupText (id: String, label: String) extends MultiLineLabel(id) {
  
  val bullets = """^(\*{1,3}) (.+)""".r
  val numbers = """^(#{1,3}) (.+)""".r
  
  setEscapeModelStrings(false)
  val escaped = if (label != null) createMarkup(escapeMarkup(label).toString) else null
  println(escaped)
  
  private def createMarkup(text: String) = {
    val bulleted = bullets(text)
    applySimpleRegexes(bulleted, WikiMarkupText.regexes)
  }
  
  
  private def bullets(text: String) = {
    val lines = text.split("""\n""").toList
    lines.map(line => line match {
      case bullets(indent, theRest) => (<ul><li>{theRest}</li></ul>).toString
      case numbers(indent, theRest) => (<ol><li>{theRest}</li></ol>).toString
      case x => println(x); x
    }).mkString("\n")                       
  }
  

  
  private def applySimpleRegexes(value: String, holders: List[(Regex, String)]): String = 
    holders match {
      case (regex, replacement) :: xs => applySimpleRegexes(regex.replaceAllIn(value, replacement), xs)
      case Nil => value
    }
  
  setModel(new Model(escaped))

  
  
  private class BulletLine(text: String, indent: Int) {
    override def toString = (<li>{text}</li>).toString
  }
  
}

private object WikiMarkupText {

  val bold = ("""\*([^*]*)\*""".r, """<b>$1</b>""")
  val italics = ("""_([^_]*)_""".r, """<i>$1</i>""")
  val regexes = List(bold, italics)
}
