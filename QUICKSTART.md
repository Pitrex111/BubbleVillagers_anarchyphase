# 🚀 Quick Start Guide

## What You Have

A fully prepared BubbleVillagers plugin project with:
- ✅ Modern Maven structure
- ✅ Updated dependencies (Minecraft 1.21.4)
- ✅ Complete documentation
- ✅ Release workflows
- ✅ Configuration files
- ⚠️ **Missing: Source code (.java files)**

## 🔧 Step-by-Step Setup

### Step 1: Get the Source Code

You have the compiled JAR extracted in `VillagerOptimizer-1.6.2/`. You need the Java source files.

#### Option A: If you have original source
```powershell
# Copy source files to:
Copy-Item -Recurse "path\to\original\source\*" "C:\Users\eirvi\Documents\BubbleCraft\Plugins\BubbleVillagers\src\main\java\"
```

#### Option B: Decompile the JAR
1. Download **JD-GUI**: http://java-decompiler.github.io/
2. Open `VillagerOptimizer-1.6.2.jar`
3. File → Save All Sources
4. Extract to `src/main/java/`

#### Option C: Use IntelliJ IDEA
1. Open the project in IntelliJ IDEA
2. Add the old JAR as a library
3. Use "Decompile" feature
4. Copy decompiled sources to `src/main/java/`

### Step 2: Verify Structure

After adding source code, your structure should be:
```
src/main/java/me/xginko/villageroptimizer/
├── VillagerOptimizer.java          (Main plugin class)
├── commands/
│   ├── VillagerOptimizerCommand.java
│   ├── OptimizeVillagersCommand.java
│   └── UnoptimizeVillagersCommand.java
├── config/
│   └── Config.java
├── events/
│   └── VillagerEvents.java
├── modules/
│   └── ...
└── utils/
    └── ...
```

### Step 3: Build

#### Windows:
```cmd
cd C:\Users\eirvi\Documents\BubbleCraft\Plugins\BubbleVillagers
build.bat
```

#### Linux/Mac:
```bash
cd /path/to/BubbleVillagers
chmod +x build.sh
./build.sh
```

#### Or Maven directly:
```bash
mvn clean package
```

### Step 4: Test

```cmd
# Find your built JAR
dir target\BubbleVillagers-*.jar

# Copy to test server
copy target\BubbleVillagers-2.0.0.jar "path\to\testserver\plugins\"

# Start server and test
```

## 🐛 Common Issues

### "Cannot find source files"
- Source code is not in `src/main/java/`
- Follow Step 1 to add source files

### "Package does not exist"
- Missing dependencies
- Run: `mvn clean install`

### "Class file has wrong version"
- Java version mismatch
- Ensure you have Java 17+
- Run: `java -version`

### "Failed to execute goal"
- Maven is not installed
- Install from: https://maven.apache.org/

## 📦 What Happens When You Build

1. Maven compiles your Java source files
2. Processes resources (plugin.yml, config.yml, languages)
3. Packages everything into a JAR
4. Shades (embeds) dependencies
5. Creates final JAR in `target/` folder

## ✅ Verification Checklist

Before releasing, verify:
- [ ] JAR builds without errors
- [ ] Plugin loads on test server
- [ ] Commands work (`/bv`, `/optimizevillagers`)
- [ ] Permissions work
- [ ] Config loads correctly
- [ ] Language files work
- [ ] Optimization methods work
- [ ] No console errors

## 📤 Publishing

Once verified:

### Spigot
1. Go to: https://www.spigotmc.org/resources/
2. Upload `target/BubbleVillagers-2.0.0.jar`
3. Use `DESCRIPTION.md` for description
4. Set versions: 1.16.5 - 1.21.4

### Modrinth  
1. Go to: https://modrinth.com/
2. Upload JAR
3. Use `DESCRIPTION.md` for description
4. Select loaders: Paper, Folia, Spigot, Purpur
5. Set versions: 1.16.5 - 1.21.4

### GitHub
1. Push code: `git push`
2. Create tag: `git tag v2.0.0`
3. Push tag: `git push --tags`
4. GitHub Actions builds automatically

## 🆘 Need Help?

Check these files:
- `SETUP_COMPLETE.md` - Full setup guide
- `CONTRIBUTING.md` - Development guide
- `RELEASE_CHECKLIST.md` - Publishing guide
- `README.md` - User documentation

## 💡 Tips

- Test on multiple Minecraft versions
- Always backup before updating
- Use `/bv reload` to test config changes
- Monitor console for errors
- Check bStats for usage patterns

---

**Ready?** Add your source code and run `build.bat`! 🎉
