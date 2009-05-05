package no.java.submitit.common

import org.slf4j.{Logger, LoggerFactory}

@serializable
trait LoggHandling {
	
	protected def logger = log
  
	@transient
	private var log = createLogger
 
	private def createLogger = LoggerFactory.getLogger(getClass)
 
	private def readResolve = {
		createLogger; 
		this
	}
 
}
