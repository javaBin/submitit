import sbt._


class SubliftitProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val lift_webkit = "net.liftweb" %% "lift-webkit" % "2.1-RC1"
  val lift_mapper = "net.liftweb" %% "lift-mapper" % "2.1-RC1"
  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.21" % "test"
  val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided"

  override def scanDirectories = Nil

  override def jettyWebappPath = webappPath
}