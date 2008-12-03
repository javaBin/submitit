package no.java.submitit.app.pages

object Implicits {
  
  implicit def listToJavaList[T](l: List[T]) = _root_.java.util.Arrays.asList(l.toArray: _*)
  
}
