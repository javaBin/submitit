package no.java.submitit.app.pages

import org.apache.wicket.markup.html.form.FormComponent

object WicketImplicits {

  /**
   * Mark any form compnent mandatory with a pound sign! :-)
   */
  implicit def componentRequiredWithPound[T <: FormComponent](comp: T) = new {
    def unary_! : T = {
      comp.setRequired(true);
      comp
    }

  }
  
}
