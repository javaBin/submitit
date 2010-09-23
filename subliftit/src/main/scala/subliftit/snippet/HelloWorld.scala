package subliftit
package snippet

import xml.NodeSeq
import net.liftweb.util.Helpers._

class HelloWorld {
  def howdy(xhtml: NodeSeq): NodeSeq =
    bind("b", xhtml,
      "time" -> (new _root_.java.util.Date).toString)
}
