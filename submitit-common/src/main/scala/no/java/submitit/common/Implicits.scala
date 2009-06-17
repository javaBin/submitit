package no.java.submitit.common

import _root_.java.util.ArrayList

object Implicits {
  
  implicit def listToJavaList[T](l: List[T]) = l.foldLeft(new ArrayList[T]()) { (l, e) => l.add(e); l}
  
}
