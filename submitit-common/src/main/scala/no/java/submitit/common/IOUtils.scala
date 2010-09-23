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

package no.java.submitit.common

import _root_.java.io.InputStream

object IOUtils {

  def using[T, C <: { def close() }]
           (closeable: C)
           (f: C => T): T = {
             val converted = new Closeable { def close = closeable.close}
             execAndClose(converted, closeable, f)
           }	
           
  // Because of reflective invocation of the above function some close methods failed, because close was called on an internal sun class.           
  def usingIS[T] (closeable: InputStream)
           (f: InputStream => T): T = {
             val converted = new Closeable { def close = closeable.close}
             execAndClose(converted, closeable, f)
           }	     
           
  private trait Closeable {
    def close()
  }
  
  
  private def execAndClose[T, R] (closeable: Closeable, actual: R, f: R => T) = {
    try {
      f(actual)
    } finally
      if (closeable != null)
        try {
          closeable.close()
        } catch {
          case e => Console.err.println("Exception on close " + e.toString)
        }
  }
  
}
