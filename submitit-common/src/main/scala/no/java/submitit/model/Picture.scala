package no.java.submitit.model

class Picture(var id: String, val content: Array[byte], val name: String, val contentType: String) {

  def this(content: Array[byte], name: String, contentType: String) = 
    this(null, content, name, contentType)
  
}
