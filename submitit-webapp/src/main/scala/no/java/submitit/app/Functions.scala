package no.java.submitit.app


object Functions {

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
 

}
