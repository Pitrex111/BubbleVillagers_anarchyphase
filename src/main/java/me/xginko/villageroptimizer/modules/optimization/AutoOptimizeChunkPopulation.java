package me.xginko.villageroptimizer.modules.optimization;

import com.cryptomorin.xseries.XEntityType;
import me.xginko.villageroptimizer.events.VillagerOptimizeEvent;
import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;
import me.xginko.villageroptimizer.struct.enums.OptimizationType;
import me.xginko.villageroptimizer.wrapper.WrappedVillager;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import space.arim.morepaperlib.scheduling.ScheduledTask;

public class AutoOptimizeChunkPopulation extends VillagerOptimizerModule implements Listener, Runnable {

    private final int threshold;
    private final long checkPeriodTicks;
    private ScheduledTask periodicCheck;

    public AutoOptimizeChunkPopulation() {
        super("optimization-methods.auto-optimize-chunk-population");
        this.threshold = Math.max(1, config.getInt(configPath + ".threshold", 20,
                "If a chunk has more than this many villagers, unoptimized villagers in that chunk will be optimized."));
        this.checkPeriodTicks = Math.max(20L, config.getInt(configPath + ".check-period-ticks", 200,
                "How often to scan loaded chunks (in ticks)."));
    }

    @Override
    public void enable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        periodicCheck = scheduling.globalRegionalScheduler().runAtFixedRate(this, checkPeriodTicks, checkPeriodTicks);
        run();
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
        if (periodicCheck != null) {
            periodicCheck.cancel();
            periodicCheck = null;
        }
    }

	@Override
	public boolean shouldEnable() {
		return false;
	}

    @Override
    public void run() {
        for (World world : plugin.getServer().getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                checkChunk(chunk);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onChunkLoad(ChunkLoadEvent event) {
        checkChunk(event.getChunk());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != XEntityType.VILLAGER.get()) return;
        checkChunk(event.getLocation().getChunk());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != XEntityType.VILLAGER.get()) return;
        checkChunk(event.getRightClicked().getLocation().getChunk());
    }

    private void checkChunk(Chunk chunk) {
        scheduling.regionSpecificScheduler(chunk.getWorld(), chunk.getX(), chunk.getZ()).run(() -> {
            int villagerCount = 0;
            for (Entity entity : chunk.getEntities()) {
                if (entity.getType() == XEntityType.VILLAGER.get()) villagerCount++;
            }

            if (villagerCount <= threshold) return;

            for (Entity entity : chunk.getEntities()) {
                if (!(entity instanceof Villager villager)) continue;

                WrappedVillager wrapped = wrapperCache.get(villager, WrappedVillager::new);
                if (wrapped.isOptimized()) continue;

                VillagerOptimizeEvent optimizeEvent = new VillagerOptimizeEvent(
                        wrapped,
                        OptimizationType.COMMAND,
                        null,
                        false
                );

                if (!optimizeEvent.callEvent()) continue;

                wrapped.setOptimizationType(optimizeEvent.getOptimizationType());
                wrapped.saveOptimizeTime();
            }
        });
    }
}
