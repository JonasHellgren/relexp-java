---
name: inspect-repository
description: Inspect the Java codebase to identify packages, classes, entry points, and build files worth showing in a high-level architecture diagram. Use first, before build-codebase-map, whenever a codebase map/overview needs to be generated or refreshed.
---

# Purpose

Create a high-level map of the Java codebase located in:

`C:\JavaCode\relexp-java`

# Instructions

Inspect `C:\JavaCode\relexp-java` recursively.

Identify:

- Java source directories
- packages and subpackages
- important classes
- interfaces
- application entry points
- Maven or Gradle configuration
- tests
- resources
- configuration files
- external interfaces

Pay particular attention to:

- `src/main/java`
- `src/test/java`
- `src/main/resources`
- `pom.xml`
- `build.gradle`
- classes containing `public static void main(...)`

Ignore generated or irrelevant content such as:

- `target/`
- `build/`
- `.idea/`
- `.gradle/`
- compiled classes
- temporary files

Determine which parts are important enough to appear in a high-level architecture diagram.

Do not modify anything inside `C:\JavaCode\relexp-java`.