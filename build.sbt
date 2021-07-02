lazy val scalaTestV = "3.2.5"
lazy val catsEffectV = "3.1.1"
lazy val fs2V = "3.0.0"
lazy val pureConfigV = "0.16.0"

lazy val root = (project in file("."))
  .settings(
    name := "scgol",
    scalaVersion := "2.13.5",
    sbtVersion := "1.5.4",
    organization := "io.github.mjftw",
    version := "0.1.0",
    scalafixOnCompile := true,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions ++= Seq(
      "-Ywarn-unused",
      "-feature",
      "-deprecation",
      "-unchecked",
      "-language:postfixOps"
    ),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % catsEffectV,
      "org.typelevel" %% "cats-effect-kernel" % catsEffectV,
      "co.fs2" %% "fs2-core" % fs2V,
      "co.fs2" %% "fs2-io" % fs2V,
      "co.fs2" %% "fs2-reactive-streams" % fs2V,
      "com.github.pureconfig" %% "pureconfig" % pureConfigV,
      "org.typelevel" %% "cats-effect-laws" % catsEffectV % Test,
      "org.scalatest" %% "scalatest" % scalaTestV % Test
    )
  )
