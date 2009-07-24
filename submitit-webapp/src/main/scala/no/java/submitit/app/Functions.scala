package no.java.submitit.app

object Functions {

	def stringToOption(x: String) = x match {
  	case s if s != null && s.trim != "" => Some(s.trim) 
    case _ => None
  }

}
