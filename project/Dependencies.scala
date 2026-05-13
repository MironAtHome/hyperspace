import sbt._

object Dependencies {
  def commonDeps = Seq(
    "org.apache.spark" %% "spark-catalyst" % "3.1.1" % "provided" withSources (),
    "org.apache.spark" %% "spark-core" % "3.1.1" % "provided" withSources (),
    "org.apache.spark" %% "spark-sql" % "3.1.1" % "provided" withSources (),
    "org.mockito" %% "mockito-scala" % "0.4.0" % "test",
    "org.scalacheck" %% "scalacheck" % "1.14.2" % "test",
    "org.apache.spark" %% "spark-catalyst" % "3.1.1" % "test" classifier "tests",
    "org.apache.spark" %% "spark-core" % "3.1.1" % "test" classifier "tests",
    "org.apache.spark" %% "spark-sql" % "3.1.1" % "test" classifier "tests",
    "org.scalatest" %% "scalatest" % "3.2.3" % "test",
    "org.scalatestplus" %% "scalatestplus-scalacheck" % "3.1.0.0-RC2" % "test",
    "io.delta" %% "delta-core" % "0.8.0" % "provided" withSources (),
    "org.apache.iceberg" % "iceberg-spark3-runtime" % "0.11.1" % "provided" withSources ()
  )
}

