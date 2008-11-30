package no.java.submitit.app.pages

object Implicits {
  
  implicit def listToArrayList[T](l: List[T]): _root_.java.util.List[T] = {
    _root_.java.util.Arrays.asList(l.toArray: _*)
  }

}
