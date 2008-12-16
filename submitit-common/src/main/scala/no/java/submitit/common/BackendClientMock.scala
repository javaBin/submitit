package no.java.submitit.common

import scala.collection.mutable.Map

import no.java.submitit.model.Presentation

class BackendClientMock extends BackendClient {

  var nextId = 1
  
  val presentations = Map.empty[String, Presentation]
  
  def savePresentation(pres: Presentation): String = {
    if (pres.sessionId == null) {
      pres.sessionId = nextId.toString
      nextId = nextId + 1
    }
    presentations(pres.sessionId) = pres
    pres.sessionId
  }
  
  def loadPresentation(id: String): Presentation = {
    presentations(id)
  }
  
}
