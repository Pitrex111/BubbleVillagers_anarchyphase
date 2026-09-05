package me.xginko.villageroptimizer.struct.enums;

public enum OptimizationType {

	/*
	 * Legacy values.
	 *
	 * DO NOT rename/remove these because old villagers may already have
	 * these exact strings stored in their PDC.
	 */
	CHUNK_LIMIT,
	REGIONAL_ACTIVITY,
	COMMAND,
	NAMETAG,
	SHIFT_RIGHT_CLICK,
	WORKSTATION,
	BLOCK,

	/*
	 * AnarchyPhase fork:
	 * Villager was automatically optimized after naturally becoming adult.
	 */
	ADULT_GROWTH,

	NONE
}
