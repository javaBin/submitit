package no.java.submitit.app.pages.widgets

import junit.framework._
import Assert._

class WikiMarkupTest extends junit.framework.TestCase {

  def testSimpleMarkup {
    assertEquals("bla", MarkupHandler.applySimpleRegexes("bla"))
    assertEquals("<b>bla</b>", MarkupHandler.applySimpleRegexes("*bla*"))
    assertEquals("<i>bla</i>", MarkupHandler.applySimpleRegexes("_bla_"))
    assertEquals("<i>bla bla</i>", MarkupHandler.applySimpleRegexes("""_bla bla_"""))
  }
  
  def testListMarkup {
    val bullets = """"* line1
                     |* line2""".stripMargin
    val expected = """"<ul><li>line1
                     |</li></ul>""".stripMargin

    assertEquals(expected, MarkupHandler.bullets(bullets))
    println("aeoiajf") 
    true
  }
  
  
}
