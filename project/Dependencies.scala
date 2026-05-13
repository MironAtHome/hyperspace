/*
 * Copyright (2021) The Hyperspace Project Authors.
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

import sbt._

object Dependencies {
  def commonDeps = Seq(
    "org.apache.spark" %% "spark-catalyst" % "2.4.2" % "provided" withSources (),
    "org.apache.spark" %% "spark-core" % "2.4.2" % "provided" withSources (),
    "org.apache.spark" %% "spark-sql" % "2.4.2" % "provided" withSources (),
    // Test dependencies
    "org.mockito" %% "mockito-scala" % "0.4.0" % "test",
    "org.scalacheck" %% "scalacheck" % "1.14.2" % "test",
    "org.apache.spark" %% "spark-catalyst" % "2.4.2" % "test" classifier "tests",
    "org.apache.spark" %% "spark-core" % "2.4.2" % "test" classifier "tests",
    "org.apache.spark" %% "spark-sql" % "2.4.2" % "test" classifier "tests",
    "org.scalatest" %% "scalatest" % "3.0.8" % "test",
    "org.apache.iceberg" % "iceberg-spark-runtime" % "0.11.0" % "provided" withSources ()
  )
}

