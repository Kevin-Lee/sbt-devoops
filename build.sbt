import BuildTools._
import ProjectInfo._
import sbt.ScmInfo

ThisBuild / scalaVersion := props.ProjectScalaVersion
ThisBuild / crossSbtVersions := props.CrossSbtVersions
ThisBuild / developers := List(
  Developer(
    props.GitHubUsername,
    "Kevin Lee",
    "kevin.code@kevinlee.io",
    url(s"https://github.com/${props.GitHubUsername}"),
  )
)
ThisBuild / homepage := url(s"https://github.com/${props.GitHubUsername}/${props.ProjectName}").some
ThisBuild / scmInfo :=
  ScmInfo(
    url(s"https://github.com/${props.GitHubUsername}/${props.ProjectName}"),
    s"git@github.com:${props.GitHubUsername}/${props.ProjectName}.git",
  ).some
ThisBuild / licenses := List("MIT" -> url("http://opensource.org/licenses/MIT"))
ThisBuild / startYear := 2018.some
ThisBuild / testFrameworks ~=
  (frameworks => (TestFramework("hedgehog.sbt.Framework") +: frameworks).distinct)
Global / sbtVersion := props.GlobalSbtVersion

lazy val sbtDevOops = Project(props.ProjectName, file("."))
  .enablePlugins(SbtPlugin)
  .enablePlugins(DevOopsGitHubReleasePlugin, DocusaurPlugin)
  .settings(
    organization := props.Org,
    name := props.ProjectName,
    description := "DevOops - DevOps tool for GitHub",
    writeVersion := versionWriter(Def.spaceDelimited("filename").parsed)(version.value),
    docusaurDir := (ThisBuild / baseDirectory).value / "website",
    docusaurBuildDir := docusaurDir.value / "build",
    gitHubPagesOrgName := props.GitHubUsername,
    gitHubPagesRepoName := props.ProjectName,
    publishMavenStyle := true,
  )
  .dependsOn(
    sbtDevOopsCommon,
    sbtDevOopsScala,
    sbtDevOopsSbtExtra,
    sbtDevOopsGitHub,
    sbtDevOopsJava,
  )
  .aggregate(
    sbtDevOopsCommon,
    sbtDevOopsScala,
    sbtDevOopsSbtExtra,
    sbtDevOopsGitHub,
    sbtDevOopsJava,
  )

lazy val sbtDevOopsCommon = subProject(props.SubProjectNameCommon, file(props.SubProjectNameCommon))
  .enablePlugins(SbtPlugin)
  .settings(
    libraryDependencies ++= List(
      libs.semVer,
      libs.commonsIo,
      libs.newtype % Test,
      libs.cats    % Test,
    ) ++ libs.hedgehogLibs
  )

lazy val sbtDevOopsScala = subProject(props.SubProjectNameScala, file(props.SubProjectNameScala))
  .enablePlugins(SbtPlugin)
  .dependsOn(sbtDevOopsCommon)

lazy val sbtDevOopsSbtExtra = subProject(props.SubProjectNameSbtExtra, file(props.SubProjectNameSbtExtra))
  .enablePlugins(SbtPlugin)

lazy val sbtDevOopsGitHub = subProject(props.SubProjectNameGitHub, file(props.SubProjectNameGitHub))
  .enablePlugins(SbtPlugin)
  .settings(
    libraryDependencies ++= libs.all(scalaVersion.value)
  )
  .dependsOn(sbtDevOopsCommon)

lazy val sbtDevOopsJava = subProject(props.SubProjectNameJava, file(props.SubProjectNameJava))
  .enablePlugins(SbtPlugin)

def subProject(projectName: String, path: File) = Project(projectName, path)
  .settings(
    organization := props.Org,
    name := projectName,
    Compile / console / scalacOptions := scalacOptions.value diff List("-Ywarn-unused-import", "-Xfatal-warnings"),
    Compile / compile / wartremoverErrors ++= commonWarts,
    Test / compile / wartremoverErrors ++= commonWarts,
    testFrameworks ~=
      (frameworks => (TestFramework("hedgehog.sbt.Framework") +: frameworks).distinct),
    licenses := List("MIT" -> url("http://opensource.org/licenses/MIT")),
    publishMavenStyle := true,
    console / initialCommands := """import kevinlee.sbt._""",
    coverageHighlighting := (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 10)) =>
        false
      case _             =>
        true
    }),
  )

lazy val props =
  new {

    final val Org            = "io.kevinlee"
    final val GitHubUsername = "Kevin-Lee"
    final val ProjectName    = "sbt-devoops"

    final val SubProjectNameCommon   = s"${ProjectName}-common"
    final val SubProjectNameScala    = s"${ProjectName}-scala"
    final val SubProjectNameSbtExtra = s"${ProjectName}-sbt-extra"
    final val SubProjectNameGitHub   = s"${ProjectName}-github"
    final val SubProjectNameJava     = s"${ProjectName}-java"

    final val ProjectScalaVersion = "2.12.12"
    final val CrossScalaVersions  = List(ProjectScalaVersion).distinct

    final val GlobalSbtVersion = "1.3.4"

    final val CrossSbtVersions = List(GlobalSbtVersion).distinct

    final val hedgehogVersion = "0.7.0"

    final val newtypeVersion = "0.4.4"

    final val catsVersion       = "2.6.1"
    final val catsEffectVersion = "2.5.1"

    final val effectieVersion = "1.11.0"
    final val loggerFVersion  = "1.11.0"

    final val refinedVersion = "0.9.25"

    final val circeVersion = "0.13.0"

    final val http4sVersion = "0.21.23"

    final val justSemVerVersion = "0.3.0"

    final val commonsIoVersion = "2.8.0"

    final val activationVersion    = "1.1.1"
    final val activationApiVersion = "1.2.0"

    final val IncludeTest = "compile->compile;test->test"
  }

lazy val libs =
  new {

    lazy val hedgehogLibs = List(
      "qa.hedgehog" %% "hedgehog-core"   % props.hedgehogVersion % Test,
      "qa.hedgehog" %% "hedgehog-runner" % props.hedgehogVersion % Test,
      "qa.hedgehog" %% "hedgehog-sbt"    % props.hedgehogVersion % Test,
    )

    lazy val newtype = "io.estatico" %% "newtype" % props.newtypeVersion

    lazy val refined = Seq(
      "eu.timepit" %% "refined"      % props.refinedVersion,
      "eu.timepit" %% "refined-cats" % props.refinedVersion,
    )

    lazy val cats       = "org.typelevel" %% "cats-core"   % props.catsVersion
    lazy val catsEffect = "org.typelevel" %% "cats-effect" % props.catsEffectVersion

    lazy val effectie = "io.kevinlee" %% "effectie-cats-effect" % props.effectieVersion

    lazy val loggerF = List(
      "io.kevinlee" %% "logger-f-cats-effect" % props.loggerFVersion,
      "io.kevinlee" %% "logger-f-sbt-logging" % props.loggerFVersion,
    )

    lazy val http4sClient = List(
      "org.http4s" %% "http4s-dsl"          % props.http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % props.http4sVersion,
      "org.http4s" %% "http4s-circe"        % props.http4sVersion,
    )

    lazy val circe = List(
      "io.circe" %% "circe-generic" % props.circeVersion,
      "io.circe" %% "circe-parser"  % props.circeVersion,
      "io.circe" %% "circe-refined" % props.circeVersion,
    )

    lazy val semVer = "io.kevinlee" %% "just-semver" % props.justSemVerVersion

    lazy val commonsIo = "commons-io" % "commons-io" % props.commonsIoVersion

    lazy val javaxActivation212 = List(
      "javax.activation" % "activation" % props.activationVersion
    )

    def all(scalaVersion: String) = crossVersionProps(
      List(
        commonsIo,
        semVer,
        newtype,
        cats,
        catsEffect,
        effectie,
      ) ++
        hedgehogLibs ++
        loggerF ++
        http4sClient ++
        circe,
      scalaVersion,
    ) {
      case Some((2, 12)) =>
        javaxActivation212
      case Some((2, 10)) =>
        Seq.empty
    }
  }

lazy val writeVersion = inputKey[Unit]("Write Version in File'")
