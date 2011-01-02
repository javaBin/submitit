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

package no.java.submitit.encrypt

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class EncryptionUtilsTest extends FunSuite {

  private def doAssert(secretKey: String, value: String): Unit = {
    val encrypted = EncryptionUtils.encrypt(secretKey, value)
    assert(value != encrypted, "Make sure its actually different")
    val decrypted = EncryptionUtils.decrypt(secretKey, encrypted)
    assert(value === decrypted)
  }

  test("That encrypting and decrypting a password works as expected") {
    doAssert("longkeylongkeylongkeylongkeylongkeylongkeylongkey", "testing")
    doAssert("1", "1")
    doAssert("", "")
    doAssert("", "1")
  }

}