/*
 * Copyright 2009 JavaBin
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
  
  private var configKeyList: List[ConfigKey] = Nil 

  private [app] def getKey(key: String) = configKeyList.find(_.toString == key)
  
  private [app] def key(key: String) = configKeyList.find(_.toString == key).getOrElse(throw new IllegalArgumentException("Should not happen"))
  
	sealed abstract case class ConfigKey() {
	  configKeyList = this :: configKeyList
	}
 
	case object showFeedbackBoolean extends ConfigKey
	case object showSpecialMessageOnRejectBoolean extends ConfigKey
	case object showActualStatusInReviewPageBoolean extends ConfigKey
	case object passPhraseSubmitSpecialURL extends ConfigKey
	case object captchaLengthInt extends ConfigKey
	case object allowSlideUploadBoolen extends ConfigKey
	case object showUserSelectedKeywordsInReviewPageWhenEditNotAllowedBoolean extends ConfigKey
	case object eventName extends ConfigKey
	case object submititBaseUrl extends ConfigKey
	case object presentationUploadSizeInMBInt extends ConfigKey
	case object officialEmailReplyTo extends ConfigKey
	case object allowIndidualFeedbackOnRejectBoolean extends ConfigKey
	case object smtpHost extends ConfigKey
	case object presentationAllowedExtendsionFileTypes extends ConfigKey
	case object showRoomWhenApprovedBoolean extends ConfigKey
	case object emailBccCommaSeparatedList extends ConfigKey
	case object submitAllowedBoolean extends ConfigKey
	case object editPageInfoTextHtml extends ConfigKey
	case object reviewPageViewSubmittedChangeAllowedHthml extends ConfigKey
	case object globalEditAllowedBoolean extends ConfigKey
	case object feedbackRejected extends ConfigKey
	case object userSelectedKeywords extends ConfigKey
	case object showTimeslotWhenApprovedBoolean extends ConfigKey
	case object reviewPageViewSubmittedHthml extends ConfigKey
	case object reviewPageBeforeSubmitHtml extends ConfigKey
	case object headerText extends ConfigKey
	case object submitNotAllowedHtml extends ConfigKey
	case object globalEditAllowedForAcceptedBoolean extends ConfigKey
  
  private [app] val configValues = Map(
		showFeedbackBoolean -> "false",
		showSpecialMessageOnRejectBoolean -> "false",
		showActualStatusInReviewPageBoolean -> "false",
		passPhraseSubmitSpecialURL -> "jz",
		captchaLengthInt -> "1",
		allowSlideUploadBoolen -> "false",
		showUserSelectedKeywordsInReviewPageWhenEditNotAllowedBoolean -> "true",
		eventName -> "JavaZone 2009",
		submititBaseUrl -> "http://localhost:8080",
		presentationUploadSizeInMBInt -> "5",
		officialEmailReplyTo -> "program@java.no",
		allowIndidualFeedbackOnRejectBoolean -> "false",
		smtpHost -> null,
		presentationAllowedExtendsionFileTypes -> "pdf, ppt, key, odp",
		showRoomWhenApprovedBoolean -> "false",
		emailBccCommaSeparatedList -> null,
		submitAllowedBoolean -> "true",
		editPageInfoTextHtml -> """<ul><li>Click the "Help" link, or press the question mark at each field for information about what to enter.</li><li>Before you submit your presentation you have to review it by pressing the "Review presentation" link.</li></ul>""",
		reviewPageViewSubmittedChangeAllowedHthml -> """You can still change the contents of your submission. You may edit by pressing the "Edit link".<br>If you have any questions, please email <a href="mailto:program@java.no">program@java.no</a>""",
		globalEditAllowedBoolean -> "true",
		feedbackRejected -> "Unfortunately we could not provide a presentation slot for this presentation. We have gotten more than 200 proposals this year, selecting the best have been difficult. If you have other submissions you should check their status as well. If you have any questions you may email program@java.no.",
		userSelectedKeywords -> "Core Java|Tools and Techniques|Java Frameworks|Frontend Technologies|Usability|Embedded, Mobile and Gaming|Enterprise Architecture and Integration|Web as a Platform|Architecture and Design|Agile and Software Engineering|Alternative Languages|Experience Reports|Innovative use of IT|Green IT|Domain-driven design",
		showTimeslotWhenApprovedBoolean -> "false",
		reviewPageViewSubmittedHthml -> """If you have any questions, please email <a href="mailto:program@java.no">program@java.no</a>""",
		reviewPageBeforeSubmitHtml -> """Your presentation has not yet been submittet. Please review, and press the "Submit presentation" link when you are ready.""",
		headerText -> "Submit your JavaZone 2009 presentation",
		submitNotAllowedHtml -> "Call for papers is currently not open.",
		globalEditAllowedForAcceptedBoolean -> "true"
  )

}
