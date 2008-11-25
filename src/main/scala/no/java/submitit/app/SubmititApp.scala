package no.java.submitit.app


import pages.SubmitPage;

import org.apache.wicket._
import org.apache.wicket.protocol.http.WebApplication

class SubmititApp extends WebApplication {

  	@Override
	def getHomePage() = {
		new SubmitPage().getClass;
	}
  
}
