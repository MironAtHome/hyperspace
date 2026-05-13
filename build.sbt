import Dependencies._

ThisBuild / scalaVersion := "2.12.18"
ThisBuild / javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
ThisBuild / javaOptions += "-Xmx1024m"

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "hyperspace-core",
    organization := "com.microsoft",
    version := "0.4.0",
    scalacOptions ++= Seq("-deprecation", "-feature"),
    libraryDependencies ++= commonDeps,
    buildInfoKeys := Seq[BuildInfoKey](version),
    buildInfoPackage := "com.microsoft.hyperspace"
  )

