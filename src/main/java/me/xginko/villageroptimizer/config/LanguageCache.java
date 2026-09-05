package me.xginko.villageroptimizer.config;

import me.xginko.villageroptimizer.VillagerOptimizer;
import me.xginko.villageroptimizer.utils.KyoriUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LanguageCache {

	private final @NotNull File langFile;
	private final @NotNull YamlConfiguration lang;

	public final @NotNull Component no_permission;

	public final @NotNull List<Component>
			nametag_optimize_success,
			nametag_on_optimize_cooldown,
			nametag_unoptimize_success,

	shift_right_click_optimize_success,
			shift_right_click_on_optimize_cooldown,
			shift_right_click_unoptimize_success,

	block_optimize_success,
			block_on_optimize_cooldown,
			block_unoptimize_success,

	workstation_optimize_success,
			workstation_on_optimize_cooldown,
			workstation_unoptimize_success,

	activity_optimize_success,

	command_optimize_success,
			command_radius_limit_exceed,
			command_optimize_fail,
			command_unoptimize_success,
			command_specify_radius,
			command_radius_invalid,
			command_no_villagers_nearby,

	trades_restocked,
			optimize_for_trading,
			villager_leveling_up,

	villager_status_optimized,
			villager_status_unoptimized;

	public LanguageCache(String locale) throws Exception {

		VillagerOptimizer plugin =
				VillagerOptimizer.getInstance();

		this.langFile = new File(
				plugin.getDataFolder()
						+ File.separator
						+ "lang",
				locale + ".yml"
		);

		File parent = this.langFile.getParentFile();

		if (!parent.exists() && !parent.mkdirs()) {
			VillagerOptimizer.logger()
					.error("Failed to create lang directory.");
		}

		if (!this.langFile.exists()) {
			plugin.saveResource(
					"lang/" + locale + ".yml",
					false
			);
		}

		this.lang =
				YamlConfiguration.loadConfiguration(
						this.langFile
				);

		// General / persistent chat messages

		this.no_permission = getTranslation(
				"messages.no-permission",
				"<red>You don't have permission to use this command."
		);

		/*
		 * This deliberately stays in CHAT because it explains why normal
		 * trading is blocked and may need to be reread.
		 */
		this.optimize_for_trading = getListTranslation(
				"messages.optimize-to-trade",
				"<red>You need to optimize this villager before you can trade with it."
		);

		// Action-bar gameplay feedback

		this.trades_restocked = getListTranslation(
				"messages.trades-restocked",
				"<green>Trades restocked! <gray>Next restock in %time%."
		);

		this.villager_leveling_up = getListTranslation(
				"messages.villager-leveling-up",
				"<yellow>Villager level update available again in %time%."
		);

		this.villager_status_optimized = getListTranslation(
				"messages.villager-status.optimized",
				"<green>This villager is optimized."
		);

		this.villager_status_unoptimized = getListTranslation(
				"messages.villager-status.unoptimized",
				"<yellow>This villager is unoptimized."
		);

		// Nametag

		this.nametag_optimize_success = getListTranslation(
				"messages.nametag.optimize-success",
				"<green>Successfully optimized villager by using a nametag."
		);

		this.nametag_on_optimize_cooldown = getListTranslation(
				"messages.nametag.optimize-on-cooldown",
				"<gray>You need to wait %time% until you can optimize this villager again."
		);

		this.nametag_unoptimize_success = getListTranslation(
				"messages.nametag.unoptimize-success",
				"<green>Successfully unoptimized villager by using a nametag."
		);

		// Shift + Right Click

		this.shift_right_click_optimize_success = getListTranslation(
				"messages.shift-right-click.optimize-success",
				"<green>Successfully optimized villager."
		);

		this.shift_right_click_on_optimize_cooldown = getListTranslation(
				"messages.shift-right-click.optimize-on-cooldown",
				"<gray>You need to wait %time% until you can optimize this villager again."
		);

		this.shift_right_click_unoptimize_success = getListTranslation(
				"messages.shift-right-click.unoptimize-success",
				"<green>Successfully unoptimized villager."
		);

		// Block

		this.block_optimize_success = getListTranslation(
				"messages.block.optimize-success",
				"<green>%villagertype% villager successfully optimized using block %blocktype%."
		);

		this.block_on_optimize_cooldown = getListTranslation(
				"messages.block.optimize-on-cooldown",
				"<gray>You need to wait %time% until you can optimize this villager again."
		);

		this.block_unoptimize_success = getListTranslation(
				"messages.block.unoptimize-success",
				"<green>Successfully unoptimized %villagertype% villager by removing %blocktype%."
		);

		// Workstation

		this.workstation_optimize_success = getListTranslation(
				"messages.workstation.optimize-success",
				"<green>%villagertype% villager successfully optimized using workstation %blocktype%."
		);

		this.workstation_on_optimize_cooldown = getListTranslation(
				"messages.workstation.optimize-on-cooldown",
				"<gray>You need to wait %time% until you can optimize this villager again."
		);

		this.workstation_unoptimize_success = getListTranslation(
				"messages.workstation.unoptimize-success",
				"<green>Successfully unoptimized %villagertype% villager by removing workstation block %blocktype%."
		);

		// Activity

		this.activity_optimize_success = getListTranslation(
				"messages.activity.optimized-near-you",
				"<gray>%amount% villagers close to you were automatically optimized due to high activity."
		);

		// Commands - intentionally stay suitable for chat

		this.command_optimize_success = getListTranslation(
				"messages.command.optimize-success",
				"<green>Successfully optimized %amount% villager(s) in a radius of %radius% blocks."
		);

		this.command_radius_limit_exceed = getListTranslation(
				"messages.command.radius-limit-exceed",
				"<red>The radius you entered exceeds the limit of %distance% blocks."
		);

		this.command_optimize_fail = getListTranslation(
				"messages.command.optimize-fail",
				"<gray>%amount% villagers couldn't be optimized because they have recently been optimized."
		);

		this.command_unoptimize_success = getListTranslation(
				"messages.command.unoptimize-success",
				"<green>Successfully unoptimized %amount% villager(s) in a radius of %radius% blocks."
		);

		this.command_specify_radius = getListTranslation(
				"messages.command.specify-radius",
				"<red>Please specify a radius."
		);

		this.command_radius_invalid = getListTranslation(
				"messages.command.radius-invalid",
				"<red>The radius you entered is not a valid number. Try again."
		);

		this.command_no_villagers_nearby = getListTranslation(
				"messages.command.no-villagers-nearby",
				"<gray>Couldn't find any employed villagers within a radius of %radius%."
		);

		try {
			this.lang.options().copyDefaults(true);
			this.lang.save(this.langFile);
		} catch (IOException e) {
			VillagerOptimizer.logger().error(
					"Failed to save language file: "
							+ this.langFile.getName(),
					e
			);
		}
	}

	public @NotNull Component getTranslation(
			@NotNull String path,
			@NotNull String defaultTranslation
	) {
		this.lang.addDefault(path, defaultTranslation);

		String translation =
				this.lang.getString(
						path,
						defaultTranslation
				);

		return MiniMessage.miniMessage().deserialize(
				KyoriUtil.translateChatColor(translation)
		);
	}

	public @NotNull List<Component> getListTranslation(
			@NotNull String path,
			@NotNull String... defaultTranslation
	) {
		this.lang.addDefault(
				path,
				Arrays.asList(defaultTranslation)
		);

		List<String> translations =
				this.lang.getStringList(path);

		if (translations.isEmpty()) {
			translations =
					Arrays.asList(defaultTranslation);
		}

		return translations.stream()
				.map(KyoriUtil::translateChatColor)
				.map(MiniMessage.miniMessage()::deserialize)
				.collect(Collectors.toList());
	}
}