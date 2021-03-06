import com.typesafe.sbt.GitPlugin.autoImport.git
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.{HeaderLicense, headerLicense}
import sbt.Keys._
import sbt._

object CommonSettingsPlugin extends CommonSettingsPluginTpl {

  // the rationale for placing settings defs here is that they should (or can) not be updated automatically using the scala-base-sync script
  // in the following, organizationName and startYear would also be required by sbt-header to generate ready-made license headers
  override lazy val projectSettings: Seq[Def.Setting[_]] = {
    tplProjectSettingsPlus(
      developers := List(Developer("cerst", "Constantin Gerstberger", "", url("https://github.com/cerst"))),
      git.baseVersion := "0.0.0",
      // basically only needed for sbt-ghpages
      git.remoteRepo := "git@github.com:cerst/structured-log4j.git",
      headerLicense := Some(HeaderLicense.MIT(startYear.value.get.toString, organizationName.value)),
      homepage := Some(url("https://github.com/cerst/structured-log4j")),
      licenses += "MIT" -> url("https://opensource.org/licenses/MIT"),
      organization := "com.github.cerst",
      organizationName := "Constantin Gerstberger",
      publishMavenStyle := true,
      resolvers ++= Dependencies.resolvers,
      // Do not produce warnings for macro-generated code (complains about unused pattern vars)
      scalacOptions ++= Seq("-Ywarn-macros:after"),
      scmInfo := Some(ScmInfo(homepage.value.get, git.remoteRepo.value)),
      startYear := Some(2019)
    )
  }
}
