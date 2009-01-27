package no.java.submitit.model

object Language extends Enumeration {
  val Norwegian = Value("Norwegian")
  val English = Value("English")
}

object Level extends Enumeration {
  val Beginner = Value("Beginner")
  val Intermediate = Value("Intermediate")
  val Advanced = Value("Advanced")
}

object PresentationFormat extends Enumeration {
  val Presentation = Value("Presentation")
  val LightningTalk = Value("Lightning talk")
}
