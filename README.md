# ReSaver

A tool for managing Skyrim save files, written in Java with JavaFX.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Development Setup

1. **Clone or download this repository**

2. **Open in VS Code**
   - Open the `ReSaver.code-workspace` file in VS Code
   - VS Code should automatically detect the Java project and install recommended extensions

3. **Build the project**
   ```bash
   mvn clean compile
   ```

4. **Run the application**
   ```bash
   mvn javafx:run
   ```

## Available Tasks

In VS Code, you can use the following tasks (Ctrl+Shift+P → "Tasks: Run Task"):

- **Maven: Clean** - Clean the build directory
- **Maven: Compile** - Compile the source code
- **Maven: Test** - Run unit tests
- **Maven: Package** - Create JAR files
- **Maven: Clean and Compile** - Clean and compile (default build task)
- **JavaFX: Run** - Run the JavaFX application

## Dependencies

- JavaFX 11 - GUI framework
- PicoCLI 4.6.3 - Command line interface
- j2html 1.5.0 - HTML generation
- LZ4 Pure Java 1.7.0 - Compression
- JUniversalCharDet 1.0.3 - Character detection
- JUnit 5 - Testing framework

## Project Structure

```
src/
├── main/
│   ├── java/           # Source code
│   └── resources/      # Resources (images, properties, etc.)
└── test/
    └── java/           # Test code
target/                 # Compiled classes and JARs
```

## Building for Distribution

To create a distributable JAR with all dependencies:

```bash
mvn clean package
```

This will create:
- `target/resaver-7.28-SNAPSHOT.jar` - Regular JAR
- `target/resaver-7.28-SNAPSHOT-shaded.jar` - Fat JAR with dependencies

## License

Licensed under the Apache License, Version 2.0. See LICENSE.TXT for details.
