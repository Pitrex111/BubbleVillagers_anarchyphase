package me.xginko.villageroptimizer.modules.gameplay;

import me.xginko.villageroptimizer.VillagerOptimizer;
import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;
import me.xginko.villageroptimizer.utils.KyoriUtil;
import me.xginko.villageroptimizer.wrapper.WrappedVillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class VillagerOptimizationStatus
		extends VillagerOptimizerModule
		implements Listener {

	public VillagerOptimizationStatus() {
		super("gameplay.show-optimization-status");
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
		/*
		 * Core UX feature for the AnarchyPhase fork.
		 */
		return true;
	}

	@EventHandler(
			priority = EventPriority.MONITOR,
			ignoreCancelled = true
	)
	private void onVillagerInteract(
			PlayerInteractEntityEvent event
	) {

		/*
		 * Prevent duplicate main-hand/off-hand messages.
		 */
		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}

		if (!(event.getRightClicked()
				instanceof Villager villager)) {
			return;
		}

		Player player = event.getPlayer();

		/*
		 * Sneak + right-click is the actual optimization toggle.
		 *
		 * OptimizeByShiftRightClick already provides success/cooldown
		 * feedback, so don't overwrite it with a generic status message.
		 */
		if (player.isSneaking()) {
			return;
		}

		WrappedVillager wrapped =
				wrapperCache.get(
						villager,
						WrappedVillager::new
				);

		boolean optimized =
				wrapped.isOptimized();

		Villager.Profession profession =
				villager.getProfession();

		boolean hasUsableJob =
				profession != Villager.Profession.NONE
						&& profession != Villager.Profession.NITWIT;

		/*
		 * Desired UX:
		 *
		 * No job / nitwit:
		 *   optimized   -> show optimized
		 *   unoptimized -> show unoptimized
		 *
		 * Has job:
		 *   optimized   -> say nothing
		 *   unoptimized -> show unoptimized
		 */
		if (hasUsableJob && optimized) {
			return;
		}

		if (optimized) {
			KyoriUtil.sendActionBar(
					player,
					VillagerOptimizer
							.getLang(player.locale())
							.villager_status_optimized
			);
		} else {
			KyoriUtil.sendActionBar(
					player,
					VillagerOptimizer
							.getLang(player.locale())
							.villager_status_unoptimized
			);
		}
	}
}