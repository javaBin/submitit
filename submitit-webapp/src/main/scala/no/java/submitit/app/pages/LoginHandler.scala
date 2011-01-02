/*
 * Copyright 2011 javaBin
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package no.java.submitit.app.pages

import _root_.java.io.Serializable

/**
 * Becuase of combination of Wicket versioning (Serialization) and Scala's non-serializable functions we cannot simply pass inn functions.
 * This trait is the replacement.
 */
trait LoginHandler extends Serializable {

  def onLogin(pwd: String)
  
}
