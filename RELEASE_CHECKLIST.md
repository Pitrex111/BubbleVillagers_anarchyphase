# Release Checklist for BubbleVillagers

Use this checklist when preparing a new release for Spigot and Modrinth.

## Pre-Release Tasks

### Version Management
- [ ] Update version number in `pom.xml`
- [ ] Update version in `CHANGELOG.md`
- [ ] Create new version section in `CHANGELOG.md`
- [ ] Document all changes since last release
- [ ] Update `README.md` if features changed
- [ ] Review and update `DESCRIPTION.md` for marketplace

### Code Quality
- [ ] Run full Maven build: `mvn clean package`
- [ ] Verify JAR builds without errors
- [ ] Check for any deprecation warnings
- [ ] Review code for any TODOs or FIXMEs
- [ ] Ensure all dependencies are up to date

### Testing
- [ ] Test on Paper 1.21.4 (latest)
- [ ] Test on Paper 1.20.4 (LTS)
- [ ] Test on Paper 1.16.5 (minimum version)
- [ ] Test basic optimization commands
- [ ] Test nametag optimization
- [ ] Test block optimization
- [ ] Test workstation optimization
- [ ] Test permission system
- [ ] Test configuration reload
- [ ] Test with Folia (if available)
- [ ] Verify language files load correctly
- [ ] Test all 6 language translations
- [ ] Check for console errors/warnings

### Documentation
- [ ] Update README.md with new features
- [ ] Update command documentation
- [ ] Update configuration examples
- [ ] Verify all links work (GitHub, Discord, etc.)
- [ ] Check markdown formatting
- [ ] Update screenshots if UI changed

## Build & Package

### Maven Build
- [ ] Clean workspace: `mvn clean`
- [ ] Build plugin: `mvn package`
- [ ] Verify JAR size is reasonable
- [ ] Check that dependencies are shaded correctly
- [ ] Test JAR on local server

### File Preparation
- [ ] Rename JAR to: `BubbleVillagers-X.X.X.jar`
- [ ] Create SHA-256 checksum
- [ ] Prepare changelog for release notes
- [ ] Gather screenshots for marketplace
- [ ] Prepare promotional graphics (if needed)

## GitHub Release

### Git Tasks
- [ ] Commit all changes
- [ ] Push to GitHub
- [ ] Create version tag: `git tag v2.0.0`
- [ ] Push tag: `git push origin v2.0.0`

### GitHub Release Page
- [ ] Create new release on GitHub
- [ ] Set correct tag (v2.0.0)
- [ ] Use version number as title
- [ ] Copy changelog to release notes
- [ ] Upload JAR file
- [ ] Mark as latest release
- [ ] Publish release

## Spigot Release

### SpigotMC.org Upload
- [ ] Log into SpigotMC account
- [ ] Navigate to resource page
- [ ] Click "Update Resource"
- [ ] Upload new JAR file
- [ ] Set version number
- [ ] Update description from DESCRIPTION.md
- [ ] Add changelog entry
- [ ] Update screenshots (if changed)
- [ ] Verify supported Minecraft versions
- [ ] Check pricing/license settings
- [ ] Save and publish

### Spigot Post-Release
- [ ] Respond to any pending questions
- [ ] Check for any immediate bug reports
- [ ] Update discussion threads if needed

## Modrinth Release

### Modrinth Upload
- [ ] Log into Modrinth account
- [ ] Navigate to project page
- [ ] Click "Create a version"
- [ ] Set version number and title
- [ ] Select version type (release/beta/alpha)
- [ ] Upload JAR file
- [ ] Copy changelog from CHANGELOG.md
- [ ] Select supported game versions:
  - [ ] 1.21.4
  - [ ] 1.21.3
  - [ ] 1.21.2
  - [ ] 1.21.1
  - [ ] 1.21
  - [ ] 1.20.6
  - [ ] 1.20.5
  - [ ] 1.20.4
  - [ ] 1.20.3
  - [ ] 1.20.2
  - [ ] 1.20.1
  - [ ] 1.20
  - [ ] 1.19.x (if still supported)
  - [ ] 1.18.x (if still supported)
  - [ ] 1.17.x (if still supported)
  - [ ] 1.16.5
- [ ] Select mod loaders:
  - [ ] Paper
  - [ ] Folia
  - [ ] Spigot
  - [ ] Purpur
- [ ] Add dependencies (Paper: required)
- [ ] Verify project description is current
- [ ] Update screenshots (if changed)
- [ ] Check categories and tags
- [ ] Publish version

### Modrinth Post-Release
- [ ] Verify download works
- [ ] Check version appears correctly
- [ ] Respond to comments

## Post-Release Tasks

### Communication
- [ ] Announce release on Discord (if applicable)
- [ ] Update any external wikis
- [ ] Notify server owners who requested features
- [ ] Update any partnership servers

### Monitoring
- [ ] Monitor GitHub issues for new bugs
- [ ] Check Spigot reviews and discussions
- [ ] Check Modrinth comments
- [ ] Monitor bStats for usage patterns
- [ ] Watch for error reports

### Documentation
- [ ] Update wiki with new features (if applicable)
- [ ] Update any video tutorials (if needed)
- [ ] Update API documentation (if changed)

## Version-Specific Notes

### Major Versions (X.0.0)
- [ ] Write comprehensive blog post
- [ ] Create promotional video (optional)
- [ ] Announce on social media
- [ ] Update all documentation
- [ ] Consider migration guide

### Minor Versions (1.X.0)
- [ ] Update changelog prominently
- [ ] Highlight new features
- [ ] Update documentation for new features

### Patch Versions (1.1.X)
- [ ] Focus on bug fixes in changelog
- [ ] Quick testing cycle
- [ ] Fast-track release if critical

## Emergency Hotfix Checklist

If releasing an emergency hotfix:
- [ ] Identify critical bug
- [ ] Create hotfix branch
- [ ] Fix bug quickly
- [ ] Test fix thoroughly
- [ ] Skip some non-essential tasks
- [ ] Release as patch version
- [ ] Mark as "Hotfix" in release notes
- [ ] Notify users of critical nature

## Rollback Plan

If a release has critical issues:
- [ ] Mark release as "yanked" on Modrinth
- [ ] Add warning to Spigot resource
- [ ] Create issue on GitHub
- [ ] Communicate issue to users
- [ ] Prepare hotfix ASAP
- [ ] Document what went wrong

---

## Notes

- Always test before releasing
- Keep changelog up to date
- Respond to user feedback quickly
- Monitor error reports closely
- Maintain professional communication

**Last Updated**: December 15, 2024
**Template Version**: 2.0.0
