# 🔧 Build Requirements Not Met

## Current Status
❌ Maven not installed
❌ Java 8 detected (need Java 17+)
❌ Java source code not added yet

## Quick Solutions

### Option 1: Install Build Tools & Build from Source

#### Step 1: Install Java 17
```powershell
# Download and install from:
https://adoptium.net/temurin/releases/?version=17

# Or use winget (Windows Package Manager):
winget install EclipseAdoptium.Temurin.17.JDK
```

#### Step 2: Install Maven
```powershell
# Download from:
https://maven.apache.org/download.cgi

# Extract to: C:\Program Files\Maven

# Add to PATH:
# Control Panel → System → Advanced → Environment Variables
# Add: C:\Program Files\Maven\bin
```

#### Step 3: Add Java Source Code
See: src/main/java/me/xginko/villageroptimizer/README.txt

Options:
- Copy original source code if available
- Decompile using JD-GUI: http://java-decompiler.github.io/

#### Step 4: Build
```powershell
mvn clean package
```

JAR will be in: `target/BubbleVillagers-2.0.0.jar`

---

### Option 2: Use Existing JAR (Quick)

You have the compiled plugin already extracted. To use it as-is:

#### Find the Original JAR
Look for the original `VillagerOptimizer-*.jar` file that was extracted to create the `VillagerOptimizer-1.6.2/` folder.

Common locations:
- Downloads folder
- Server plugins folder
- Backup folders

#### Rename and Use
Once found:
```powershell
# Copy to target directory
New-Item -ItemType Directory -Path "target" -Force
Copy-Item "path\to\original.jar" "target\BubbleVillagers-2.0.0.jar"
```

**Note**: This won't include the updated configurations and improvements from the new project structure.

---

### Option 3: Use Pre-compiled Binary (If Available)

If the original VillagerOptimizer has releases on GitHub:
1. Visit: https://github.com/xGinko/VillagerOptimizer/releases
2. Download latest release JAR
3. Copy to your project

---

## Recommended Approach

**For Development**: Install Java 17 + Maven (Option 1)
- Allows you to make code changes
- Build with latest dependencies
- Include new configurations

**For Quick Testing**: Use existing JAR (Option 2)
- No setup required
- Can test immediately
- Won't have new features

---

## Installation Scripts

### Install Java 17 (Windows)
```powershell
# Using Chocolatey
choco install temurin17

# Or winget
winget install EclipseAdoptium.Temurin.17.JDK

# Or Scoop
scoop install java/temurin17-jdk
```

### Install Maven (Windows)
```powershell
# Using Chocolatey
choco install maven

# Or Scoop
scoop install maven
```

After installation, restart your terminal and verify:
```powershell
java -version    # Should show Java 17+
mvn -version     # Should show Maven version
```

---

## Need Help?

- Check PROJECT_SUMMARY.txt for complete setup
- See QUICKSTART.md for step-by-step guide
- Read SETUP_COMPLETE.md for detailed instructions

---

**Status**: Waiting for build tools installation
**Next Step**: Install Java 17 and Maven, then run: `mvn clean package`
