/*
 * Copyright (2020) The Hyperspace Project Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import Dependencies._
import Path.relativeTo

lazy val scala212 = "2.12.18"
lazy val scala211 = "2.11.12"

ThisBuild / scalaVersion := scala212
ThisBuild / javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
ThisBuild / javaOptions += "-Xmx1024m"

lazy val pythonFiles = taskKey[Seq[(File, String)]]("Include Python files in package")

lazy val commonSettings = Seq(
  name := "hyperspace-core",
  organization := "com.microsoft",
  version := "0.4.0",

  // Compiler settings
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature"
  ),

  // Build info
  buildInfoKeys := Seq[BuildInfoKey](version),
  buildInfoPackage := "com.microsoft.hyperspace",

  // Dependencies
  libraryDependencies ++= commonDeps,

  // Package Python files if present
  (Compile / packageBin / mappings) := (Compile / packageBin / mappings).value ++ pythonFiles.value,
  pythonFiles := {
    val pythonBase = (ThisBuild / baseDirectory).value / "python"
    if (pythonBase.exists()) {
      pythonBase ** "*.py" pair relativeTo(pythonBase)
    } else {
      Seq()
    }
  }
)

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(commonSettings)
  .settings(
    crossScalaVersions := List(scala212, scala211)
  )

// ...existing code...

// Publish settings
publishMavenStyle := true
publishTo := Some("Maven Central" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/")

