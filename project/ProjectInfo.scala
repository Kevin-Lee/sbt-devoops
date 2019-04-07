import wartremover.WartRemover.autoImport.{Wart, Warts}

/**
  * @author Kevin Lee
  * @since 2018-05-21
  */
object ProjectInfo {

  val ProjectScalaVersion = "2.12.7"
  val CrossScalaVersions = Seq("2.10.7", ProjectScalaVersion)

  val CrossSbtVersions = Vector("0.13.17", "1.2.6")

  val ProjectVersion = "0.1.0"

  val commonScalacOptions = Seq(
    "-deprecation"
    , "-unchecked"
    , "-feature"
    , "-Ywarn-value-discard"
    , "-Yno-adapted-args"
    , "-Xlint"
    , "-Xfatal-warnings"
    , "-Ywarn-dead-code"
    , "-Ywarn-inaccessible"
    , "-Ywarn-nullary-unit"
    , "-Ywarn-nullary-override"
    , "-encoding", "UTF-8"
  )

  val commonWarts = Warts.allBut(Wart.DefaultArguments, Wart.Overloading, Wart.Any, Wart.Nothing, Wart.NonUnitStatements)

}
