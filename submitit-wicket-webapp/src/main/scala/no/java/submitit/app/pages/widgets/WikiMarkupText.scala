package no.java.submitit.app.pages.widgets

import org.apache.wicket.markup.html.basic._
import org.apache.wicket.model._
import org.apache.wicket.util.string.Strings.escapeMarkup
import scala.util.matching._
import WikiMarkupText._

class WikiMarkupText (id: String, label: String) extends MultiLineLabel(id) {
  
  setEscapeModelStrings(false)
  val escaped = if (label != null) createMarkup(escapeMarkup(label).toString) else null
  
  private def createMarkup(text: String) = {
    MarkupHandler.applySimpleRegexes(MarkupHandler.bullets(text))
  }
  
  
  setModel(new Model(escaped))

}

private object WikiMarkupText {
  
  val bullets = """(?s)^(\*{1,3}) (.+)""".r
  val numbers = """(?s)^(#{1,3}) (.+)""".r

  val bold = ("""\*([^*]*)\*""".r, """<b>$1</b>""")
  val italics = ("""_([^_]*)_""".r, """<i>$1</i>""")
  //val newLine = ("""\n""".r, """<br />""")
  val regexes = List(bold, italics)
}

object MarkupHandler {
  
  val ul = ("<ul>", "</ul>")
  val ol = ("<ol>", "</ol>")
  
  
  def applySimpleRegexes(value: String): String = {
     applySimpleRegexes(value, WikiMarkupText.regexes)
  } 
  
  private def applySimpleRegexes(value: String, holders: List[(Regex, String)]): String =  holders match {
    case (regex, replacement) :: xs => applySimpleRegexes(regex.replaceAllIn(value, replacement), xs)
    case Nil => value
  }
  
  
  def bullets(text: String) = {
    //val lines = """.+|^$""".r.findAllIn(text)
    val lines = text.split("\n").toList
    val handler = new StateHandler
    lines.foreach(line => { line match {
      case bullets(indent, theRest) => handler.add(theRest, indent.size, ul)
      case numbers(indent, theRest) => handler.add(theRest, indent.size, ol)
      case x => handler + (x + "\n")
    }
    })
    handler.toString
  }
  

  private class StateHandler {

    val listart = "<li>"
    val listop = "</li>"
    
    private var currentIndent = 0;
    private var current: Tuple2[String, String] = _
     
    private val builder = new StringBuilder
    
    private val tagIter = (i: Int, tag: String) => (for(i <- 0 to i - 1) builder append tag)

    override def toString = if (currentIndent != 0) {tagIter(currentIndent, current._2); builder.toString} else builder.toString
    
    def + (value: String) {
      builder.append(value)
    }

    def add(value: String, indent: Int, tag: Tuple2[String, String]) {
	    def resolveVal(value: String) = if (value != null) listart + value + listop else ""
     current = tag 
     println(indent + " " + currentIndent)
     if(indent == 0 && value != null) {
       builder.append(value)
     }
     else {
       val res = indent match {
         case x if x == currentIndent => resolveVal(value)
         case 0 => tagIter(currentIndent, tag._2); resolveVal(value) 
         case x if indent > currentIndent=> tagIter(indent - currentIndent, tag._1); resolveVal(value)
         case x if indent < currentIndent=> tagIter(currentIndent - indent, tag._2); resolveVal(value)
       }
       builder.append(res)
     }
     currentIndent = indent
    }
  }
  
}
