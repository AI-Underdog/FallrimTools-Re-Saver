@echo off
REM ReSaver Launcher - Fixed Java Exception Issue
REM This replaces the old ReSaver.exe that was causing Java exceptions

echo ReSaver - Skyrim Save Manager (Fixed Version)
echo Loading...

REM Try the fixed JAR file first
if exist "target\ReSaver.jar" (
    java -jar "target\ReSaver.jar" %*
    exit /b
)

REM Fallback to old jar name if it exists
if exist "target\resaver-7.28-SNAPSHOT.jar" (
    java -jar "target\resaver-7.28-SNAPSHOT.jar" %*
    exit /b
)

REM If no JAR found, show error
echo ERROR: ReSaver.jar not found in target directory
echo Please run: mvn package -DskipTests
echo Or contact support for the fixed JAR file
pause
