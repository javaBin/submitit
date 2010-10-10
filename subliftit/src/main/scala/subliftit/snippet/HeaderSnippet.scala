package subliftit.snippet

import net.liftweb.sitemap.Loc.Snippet
import subliftit.config.Config
import no.java.submitit.config.Keys.{headerLogoText, headerText}
import xml.{NodeSeq, Text}
import net.liftweb.util.Helpers._

class HeaderSnippet extends Config {
  private def logoTxt = configValue(headerLogoText).get
  private def headerTxt = configValue(headerText).get

  def header(xml: NodeSeq) = bind("txt", xml,
                                  "logotext" -> logoTxt,
                                  "headertext" -> headerTxt)
}