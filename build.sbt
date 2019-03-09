def publishSettings(enabled: Boolean): Seq[Def.Setting[_]] = {
  if (!enabled) {
    Seq(skip in publish := true)
  } else {
    publishTo := Some {
      if (isSnapshot.value) {
        Opts.resolver.sonatypeSnapshots
      } else {
        Opts.resolver.sonatypeStaging
      }
    }
  }
}

lazy val root = (project in file("."))
  .aggregate(core, doc)
  .enablePlugins(GitBranchPrompt, GitVersioning)
  // this project is not supposed to be used externally, so don't publish
  .settings(publishSettings(enabled = false))
  .settings(name := "structured-log4j-root")

lazy val core = (project in file("core"))
  .enablePlugins(GitBranchPrompt, GitVersioning)
  // TODO: eventually this should be true
  .settings(publishSettings(enabled = false))
  .settings(libraryDependencies ++= Dependencies.coreLibraries, name := "structured-log4j")

lazy val doc = (project in file("doc"))
  .dependsOn(core)
  .enablePlugins(GitBranchPrompt, GitVersioning, ParadoxPlugin)
  // this project is not supposed to be used externally, so don't publish
  .settings(publishSettings(enabled = false))
  // all these settings are only relevant to the "doc" project which is why they are not defined in CommonSettingsPlugin.scala
  .settings(
    name := "structured-log4j-doc",
    // trigger dump-license-report in all other projects and rename the output
    // (paradox uses the first heading as link name in '@@@index' containers AND cannot handle variables in links)
    (mappings in Compile) in paradoxMarkdownToHtml ++= Seq(
      (core / dumpLicenseReport).value / ((core / licenseReportTitle).value + ".md") -> "licenses/core.md",
      dumpLicenseReport.value / (licenseReportTitle.value + ".md") -> "licenses/doc.md"
    ),
    // trigger code compilation of example code
    paradox in Compile := {
      val _ = (compile in Compile).value
      (paradox in Compile).value
    },
    // properties to be accessible from within the documentation
    paradoxProperties ++= Map("version" -> version.value),
    paradoxTheme := Some(builtinParadoxTheme("generic"))
  )
