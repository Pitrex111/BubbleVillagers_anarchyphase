package me.xginko.villageroptimizer;

import me.xginko.villageroptimizer.modules.optimization.AutoOptimizeChunkPopulation;
import me.xginko.villageroptimizer.modules.optimization.AutoOptimizeTradeHallPopulation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class AutoOptimizeModesTest extends MockBukkitTestBase {

	@Test
	void autoOptimizeChunkPopulationIsDisabledInAnarchyPhaseFork() {
		AutoOptimizeChunkPopulation module =
				new AutoOptimizeChunkPopulation();

		assertFalse(
				module.shouldEnable(),
				"AutoOptimizeChunkPopulation must remain disabled in the AnarchyPhase fork."
		);
	}

	@Test
	void autoOptimizeTradeHallPopulationIsDisabledInAnarchyPhaseFork() {
		AutoOptimizeTradeHallPopulation module =
				new AutoOptimizeTradeHallPopulation();

		assertFalse(
				module.shouldEnable(),
				"AutoOptimizeTradeHallPopulation must remain disabled in the AnarchyPhase fork."
		);
	}
}
