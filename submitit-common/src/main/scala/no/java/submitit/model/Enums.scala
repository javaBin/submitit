package no.java.submitit.model

import common.LoggHandling

private object HelpFunctions extends LoggHandling {
  
  def unknownEnumValue[T](v: Any, r: T): T = {
    logger.error("Unknown enum value '" + v + "', defaulting to '" + r + "'")
    r
  }

}

import HelpFunctions._

object Language extends Enumeration {
  val Norwegian = Value("Norwegian")
  val English = Value("English")

  def toEmsValue(language: Language.Value) = language match {
    case Norwegian => "no"
    case English => "en"  
  }

  def fromEmsValue(value: String) = value match {
    case "no" => Norwegian
    case "en" => English
    case s => unknownEnumValue(s, Norwegian)
  }
}

object Level extends Enumeration {
  val Beginner = Value("Beginner")
  val Intermediate = Value("Intermediate")
  val Advanced = Value("Advanced")
  
  def toEmsValue(level: Level.Value) = level match {
      case Level.Beginner => "Introductory"
      case Level.Intermediate => "Intermediate"
      case Level.Advanced => "Advanced"
    }
  
  def fromEmsValue(value: String) = value match {
    case "Introductory" => Beginner
    case "Intermediate" => Intermediate
    case "Advanced" => Advanced
    case s => unknownEnumValue(s, Beginner)
  }
  
}

object PresentationFormat extends Enumeration {
  val Presentation = Value("Presentation")
  val LightningTalk = Value("Lightning talk")
  
  def toEmsValue(format: PresentationFormat.Value) = format match {
    case PresentationFormat.Presentation => "Presentation"
    case PresentationFormat.LightningTalk => "Quickie"
  }
  
  def fromEmsValue(value: String) = value match {
    case "Presentation" => Presentation
    case "Quickie" => LightningTalk
    case s => unknownEnumValue(s, Presentation)
  }
}


object Status extends Enumeration {
  val Pending = Value("Pending")
  val Approved = Value("Approved")
  val NotApproved = Value("Not approved")
  
  def fromEmsValue(value: String) = value match {
    case "Approved" => Approved
    case "Rejected" => NotApproved
    case "Pending" => Pending
    case s => unknownEnumValue(s, Pending)
  }
}
