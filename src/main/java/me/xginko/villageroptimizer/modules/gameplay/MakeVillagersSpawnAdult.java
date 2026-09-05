package me.xginko.villageroptimizer.modules.gameplay;

import com.cryptomorin.xseries.XEntityType;
import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MakeVillagersSpawnAdult extends VillagerOptimizerModule implements Listener {

    public MakeVillagersSpawnAdult() {
        super("gameplay.villagers-spawn-as-adults");
    }

    @Override
    public void enable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
    private void onVillagerSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == XEntityType.VILLAGER.get()) {
            final Villager villager = (Villager) event.getEntity();
            if (!villager.isAdult()) villager.setAdult();
        }
    }
}
