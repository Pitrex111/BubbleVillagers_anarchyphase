package com.gmail.pitrex111;

import com.cryptomorin.xseries.XEntityType;
import com.cryptomorin.xseries.XMaterial;
import me.xginko.villageroptimizer.VillagerOptimizer;
import me.xginko.villageroptimizer.struct.enums.OptimizationType;
import me.xginko.villageroptimizer.struct.enums.Permissions;
import me.xginko.villageroptimizer.events.VillagerOptimizeEvent;
import me.xginko.villageroptimizer.events.VillagerUnoptimizeEvent;
import me.xginko.villageroptimizer.modules.VillagerOptimizerModule;
import me.xginko.villageroptimizer.utils.KyoriUtil;
import me.xginko.villageroptimizer.utils.LocationUtil;
import me.xginko.villageroptimizer.utils.Util;
import me.xginko.villageroptimizer.wrapper.WrappedVillager;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class OptimizeByShiftRightClick extends VillagerOptimizerModule implements Listener {

    private final long cooldown;
    private final boolean notify_player, log_enabled;

    public OptimizeByShiftRightClick() {
        super("optimization-methods.shift-right-click-optimization");
        this.cooldown = TimeUnit.SECONDS.toMillis(
                config.getInt(configPath + ".optimize-cooldown-seconds", 600,
                "Cooldown in seconds until a villager can be optimized again using shift + right click.\n" +
                "Here for configuration freedom. Recommended to leave as is to not enable any exploitable behavior."));
        this.notify_player = config.getBoolean(configPath + ".notify-player", true,
                "Sends players a message when they successfully optimized a villager.");
        this.log_enabled = config.getBoolean(configPath + ".log", false);
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
        return config.getBoolean(configPath + ".enable", true);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != XEntityType.VILLAGER.get()) return;
        final Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        if (!player.hasPermission(Permissions.Optimize.SHIFT_RIGHT_CLICK.get())) return;

        event.setCancelled(true);
        final WrappedVillager wrapped = wrapperCache.get((Villager) event.getRightClicked(), WrappedVillager::new);
        if (!wrapped.isOptimized()) {
            if (wrapped.canOptimize(cooldown) || player.hasPermission(Permissions.Bypass.SHIFT_RIGHT_CLICK_COOLDOWN.get())) {
                VillagerOptimizeEvent optimizeEvent = new VillagerOptimizeEvent(
                        wrapped,
                        OptimizationType.SHIFT_RIGHT_CLICK,
                        player,
                        event.isAsynchronous()
                );

                if (!optimizeEvent.callEvent()) return;

                wrapped.setOptimizationType(optimizeEvent.getOptimizationType());
                wrapped.saveOptimizeTime();

                if (notify_player) {
                    VillagerOptimizer.getLang(player.locale()).shift_right_click_optimize_success
                            .forEach(line -> KyoriUtil.sendMessage(player, line));
                }

                if (log_enabled) {
                    info(player.getName() + " optimized villager using shift + right click at " +
                         LocationUtil.toString(wrapped.villager.getLocation()));
                }
            } else {
                wrapped.sayNo();
                if (notify_player) {
                    final TextReplacementConfig timeLeft = TextReplacementConfig.builder()
                            .matchLiteral("%time%")
                            .replacement(Util.formatDuration(Duration.ofMillis(wrapped.getOptimizeCooldownMillis(cooldown))))
                            .build();
                    VillagerOptimizer.getLang(player.locale()).shift_right_click_on_optimize_cooldown
                            .forEach(line -> KyoriUtil.sendMessage(player, line.replaceText(timeLeft)));
                }
            }
        } else {
            VillagerUnoptimizeEvent unOptimizeEvent = new VillagerUnoptimizeEvent(
                    wrapped,
                    player,
                    OptimizationType.SHIFT_RIGHT_CLICK,
                    event.isAsynchronous()
            );

            if (!unOptimizeEvent.callEvent()) return;
            wrapped.setOptimizationType(OptimizationType.NONE);

            if (notify_player) {
                VillagerOptimizer.getLang(player.locale()).shift_right_click_unoptimize_success
                        .forEach(line -> KyoriUtil.sendMessage(player, line));
            }

            if (log_enabled) {
                info(player.getName() + " unoptimized villager at " +
                     LocationUtil.toString(wrapped.villager.getLocation()));
            }
        }
    }
}