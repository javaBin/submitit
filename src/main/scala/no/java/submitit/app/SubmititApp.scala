package no.java.submitit.app

import no.java.submitit.app.pages.SubmitPage;
import org.apache.wicket.protocol.http.WebApplication;

class SubmititApp extends WebApplication {


    @Override
    def getHomePage() = {
		classOf[SubmitPage];
	}

}
