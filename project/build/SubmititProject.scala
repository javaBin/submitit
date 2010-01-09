import sbt._

class SubmititProject(info: ProjectInfo) extends ParentProject(info)
{
   lazy val common = project("submitit-common", "Model and common code")
   lazy val ems = project("submitit-ems-client", "Ems conversions and client code", common)
   lazy val ui = project("submitit-webapp", "The web application", common, ems)

   val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"

   override def parallelExecution = true
}