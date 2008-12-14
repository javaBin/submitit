package no.java.submitit.app.utils

import _root_.java.util.Properties

object PropertyLoader extends IOUtils {
	
  def loadRessource(resourceName: String) = {
    using(getClass().getClassLoader().getResourceAsStream(resourceName)) { stream =>
      val p = new Properties();
      p.load(stream);
      p
    }
  }

}