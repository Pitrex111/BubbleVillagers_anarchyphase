package me.xginko.villageroptimizer.wrapper;

import me.xginko.villageroptimizer.VillagerOptimizer;
import me.xginko.villageroptimizer.struct.enums.Keyring;
import me.xginko.villageroptimizer.struct.enums.OptimizationType;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Villager;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WrappedVillager extends PDCWrapper {

	/*
	 * IMPORTANT:
	 *
	 * BubbleVillagers' own persistent data is the authoritative state.
	 *
	 * Previous versions attempted to mix BubbleVillagers and
	 * AntiVillagerLag PDC state. For this server fork we intentionally do
	 * not allow another plugin's PDC markers to control BubbleVillagers.
	 *
	 * Existing BubbleVillagers villagers remain fully compatible because
	 * PDCWrapperVO uses the exact same namespace and keys as before.
	 */
	private final @NotNull PDCWrapperVO bubbleData;

	public WrappedVillager(@NotNull Villager villager) {
		super(villager);
		this.bubbleData = new PDCWrapperVO(villager);
	}

	/**
	 * Returns a number between 0 and 23999.
	 * Affected by /time set.
	 */
	public long currentDayTimeTicks() {
		return villager.getWorld().getTime();
	}

	/**
	 * Returns the world's full accumulated time.
	 */
	public long currentFullTimeTicks() {
		return villager.getWorld().getFullTime();
	}

	/**
	 * Performs a proper Paper 1.21.11 villager restock.
	 *
	 * Villager#restock():
	 * - updates offer demand
	 * - fires VillagerReplenishTradeEvent
	 * - resets eligible offer uses
	 *
	 * This replaces the old implementation which manually set recipe uses
	 * to zero and skipped vanilla/Paper demand handling.
	 */
	public void restock() {
		VillagerOptimizer.scheduling()
				.entitySpecificScheduler(villager)
				.run(villager::restock, null);
	}

	/**
	 * Re-applies the runtime portion of an already-persisted optimization.
	 *
	 * CRITICAL SAFETY PROPERTY:
	 *
	 * This method can only disable awareness when BubbleVillagers' own PDC
	 * marker already exists.
	 *
	 * It NEVER:
	 * - creates an optimization marker
	 * - optimizes an unmarked villager
	 * - enables AI
	 * - enables awareness
	 *
	 * Therefore it is safe to use when a villager/chunk loads.
	 */
	public void reconcileOptimizedState() {
		VillagerOptimizer.scheduling()
				.entitySpecificScheduler(villager)
				.run(() -> {
					if (!bubbleData.isOptimized()) {
						return;
					}

					if (villager.isAware()) {
						villager.setAware(false);
					}
				}, null);
	}

	/**
	 * @return level 1-5 calculated from stored villager trading experience.
	 */
	public int calculateLevel() {
		int villagerExperience = villager.getVillagerExperience();

		if (villagerExperience >= 250) return 5;
		if (villagerExperience >= 150) return 4;
		if (villagerExperience >= 70) return 3;
		if (villagerExperience >= 10) return 2;

		return 1;
	}

	/**
	 * @return true if this villager can still lose its profession after its
	 * workstation is removed.
	 *
	 * Method name retained for source compatibility with existing callers.
	 */
	public boolean canLooseProfession() {
		return villager.getVillagerLevel() <= 1
				&& villager.getVillagerExperience() <= 0;
	}

	public void sayNo() {
		try {
			villager.shakeHead();
		} catch (NoSuchMethodError e) {
			villager.getWorld().playSound(
					villager.getEyeLocation(),
					Sound.ENTITY_VILLAGER_NO,
					1.0F,
					1.0F
			);
		}
	}

	public @Nullable Location getJobSite() {
		return villager.getMemory(MemoryKey.JOB_SITE);
	}

	@Override
	public Keyring.Space getSpace() {
		return Keyring.Space.VillagerOptimizer;
	}

	/**
	 * Only BubbleVillagers' own marker determines Bubble optimization.
	 *
	 * We intentionally no longer allow legacy AntiVillagerLag markers to
	 * cause a villager to be considered Bubble-optimized.
	 */
	@Override
	public boolean isOptimized() {
		return bubbleData.isOptimized();
	}

	@Override
	public boolean canOptimize(long cooldown_millis) {
		return bubbleData.canOptimize(cooldown_millis);
	}

	@Override
	public void setOptimizationType(@NotNull OptimizationType type) {
		bubbleData.setOptimizationType(type);
	}

	@Override
	public @NotNull OptimizationType getOptimizationType() {
		return bubbleData.getOptimizationType();
	}

	@Override
	public void saveOptimizeTime() {
		bubbleData.saveOptimizeTime();
	}

	@Override
	public long getOptimizeCooldownMillis(long cooldown_millis) {
		return bubbleData.getOptimizeCooldownMillis(cooldown_millis);
	}

	@Override
	public long getLastRestockFullTime() {
		return bubbleData.getLastRestockFullTime();
	}

	@Override
	public void saveRestockTime() {
		bubbleData.saveRestockTime();
	}

	@Override
	public boolean canLevelUp(long cooldown_millis) {
		return bubbleData.canLevelUp(cooldown_millis);
	}

	@Override
	public void saveLastLevelUp() {
		bubbleData.saveLastLevelUp();
	}

	@Override
	public long getLevelCooldownMillis(long cooldown_millis) {
		return bubbleData.getLevelCooldownMillis(cooldown_millis);
	}
}