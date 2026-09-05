package me.xginko.villageroptimizer.config;

import me.xginko.villageroptimizer.VillagerOptimizer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class Config {

    private final @NotNull File configFile;
    private final @NotNull YamlConfiguration config;
    public final @NotNull Locale default_lang;
    public final @NotNull Duration cache_keep_time;
    public final boolean auto_lang, support_other_plugins;

    public Config() throws Exception {
        // Load config.yml with Bukkit YamlConfiguration
        VillagerOptimizer plugin = VillagerOptimizer.getInstance();
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        
        // Create plugin folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        // Save default config if it doesn't exist
        if (!this.configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        
        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        structureConfig();

        this.default_lang = Locale.forLanguageTag(
                getString("general.default-language", "en_us",
                        "The default language that will be used if auto-language is false\n" +
                                "or no matching language file was found.")
                        .replace("_", "-"));
        this.auto_lang = getBoolean("general.auto-language", true,
                "If set to true, will display messages based on client language");
        this.cache_keep_time = Duration.ofSeconds(Math.max(1, getInt("general.cache-keep-time-seconds", 30,
                "The amount of time in seconds a villager will be kept in the plugin's cache.")));
        this.support_other_plugins = getBoolean("general.support-avl-villagers", false,
                "Enable if you have previously used AntiVillagerLag\n" +
                        "(https://www.spigotmc.org/resources/antivillagerlag.102949/).\n" +
                        "Tries to read pre-existing info like optimization state so players\n" +
                        "don't need to reoptimize their villagers.");
    }

    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            VillagerOptimizer.logger().error("Failed to save config file!", e);
        }
    }

    private void structureConfig() {
        // Set defaults for all config values
        config.addDefault("config-version", 1.00);
        config.addDefault("general.default-language", "en_us");
        config.addDefault("general.auto-language", true);
        config.addDefault("general.cache-keep-time-seconds", 30);
        config.addDefault("general.support-avl-villagers", false);
        
        config.addDefault("optimization-methods.nametag-optimization.enable", true);
        config.addDefault("optimization-methods.commands.unoptimizevillagers", true);

        config.addDefault("optimization-methods.auto-optimize-chunk-population.enable", false);
        config.addDefault("optimization-methods.auto-optimize-chunk-population.threshold", 20);
        config.addDefault("optimization-methods.auto-optimize-chunk-population.check-period-ticks", 200);

        config.addDefault("optimization-methods.auto-optimize-trade-hall-population.enable", false);
        config.addDefault("optimization-methods.auto-optimize-trade-hall-population.population", 20);
        config.addDefault("optimization-methods.auto-optimize-trade-hall-population.radius-blocks", 16.0);
        config.addDefault("optimization-methods.auto-optimize-trade-hall-population.check-period-ticks", 200);
        
        config.addDefault("gameplay.prevent-trading-with-unoptimized.enable", false);
        config.addDefault("gameplay.unoptimize-on-job-loose.enable", true);
        config.addDefault("gameplay.villagers-can-be-leashed.enable", true);
        config.addDefault("gameplay.villagers-spawn-as-adults.enable", false);
        config.addDefault("gameplay.rename-optimized-villagers.enable", false);
        config.addDefault("gameplay.outline-optimized-villagers.enable", false);
        config.addDefault("gameplay.prevent-entities-from-targeting-optimized.enable", true);
        config.addDefault("gameplay.prevent-damage-to-optimized.enable", true);
		config.addDefault(
				"optimization-methods.auto-optimize-on-adult.enable",
				true
		);

		config.addDefault(
				"optimization-methods.auto-optimize-on-adult.check-period-ticks",
				200
		);
        
        config.options().copyDefaults(true);
        saveConfig();
    }

    public @NotNull YamlConfiguration master() {
        return this.config;
    }

    public boolean getBoolean(@NotNull String path, boolean def, @NotNull String comment) {
        // Comments are not supported in YamlConfiguration, but we can log them if needed
        config.addDefault(path, def);
        return config.getBoolean(path, def);
    }

    public boolean getBoolean(@NotNull String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, def);
    }

    public @NotNull String getString(@NotNull String path, @NotNull String def, @NotNull String comment) {
        config.addDefault(path, def);
        return config.getString(path, def);
    }

    public @NotNull String getString(@NotNull String path, @NotNull String def) {
        config.addDefault(path, def);
        return config.getString(path, def);
    }

    public double getDouble(@NotNull String path, @NotNull Double def, @NotNull String comment) {
        config.addDefault(path, def);
        return config.getDouble(path, def);
    }

    public double getDouble(@NotNull String path, @NotNull Double def) {
        config.addDefault(path, def);
        return config.getDouble(path, def);
    }

    public int getInt(@NotNull String path, int def, @NotNull String comment) {
        config.addDefault(path, def);
        return config.getInt(path, def);
    }

    public int getInt(@NotNull String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, def);
    }

    @SuppressWarnings("unchecked")
    public @NotNull <T> List<T> getList(@NotNull String path, @NotNull List<T> def, @NotNull String comment) {
        config.addDefault(path, def);
        List<?> list = config.getList(path, def);
        return (List<T>) list;
    }

    @SuppressWarnings("unchecked")
    public @NotNull <T> List<T> getList(@NotNull String path, @NotNull List<T> def) {
        config.addDefault(path, def);
        List<?> list = config.getList(path, def);
        return (List<T>) list;
    }
}
