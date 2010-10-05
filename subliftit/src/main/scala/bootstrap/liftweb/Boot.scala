package bootstrap.liftweb

import net.liftweb._
import common._
import util._
import Helpers._
import http._
import sitemap._
import Loc._

class Boot {
  def boot {
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

