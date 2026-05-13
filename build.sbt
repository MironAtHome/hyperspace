import Dependencies._
import Path.relativeTo

ThisBuild / scalaVersion := "2.12.18"

// Target Java 17 (latest LTS compatible with Scala 2.12.18)
ThisBuild / javacOptions ++= Seq("-source", "17", "-target", "17")

// Enable advanced Scala 2.12 features and stricter compilation
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Ywarn-unused-import",
  "-Ywarn-dead-code",
  "-opt:l:inline",
  "-opt-inline-from:**"
)

ThisBuild / javaOptions += "-Xmx1024m"

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "hyperspace-core",
    organization := "com.microsoft.hyperspace",
    version := "0.4.0",
    libraryDependencies ++= commonDeps,

    // BuildInfo generation for version metadata
    buildInfoKeys := Seq[BuildInfoKey](version),
    buildInfoPackage := "com.microsoft.hyperspace",

    // Scalastyle configuration
    scalastyleConfig := baseDirectory.value / "scalastyle-config.xml",
    compileScalastyle := (Compile / scalastyle).toTask("").value,
    Compile / compile := ((Compile / compile) dependsOn compileScalastyle).value,
    testScalastyle := (Test / scalastyle).toTask("").value,
    Test / test := ((Test / test) dependsOn testScalastyle).value,

    // Package Python files alongside JAR
    (Compile / packageBin / mappings) := (Compile / packageBin / mappings).value ++ listPythonFiles.value,
    listPythonFiles := {
      val pythonBase = baseDirectory.value.getParentFile / "python"
      if (pythonBase.exists()) {
        pythonBase ** "*.py" pair relativeTo(pythonBase)
      } else {
        Seq()
      }
    }
  )

lazy val listPythonFiles = taskKey[Seq[(File, String)]]("listPythonFiles")
lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
lazy val testScalastyle = taskKey[Unit]("testScalastyle")

// Test configuration
Test / parallelExecution := false
Test / fork := true
Test / javaOptions += "-Xmx1024m"
Test / envVars += "SPARK_TESTING" -> "1"



