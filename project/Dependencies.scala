import sbt._

object Dependencies {

  val resolvers: Seq[Resolver] = Seq()

  object Version {
    // Log4j-2.9 and higher require disruptor-3.3.4.jar or higher on the classpath - https://logging.apache.org/log4j/2.0/manual/async.html
    val Disruptor = "3.4.2"
    val Log4j = "2.11.2"
    val UTest = "0.6.6"
  }

  // comment licenses for dependencies using the SPDX short identifier (see e.g. https://opensource.org/licenses/Apache-2.0)
  // rationale: double check the license when adding a new library avoid having to remove a problematic one later on when it is in use and thus hard to remove
  object Library {
    val Disruptor = "com.lmax" % "disruptor" % Version.Disruptor
    // Apache-2.0
    val Log4jCore = "org.apache.logging.log4j" % "log4j-core" % Version.Log4j
    // Apache-2.0
    def scalaReflect(scalaVersion: String): ModuleID = "org.scala-lang" % "scala-reflect" % scalaVersion
    // MIT
    val Utest = "com.lihaoyi" %% "utest" % Version.UTest
  }

  def coreLibraries(scalaVersion: String): Seq[ModuleID] =
    Seq(Library.Disruptor, Library.Log4jCore, Library.scalaReflect(scalaVersion), Library.Utest % Test)

}
