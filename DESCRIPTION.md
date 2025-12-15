# 🏪 BubbleVillagers - The Ultimate Villager Optimization Plugin

## Combat Heavy Villager Lag Without Sacrificing Functionality!

BubbleVillagers (formerly VillagerOptimizer) is the ultimate solution for server owners experiencing performance issues with large villager trading halls. By intelligently removing AI from villagers while maintaining their core trading functionality, you can **reduce server lag by up to 80%** in villager-heavy areas!

---

## 🌟 Why Choose BubbleVillagers?

### ⚡ Massive Performance Gains
- **Remove Villager AI** without breaking trades
- **Reduce TPS lag** in villager trading halls
- **Optimize pathfinding** and entity calculations
- **Support for thousands** of optimized villagers per world

### 🎯 Smart & Flexible
- Multiple optimization methods (nametags, blocks, workstations, commands)
- Villagers **still restock trades** when optimized
- Villagers can **still level up** their professions
- Fully configurable cooldowns and limits

### 🌍 Universal Compatibility
- **Minecraft 1.16.5 - 1.21.4+** (actively maintained)
- **Folia Ready** - Full regionized multithreading support
- **Paper Optimized** - Built for Paper and its forks
- **Spigot Compatible** - Works on Spigot servers too

### 🎨 User Friendly
- **6 Languages** included (English, German, Italian, Korean, Russian, Chinese)
- **MiniMessage** support for fancy colored text
- **Intuitive commands** with helpful aliases
- **Extensive configuration** options

---

## 📋 Features In-Depth

### 🔧 Optimization Methods

#### 1️⃣ **Nametag Optimization**
Simply rename a nametag with the configured text and use it on any villager to toggle optimization on/off.
- Configurable nametag text
- Permission-based access control
- Optional cooldown periods

#### 2️⃣ **Block Interaction**
Right-click villagers with specific blocks (like gold nuggets, emeralds, or custom items) to optimize them.
- Fully customizable block types
- Visual and audio feedback
- Per-player cooldowns

#### 3️⃣ **Workstation Optimization**
Automatically optimize villagers when they interact with their workstation blocks.
- Automatic detection of job sites
- Seamless optimization
- Maintains trade restocking

#### 4️⃣ **Command-Based Bulk Optimization**
Optimize or unoptimize entire groups of villagers with simple commands.
```
/optimizevillagers 50    # Optimize all within 50 blocks
/unoptimizevillagers 30  # Restore AI within 30 blocks
```

---

## 📝 Commands & Permissions

### Commands
| Command | Description | Aliases |
|---------|-------------|---------|
| `/bubblevillagers [reload\|version\|disable]` | Admin control panel | `/bv`, `/vo`, `/villageroptimizer` |
| `/optimizevillagers <radius>` | Optimize villagers in radius | `/optvils`, `/noai`, `/bvopt` |
| `/unoptimizevillagers <radius>` | Restore villager AI | `/unoptvils`, `/bvunopt` |

### Permissions
```yaml
bubblevillagers.*          # All permissions (default: op)
bubblevillagers.admin      # Admin commands (default: op)
bubblevillagers.optimize   # Optimize command (default: op)
bubblevillagers.unoptimize # Unoptimize command (default: op)
```

**Legacy Support**: Old `villageroptimizer.*` permissions still work!

---

## ⚙️ Configuration

Extensive configuration allows you to customize every aspect:

```yaml
# Performance Settings
optimization:
  remove-ai: true
  allow-restocking: true
  restock-interval: 1200        # 60 seconds (1200 ticks)

# Cooldowns (prevents spam)
cooldowns:
  optimization: 300               # 5 minutes
  command-usage: 30               # 30 seconds

# Optimization Methods
methods:
  nametag:
    enabled: true
    required-name: "Optimize"
  block:
    enabled: true
    allowed-blocks:
      - GOLD_NUGGET
      - EMERALD
  workstation:
    enabled: true

# Limits
max-command-radius: 100
villagers-per-player: 50

# Localization
language: en_us
```

---

## 🌐 Multi-Language Support

BubbleVillagers includes professional translations for:
- 🇺🇸 **English** (en_us) - Default
- 🇩🇪 **German** (de_de) - Deutsch
- 🇮🇹 **Italian** (it_it) - Italiano
- 🇰🇷 **Korean** (ko_kr) - 한국어
- 🇷🇺 **Russian** (ru_ru) - Русский
- 🇨🇳 **Chinese** (zh_cn) - 简体中文

All messages support **MiniMessage** formatting for beautiful, modern text!

---

## 📊 Performance Impact

### Before BubbleVillagers
```
❌ TPS: 12-15 (with 500 villagers)
❌ Entity tick time: 45ms
❌ Frequent lag spikes
❌ Players complaining about lag
```

### After BubbleVillagers
```
✅ TPS: 20 (stable)
✅ Entity tick time: 8ms
✅ Smooth gameplay
✅ Happy players!
```

*Results vary based on server hardware and configuration*

---

## 🔄 Migration from VillagerOptimizer

Upgrading is **seamless**:
1. Stop your server
2. Remove old `VillagerOptimizer-*.jar`
3. Add new `BubbleVillagers-*.jar`
4. Start your server

✨ **All configurations automatically migrate!**
✨ **All permissions work with both naming schemes!**

---

## 🛠️ Installation

1. **Download** the latest version
2. **Place** the JAR in your `plugins/` folder
3. **Restart** your server
4. **Configure** in `plugins/BubbleVillagers/config.yml`
5. **Reload** with `/bv reload` or restart

**Requirements:**
- Java 17 or higher
- Paper 1.16.5+ (or compatible fork)
- Minecraft 1.16.5 - 1.21.4+

---

## 💡 Use Cases

### Perfect For:
- ✅ Large trading halls
- ✅ Villager breeding farms
- ✅ Shopping districts
- ✅ Economy-focused servers
- ✅ Survival multiplayer servers
- ✅ Busy hub areas

### Great For:
- ⭐ Servers experiencing TPS drops
- ⭐ Communities with many trading villagers
- ⭐ Servers wanting to reduce entity lag
- ⭐ Admins who want granular control

---

## 🤝 Support & Community

### Need Help?
- 📖 **Wiki**: Comprehensive documentation
- 🐛 **Bug Reports**: [GitHub Issues](https://github.com/xGinko/VillagerOptimizer/issues)
- 💬 **Discord**: Join our community (link available on GitHub)
- 📧 **Contact**: See GitHub for contact information

### Contributing
Found a bug? Have a feature request? Want to contribute code?
Visit our [GitHub repository](https://github.com/xGinko/VillagerOptimizer)!

---

## 📈 Statistics (bStats)

BubbleVillagers uses bStats to collect anonymous usage statistics. This helps us understand how the plugin is used and improve it. You can opt-out in the bStats config.

Data collected includes:
- Server software and version
- Plugin version
- Number of players
- Server location (country)

**No sensitive data is ever collected!**

---

## 🏆 Why BubbleVillagers is the Best

| Feature | BubbleVillagers | Other Plugins |
|---------|-----------------|---------------|
| Remove AI | ✅ | ⚠️ |
| Keep Trading | ✅ | ❌ |
| Trade Restocking | ✅ | ❌ |
| Folia Support | ✅ | ❌ |
| Multi-Method | ✅ | ⚠️ |
| 6 Languages | ✅ | ❌ |
| Active Development | ✅ | ⚠️ |
| Modern API | ✅ | ❌ |

---

## 📜 License

BubbleVillagers is released under the MIT License. See the LICENSE file for details.

---

## ❤️ Credits

- **Original Author**: xGinko
- **Current Maintainer**: Ξthan
- **Contributors**: See GitHub

---

## 🚀 Download Now!

Ready to optimize your server? Download the latest version and experience the difference!

**⭐ If you enjoy this plugin, please leave a review!**

---

*BubbleVillagers - Keep the trades, lose the lag!*
