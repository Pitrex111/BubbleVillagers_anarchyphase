@echo off
REM BubbleVillagers Build Script for Windows
REM This script builds the plugin using Maven

echo.
echo ========================================
echo   BubbleVillagers Build Script
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven is not installed or not in PATH
    echo Please install Maven from https://maven.apache.org/
    pause
    exit /b 1
)

echo [INFO] Maven found. Starting build...
echo.

REM Clean and build
echo [INFO] Running: mvn clean package
mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Build failed! Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Build Successful!
echo ========================================
echo.
echo The plugin JAR file is located at:
echo   target\BubbleVillagers-*.jar
echo.
echo You can now copy this file to your server's plugins folder.
echo.
pause
