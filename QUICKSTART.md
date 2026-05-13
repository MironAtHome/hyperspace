# Quick Start Guide - Hyperspace Spark 3.5 Branch

## Location
```
C:\dev\git\oss\hyperspace35
```

## Prerequisites

- Java 17 (or later)
- sbt 1.11.7 (or compatible)
- Python optional (for Python tests)

## Quick Build

```bash
cd C:\dev\git\oss\hyperspace35

# Compile the project
sbt compile

# Run tests (optional)
sbt test

# Create distribution JAR (optional)
sbt package
```

## Verify Installation

After compilation succeeds, you should see:
- ✓ `target/scala-2.12/classes/` with ~8000+ compiled classes
- ✓ `target/scala-2.12/src_managed/main/sbt-buildinfo/BuildInfo.scala` (auto-generated)
- ✓ `[success]` message in terminal

**Expected output:**
```
[success] Total time: 33 s, completed [timestamp]
```

## What This Branch Includes

### Spark Integration
- Apache Spark 3.5.1 (provided)
- Delta Lake 3.3.2 (with delta-spark backend)
- Apache Iceberg 0.11.1 (spark3-runtime)

### Index Types
- Covering indexes
- Z-Order covering indexes
- Dataskipping indexes with sketches

### Features
- V1 and V2 Delta Lake plan patterns
- Iceberg table support
- Cost-based index selection
- Index materialization and refresh
- Telemetry and logging

## Project Files Reference

| File | Purpose |
|------|---------|
| `build.sbt` | Single-project configuration (Spark 3.5.1) |
| `project/Dependencies.scala` | Dependency versions (Spark 3.5.1, Delta 3.3.2, etc.) |
| `project/plugins.sbt` | sbt plugins (BuildInfo, Scalastyle) |
| `project/build.properties` | sbt version |
| `scalastyle-config.xml` | Code style rules |
| `README.md` | Comprehensive documentation |
| `BRANCH_CREATION_SUMMARY.md` | Complete creation details |

## Source Code Organization

```
src/
├── main/scala/          # 149 consolidated main sources
│   └── com/microsoft/hyperspace/
│       ├── actions/
│       ├── index/
│       │   ├── sources/     # Delta, Iceberg support
│       │   ├── covering/
│       │   ├── execution/
│       │   ├── rules/
│       │   └── dataskipping/
│       ├── util/
│       └── telemetry/
└── test/scala/          # ~92 test sources
    └── com/microsoft/hyperspace/
        └── index/
            ├── sources/
            ├── covering/
            └── [test suites]
```

All sources consolidated into single `src/main/scala` tree with NO version-specific directory names.

## Compiler Target Versions

- **Java**: 17 (modern LTS with optimizations)
- **Scala**: 2.12.18
- **Spark**: 3.5.1 (latest 3.5.x)
- **sbt**: 1.11.7
- **Delta**: 3.3.2 (Spark 3.5 optimized)

## Common Commands

```bash
# Clean build
sbt clean compile

# Run specific test
sbt "testOnly com.microsoft.hyperspace.index.covering.CoveringIndexSuite"

# Run all tests
sbt test

# Check code style
sbt scalastyle

# Create JAR
sbt package

# Run REPL
sbt console
```

## Troubleshooting

### "Java version X is not supported"
- Ensure Java 17 is installed and in PATH
- Run `java -version` to verify
- Set JAVA_HOME environment variable if needed

### "sbt: command not found"
- Install sbt 1.11.7 or later
- Ensure sbt is in PATH
- On Windows, use `sbt.bat` command

### Compilation errors
- Run `sbt clean` first
- Check that all Scala files are present in `src/main/scala`
- Verify Java 17 and Scala 2.12.18 availability

### Tests fail
- Tests require Spark context: no parallel execution
- Tests fork JVM process: may need `-Xmx1024m`
- Some tests may require specific Spark configuration

## Build Artifacts

After `sbt package`, the JAR file is located at:
```
target/scala-2.12/hyperspace-core_2.12-0.4.0.jar
```

This JAR can be used with Spark 3.5.1:
```bash
spark-submit --jars target/scala-2.12/hyperspace-core_2.12-0.4.0.jar ...
```

## IDE Integration

If using IntelliJ IDEA, JetBrains Rider, or VS Code:

1. Open folder `C:\dev\git\oss\hyperspace35`
2. IDE should automatically detect sbt project
3. All sources resolve to `src/main/scala` (no ambiguity)
4. No need to configure Spark version selectively

## Documentation

- `README.md` - Full project documentation and features
- `BRANCH_CREATION_SUMMARY.md` - Detailed creation and configuration
- `scalastyle-config.xml` - Code style rules explanation

## Building for Production

```bash
# Clean compile
sbt clean compile

# Run full test suite
sbt test

# Build distribution JAR
sbt package

# Resulting artifact: target/scala-2.12/hyperspace-core_2.12-0.4.0.jar
```

## Features Enabled by Modern Java 17 & Scala 2.12.18

- Pattern matching optimizations
- Sealed classes and traits
- Better type inference
- Performance optimizations in Spark 3.5
- Latest Apache Iceberg and Delta Lake APIs
- Java 17 inline optimizations enabled

## Support

For issues or questions:
1. Check BRANCH_CREATION_SUMMARY.md for technical details
2. Review README.md for feature documentation
3. Check build.sbt for configuration
4. Verify dependencies in project/Dependencies.scala

---

**Created**: May 12, 2026  
**Spark Version**: 3.5.1  
**Java Target**: 17  
**Scala Version**: 2.12.18

