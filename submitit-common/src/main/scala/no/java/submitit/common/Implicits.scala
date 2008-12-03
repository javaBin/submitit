package no.java.submitit.common

object Implicits {
  
  implicit def listToJavaList[T](l: List[T]) = _root_.java.util.Arrays.asList(l.toArray: _*)
  
}
