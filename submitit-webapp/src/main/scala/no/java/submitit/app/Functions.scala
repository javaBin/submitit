package no.java.submitit.app

import _root_.java.io.File
import no.java.submitit.model.Binary
import no.java.submitit.common.LoggHandling

object Functions extends LoggHandling {
  
  val binariesInSession = "no.java.submitit.binaryholder"
  
  val supportedImages = List("jpg", "jpeg", "png", "gif")
  
  val supportedImageExtensions: util.matching.Regex = extensionRegex(supportedImages)
  
  def extensionRegex(ext: List[String]) = if(ext != Nil) ("""(?i)\.""" + ext.mkString("(?:", "|", ")") + "$").r else "".r 
 
	def getFileContents(file: org.apache.wicket.markup.html.form.upload.FileUpload) = {
	  if(file != null) {
	  	// Create a new file
			Some(file.getClientFileName, file.getInputStream, file.getContentType)
	  }
   else None
	}
 
	def hasExtension(fileName: String, ext: util.matching.Regex) = ext.findFirstIn(fileName).isDefined
	def hasntExtension(fileName: String, ext: util.matching.Regex) = ext.findFirstIn(fileName).isEmpty
 
	def removeBinaries(binariesTempFileNames: List[Binary]) {
  	binariesTempFileNames.foreach{ binary =>
  		if(binary.hasContent) {
  			val file = new File(binary.tmpFileName.get)
  			logger.info("Deleted file " + binary.tmpFileName.get + " which returned " + file.delete)
  		}
  	}
  }

}
