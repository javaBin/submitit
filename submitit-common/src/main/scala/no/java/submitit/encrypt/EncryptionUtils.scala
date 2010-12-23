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

object EncryptionUtils {

  private val salt = "c6843fc8".getBytes

  def encrypt(secretKey: String, value: String) = {
    val cipher = initCipher(Cipher.ENCRYPT_MODE, secretKey)
    urlEncode(cipher.doFinal(value.getBytes))
  }

  def decrypt(secretKey: String, value: String) = {
    val cipher = initCipher(Cipher.DECRYPT_MODE, secretKey)
    new String(cipher.doFinal(urlDecode(value)))
  }

  private def initCipher(cipherMode: Int, secretKey: String) = {
    val keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
    val key = keyFactory.generateSecret(new PBEKeySpec(secretKey.toCharArray))
    val pbeCipher = Cipher.getInstance("PBEWithMD5AndDES")
    pbeCipher.init(cipherMode, key, new PBEParameterSpec(salt, 20))
    pbeCipher
  }

  private def urlEncode(value: Array[Byte]) = URLEncoder.encode(new BASE64Encoder().encode(value), "UTF-8")
  private def urlDecode(value: String) = new BASE64Decoder().decodeBuffer(URLDecoder.decode(value, "UTF-8"))

}