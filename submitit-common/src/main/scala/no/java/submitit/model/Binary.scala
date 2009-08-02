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

package no.java.submitit.model
import common.IOUtils._

import _root_.java.io._

class Binary private(var id: String, val name: String, val contentType: String) extends Serializable {

  var tmpFileName: Option[String] = None
  private var fileLength: Int = _
  
  def hasContent = tmpFileName.isDefined
  def isNew = id == null
 
  def this(name: String, contentType: String) = 
    this(null, name, contentType)
  
  def content: Option[Array[byte]] = {
    if (tmpFileName.isDefined) {
      var res = List[Byte]()
    	using(new BufferedInputStream(new FileInputStream(new File(tmpFileName.get)))) { stream =>
    		var value = stream.read
    		while(value != -1) {
    			res = value.toByte :: res
    		  value = stream.read
    		}
    	}
      Some(res.reverse.toArray)
    }
    else None
  }
    
  private def content_= (content: Array[Byte]) {
   val tempFile = File.createTempFile(name, ".tmp");
   fileLength = content.length
   using(new BufferedOutputStream(new FileOutputStream(tempFile))) { stream =>
   	content.foreach(stream.write(_))
   }
   tmpFileName = Some(tempFile.getCanonicalPath)
  }
  
}

object Binary {
  
	def apply(name: String, contentType: String, content: Array[Byte]): Binary = apply(null, name, contentType, content) 
   
  def apply(id: String, name: String, contentType: String, content: Array[Byte]) = {
    println(name + " " + content)
    val res = new Binary(id, name, contentType)
    if(content != null) res.content = content
    res
  }
  
}

