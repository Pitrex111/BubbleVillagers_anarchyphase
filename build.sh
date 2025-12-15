#!/bin/bash
# BubbleVillagers Build Script for Linux/Mac
# This script builds the plugin using Maven

echo ""
echo "========================================"
echo "  BubbleVillagers Build Script"
echo "========================================"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "[ERROR] Maven is not installed or not in PATH"
    echo "Please install Maven from https://maven.apache.org/"
    exit 1
fi

echo "[INFO] Maven found. Starting build..."
echo ""

# Clean and build
echo "[INFO] Running: mvn clean package"
mvn clean package

if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Build failed! Please check the error messages above."
    exit 1
fi

echo ""
echo "========================================"
echo "  Build Successful!"
echo "========================================"
echo ""
echo "The plugin JAR file is located at:"
echo "  target/BubbleVillagers-*.jar"
echo ""
echo "You can now copy this file to your server's plugins folder."
echo ""
