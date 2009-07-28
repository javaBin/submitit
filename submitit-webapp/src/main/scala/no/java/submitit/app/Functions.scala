package no.java.submitit.app


object Functions {
  
  implicit val supportedImageExtensions: util.matching.Regex = extensionRegex(List("jpg", "jpeg", "png", "gif"))
  
  def extensionRegex(ext: List[String]) = if(ext != Nil) ("""(?i)\.""" + ext.mkString("(?:", "|", ")") + "$").r else "".r 

	def stringToOption(x: String) = x match {
  	case s if s != null && s.trim != "" => Some(s.trim) 
    case _ => None
  }
 
	def getFileContents(file: org.apache.wicket.markup.html.form.upload.FileUpload) = {
	  if(file != null) {
	  	// Create a new file
			Some(file.getClientFileName, file.getBytes, file.getContentType)
	  }
   else None
	}
 
	def hasExtension(fileName: String)(implicit ext: util.matching.Regex) = ext.findFirstIn(fileName)

}
