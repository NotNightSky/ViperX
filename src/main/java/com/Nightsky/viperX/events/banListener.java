package com.Nightsky.viperX.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class banListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBan(PlayerKickEvent event){
        Player player = event.getPlayer();
        if (event.getCause().equals(PlayerKickEvent.Cause.BANNED) || event.getCause().equals(PlayerKickEvent.Cause.IP_BANNED))
            player.getInventory().clear();
    }
}
