package bootstrap.liftweb

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import net.liftweb.mapper.{DB, DefaultConnectionIdentifier, StandardDBVendor, Schemifier}
import subliftit.model._
import subliftit.config.Config

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

    // TODO load location from servletconfig
    Config.initFromFile("subliftit/src/test/resources/submitit.properties")

    // where to search snippet
    LiftRules.addToPackages("subliftit")


    object DBVendor extends StandardDBVendor(
      Props.get("db.class").openOr("org.h2.Driver"),
      Props.get("db.url").openOr("jdbc:h2:mem:test"),
      Props.get("db.user"),
      Props.get("db.pass"))

    DB.defineConnectionManager(DefaultConnectionIdentifier, DBVendor)
    LiftRules.unloadHooks.append(DBVendor.closeAllConnections_! _)


    Schemifier.schemify(true, Schemifier.infoF _, User, Submission)

    LiftRules.uriNotFound.prepend(NamedPF("404handler") {
      case (req, failure) => NotFoundAsTemplate(
        ParsePath(List("exceptions", "404"), "html", false, false))
    })

    S.addAround(DB.buildLoanWrapper)


    // Build the application SiteMap
    def sitemap = List(
      Menu("Home") / "index",
      Menu("Submit") / "submit",
      Menu("Submissions") / "submissions") ::: User.menus

    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))
    LiftRules.setSiteMapFunc(() => SiteMap(sitemap: _*))
  }
}

