package com.gmail.pitrex111;

import com.cryptomorin.xseries.XEntityType;
import me.xginko.villageroptimizer.VillagerOptimizer;
import me.xginko.villageroptimizer.events.VillagerOptimizeEvent;
import me.xginko.villageroptimizer.events.VillagerUnoptimizeEvent;
import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;
import me.xginko.villageroptimizer.struct.enums.OptimizationType;
import me.xginko.villageroptimizer.struct.enums.Permissions;
import me.xginko.villageroptimizer.utils.KyoriUtil;
import me.xginko.villageroptimizer.utils.LocationUtil;
import me.xginko.villageroptimizer.utils.Util;
import me.xginko.villageroptimizer.wrapper.WrappedVillager;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class OptimizeByShiftRightClick
		extends VillagerOptimizerModule
		implements Listener {

	private final long cooldown;
	private final boolean notifyPlayer;
	private final boolean logEnabled;

	public OptimizeByShiftRightClick() {
		super(
				"optimization-methods.shift-right-click-optimization"
		);

		this.cooldown = TimeUnit.SECONDS.toMillis(
				Math.max(
						0,
						config.getInt(
								configPath
										+ ".optimize-cooldown-seconds",
								600,
								"Cooldown in seconds until a villager can be optimized again using shift + right click."
						)
				)
		);

		this.notifyPlayer = config.getBoolean(
				configPath + ".notify-player",
				true,
				"Sends players feedback when they optimize or unoptimize a villager."
		);

		this.logEnabled = config.getBoolean(
				configPath + ".log",
				false
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
		return config.getBoolean(
				configPath + ".enable",
				true
		);
	}

	@EventHandler(
			priority = EventPriority.HIGHEST,
			ignoreCancelled = true
	)
	private void onPlayerInteractEntity(
			PlayerInteractEntityEvent event
	) {

		if (event.getRightClicked().getType()
				!= XEntityType.VILLAGER.get()) {
			return;
		}

		Player player = event.getPlayer();

		if (!player.isSneaking()) {
			return;
		}

		if (!player.hasPermission(
				Permissions.Optimize
						.SHIFT_RIGHT_CLICK
						.get()
		)) {
			return;
		}

		/*
		 * Prevent normal villager interaction while using the optimization
		 * gesture.
		 */
		event.setCancelled(true);

		/*
		 * Only one hand may toggle optimization.
		 */
		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}

		Villager villager =
				(Villager) event.getRightClicked();

		WrappedVillager wrapped =
				wrapperCache.get(
						villager,
						WrappedVillager::new
				);

		if (wrapped.isOptimized()) {
			unoptimize(
					event,
					player,
					wrapped
			);
			return;
		}

		optimize(
				event,
				player,
				wrapped
		);
	}

	private void optimize(
			PlayerInteractEntityEvent event,
			Player player,
			WrappedVillager wrapped
	) {

		boolean bypassCooldown =
				player.hasPermission(
						Permissions.Bypass
								.SHIFT_RIGHT_CLICK_COOLDOWN
								.get()
				);

		if (!bypassCooldown
				&& !wrapped.canOptimize(cooldown)) {

			wrapped.sayNo();

			if (notifyPlayer) {

				long remainingMillis =
						wrapped.getOptimizeCooldownMillis(
								cooldown
						);

				TextReplacementConfig timeLeft =
						TextReplacementConfig.builder()
								.matchLiteral("%time%")
								.replacement(
										Util.formatDuration(
												Duration.ofMillis(
														remainingMillis
												)
										)
								)
								.build();

				KyoriUtil.sendActionBar(
						player,
						VillagerOptimizer
								.getLang(player.locale())
								.shift_right_click_on_optimize_cooldown
								.stream()
								.map(line ->
										line.replaceText(
												timeLeft
										)
								)
								.toList()
				);
			}

			return;
		}

		VillagerOptimizeEvent optimizeEvent =
				new VillagerOptimizeEvent(
						wrapped,
						OptimizationType.SHIFT_RIGHT_CLICK,
						player,
						event.isAsynchronous()
				);

		if (!optimizeEvent.callEvent()) {
			return;
		}

		wrapped.setOptimizationType(
				optimizeEvent.getOptimizationType()
		);

		wrapped.saveOptimizeTime();

		if (notifyPlayer) {
			KyoriUtil.sendActionBar(
					player,
					VillagerOptimizer
							.getLang(player.locale())
							.shift_right_click_optimize_success
			);
		}

		if (logEnabled) {
			info(
					player.getName()
							+ " optimized villager using shift + right click at "
							+ LocationUtil.toString(
							wrapped.villager
									.getLocation()
					)
			);
		}
	}

	private void unoptimize(
			PlayerInteractEntityEvent event,
			Player player,
			WrappedVillager wrapped
	) {

		OptimizationType previousType =
				wrapped.getOptimizationType();

		VillagerUnoptimizeEvent unoptimizeEvent =
				new VillagerUnoptimizeEvent(
						wrapped,
						player,
						previousType,
						event.isAsynchronous()
				);

		if (!unoptimizeEvent.callEvent()) {
			return;
		}

		wrapped.setOptimizationType(
				OptimizationType.NONE
		);

		if (notifyPlayer) {
			KyoriUtil.sendActionBar(
					player,
					VillagerOptimizer
							.getLang(player.locale())
							.shift_right_click_unoptimize_success
			);
		}

		if (logEnabled) {
			info(
					player.getName()
							+ " unoptimized villager at "
							+ LocationUtil.toString(
							wrapped.villager
									.getLocation()
					)
			);
		}
	}
}