package me.xginko.villageroptimizer.modules.gameplay;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;
import me.xginko.villageroptimizer.wrapper.WrappedVillager;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ReconcileOptimizedVillagerState
		extends VillagerOptimizerModule
		implements Listener {

	public ReconcileOptimizedVillagerState() {
		super("safety.reconcile-optimized-villagers");
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
		 * IMPORTANT:
		 *
		 * Disabling this module only unregisters the listener.
		 *
		 * It does NOT:
		 * - remove optimization PDC
		 * - set awareness true
		 * - set AI true
		 * - otherwise alter villagers
		 */
		HandlerList.unregisterAll(this);
	}

	@Override
	public boolean shouldEnable() {
		/*
		 * This is a safety mechanism, not an optional optimization method.
		 */
		return true;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onEntityAddedToWorld(EntityAddToWorldEvent event) {

		if (!(event.getEntity() instanceof Villager villager)) {
			return;
		}

		/*
		 * EntityAddToWorldEvent also fires when an entity is added because
		 * its chunk was loaded.
		 *
		 * Use the entity scheduler so all PDC/entity access occurs in the
		 * villager's owning Folia region.
		 */
		scheduling.entitySpecificScheduler(villager).run(() -> {

			WrappedVillager wrapped =
					wrapperCache.get(
							villager,
							WrappedVillager::new
					);

			/*
			 * CRITICAL DROP-IN SAFETY:
			 *
			 * reconcileOptimizedState() only performs:
			 *
			 * PDC says optimized + aware=true
			 *              ↓
			 *          aware=false
			 *
			 * It NEVER creates a PDC marker and NEVER touches an
			 * unoptimized villager.
			 */
			wrapped.reconcileOptimizedState();

		}, null);
	}
}