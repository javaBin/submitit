package subliftit.snippet

import xml.NodeSeq
import net.liftweb.http.{SHtml, DispatchSnippet}
import net.liftweb.util.Helpers._

class Submit extends DispatchSnippet {
  def dispatch = {
    case "talk" => talk _
  }

  def talk(xhtml:NodeSeq):NodeSeq = {

    var title = ""
    var body = ""

    bind("talk", xhtml,
      "title" -> SHtml.text(title, title = _),
      "body" -> SHtml.textarea(body, body = _),
      "submit" -> SHtml.submit("Submit", () => println(title + " " + body)))
  }
}