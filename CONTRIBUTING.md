# Contributing to BubbleVillagers

First off, thank you for considering contributing to BubbleVillagers! It's people like you that make BubbleVillagers such a great tool.

## Code of Conduct

This project and everyone participating in it is governed by respect and professionalism. Please be kind and courteous to other contributors.

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check existing issues as you might find that you don't need to create one. When you are creating a bug report, please include as many details as possible:

* **Use a clear and descriptive title**
* **Describe the exact steps to reproduce the problem**
* **Provide specific examples**
* **Describe the behavior you observed and what you expected**
* **Include server logs if applicable**
* **Specify Minecraft version, server software, and plugin version**

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, please include:

* **A clear and descriptive title**
* **A detailed description of the proposed enhancement**
* **Examples of how it would work**
* **Why this enhancement would be useful**

### Pull Requests

* Fork the repository and create your branch from `main`
* If you've added code that should be tested, add tests
* Ensure your code follows the existing code style
* Write clear, descriptive commit messages
* Update documentation as needed

## Development Setup

### Prerequisites

* Java Development Kit (JDK) 17 or higher
* Apache Maven 3.6+
* Git
* A Minecraft server for testing (Paper 1.21.4 recommended)

### Building from Source

1. **Clone the repository**
   ```bash
   git clone https://github.com/xGinko/VillagerOptimizer.git
   cd VillagerOptimizer
   ```

2. **Build with Maven**
   ```bash
   mvn clean package
   ```

3. **Find the JAR**
   ```
   target/BubbleVillagers-X.X.X.jar
   ```

### Project Structure

```
BubbleVillagers/
├── .github/
│   └── workflows/        # GitHub Actions CI/CD
├── src/
│   ├── main/
│   │   ├── java/         # Source code
│   │   └── resources/    # Plugin resources
│   └── test/             # Unit tests
├── pom.xml               # Maven configuration
├── README.md             # Main documentation
├── CHANGELOG.md          # Version history
└── LICENSE               # MIT License
```

## Code Style Guidelines

### Java Code Style

* **Indentation**: 4 spaces (no tabs)
* **Line Length**: Maximum 120 characters
* **Braces**: Opening brace on same line
* **Naming Conventions**:
  - Classes: `PascalCase`
  - Methods: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Variables: `camelCase`

### Example

```java
public class VillagerOptimizer extends JavaPlugin {
    private static final String PLUGIN_NAME = "BubbleVillagers";
    
    @Override
    public void onEnable() {
        getLogger().info("Enabling " + PLUGIN_NAME);
    }
    
    private void loadConfiguration() {
        // Implementation
    }
}
```

## Testing

* Write unit tests for new features
* Ensure all tests pass before submitting PR
* Test on a live server when possible
* Test with different Minecraft versions if your change affects compatibility

## Documentation

* Update README.md for user-facing changes
* Update CHANGELOG.md following Keep a Changelog format
* Add JavaDoc comments for public APIs
* Include code examples for complex features

## Commit Messages

Use clear and meaningful commit messages:

```
Add feature: bulk villager optimization command

- Implement /optimizevillagers command
- Add radius parameter validation
- Include permission checks
- Update language files with new messages
```

### Commit Message Format

* Use present tense ("Add feature" not "Added feature")
* Use imperative mood ("Move cursor to..." not "Moves cursor to...")
* First line: brief summary (50 chars or less)
* Follow with detailed description if needed
* Reference issues and pull requests when relevant

## Release Process

Releases are managed by project maintainers:

1. Update version in `pom.xml`
2. Update `CHANGELOG.md`
3. Create Git tag: `v2.0.0`
4. Push tag to trigger GitHub Actions
5. GitHub Actions builds and creates release
6. Manually upload to Spigot/Modrinth if needed

## Need Help?

* Check existing issues and pull requests
* Join our Discord community
* Read the documentation
* Ask questions in GitHub Discussions

## Recognition

Contributors will be:
* Listed in the CHANGELOG
* Mentioned in release notes
* Added to the contributors list on GitHub

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

Thank you for contributing to BubbleVillagers! 🎉
