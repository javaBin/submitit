package no.java.submitit.app.pages

import no.java.submitit.model.Presentation
import no.java.submitit.app.State


trait UpdateSessionHandling {

  val pres: Presentation

  State().currentPresentation = pres

}