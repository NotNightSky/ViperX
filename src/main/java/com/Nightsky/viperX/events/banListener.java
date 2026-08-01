package com.Nightsky.viperX.events;

import com.Nightsky.viperX.utils.NBTClear;
import org.bukkit.Bukkit;
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
            Method getCauseMethod = event.getClass().getMethod("getCause");
            Object cause = getCauseMethod.invoke(event);
            if (cause != null) {
                String causeName = cause.toString();
                if (causeName.equalsIgnoreCase("BANNED") || causeName.equalsIgnoreCase("IP_BANNED")) {
                    isBan = true;
                }
            }
        } catch (NoSuchMethodException ignored) {
            String reason = event.getReason().toLowerCase();
            if (reason.contains("ban")) {
                isBan = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isBan) {
            NBTClear.clearNBT(player.getUniqueId());
            Bukkit.getLogger().info("[ViperX] Player " + player.getName() + " was banned, clearing NBT data.");
        }
    }
}
