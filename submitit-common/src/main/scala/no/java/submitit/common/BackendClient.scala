package no.java.submitit.common

import model.Presentation

trait BackendClient {

  def savePresentation(presentation: Presentation): String
  
  def loadPresentation(id: String): Presentation
  
}
