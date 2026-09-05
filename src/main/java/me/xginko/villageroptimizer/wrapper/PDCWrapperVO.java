package me.xginko.villageroptimizer.wrapper;
import org.bukkit.Bukkit;
import me.xginko.villageroptimizer.VillagerOptimizer;
import me.xginko.villageroptimizer.struct.enums.Keyring;
import me.xginko.villageroptimizer.struct.enums.OptimizationType;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class PDCWrapperVO extends PDCWrapper {

	PDCWrapperVO(@NotNull Villager villager) {
		super(villager);
	}

	@Override
	public Keyring.Space getSpace() {
		return Keyring.Space.VillagerOptimizer;
	}

	/**
	 * IMPORTANT:
	 * The existence of the old BubbleVillagers/VillagerOptimizer PDC marker
	 * is the authoritative indication that this villager is optimized.
	 *
	 * Do not change this key or namespace. Existing villagers in the world
	 * rely on it for drop-in compatibility.
	 */
	@Override
	public boolean isOptimized() {
		return dataContainer.has(
				Keyring.VillagerOptimizer.OPTIMIZATION_TYPE.getKey(),
				PersistentDataType.STRING
		);
	}

	@Override
	public boolean canOptimize(long cooldown_millis) {
		long lastOptimize = getLastOptimize();

		if (lastOptimize <= 0L) {
			return true;
		}

		return System.currentTimeMillis() >= lastOptimize + cooldown_millis;
	}

	/**
	 * Changes BubbleVillagers optimization state.
	 *
	 * SAFETY RULES:
	 *
	 * Optimization:
	 *   1. Persist the optimization marker.
	 *   2. Disable awareness.
	 *
	 * Unoptimization:
	 *   1. Remove the optimization marker.
	 *   2. Enable awareness.
	 *
	 * This ordering intentionally biases failures toward villagers remaining
	 * inactive instead of unexpectedly waking large numbers of villagers.
	 *
	 * BubbleVillagers only changes awareness. It must NOT force AI on/off,
	 * because AI may be managed by another plugin such as FarmControl.
	 */
	@Override
	public void setOptimizationType(@NotNull OptimizationType type) {

		/*
		 * Most calls we care about, especially shift + right-click,
		 * already run on the villager's owning region.
		 *
		 * In that case apply the state immediately instead of waiting
		 * for another scheduler tick.
		 */
		if (Bukkit.isOwnedByCurrentRegion(villager) && !villager.isTrading()) {
			applyOptimizationType(type);
			return;
		}

		/*
		 * If we are not currently on the entity's owning region, or the
		 * villager is currently trading, fall back to the entity scheduler.
		 *
		 * The repeating task is only needed so an operation requested while
		 * trading can wait until the trade session has finished.
		 */
		VillagerOptimizer.scheduling()
				.entitySpecificScheduler(villager)
				.runAtFixedRate(task -> {

					if (villager.isTrading()) {
						return;
					}

					applyOptimizationType(type);
					task.cancel();

				}, null, 1L, 20L);
	}

	/**
	 * Must only be called from the villager's owning region.
	 */
	private void applyOptimizationType(@NotNull OptimizationType type) {

		if (type == OptimizationType.NONE) {

			/*
			 * BubbleVillagers must never wake a villager merely because
			 * somebody called setOptimizationType(NONE).
			 *
			 * Only remove/undo state when our own legacy-compatible PDC
			 * marker actually exists.
			 */
			if (!isOptimized()) {
				return;
			}

			/*
			 * Fail-safe ordering:
			 *
			 * Remove the PDC marker first, then enable awareness.
			 *
			 * If something catastrophic happens between these two operations,
			 * one villager may remain inactive. That is much safer than waking
			 * villagers unexpectedly.
			 */
			dataContainer.remove(
					Keyring.VillagerOptimizer.OPTIMIZATION_TYPE.getKey()
			);

			villager.setAware(true);

			/*
			 * DO NOT call setAI(true).
			 *
			 * BubbleVillagers does not own the AI property. FarmControl or
			 * another plugin may intentionally have AI disabled.
			 */
			return;
		}

		/*
		 * Optimization uses the opposite fail-safe ordering:
		 *
		 * Persist first, then disable awareness.
		 *
		 * If the server stops between these operations, the PDC marker remains
		 * and our later reconciliation system can safely restore aware=false.
		 */
		dataContainer.set(
				Keyring.VillagerOptimizer.OPTIMIZATION_TYPE.getKey(),
				PersistentDataType.STRING,
				type.name()
		);

		villager.setAware(false);
	}

	@Override
	public @NotNull OptimizationType getOptimizationType() {
		String stored = dataContainer.get(
				Keyring.VillagerOptimizer.OPTIMIZATION_TYPE.getKey(),
				PersistentDataType.STRING
		);

		if (stored == null) {
			return OptimizationType.NONE;
		}

		try {
			return OptimizationType.valueOf(stored);
		} catch (IllegalArgumentException ignored) {

			/*
			 * FAIL-SAFE:
			 *
			 * A marker exists, therefore we must continue treating this entity
			 * as optimized even if an old/unknown enum value was stored.
			 *
			 * Returning NONE here would be dangerous because other code might
			 * interpret it as an unoptimized villager and wake it.
			 *
			 * SHIFT_RIGHT_CLICK is our supported optimization method and is a
			 * safe legacy fallback for this private fork.
			 */
			return OptimizationType.SHIFT_RIGHT_CLICK;
		}
	}

	@Override
	public void saveOptimizeTime() {
		dataContainer.set(
				Keyring.VillagerOptimizer.LAST_OPTIMIZE_SYSTIME_MILLIS.getKey(),
				PersistentDataType.LONG,
				System.currentTimeMillis()
		);
	}

	/**
	 * @return System time in milliseconds when the villager was last optimized,
	 *         or 0 if no optimization time has ever been stored.
	 */
	private long getLastOptimize() {
		Long stored = dataContainer.get(
				Keyring.VillagerOptimizer.LAST_OPTIMIZE_SYSTIME_MILLIS.getKey(),
				PersistentDataType.LONG
		);

		return stored != null ? stored : 0L;
	}

	@Override
	public long getOptimizeCooldownMillis(long cooldown_millis) {
		long lastOptimize = getLastOptimize();

		if (lastOptimize <= 0L) {
			return 0L;
		}

		long remaining =
				(lastOptimize + cooldown_millis) - System.currentTimeMillis();

		return Math.max(0L, remaining);
	}

	@Override
	public long getLastRestockFullTime() {
		Long stored = dataContainer.get(
				Keyring.VillagerOptimizer.LAST_RESTOCK_WORLD_FULLTIME.getKey(),
				PersistentDataType.LONG
		);

		return stored != null ? stored : 0L;
	}

	@Override
	public void saveRestockTime() {
		dataContainer.set(
				Keyring.VillagerOptimizer.LAST_RESTOCK_WORLD_FULLTIME.getKey(),
				PersistentDataType.LONG,
				villager.getWorld().getFullTime()
		);
	}

	@Override
	public boolean canLevelUp(long cooldown_millis) {
		long lastLevelUp = getLastLevelUpTime();

		if (lastLevelUp <= 0L) {
			return true;
		}

		return System.currentTimeMillis() >= lastLevelUp + cooldown_millis;
	}

	@Override
	public void saveLastLevelUp() {
		dataContainer.set(
				Keyring.VillagerOptimizer.LAST_LEVELUP_SYSTIME_MILLIS.getKey(),
				PersistentDataType.LONG,
				System.currentTimeMillis()
		);
	}

	/**
	 * @return System time in milliseconds when the last level-up process
	 *         occurred, or 0 if none has been recorded.
	 */
	private long getLastLevelUpTime() {
		Long stored = dataContainer.get(
				Keyring.VillagerOptimizer.LAST_LEVELUP_SYSTIME_MILLIS.getKey(),
				PersistentDataType.LONG
		);

		return stored != null ? stored : 0L;
	}

	@Override
	public long getLevelCooldownMillis(long cooldown_millis) {
		long lastLevelUp = getLastLevelUpTime();

		if (lastLevelUp <= 0L) {
			return 0L;
		}

		long remaining =
				(lastLevelUp + cooldown_millis) - System.currentTimeMillis();

		return Math.max(0L, remaining);
	}
}