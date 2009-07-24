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

package no.java.submitit.app

object DefaultConfigValues {
  
  private[app] var configKeyList: List[ConfigKey] = Nil 

  private[app] def getKey(key: String) = configKeyList.find(_.toString == key)
  
  private[app] def key(key: String) = getKey(key).getOrElse(throw new IllegalArgumentException("Should not happen"))
  
  private[app] val booleanParse = (x: String) => x.toBoolean
  private[app] val notNullParse = (x: String) => if(x == null || x.trim == "") throw new Exception("Cannot be empty")
  private[app] val intParse = (x: String) => x.toInt
  private[app] val defParse = (x: String) => x
  
	sealed abstract case class ConfigKey(val parser: String => Any) {
	  
	  def this() {this(defParse)}
   
	  configKeyList = this :: configKeyList
   
	  val name = """\w+""".r.findFirstIn(toString).get
   
    val description: String
    val editable = true
    val visible = true
    val mandatoryInFile = false
	}
 
	case object showFeedbackBoolean extends ConfigKey(booleanParse) {
	  val description = "Toggle viewing feedback. Regardless of submission status."
	}
	case object showSpecialMessageOnRejectBoolean extends ConfigKey(booleanParse) {
	  val description = "Toggle vieing global feedback on rejected presentations. Text defined in " + feedbackRejected.name + ". Will be 'overriden' if individual presentation has feedback and " + allowIndidualFeedbackOnRejectBoolean.name + " is true"
	}
	case object showActualStatusInReviewPageBoolean extends ConfigKey(booleanParse) {
	  val description = "Hides the status if set to true and will always show pending. Convenient so actual status may be set in EMS"
	}
	case object passPhraseSubmitSpecialURL extends ConfigKey {
	  val description = "The passphrase used for special invite page. So that speakers may submit after CfP has ended. If empty submission is not possible"
	}
	case object captchaLengthInt extends ConfigKey {
	  val description = "The length of the captha, will always be minimum 1"
	}
	case object allowSlideUploadBoolen extends ConfigKey(booleanParse) {
	  val description = "Toggle slide upload. Currently not implemented properly, should be false"
	}
	case object showUserSelectedKeywordsInReviewPageWhenEditNotAllowedBoolean extends ConfigKey(booleanParse) {
	  val description = "Allow users to save tags on their presentations in the review page. This is only available if the presentation is not editable. This is because normally the edit page must be used to select tags/keywords. Available tags are specified in " + userSelectedKeywords.name
	}
	case object eventName extends ConfigKey {
	  val description = "Name used to identify the event in EMS. Should NEVER be changed after SubmitIT has been started to be used"
	  override val editable = false
	  override val mandatoryInFile = true
	}
	case object submititBaseUrl extends ConfigKey(notNullParse) {
	  val description = "This URL is used in the confirmation URL. Should point to base URL without path. Should not be changed during operation."
	  override val editable = false
	}
	case object presentationUploadSizeInMBInt extends ConfigKey(intParse) {
	  val description = "Max size for uploading slides in MB"
	}
	case object officialEmailReplyTo extends ConfigKey(notNullParse) {
	  val description = "Email which is viewed in several pages for contacting the programme committee"
	}
	case object allowIndidualFeedbackOnRejectBoolean extends ConfigKey(booleanParse) {
	  val description = "If true individual feedback fields will be shown for rejected submissions, but only if feedback on the presentation is defined"
	}
	case object smtpHost extends ConfigKey {
	  val description = "Hostname of the smtp server. Should normally never be changed during operation. If emtpy no emails will be sent"
	}
	case object presentationAllowedExtendsionFileTypes extends ConfigKey {
	  val description = "File extension types allowed for presentations, comma separated list"
	}
	case object showRoomWhenApprovedBoolean extends ConfigKey(booleanParse) {
	  val description = "If room is set and presetation is approvoed setting this to true will show room in review page"
	}
	case object emailBccCommaSeparatedList extends ConfigKey {
	  val description = "Comma separated list of emails to bcc to for confirmation emails"
	}
	case object submitAllowedBoolean extends ConfigKey(booleanParse) {
	  val description = "Allows new submissions when true"
	}
	case object editPageInfoTextHtml extends ConfigKey {
	  val description = "Shows the information text in the edit page. Allows HTML"
	}
	case object reviewPageViewSubmittedChangeAllowedHthml extends ConfigKey {
	  val description = "Shows information text in review page when it is allowed to change the submission. Allows HTML"
	}
	case object globalEditAllowedBoolean extends ConfigKey(booleanParse) {
	  val description = "If true, allows editing of already submitted submissions"
	}
	case object feedbackRejected extends ConfigKey {
	  val description = "Global feedback message for abstracts that are rejected. This will be shown when " + showSpecialMessageOnRejectBoolean.name + " is true"
	}
	case object userSelectedKeywords extends ConfigKey {
	  val description = "Bar '|' separted list of tags/keywords the user may select"
	}
	case object showTimeslotWhenApprovedBoolean extends ConfigKey(booleanParse) {
	  val description = "If room is set and presetation is approvoed setting this to true will show time slot in review page"
	}
	case object reviewPageViewSubmittedHthml extends ConfigKey {
	  val description = "Shows info text in HTML for submitted submissions, when the user cannot edit the submission."
	}
	case object reviewPageBeforeSubmitHtml extends ConfigKey {
	  val description = "Shows the information text for a presentation that has not yet been submitted. Allows HTML"
	}
	case object headerText extends ConfigKey(notNullParse) {
	  val description = "Global text in header."
	}
	case object submitNotAllowedHtml extends ConfigKey {
	  val description = "Message shown when user tries to access SubmitIT to send in a new submission."
	}
	case object globalEditAllowedForAcceptedBoolean extends ConfigKey(booleanParse) {
	  val description = "If true allows accepted submissions to be edited."
	}
	case object listAllSubmissionsPassword extends ConfigKey {
		val description = "Password for accessing all submissions in a list view."
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
 
  
  private [app] val configValues = collection.mutable.LinkedHashMap(
    eventName -> "JavaZone 2009",
  	headerText -> "Submit your JavaZone 2009 presentation",
  	submitAllowedBoolean -> "true",
		showFeedbackBoolean -> "false",
		showSpecialMessageOnRejectBoolean -> "false",
		showActualStatusInReviewPageBoolean -> "false",
		allowIndidualFeedbackOnRejectBoolean -> "false",
		globalEditAllowedForAcceptedBoolean -> "true",
		globalEditAllowedBoolean -> "true",
		showUserSelectedKeywordsInReviewPageWhenEditNotAllowedBoolean -> "true",
		showRoomWhenApprovedBoolean -> "false",
		showTimeslotWhenApprovedBoolean -> "false",
		allowSlideUploadBoolen -> "false",
		presentationUploadSizeInMBInt -> "5",
		presentationAllowedExtendsionFileTypes -> "pdf, ppt, key, odp",
		captchaLengthInt -> "1",
		passPhraseSubmitSpecialURL -> "jz",
		listAllSubmissionsPassword -> "jzListPassword",
		submitNotAllowedHtml -> "Call for papers is currently not open.",
		editPageInfoTextHtml -> """<ul><li>Click the "Help" link, or press the question mark at each field for information about what to enter.</li><li>Before you submit your presentation you have to review it by pressing the "Review presentation" link.</li></ul>""",
		reviewPageViewSubmittedChangeAllowedHthml -> """You can still change the contents of your submission. You may edit by pressing the "Edit link".<br>If you have any questions, please email <a href="mailto:program@java.no">program@java.no</a>""",
		feedbackRejected -> "Unfortunately we could not provide a presentation slot for this presentation. We have gotten more than 200 proposals this year, selecting the best have been difficult. If you have other submissions you should check their status as well. If you have any questions you may email program@java.no.",
		userSelectedKeywords -> "Core Java|Tools and Techniques|Java Frameworks|Frontend Technologies|Usability|Embedded, Mobile and Gaming|Enterprise Architecture and Integration|Web as a Platform|Architecture and Design|Agile and Software Engineering|Alternative Languages|Experience Reports|Innovative use of IT|Green IT|Domain-driven design",
		reviewPageViewSubmittedHthml -> """If you have any questions, please email <a href="mailto:program@java.no">program@java.no</a>""",
		reviewPageBeforeSubmitHtml -> """Your presentation has not yet been submittet. Please review, and press the "Submit presentation" link when you are ready.""",
		submititBaseUrl -> "http://localhost:8080",
		officialEmailReplyTo -> "program@java.no",
		smtpHost -> null,
		emailBccCommaSeparatedList -> null,
		commaSeparatedListOfTagsForNewSubmissions -> "fra_submitit",
		adminPassPhrase -> "r",
		emsUrl -> "", 
		emsUser -> "",
    emsPwd -> ""
  )

}
