package no.java.submitit.app.pages

object Implicits {
  
  implicit def listToJavaList(l: List[_]) = _root_.java.util.Arrays.asList(l.toArray: _*)
}
