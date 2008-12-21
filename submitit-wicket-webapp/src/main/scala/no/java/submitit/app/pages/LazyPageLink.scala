package no.java.submitit.app.pages

import org.apache.wicket.Page
import org.apache.wicket.markup.html.link.IPageLink

class LazyPageLink[C <: Page](f: => Page, c: Class[C]) extends IPageLink {
  
  def getPage = f
  
  def getPageIdentity = c

}
