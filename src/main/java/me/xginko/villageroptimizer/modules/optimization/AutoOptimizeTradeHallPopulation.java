package me.xginko.villageroptimizer.modules.optimization;

import com.cryptomorin.xseries.XEntityType;
import me.xginko.villageroptimizer.events.VillagerOptimizeEvent;
import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;
import me.xginko.villageroptimizer.struct.enums.OptimizationType;
import me.xginko.villageroptimizer.wrapper.WrappedVillager;
import org.bukkit.Chunk;
import org.bukkit.Location;
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

import java.util.Collection;

public class AutoOptimizeTradeHallPopulation extends VillagerOptimizerModule implements Listener, Runnable {

    private final int population;
    private final double radiusBlocks;
    private final long checkPeriodTicks;
    private ScheduledTask periodicCheck;

    public AutoOptimizeTradeHallPopulation() {
        super("optimization-methods.auto-optimize-trade-hall-population");
        this.population = Math.max(1, config.getInt(configPath + ".population", 20,
                "If there are at least this many villagers within radius-blocks, optimize unoptimized villagers in that area."));
        this.radiusBlocks = Math.max(1.0, config.getDouble(configPath + ".radius-blocks", 16.0,
                "Radius (in blocks) to consider as a single trade hall."));
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
                scanChunk(chunk);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onChunkLoad(ChunkLoadEvent event) {
        scanChunk(event.getChunk());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != XEntityType.VILLAGER.get()) return;
        checkTradeHallAt(event.getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != XEntityType.VILLAGER.get()) return;
        checkTradeHallAt(event.getRightClicked().getLocation());
    }

    private void scanChunk(Chunk chunk) {
        scheduling.regionSpecificScheduler(chunk.getWorld(), chunk.getX(), chunk.getZ()).run(() -> {
            for (Entity entity : chunk.getEntities()) {
                if (!(entity instanceof Villager villager)) continue;
                if (wrapperCache.get(villager, WrappedVillager::new).isOptimized()) continue;
                checkTradeHallAt(villager.getLocation());
            }
        });
    }

    private void checkTradeHallAt(Location origin) {
        scheduling.regionSpecificScheduler(origin).run(() -> {
            Collection<Villager> nearby = origin.getNearbyEntitiesByType(Villager.class, radiusBlocks);
            if (nearby.size() < population) return;

            for (Villager villager : nearby) {
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
