package bootstrap.liftweb

import net.liftweb._
import common._
import util._
import Helpers._
import http._
import sitemap._
import Loc._
import subliftit.config.Config

class Boot extends Logger {
  def boot {
    
    val configFile = LiftRules.context.initParam("submitit.properties").getOrElse(throw new Exception("Cannot find configuration for " +
            "submitit.properties in servet context. Cannot start application"))
    info("Loading properties from '" + configFile + "'")
    Config.initFromFile(configFile)
    
    LiftRules.addToPackages("subliftit")

    LiftRules.uriNotFound.prepend(NamedPF("404handler") {
      case (req, failure) => NotFoundAsTemplate(
        ParsePath(List("exceptions", "404"), "html", false, false))
    })

    def sitemap = List(
      Menu("Home") / "index",
      Menu("Submit") / "submit",
      Menu("Review") / "review" >> Hidden,
      Menu("Confirm") / "confirm" >> Hidden)

    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))
    LiftRules.setSiteMapFunc(() => SiteMap(sitemap: _*))
  }
}

