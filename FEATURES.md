# ReSaver Application Features

## Core Functionality
- **Save File Management**: Comprehensive tool for managing Elder Scrolls (Skyrim) save files
- **Save File Analysis**: Detailed inspection of save file contents and metadata
- **Save File Repair**: Capability to fix corrupted save files
- **File Format Support**: Handles ESS, ESP, PEX, and various archive formats

## Technical Capabilities
- **Modern Java Compatibility**: Fully functional on Java 24
- **Automated Build System**: Maven-based build process (~30 second build time)
- **Dependency Management**: 19+ external libraries automatically managed
- **Cross-Platform**: Runs on Windows, macOS, and Linux systems

## User Interface Features
- **Comprehensive Information Display**: Detailed savegame metadata including:
  - Game and save information (name, version, save number)
  - Character details (name, level, race, gender, XP progress)
  - Screenshot information (dimensions, data size)
  - File size and compression details
  - Plugin and mod information
- **Hybrid GUI Framework**: Swing/JavaFX implementation with smart fallback
- **Visual Elements**: Icons, images, and visual indicators throughout the UI

## Advanced Capabilities
- **Command Line Interface**: Powerful CLI with help system via PicoCLI
- **HTML Generation**: Capability to generate HTML reports using j2html
- **Compression Support**: LZ4 compression for efficient file handling
- **Character Detection**: Automatic character encoding detection

## Restoration Enhancements
- **Modernized Architecture**: Professional multi-tier design
- **VS Code Integration**: Full development environment setup
- **Production-Ready Build**: Single JAR distribution
- **Future-Proofing**: Addressed deprecated API usage for Java compatibility

## Utility Features
- **Progress Tracking**: Console progress indicators
- **Error Handling**: Robust exception handling mechanisms
- **Data Structures**: Specialized collections and utilities
- **Formatting Utilities**: Hex formatters and string manipulation tools

## Suggested Future Features
- **Save File Comparison**: Identify changes between different save files
- **Mod Impact Analysis**: Visualize which mods affect specific game elements
- **Script Cleanup**: Detect and remove orphaned scripts causing save bloat
- **Character Build Manager**: Track perk progression and skill development
- **Save File Timeline**: Visual progression through game saves with key events
- **Conflict Detection**: Identify potential mod conflicts in save files
- **Backup System**: Automated save file versioning and restoration
- **Performance Metrics**: Display script latency and memory usage stats
- **Plugin Sorting**: Integrate LOOT-like functionality for load order
- **Screenshot Gallery**: Browse and manage embedded save screenshots

### Mod Impact Analysis
**Implementation Plan:**
1. **Data Collection Layer**
   - Parse mod metadata from ESP/ESM files
   - Extract mod load order from save files
   - Map base game records vs modified records

2. **Analysis Engine**
   - Develop dependency graph of mod relationships
   - Implement change detection algorithms
   - Calculate "impact score" for each mod

3. **UI Integration**
   - New "Mod Impact" tab in main interface
   - Force-directed graph visualization
   - Filterable conflict reports

4. **Technical Requirements**
   - Add `graphviz-java` dependency
   - Implement lazy loading for large mod sets
   - Disk-based caching system

5. **Implementation Phases**
   - Data Layer (3 weeks)
   - Analysis Engine (4 weeks)
   - UI Integration (3 weeks)
   - Testing & Optimization (3 weeks)

6. **Challenges**
   - Reverse engineering mod record formats
   - Handling script fragment injections
   - Performance with 1000+ mod setups
