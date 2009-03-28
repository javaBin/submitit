package no.java.submitit.app.pages

import _root_.java.io.Serializable

/**
 * Becuase of combination of Wicket versioning (Serialization) and Scala's non-serializable functions we cannot simply pass inn functions.
 * This trait is the replacement.
 */
trait LoginHandler extends Serializable {

  def onLogin(pwd: String)
  
}
