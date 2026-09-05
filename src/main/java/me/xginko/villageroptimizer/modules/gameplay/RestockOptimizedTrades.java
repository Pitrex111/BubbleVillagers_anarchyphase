package me.xginko.villageroptimizer.modules.gameplay;

import com.cryptomorin.xseries.XEntityType;
import me.xginko.villageroptimizer.VillagerOptimizer;
import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;
import me.xginko.villageroptimizer.struct.enums.Permissions;
import me.xginko.villageroptimizer.utils.KyoriUtil;
import me.xginko.villageroptimizer.utils.LocationUtil;
import me.xginko.villageroptimizer.utils.Util;
import me.xginko.villageroptimizer.wrapper.WrappedVillager;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public class RestockOptimizedTrades
		extends VillagerOptimizerModule
		implements Listener {

	private final NavigableSet<Long> restockDayTimes =
			new TreeSet<>();

	private final boolean logEnabled;
	private final boolean notifyPlayer;

	public RestockOptimizedTrades() {
		super("gameplay.restock-optimized-trades");

		List<Long> defaults =
				List.of(
						1000L,
						13000L
				);

		List<?> rawRestockTimes =
				config.master().getList(
						configPath + ".restock-times",
						new ArrayList<>(defaults)
				);

		for (Object raw : rawRestockTimes) {

			Long parsed =
					parseRestockTime(raw);

			if (parsed == null) {
				continue;
			}

			if (parsed < 0L || parsed >= 24000L) {
				warn(
						"Ignoring invalid restock time "
								+ parsed
								+ ". Valid values are 0-23999."
				);
				continue;
			}

			restockDayTimes.add(parsed);
		}

		if (restockDayTimes.isEmpty()) {
			restockDayTimes.addAll(defaults);
		}

		config.master().addDefault(
				configPath + ".restock-times",
				new ArrayList<>(defaults)
		);

		this.notifyPlayer = config.getBoolean(
				configPath + ".notify-player",
				true,
				"Sends action-bar feedback when trades are restocked."
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
		return true;
	}

	@EventHandler(
			priority = EventPriority.HIGHEST,
			ignoreCancelled = true
	)
	private void onPlayerInteractEntity(
			PlayerInteractEntityEvent event
	) {

		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}

		if (event.getRightClicked().getType()
				!= XEntityType.VILLAGER.get()) {
			return;
		}

		Villager villager =
				(Villager) event.getRightClicked();

		WrappedVillager wrapped =
				wrapperCache.get(
						villager,
						WrappedVillager::new
				);

		if (!wrapped.isOptimized()) {
			return;
		}

		if (event.getPlayer().hasPermission(
				Permissions.Bypass
						.RESTOCK_COOLDOWN
						.get()
		)) {

			villager.restock();
			wrapped.saveRestockTime();

			if (logEnabled) {
				info(
						"Force-restocked optimized villager at "
								+ LocationUtil.toString(
								villager.getLocation()
						)
				);
			}

			return;
		}

		long currentFullTime =
				wrapped.currentFullTimeTicks();

		long currentDayTime =
				wrapped.currentDayTimeTicks();

		long lastRestockFullTime =
				wrapped.getLastRestockFullTime();

		long currentDayStart =
				currentFullTime - currentDayTime;

		if (!hasUnconsumedRestockWindow(
				currentDayStart,
				currentFullTime,
				lastRestockFullTime
		)) {
			return;
		}

		villager.restock();
		wrapped.saveRestockTime();

		long ticksUntilNext =
				ticksUntilNextRestock(
						currentDayTime
				);

		if (notifyPlayer) {

			TextReplacementConfig timeLeft =
					TextReplacementConfig.builder()
							.matchLiteral("%time%")
							.replacement(
									Util.formatDuration(
											Duration.ofMillis(
													ticksUntilNext
															* 50L
											)
									)
							)
							.build();

			KyoriUtil.sendActionBar(
					event.getPlayer(),
					VillagerOptimizer
							.getLang(
									event.getPlayer()
											.locale()
							)
							.trades_restocked
							.stream()
							.map(line ->
									line.replaceText(
											timeLeft
									)
							)
							.toList()
			);
		}

		if (logEnabled) {
			info(
					"Restocked optimized villager at "
							+ LocationUtil.toString(
							villager.getLocation()
					)
			);
		}
	}

	private boolean hasUnconsumedRestockWindow(
			long currentDayStart,
			long currentFullTime,
			long lastRestockFullTime
	) {

		for (long restockDayTime
				: restockDayTimes) {

			long absoluteRestockTime =
					currentDayStart
							+ restockDayTime;

			if (absoluteRestockTime
					> currentFullTime) {
				break;
			}

			if (lastRestockFullTime
					< absoluteRestockTime) {
				return true;
			}
		}

		return false;
	}

	private long ticksUntilNextRestock(
			long currentDayTime
	) {

		Long nextToday =
				restockDayTimes.higher(
						currentDayTime
				);

		if (nextToday != null) {
			return Math.max(
					0L,
					nextToday - currentDayTime
			);
		}

		return Math.max(
				0L,
				(24000L - currentDayTime)
						+ restockDayTimes.first()
		);
	}

	private Long parseRestockTime(
			Object raw
	) {

		if (raw instanceof Number number) {
			return number.longValue();
		}

		if (raw instanceof String string) {
			try {
				return Long.parseLong(string);
			} catch (NumberFormatException ignored) {
				return null;
			}
		}

		return null;
	}
}