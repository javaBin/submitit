import sbt._
import java.io.File       
import sbt.BasicScalaProject._

class SubmititProject(info: ProjectInfo) extends ParentProject(info){
  
  val javabin_release = "javaBin Smia" at "http://smia.java.no/maven/repo/release"	
  //Necessary for transitive dependencies in ems-client org.restlet. Project won't build without it
  val maven_restlet = "Public online Restlet repository" at "http://maven.restlet.org"
  val scalaToolsSnapshot = ScalaToolsSnapshots

  val ems_version = "1.1"
  val wicket_version = "1.4.10"
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
  })
  
  lazy val ems = project("submitit-ems-client", "Ems Client", new DefaultProject(_) with OutPutPaths {
    val ems_client = "no.java.ems" % "ems-client" % ems_version
    val junit = "junit" % "junit" % "4.5" % "test"    	
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
  
  lazy val lift = project("subliftit", "Lift webapp", new DefaultWebProject(_) with OutPutPaths {
    override def ivyXML =
     <dependencies>
       <dependency org="no.java.ems" name="ems-client" rev="1.1"> 
         <exclude org="joda-time" name="joda-time"/>
       </dependency>
     </dependencies>

    def lift(name:String) = "net.liftweb" %% ("lift-"+name) % "2.1" withSources()

    val lift_webkit = lift("webkit")
    val lift_util = lift("util") // silly ivy not supporting transitivity for source download
    val lift_record = lift("record")
    val lift_textile = lift("textile")
    
    val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.21" % "test"
    val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided"
    val ems_client = "no.java.ems" % "ems-client" % ems_version
    val scala_test = "org.scalatest" % "scalatest" % "1.2-for-scala-2.8.0.final-SNAPSHOT" % "test"
    val junit = "junit" % "junit" % "4.5" % "test"

    override def scanDirectories = Nil
    override def jettyWebappPath = webappPath
  }, common)
  
}