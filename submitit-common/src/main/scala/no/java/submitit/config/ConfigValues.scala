/*
 * Copyright 2009 javaBin
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package no.java.submitit.config

sealed abstract class ConfigKey {

  val parser: String => Any = Keys.defParse

  val description: String
  val editable = true
  val visible = true
  val mandatoryInFile = false

  override def equals(other: Any) = other match {
    case o: AnyRef => this.getClass.equals(o.getClass)
    case _ => false
  }
  override def hashCode = this.getClass.hashCode

}

object Keys {

  def toOption(x: String) = x match {
    case s if s != null && s.trim != "" => Some(s.trim)
    case _ => None
  }

  val booleanParse = (x: String) => x.toBoolean
  val notNullParse = (x: String) => if(toOption(x).isEmpty) throw new Exception("Cannot be empty")
  val intParse = (x: String) => x.toInt
  val defParse = (x: String) => x

  case object showFeedbackBoolean extends ConfigKey {
    override val parser = booleanParse
    val description = "Toggle viewing feedback. Regardless of submission status."
  }
  case object showSpecialMessageOnRejectBoolean extends ConfigKey{
    override val parser = booleanParse
    val description = "Toggle vieing global feedback on rejected presentations. Text defined in " + feedbackRejected + ". Will be 'overriden' if individual presentation has feedback and " + allowIndidualFeedbackOnRejectBoolean + " is true"
  }
  case object showActualStatusInReviewPageBoolean extends ConfigKey {
    override val parser = booleanParse
    val description = "Hides the status if set to false and will always show pending. Convenient so actual status may be set in EMS"
  }
  case object passPhraseSubmitSpecialURL extends ConfigKey {
    val description = "The passphrase used for special invite page. So that speakers may submit after CfP has ended. If empty submission is not possible"
  }
  case object captchaLengthInt extends ConfigKey {
    val description = "The length of the captha, will always be minimum 1"
  }
  case object allowSlideUploadBoolen extends ConfigKey {
    override val parser = booleanParse
    val description = "Toggle slide upload. Only available for presentations that are approved."
  }
  case object eventName extends ConfigKey {
    val description = "Name used to identify the event in EMS. Should NEVER be changed after SubmitIT has been started to be used"
    override val editable = false
    override val mandatoryInFile = true
  }
  case object submititBaseUrl extends ConfigKey {
    override val parser = notNullParse
    val description = "This URL is used in the confirmation URL. Should point to base URL without path. Should not be changed during operation."
    override val editable = false
  }
  case object presentationUploadSizeInMBInt extends ConfigKey {
    override val parser = intParse
    val description = "Max size for uploading slides in MB"
  }
  case object presentationUploadPdfSizeInMBInt extends ConfigKey {
    override val parser = intParse
    val description = "Max size for pdf slides for publishing in MB"
  }
  case object officialEmailReplyTo extends ConfigKey {
    override val parser = notNullParse
    val description = "Email which is viewed in several pages for contacting the programme committee"
  }
  case object allowIndidualFeedbackOnRejectBoolean extends ConfigKey {
    override val parser = booleanParse
    val description = "If true individual feedback fields will be shown for rejected submissions, but only if feedback on the presentation is defined"
  }
  case object smtpHost extends ConfigKey {
    val description = "Hostname of the smtp server. Should normally never be changed during operation. If emtpy no emails will be sent"
  }
  case object showRoomWhenApprovedBoolean extends ConfigKey {
    override val parser = booleanParse
    val description = "If room is set and presetation is approvoed setting this to true will show room in review page"
  }
  case object emailBccCommaSeparatedList extends ConfigKey {
    val description = "Comma separated list of emails to bcc to for confirmation emails"
  }
  case object submitAllowedBoolean extends ConfigKey {
    override val parser = booleanParse
    val description = "Allows new submissions when true"
  }
  case object editPageInfoTextHtml extends ConfigKey {
    val description = "Shows the information text in the edit page. Allows HTML"
  }
  case object reviewPageViewSubmittedChangeAllowedHthml extends ConfigKey {
    val description = "Shows information text in review page when it is allowed to change the submission. Allows HTML"
  }
  case object globalEditAllowedBoolean extends ConfigKey {
    override val parser = booleanParse
    val description = "If true, allows editing of already submitted submissions"
  }
  case object feedbackRejected extends ConfigKey {
    val description = "Global feedback message for abstracts that are rejected. This will be shown when " + showSpecialMessageOnRejectBoolean + " is true"
  }
  case object userSelectedKeywords extends ConfigKey {
    val description = "Bar '|' separted list of tags/keywords the user may select"
  }
  case object showTimeslotWhenApprovedBoolean extends ConfigKey {
    override val parser = booleanParse
    val description = "If room is set and presetation is approvoed setting this to true will show time slot in review page"
  }
  case object reviewPageViewSubmittedHthml extends ConfigKey {
    val description = "Shows info text in HTML for submitted submissions, when the user cannot edit the submission."
  }
  case object reviewPageBeforeSubmitHtml extends ConfigKey {
    val description = "Shows the information text for a presentation that has not yet been submitted. Allows HTML"
  }
  case object headerText extends ConfigKey {
    override val parser = notNullParse
    val description = "Global text in header."
  }
  case object headerLogoText extends ConfigKey {
    override val parser = notNullParse
    val description = "Shown as first part of logo in header. This should be the name of the conference."
    override val editable = false
  }
  case object submitNotAllowedHtml extends ConfigKey {
    val description = "Message shown when user tries to access SubmitIT to send in a new submission."
  }
  case object globalEditAllowedForAcceptedBoolean extends ConfigKey {
    override val parser = booleanParse
    val description = "If true allows accepted submissions to be edited."
  }
  case object commaSeparatedListOfTagsForNewSubmissions extends ConfigKey {
    val description = "All new submissions sent from SubmitIT will get the tags set by this comma separated list of strings."
  }
  case object adminPassPhrase extends ConfigKey {
    val description = "Passphrase for admin gui"
    override val visible = false
    override val mandatoryInFile = true
  }
  case object emsUrl extends ConfigKey {
    val description = "url for ems. If null uses mock."
    override val visible = false
    override val mandatoryInFile = true
  }
  case object emsUser extends ConfigKey {
    val description = "The user used to connect to ems"
    override val visible = false
    override val mandatoryInFile = true
  }
  case object emsPwd extends ConfigKey {
    val description = "The password for ems."
    override val visible = false
    override val mandatoryInFile = true
  }

}



trait ConfigValues {

  def getKey(key: String) = configKeyList.find(_.toString == key)

  def key(key: String) = getKey(key).getOrElse(throw new IllegalArgumentException("Should not happen"))

  implicit def stringToSome(v: String) = Some(v)

  protected val configValues = collection.mutable.LinkedHashMap[ConfigKey, Option[String]](
    Keys.eventName -> "JavaZone 2010",
    Keys.headerLogoText -> "JavaZone 2010",
  	Keys.headerText -> "Submit your JavaZone 2010 presentation",
  	Keys.submitAllowedBoolean -> "true",
		Keys.showFeedbackBoolean -> "false",
		Keys.showSpecialMessageOnRejectBoolean -> "false",
		Keys.showActualStatusInReviewPageBoolean -> "false",
		Keys.allowIndidualFeedbackOnRejectBoolean -> "false",
		Keys.globalEditAllowedForAcceptedBoolean -> "true",
		Keys.globalEditAllowedBoolean -> "true",
		Keys.showRoomWhenApprovedBoolean -> "false",
		Keys.showTimeslotWhenApprovedBoolean -> "false",
		Keys.allowSlideUploadBoolen -> "false",
		Keys.presentationUploadSizeInMBInt -> "30",
		Keys.presentationUploadPdfSizeInMBInt -> "20",
		Keys.captchaLengthInt -> "1",
		Keys.passPhraseSubmitSpecialURL -> "jz",
		Keys.submitNotAllowedHtml -> "Call for papers is currently not open.",
		Keys.editPageInfoTextHtml -> """<ul><li>Click the "Help", or press the question mark at each field for information about what to enter.</li><li>Before you submit your presentation you have to review it by pressing "Review presentation".</li><li>The session timeout is 2 hours. Wondering what <a style="font-weight:normal;" target="_blank" href="http://www.juniper.net/techpubs/software/management/sdx/sdx50x/sw-sdx-sw-basics/html/web-app-installing4.html" style="font-weight: normal;">session timeout is?</a></li></ul></li></ul>""",
		Keys.reviewPageViewSubmittedChangeAllowedHthml -> """You can still change the contents of your submission. You may edit by pressing the "Edit presentation".<br>If you have any questions, please email <a href="mailto:program@java.no">program@java.no</a><br>""",
		Keys.feedbackRejected -> "The high quality of all the submissions this year has made the selection process very difficult, and unfortunately we cannot accept all talks. We actually had almost 250 submissions, and we only have 90 slots. We regret to inform you that we are unable to accept your proposal. We hope however that you will keep up the good effort and submit proposals next year as well!",
		Keys.userSelectedKeywords -> "alternative languages|concurrency / scalability|enterprise|core / jvm|web / frontend|methodology|testing|experience report",
		Keys.reviewPageViewSubmittedHthml -> """If you have any questions, please email <a href="mailto:program@java.no">program@java.no</a>""",
		Keys.reviewPageBeforeSubmitHtml -> """Your presentation has not yet been submittet. Please review, and press "Submit presentation" when you are ready.""",
		Keys.submititBaseUrl -> None,
		Keys.officialEmailReplyTo -> "program@java.no",
		Keys.smtpHost -> None,
		Keys.emailBccCommaSeparatedList -> None,
		Keys.commaSeparatedListOfTagsForNewSubmissions -> "fra_submitit",
		Keys.adminPassPhrase -> "r",
		Keys.emsUrl -> "",
		Keys.emsUser -> "",
    Keys.emsPwd -> ""
  )

  protected var configKeyList: List[ConfigKey] = configValues.keys.toList

}