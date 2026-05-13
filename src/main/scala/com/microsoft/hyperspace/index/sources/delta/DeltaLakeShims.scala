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

package com.microsoft.hyperspace.index.sources.delta

import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.connector.catalog.Table
import org.apache.spark.sql.delta.actions.AddFile
import org.apache.spark.sql.delta.files.TahoeLogFileIndex
import org.apache.spark.sql.execution.datasources.{HadoopFsRelation, LogicalRelation}
import org.apache.spark.sql.execution.datasources.v2.{DataSourceV2Relation, DataSourceV2ScanRelation}

object DeltaLakeShims {
  def getFiles(location: TahoeLogFileIndex): Seq[AddFile] = {
    location.getSnapshot
      .filesForScan(projection = Nil, location.partitionFilters)
      .files
  }

  /**
   * Detects if a logical plan is a Delta Lake relation.
   * Supports both V1 LogicalRelation and V2 DataSourceV2(Scan)Relation patterns.
   *
   * In Spark 3.x, the V2ScanRelationPushdown rule can convert DataSourceV2Relation
   * into DataSourceV2ScanRelation during plan optimization.
   */
  def isDeltaRelation(plan: LogicalPlan): Boolean =
    plan match {
      // V1 pattern: LogicalRelation with HadoopFsRelation and TahoeLogFileIndex
      case LogicalRelation(HadoopFsRelation(_: TahoeLogFileIndex, _, _, _, _, _), _, _, _) =>
        true
      // V2 pattern: DataSourceV2Relation with Delta catalog table
      case DataSourceV2Relation(_, _, _, _, _) =>
        isDeltaV2Relation(plan)
      // V2 pattern: DataSourceV2ScanRelation wrapping Delta table
      case DataSourceV2ScanRelation(_, _, _) =>
        isDeltaV2ScanRelation(plan)
      case _ => false
    }

  /**
   * Converts a Delta plan (V1 or V2) to LogicalRelation for compatibility.
   * V2 plans are converted back to V1 representation when possible.
   */
  def toLogicalRelation(plan: LogicalPlan): LogicalRelation = {
    plan match {
      // Already V1
      case lr: LogicalRelation => lr
      // V2 Relation with Delta source
      case d2r @ DataSourceV2Relation(_, _, _, _, _) =>
        convertV2ToLogicalRelation(d2r, plan)
      // V2 ScanRelation
      case DataSourceV2ScanRelation(d2r: DataSourceV2Relation, _, _) =>
        convertV2ToLogicalRelation(d2r, plan)
      case _ =>
        throw new IllegalArgumentException(s"Unexpected Delta plan type: ${plan.getClass.toString}")
    }
  }

  private def isDeltaV2Relation(plan: LogicalPlan): Boolean = {
    try {
      plan match {
        case DataSourceV2Relation(table: Table, _, _, _, _) =>
          // Check if it's a Delta table by checking table class name
          table.getClass.getName.contains("DeltaTableV2") ||
          table.getClass.getName.contains("DeltaTable")
        case _ => false
      }
    } catch {
      case _: Exception => false
    }
  }

  private def isDeltaV2ScanRelation(plan: LogicalPlan): Boolean = {
    try {
      plan match {
        case DataSourceV2ScanRelation(DataSourceV2Relation(table, _, _, _, _), _, _) =>
          table.getClass.getName.contains("DeltaTableV2") ||
          table.getClass.getName.contains("DeltaTable")
        case _ => false
      }
    } catch {
      case _: Exception => false
    }
  }

  private def convertV2ToLogicalRelation(d2r: DataSourceV2Relation,
                                         originalPlan: LogicalPlan): LogicalRelation = {
    // For now, throw an error as V2 to V1 conversion requires deeper integration
    // This can be enhanced in future to create a synthetic LogicalRelation
    throw new UnsupportedOperationException(
      s"Delta V2 relations (DataSourceV2Relation) are not yet fully supported. " +
      s"Please ensure spark.sql.sources.useV1SourceList='delta' is set in your Spark config. " +
      s"Plan type: ${originalPlan.getClass.toString}")
  }
}
