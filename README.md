# ReSaver


A tool for managing Skyrim save files, written in Java with JavaFX.

## For End Users (No Java or Maven Required)

**Windows users:**

- Download the latest release ZIP or EXE package (see Releases or distribution instructions).
- Extract the ZIP (if applicable).
- Double-click `ReSaver.exe` inside the extracted folder to launch the application.
- **No Java or Maven installation is required.**

If you are a developer or want to build from source, see below.


## Prerequisites (Development Only)

- Java 11 or higher (Java 8+ supported; Java 24 tested)
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


## Building for Distribution (Advanced/Developers)

To create a distributable JAR with all dependencies:

```bash
mvn clean package
```

This will create:
- `target/resaver-7.28-SNAPSHOT.jar` - Regular JAR
- `target/resaver-7.28-SNAPSHOT-shaded.jar` - Fat JAR with dependencies

### Creating a Standalone Windows EXE (No Java Required)

If you want to generate a portable Windows EXE (for end users):

1. Install JDK 14 or newer (with `jpackage` included).
2. Run the following command in PowerShell (adjust paths as needed):

```powershell
& "C:\Program Files\Java\jdk-24\bin\jpackage.exe" `
  --type app-image `
  --input target `
  --name ReSaver `
  --main-jar ReSaver.jar `
  --main-class resaver.ReSaver `
  --icon src/main/resources/Disk.ico `
  --dest dist
```

This will create a folder in `dist/ReSaver` containing `ReSaver.exe` and all required files. Distribute the entire folder to users.

**Note:** For a true Windows installer (MSI), install WiX Toolset and use `--type msi` instead.

## License

Licensed under the Apache License, Version 2.0. See LICENSE.TXT for details.
