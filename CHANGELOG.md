# Changelog

All notable changes to BubbleVillagers will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2024-12-15

### 🎉 Major Release - Rebranded as BubbleVillagers

#### ✨ Added
- Complete rebrand from VillagerOptimizer to BubbleVillagers
- New permission system with `bubblevillagers.*` namespace
- Backward compatibility with legacy `villageroptimizer.*` permissions
- Enhanced command aliases (`/bv`, `/bvopt`, `/bvunopt`)
- Comprehensive README.md with detailed documentation
- Modern Maven project structure
- Updated build configuration for Minecraft 1.21.x
- Java 17 support with modern dependency versions

#### 🔄 Changed
- Updated to Minecraft API version 1.21.4
- Upgraded Paper API to latest stable version
- Updated Caffeine cache library to 3.1.8 (Java 11+ version)
- Updated XSeries to 11.3.0 for better cross-version support
- Updated Adventure libraries to 4.17.0
- Updated bStats to 3.1.0
- Updated morepaperlib to 0.4.4 for improved Folia support
- Improved plugin.yml with better command descriptions
- Enhanced permission hierarchy with wildcard support

#### 🐛 Fixed
- Fixed typo in plugin.yml description ("Villager optimser" → "optimizer")
- Fixed command usage formatting for better clarity
- Improved permission-message readability

#### 📚 Documentation
- Added comprehensive README.md for Spigot and Modrinth
- Added CHANGELOG.md for version tracking
- Added detailed feature list and usage examples
- Added installation and configuration guides
- Added multi-language support documentation

#### 🔧 Technical Improvements
- Modernized Maven POM with latest plugin versions
- Enhanced shade plugin configuration
- Better artifact filtering and relocation
- Improved build reproducibility
- Added proper source and target Java version (17)
- Updated maven-compiler-plugin to 3.13.0
- Updated maven-shade-plugin to 3.6.0

---

## [1.6.2-bubble] - Previous Version

### Legacy VillagerOptimizer Release

#### Features
- Basic villager optimization system
- Nametag-based optimization
- Block interaction optimization
- Workstation optimization
- Command-based radius optimization
- Multi-language support (6 languages)
- Folia compatibility
- Trade restocking system
- Cooldown management

#### Supported Languages
- English (en_us)
- German (de_de)
- Italian (it_it)
- Korean (ko_kr)
- Russian (ru_ru)
- Chinese (zh_cn)

---

## Migration Notes

### From VillagerOptimizer 1.6.2 to BubbleVillagers 2.0.0

**No Configuration Changes Required** - All existing configurations are compatible.

**Permission Updates** (Optional, but recommended):
- Old: `villageroptimizer.admin` → New: `bubblevillagers.admin`
- Old: `villageroptimizer.optimize` → New: `bubblevillagers.optimize`
- Old: `villageroptimizer.unoptimize` → New: `bubblevillagers.unoptimize`

**Legacy permissions are still supported** for backward compatibility.

**Command Aliases**:
New aliases added, old aliases still work:
- `/bv` - Quick access to admin commands
- `/bvopt` - Quick optimization command
- `/bvunopt` - Quick unoptimization command

---

## Upcoming Features

### Planned for 2.1.0
- [ ] Web-based configuration interface
- [ ] Advanced statistics and metrics dashboard
- [ ] Villager group management
- [ ] Custom optimization profiles
- [ ] PlaceholderAPI integration
- [ ] WorldGuard region integration
- [ ] Economy integration for paid optimization

### Under Consideration
- [ ] Automatic optimization based on TPS
- [ ] Villager protection zones
- [ ] Custom optimization sounds and effects
- [ ] MySQL/SQLite data storage
- [ ] Cross-server villager synchronization

---

## Support

For bug reports, feature requests, or questions:
- **GitHub Issues**: [Create an issue](https://github.com/xGinko/VillagerOptimizer/issues)
- **Discord**: Join our community server (if available)
- **Spigot**: Visit our [Spigot page](https://www.spigotmc.org/)
- **Modrinth**: Check our [Modrinth page](https://modrinth.com/)

---

**Legend**:
- ✨ Added - New features
- 🔄 Changed - Changes in existing functionality
- 🐛 Fixed - Bug fixes
- 📚 Documentation - Documentation changes
- 🔧 Technical - Technical/infrastructure changes
- 🎉 Major - Major release milestone
