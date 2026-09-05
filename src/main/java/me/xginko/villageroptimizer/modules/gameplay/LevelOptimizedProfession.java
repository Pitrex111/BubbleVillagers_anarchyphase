package me.xginko.villageroptimizer.modules.gameplay;

import me.xginko.villageroptimizer.VillagerOptimizer;
import me.xginko.villageroptimizer.config.Config;
import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;
import me.xginko.villageroptimizer.utils.KyoriUtil;
import me.xginko.villageroptimizer.utils.Util;
import me.xginko.villageroptimizer.wrapper.WrappedVillager;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class LevelOptimizedProfession
		extends VillagerOptimizerModule
		implements Listener {

	private final boolean notifyPlayer;
	private final long cooldownMillis;

	public LevelOptimizedProfession() {
		super("gameplay.level-optimized-profession");

		Config config =
				VillagerOptimizer.config();

		this.cooldownMillis =
				TimeUnit.SECONDS.toMillis(
						Math.max(
								0,
								config.getInt(
										configPath
												+ ".level-check-cooldown-seconds",
										5,
										"Cooldown in seconds until the level of an optimized villager is checked again."
								)
						)
				);

		this.notifyPlayer =
				config.getBoolean(
						configPath
								+ ".notify-player",
						true,
						"Show action-bar feedback when the villager level update is on cooldown."
				);
	}

	@Override
	public void enable() {
		plugin.getServer()
				.getPluginManager()
				.registerEvents(this, plugin);
	}

	@Override
	public void disable() {
		HandlerList.unregisterAll(this);
	}

	@Override
	public boolean shouldEnable() {
		return true;
	}

	@EventHandler(
			priority = EventPriority.MONITOR,
			ignoreCancelled = true
	)
	private void onTradeScreenClose(
			InventoryCloseEvent event
	) {

		if (event.getInventory().getType()
				!= InventoryType.MERCHANT) {
			return;
		}

		if (!(event.getInventory()
				.getHolder()
				instanceof Villager villager)) {
			return;
		}

		Player player =
				event.getPlayer()
						instanceof Player
						? (Player) event.getPlayer()
						: null;

		scheduling.entitySpecificScheduler(
				villager
		).run(() -> {

			WrappedVillager wrapped =
					wrapperCache.get(
							villager,
							WrappedVillager::new
					);

			if (!wrapped.isOptimized()) {
				return;
			}

			int currentLevel =
					villager.getVillagerLevel();

			int targetLevel =
					wrapped.calculateLevel();

			if (targetLevel <= currentLevel) {
				return;
			}

			if (!wrapped.canLevelUp(
					cooldownMillis
			)) {

				if (notifyPlayer
						&& player != null) {

					long remainingMillis =
							wrapped.getLevelCooldownMillis(
									cooldownMillis
							);

					notifyCooldown(
							player,
							remainingMillis
					);
				}

				return;
			}

			int levelsToIncrease =
					targetLevel - currentLevel;

			try {

				villager.increaseLevel(
						levelsToIncrease
				);

				wrapped.saveLastLevelUp();

			} catch (
					IllegalArgumentException exception
			) {

				error(
						"Failed to increase optimized villager from level "
								+ currentLevel
								+ " to "
								+ targetLevel,
						exception
				);

				return;
			}

			/*
			 * Never allow the leveling process to wake an optimized
			 * BubbleVillagers villager.
			 */
			if (wrapped.isOptimized()
					&& villager.isAware()) {
				villager.setAware(false);
			}

		}, null);
	}

	private void notifyCooldown(
			Player player,
			long remainingMillis
	) {

		TextReplacementConfig timeLeft =
				TextReplacementConfig.builder()
						.matchLiteral("%time%")
						.replacement(
								Util.formatDuration(
										Duration.ofMillis(
												Math.max(
														0L,
														remainingMillis
												)
										)
								)
						)
						.build();

		scheduling.entitySpecificScheduler(
				player
		).run(() ->

						KyoriUtil.sendActionBar(
								player,
								VillagerOptimizer
										.getLang(
												player.locale()
										)
										.villager_leveling_up
										.stream()
										.map(line ->
												line.replaceText(
														timeLeft
												)
										)
										.toList()
						),

				null
		);
	}
}
