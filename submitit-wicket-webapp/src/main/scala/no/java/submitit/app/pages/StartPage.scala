package no.java.submitit.app.pages


class StartPage extends org.apache.wicket.markup.html.WebPage {
  
  if(State.get.lockPresentation) {
    setResponsePage(classOf[ReviewPage])
  }
  else {
    setResponsePage(classOf[SubmitPage])
  }

}
