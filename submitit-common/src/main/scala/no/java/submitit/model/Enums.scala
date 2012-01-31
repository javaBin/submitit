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

package no.java.submitit.model

object Language extends Enumeration {
  val Norwegian = Value("Norwegian")
  val English = Value("English")
}

object Level extends Enumeration {
  val Beginner = Value("Beginner")
  val Intermediate = Value("Intermediate")
  val Advanced = Value("Advanced")
  val Hardcore = Value("Hardcore")
}

object PresentationFormat extends Enumeration {
  val Presentation = Value("Presentation")
  val LightningTalk = Value("Lightning talk")
}


object Status extends Enumeration {
  val Pending = Value("Pending")
  val Approved = Value("Approved")
  val NotApproved = Value("Not approved")
}

