package me.xginko.villageroptimizer.modules.optimization;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import me.xginko.villageroptimizer.events.VillagerOptimizeEvent;
import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;
import me.xginko.villageroptimizer.struct.enums.OptimizationType;
import me.xginko.villageroptimizer.wrapper.WrappedVillager;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class AutoOptimizeOnAdult
		extends VillagerOptimizerModule
		implements Listener {

	private final long checkPeriodTicks;

	public AutoOptimizeOnAdult() {
		super("optimization-methods.auto-optimize-on-adult");

		/*
		 * We deliberately do NOT check every tick.
		 *
		 * Once every 10 seconds is more than enough for this use case.
		 *
		 * Even with 100 baby villagers this means roughly 10 lightweight
		 * checks per second spread across their respective Folia regions.
		 */
		this.checkPeriodTicks = Math.max(
				20L,
				config.getInt(
						configPath + ".check-period-ticks",
						200,
						"How often baby villagers are checked to see whether they became adults."
				)
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

		/*
		 * Entity scheduler tasks belonging to this plugin will be retired
		 * normally when appropriate.
		 *
		 * Most importantly, disabling this module NEVER unoptimizes
		 * anything.
		 */
		HandlerList.unregisterAll(this);
	}

	@Override
	public boolean shouldEnable() {
		return config.getBoolean(
				configPath + ".enable",
				true
		);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onEntityAddedToWorld(
			EntityAddToWorldEvent event
	) {

		if (!(event.getEntity() instanceof Villager villager)) {
			return;
		}

		/*
		 * CRITICAL SAFETY RULE:
		 *
		 * Existing adult villagers are completely ignored.
		 *
		 * We do NOT:
		 * - inspect their profession
		 * - optimize them
		 * - add PDC
		 * - change awareness
		 *
		 * Therefore installing this plugin cannot cause an existing adult
		 * villager population to suddenly become optimized.
		 */
		if (villager.isAdult()) {
			return;
		}

		/*
		 * A baby somehow already carrying Bubble's PDC marker is already
		 * optimized. Don't start another lifecycle for it.
		 */
		WrappedVillager wrapped =
				wrapperCache.get(
						villager,
						WrappedVillager::new
				);

		if (wrapped.isOptimized()) {
			return;
		}

		watchUntilAdult(villager);
	}

	private void watchUntilAdult(
			Villager villager
	) {

		scheduling.entitySpecificScheduler(villager)
				.runAtFixedRate(task -> {

					/*
					 * Entity scheduler execution guarantees we are operating
					 * in this villager's Folia ownership context.
					 */

					WrappedVillager wrapped =
							wrapperCache.get(
									villager,
									WrappedVillager::new
							);

					/*
					 * Something else may have optimized the baby while we
					 * were waiting.
					 *
					 * In that case our job is finished.
					 */
					if (wrapped.isOptimized()) {
						task.cancel();
						return;
					}

					/*
					 * Still a baby.
					 *
					 * Do absolutely nothing.
					 */
					if (!villager.isAdult()) {
						return;
					}

					/*
					 * The villager became an adult WHILE this watcher was
					 * active.
					 *
					 * This is the only circumstance under which this module
					 * creates a Bubble optimization marker.
					 */
					VillagerOptimizeEvent optimizeEvent =
							new VillagerOptimizeEvent(
									wrapped,
									OptimizationType.ADULT_GROWTH,
									null,
									false
							);

					if (!optimizeEvent.callEvent()) {
						task.cancel();
						return;
					}

					/*
					 * PDCWrapperVO performs:
					 *
					 * 1. persist ADULT_GROWTH
					 * 2. setAware(false)
					 *
					 * It does NOT touch setAI().
					 */
					wrapped.setOptimizationType(
							optimizeEvent.getOptimizationType()
					);

					/*
					 * IMPORTANT:
					 *
					 * Do NOT call saveOptimizeTime() here.
					 *
					 * If a player wants this villager for trading:
					 *
					 *   shift-right-click -> unoptimize
					 *   assign workstation / profession
					 *   shift-right-click -> optimize again
					 *
					 * We don't want automatic breeder optimization to give
					 * them a 10-minute manual optimization cooldown.
					 */

					task.cancel();

				}, null, 20L, checkPeriodTicks);
	}
}
