package no.java.submitit.model

import _root_.java.io.Serializable

class Picture(var id: String, val content: Array[byte], val name: String, val contentType: String) extends Serializable {

  def this(content: Array[byte], name: String, contentType: String) = 
    this(null, content, name, contentType)
  
}
