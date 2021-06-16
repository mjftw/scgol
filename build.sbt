lazy val CatsEffectVersion = "3.1.1"
lazy val ScalaTestVersion = "3.2.5"

lazy val root = (project in file("."))
  .settings(
    organization := "io.github.mjftw",
    name := "scgol",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.14",
    scalacOptions := Seq(
      "-deprecation",
      "-encoding",
      "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-Ypartial-unification"
    ),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % CatsEffectVersion,
      "org.scalatest" %% "scalatest" % ScalaTestVersion % Test
    )
  )
