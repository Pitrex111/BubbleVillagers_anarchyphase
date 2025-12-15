# ⚠️ JAVA SOURCE CODE NEEDED

This directory needs to contain the Java source files (.java) for the plugin.

## Current Status
- ✅ Directory structure created
- ⚠️ Java source files not yet added

## Where to Get Source Files

You have the compiled plugin in:
```
../../../../../VillagerOptimizer-1.6.2/me/xginko/villageroptimizer/
```

### Option 1: Original Source Code
If you have the original Java source code, copy all .java files here:
```powershell
Copy-Item -Recurse "path\to\original\src\*" "."
```

### Option 2: Decompile the JAR

#### Using JD-GUI (Recommended)
1. Download from: http://java-decompiler.github.io/
2. Open the old JAR file
3. File → Save All Sources
4. Extract the ZIP to this directory

#### Using IntelliJ IDEA
1. Open this project in IntelliJ
2. Navigate to the old JAR in project structure
3. Right-click → "Decompile"
4. Copy decompiled sources here

#### Using Fernflower (Command Line)
```bash
java -jar fernflower.jar VillagerOptimizer-1.6.2.jar output_dir
```

## Expected Structure

After adding source files, this directory should contain:
```
villageroptimizer/
├── VillagerOptimizer.java        # Main plugin class
├── WrapperCache.java
├── commands/
│   ├── VillagerOptimizerCommand.java
│   ├── OptimizeVillagersCommand.java
│   └── UnoptimizeVillagersCommand.java
├── config/
│   └── Config.java
├── enums/
│   └── ...
├── events/
│   └── ...
├── libs/                          # Shaded dependencies
├── modules/
│   └── ...
├── utils/
│   └── ...
└── wrapper/
    └── ...
```

## Next Steps

1. Add Java source files to this directory
2. Return to project root
3. Run: `build.bat` (Windows) or `./build.sh` (Linux/Mac)
4. Find built JAR in: `target/BubbleVillagers-2.0.0.jar`

## Need Help?

See the following guides in the project root:
- QUICKSTART.md - Quick setup guide
- SETUP_COMPLETE.md - Detailed setup instructions
- CONTRIBUTING.md - Development guide

---

**Note**: The .class files in VillagerOptimizer-1.6.2 are compiled bytecode.
You need the original .java source files to build with Maven.
