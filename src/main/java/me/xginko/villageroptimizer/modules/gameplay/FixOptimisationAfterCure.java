package me.xginko.villageroptimizer.modules.gameplay;

import com.cryptomorin.xseries.XEntityType;
import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;
import me.xginko.villageroptimizer.wrapper.WrappedVillager;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;

public class FixOptimisationAfterCure
		extends VillagerOptimizerModule
		implements Listener {

	public FixOptimisationAfterCure() {
		super("post-cure-optimization-fix");
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
	private void onTransform(EntityTransformEvent event) {

		if (event.getTransformReason()
				!= EntityTransformEvent.TransformReason.CURED) {
			return;
		}

		if (event.getTransformedEntity().getType()
				!= XEntityType.VILLAGER.get()) {
			return;
		}

		Villager villager =
				(Villager) event.getTransformedEntity();

		/*
		 * Keep the historical delay because Minecraft still performs
		 * additional conversion initialization around the cure.
		 *
		 * The important difference from the old implementation is that
		 * this task does NOT create or rewrite optimization state.
		 *
		 * It only re-applies aware=false if the transformed villager
		 * already retained BubbleVillagers' PDC marker.
		 */
		scheduling.entitySpecificScheduler(villager)
				.runDelayed(() -> {

					WrappedVillager wrapped =
							wrapperCache.get(
									villager,
									WrappedVillager::new
							);

					wrapped.reconcileOptimizedState();

				}, null, 40L);
	}
}