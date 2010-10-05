package subliftit
package snippet

import no.java.submitit.model._

import net.liftweb._
import textile.TextileParser
import util.Helpers._
import common._
import http._
import js.JE._
import js.jquery.JqJE.{JqId, JqRemove, JqAppend}
import js.JsCmd
import js.JsCmds._
import xml.{Text, NodeSeq}

class Submit extends StatefulSnippet {
  def dispatch = {
    case "create" => create _
    case "review" => review _
    case "confirm" => confirm _
  }

  var submissionBox: Box[Presentation] = Empty

  def submission = submissionBox.open_! // urk...

  def create(xhtml: NodeSeq): NodeSeq = {

    if (submissionBox.isEmpty) {
      val presentation = new Presentation
      presentation.speakers :+= Speaker("", "", "", None)
      submissionBox = Full(presentation)
    }

    val fieldXhtml = chooseTemplate("submit", "field", xhtml)

    def bindField(name:String, x:NodeSeq) = name -> bind("field", fieldXhtml, "title" -> Text(name), "bind" -> x)

    def bindSpeakers(xhtml: NodeSeq): NodeSeq = {
      val id = nextFuncName
      val speakerXhtml = chooseTemplate("speakers", "speaker", xhtml)

      def add: JsCmd = {
        val speaker = Speaker("", "", "", None)
        submission.speakers :+= speaker
        JqId(id) ~> JqAppend(bindSpeaker(speakerXhtml)(speaker))
      }

      bind("speakers", xhtml,
        "speaker" -> <div id={id}>
          {submission.speakers.flatMap {bindSpeaker(speakerXhtml)}}
        </div>,
        "add" -> SHtml.a(add _, Text("Add")))
    }

    def bindSpeaker(xhtml: NodeSeq)(speaker: Speaker): NodeSeq = {

      val id = nextFuncName
      val nameId = nextFuncName
      val bioId = nextFuncName

      def remove: JsCmd = {
        submission.speakers = submission.speakers.filterNot(_ == speaker)
        JqId(id) ~> JqRemove()
      }

      def populate(email: String): JsCmd = {
        // TODO - populate speaker data if found in backend
        speaker.name = "test-name"
        speaker.bio = "test-bio"

        SetValById(nameId, speaker.name) & SetValById(bioId, speaker.bio)
      }

      val b = bind("speaker", xhtml,
        bindField("name", SHtml.text(speaker.name, speaker.name = _) % ("id" -> nameId)),
        bindField("email", SHtml.ajaxText(speaker.email, s => {speaker.email = s; populate(s)})), // populate on blur
        bindField("bio", SHtml.textarea(speaker.bio, speaker.bio = _) % ("id" -> bioId)),
        bindField("picture", SHtml.fileUpload(f => speaker.picture = Some(Binary(nextFuncName, f.mimeType, Some(f.fileStream))))),
        "remove" -> SHtml.a(remove _, Text(S ? "Remove")))

      <div id={id}>
        {b}
      </div>
    }


    def bindPresentation(xhtml: NodeSeq): NodeSeq = {
      bind("presentation", xhtml,
        bindField("format", SHtml.selectObj[PresentationFormat.Value](PresentationFormat.values.toSeq.map {s => s -> S ? s.toString}, Full(submission.format), submission.format = _)),
        bindField("title", SHtml.text(submission.title, submission.title = _)),
        bindField("abstract", SHtml.textarea(submission.abstr, submission.abstr = _)),
        bindField("language", SHtml.selectObj[Language.Value](Language.values.toSeq.map {l => l -> S ? l.toString}, Full(submission.language), submission.language = _)),
        bindField("level", SHtml.selectObj[Level.Value](Level.values.toSeq.map {l => l -> S ? l.toString}, Full(submission.level), submission.level = _)),
        bindField("outline", SHtml.textarea(submission.outline, submission.outline = _)),
        bindField("equipment", SHtml.textarea(submission.equipment, submission.equipment = _)),
        bindField("expectedAudience", SHtml.textarea(submission.expectedAudience, submission.expectedAudience = _)))
    }

    def submit {
      redirectTo("/review")
    }

    bind("submit", xhtml,
      "speakers" -> bindSpeakers _,
      "presentation" -> bindPresentation _,
      "review" -> SHtml.submit(S ? "Review", submit _),
      "field" -> NodeSeq.Empty)
  }


  def review(xhtml: NodeSeq): NodeSeq = {
    // TODO load submission from db if empty

    def submit {
      redirectTo("/confirm")
    }

    def edit {
      redirectTo("/submit")
    }


    bind("submission", xhtml,
      "presentationFormat" -> Text(submission.format.toString),
      "title" -> Text(submission.title),
      "abstract" -> TextileParser.toHtml(submission.abstr),
      "language" -> Text(submission.language.toString),
      "level" -> Text(submission.level.toString),
      "outline" -> TextileParser.toHtml(submission.outline),
      "equipment" -> Text(submission.equipment),
      "expectedAudience" -> Text(submission.expectedAudience),
      "edit" -> SHtml.submit(S ? "Edit", edit _),
      "submit" -> SHtml.submit(S ? "Submit", submit _))
  }

  def confirm(xhtml: NodeSeq): NodeSeq = {
    unregisterThisSnippet()
    bind("confirm", xhtml,
      "what" -> Text(if(submission.isNew) "submitting" else "updating"),
      "title" -> Text(submission.title))
  }
}

