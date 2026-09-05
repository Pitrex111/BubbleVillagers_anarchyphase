package me.xginko.villageroptimizer.modules.gameplay;

import me.xginko.villageroptimizer.events.VillagerOptimizeEvent;
import me.xginko.villageroptimizer.events.VillagerUnoptimizeEvent;
import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;
import me.xginko.villageroptimizer.VillagerOptimizer;
import me.xginko.villageroptimizer.wrapper.WrappedVillager;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class VisuallyHighlightOptimized extends VillagerOptimizerModule implements Listener {

    public VisuallyHighlightOptimized() {
        super("gameplay.outline-optimized-villagers");
    }

    @Override
    public void enable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // Apply glow to already-optimized villagers in currently loaded chunks.
        for (World world : plugin.getServer().getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                glowOptimizedVillagersInChunk(chunk);
            }
        }
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

	@Override
	public boolean shouldEnable() {
		return false;
	}

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onOptimize(VillagerOptimizeEvent event) {
        Villager villager = event.getWrappedVillager().villager;
        scheduling.entitySpecificScheduler(villager).run(glow -> {
            if (!villager.isGlowing()) villager.setGlowing(true);
        }, null);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onUnOptimize(VillagerUnoptimizeEvent event) {
        Villager villager = event.getWrappedVillager().villager;
        scheduling.entitySpecificScheduler(villager).run(unGlow -> {
            if (villager.isGlowing()) villager.setGlowing(false);
        }, null);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onChunkLoad(ChunkLoadEvent event) {
        glowOptimizedVillagersInChunk(event.getChunk());
    }

    private void glowOptimizedVillagersInChunk(Chunk chunk) {
        for (org.bukkit.entity.Entity entity : chunk.getEntities()) {
            if (!(entity instanceof Villager villager)) continue;
            if (villager.isGlowing()) continue;

            WrappedVillager wrapped = VillagerOptimizer.wrappers().get(villager, WrappedVillager::new);
            if (!wrapped.isOptimized()) continue;

            scheduling.entitySpecificScheduler(villager).run(glow -> {
                if (!villager.isGlowing()) villager.setGlowing(true);
            }, null);
        }
    }
}