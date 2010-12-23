import sbt._
import java.io.File       
import sbt.BasicScalaProject._

class SubmititProject(info: ProjectInfo) extends ParentProject(info){
  
  val javabin_release = "javaBin Smia" at "http://smia.java.no/maven/repo/release"	
  //Necessary for transitive dependencies in ems-client org.restlet. Project won't build without it
  val maven_restlet = "Public online Restlet repository" at "http://maven.restlet.org"
  val scalaToolsSnapshots = "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"

  val ems_version = "1.1"
  val wicket_version = "1.4.14"
  val junit_version = "4.5"
  
  override def parallelExecution = true

  override def disableCrossPaths = true

  trait OutPutPaths extends BasicScalaProject {

    override def outputPath = "target"
    override def mainCompilePath = outputPath / "classes"
    override def testCompilePath = outputPath / "test-classes"

  }

  lazy val common = project("submitit-common", "Common", new DefaultProject(_) with OutPutPaths {
    val slf4j = "org.slf4j" % "slf4j-log4j12" % "1.4.2"
    val log4j = "log4j" % "log4j" % "1.2.14"
    val junit = "junit" % "junit" % "4.8.2" % "test"
    val scala_test = "org.scalatest" % "scalatest" % "1.2" % "test"
  })
  
  lazy val ems = project("submitit-ems-client", "Ems Client", new DefaultProject(_) with OutPutPaths {
    val ems_client = "no.java.ems" % "ems-client" % ems_version
  }, common)
  
  lazy val ui = project("submitit-webapp", "WebApplication", new DefaultWebProject(_) with OutPutPaths {
    system[File]("submitit.properties")() = "src" / "test" / "resources" / "submitit.properties" asFile
    override def mainResources = super.mainResources +++ descendents( mainSourceRoots, "*" ) --- mainSources
    override val mainResourcesOutputPath = "target" / "classes"
    override val testResourcesOutputPath = "target" / "test-classes"

    protected override def copyResourcesAction = copyTask(mainResources, mainResourcesOutputPath) describedAs CopyResourcesDescription
    protected override def copyTestResourcesAction = copyTask(testResources, testResourcesOutputPath) describedAs CopyTestResourcesDescription

    val ems_wiki = "no.java.ems" % "ems-wiki" % ems_version
    val wicket = "org.apache.wicket" % "wicket" % wicket_version
    val wicket_extensions = "org.apache.wicket" % "wicket-extensions" % wicket_version
    val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided"
    val mail = "javax.mail" % "mail" % "1.4.1"
    val jetty_server = "org.mortbay.jetty" % "jetty" % "6.1.14" % "test"
  }, common, ems)

}