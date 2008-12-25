package no.java.submitit.app.utils

import _root_.java.util.Properties
import _root_.java.io._

object PropertyIOUtils extends IOUtils {
	
  def loadRessource(resourceName: String) = {
    val file = new File(resourceName)
    using(new BufferedReader(new FileReader(file))) { stream =>
      val p = new Properties();
      p.load(stream);
      p
    }
  }
  
  def writeResource(passPhrase: String, fileName: String, props: Map[String, String]) {
    val file = new File(fileName)
    val transformed = (SubmititApp.adminPassPhrase + "=" + passPhrase) :: props.map(tuple => tuple._1 + "=" + emptyForNull(tuple._2)).toList
    val propertyString = transformed.mkString("", "\n", "")
    using(new BufferedWriter(new FileWriter(file))) { stream =>
      propertyString.foreach(stream.write(_))
      stream.flush
    }
  }

  def emptyForNull(value: String) = if (value == null) "" else value
    
}