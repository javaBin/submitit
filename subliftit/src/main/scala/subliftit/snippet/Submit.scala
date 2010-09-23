package subliftit.snippet

import net.liftweb.http.{SHtml, DispatchSnippet}
import net.liftweb.util.Helpers._
import subliftit.model.{User, Submission}
import net.liftweb.mapper.By
import net.liftweb.http.S
import xml.{NodeSeq}

class Submit extends DispatchSnippet {
  def dispatch = {
    case "talk" => talk _
    case "list" => list _
  }

  def talk(xhtml: NodeSeq): NodeSeq = {

    val submission = Submission.createInstance.speaker(User.currentUser.open_!)

    bind("talk", xhtml,
      "title" -> SHtml.text(submission.title.is, submission.title(_)),
      "body" -> SHtml.textarea(submission.body.is, submission.body(_)),
      "submit" -> SHtml.submit("Submit", () => {
        submission.save
        S.notice("Submission created")
        S.redirectTo("/submissions")
      }))
  }

  def list(xhtml: NodeSeq): NodeSeq = {
    User.currentUser.map {
      user =>
        Submission.findAll(By(Submission.speaker, user)).flatMap(submission =>
          bind("talk", xhtml, "title" -> submission.title))
    }.openOr(NodeSeq.Empty)
  }
}