package me.xginko.villageroptimizer.modules.gameplay;

import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;

/**
 * Legacy module intentionally disabled in the AnarchyPhase fork.
 *
 * BubbleVillagers optimization is persistent and should only be removed
 * explicitly by a player/admin action.
 *
 * In particular, losing a workstation/job must NOT automatically wake an
 * optimized villager.
 */
public class UnoptimizeOnJobLoose extends VillagerOptimizerModule {

	public UnoptimizeOnJobLoose() {
		super("gameplay.unoptimize-on-job-loose");
	}

	@Override
	public void enable() {
		// Intentionally disabled.
	}

	@Override
	public void disable() {
		// Nothing to unregister.
	}

	@Override
	public boolean shouldEnable() {
		return false;
	}
}