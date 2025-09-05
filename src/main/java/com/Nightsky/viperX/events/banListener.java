package com.Nightsky.viperX.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import java.lang.reflect.Method;

public class banListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBan(PlayerKickEvent event) {
        Player player = event.getPlayer();
        boolean isBan = false;

        try {
            // Try modern API: PlayerKickEvent#getCause()
            Method getCauseMethod = event.getClass().getMethod("getCause");
            Object cause = getCauseMethod.invoke(event);
            if (cause != null) {
                String causeName = cause.toString();
                if (causeName.equalsIgnoreCase("BANNED") || causeName.equalsIgnoreCase("IP_BANNED")) {
                    isBan = true;
                }
            }
        } catch (NoSuchMethodException ignored) {
            // Fallback for 1.8.9 → 1.11: check reason string
            String reason = event.getReason().toLowerCase();
            if (reason.contains("ban")) {
                isBan = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isBan) {
            player.getInventory().clear();
            player.getEnderChest().clear();
            player.updateInventory(); // needed for 1.8, harmless in newer versions
        }
    }
}
