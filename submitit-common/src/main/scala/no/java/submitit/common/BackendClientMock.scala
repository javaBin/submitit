package no.java.submitit.common

import _root_.java.io.Serializable

import scala.collection.mutable.Map

import no.java.submitit.model.Presentation

object BackendClientMock extends BackendClient with Serializable {

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
  
  def loadPresentation(id: String): Option[Presentation] = {
    presentations.get(id)
  }
  
}
