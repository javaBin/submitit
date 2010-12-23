/*
 * Copyright 2009 javaBin
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

import javax.crypto.spec. {PBEKeySpec, PBEParameterSpec}
import javax.crypto. {SecretKeyFactory, SecretKey, Cipher}
import sun.misc. {BASE64Decoder, BASE64Encoder}
import java.net. {URLDecoder, URLEncoder}
import java.lang.String

object EncryptionUtils {

  private val salt = "c6843fc8".getBytes
  private val encAlg: String = "PBEWithMD5AndTripleDES"

  def encrypt(secretKey: String, value: String) = {
    val cipher = initCipher(Cipher.ENCRYPT_MODE, secretKey)
    encode(cipher.doFinal(value.getBytes("UTF-8")))
  }

  def decrypt(secretKey: String, value: String) = {
    val cipher = initCipher(Cipher.DECRYPT_MODE, secretKey)
    new String(cipher.doFinal(decode(value)))
  }

  private def initCipher(cipherMode: Int, secretKey: String) = {

    val keyFactory = SecretKeyFactory.getInstance(encAlg)
    val key = keyFactory.generateSecret(new PBEKeySpec(secretKey.toCharArray))
    val pbeCipher = Cipher.getInstance(encAlg)
    pbeCipher.init(cipherMode, key, new PBEParameterSpec(salt, 20))
    pbeCipher
  }

  private def encode(value: Array[Byte]) = new BASE64Encoder().encode(value)
  private def decode(value: String) = new BASE64Decoder().decodeBuffer(value)

}