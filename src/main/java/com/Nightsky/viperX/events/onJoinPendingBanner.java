//1.8.9 comp
package com.Nightsky.viperX.events;

import com.Nightsky.viperX.utils.pendingManager;
import com.Nightsky.viperX.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class onJoinPendingBanner implements Listener {

    private final pendingManager pendingManager;

    public onJoinPendingBanner(pendingManager pendingManager) {
        this.pendingManager = pendingManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();

        if (pendingManager.isPending(id)) {
            // Clear inventory + ender chest
            player.getInventory().clear();
            player.getEnderChest().clear();
            player.updateInventory();

            // Remove from pending list
            pendingManager.remove(id);
            pendingManager.save();

            main.getPlugin().getLogger().info("[ViperX] Cleared inventory for banned player on join: " + player.getName());
        }
    }
}
