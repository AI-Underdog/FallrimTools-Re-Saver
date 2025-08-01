@echo off
echo Starting ReSaver - Skyrim Save File Manager...

REM Try to run with Java 8+ compatibility settings
java -Xms512m -Xmx4g -jar target\ReSaver.jar %*

REM If that fails, try with more aggressive compatibility settings
if errorlevel 1 (
    echo.
    echo Trying with enhanced Java compatibility...
    java -Djava.awt.headless=false -Xms512m -Xmx4g --add-exports java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED -jar target\ReSaver.jar %*
)

if errorlevel 1 (
    echo.
    echo Error: Unable to start ReSaver.
    echo Please ensure you have Java 8 or higher installed.
    echo Java version check:
    java -version
    pause
)
