package com.Nightsky.viperX.events;
import com.Nightsky.viperX.main;
import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.UUID;

/*
    Explosive Listener
    Recommended not to use

 */
public class advancedBanEventListener implements Listener {
   @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(PunishmentEvent punishment){
       String playerName = punishment.getPunishment().getName();
       OfflinePlayer offPlayer= Bukkit.getServer().getOfflinePlayer(playerName);

       UUID id = offPlayer.getUniqueId();

       main.getPlugin().getLogger().info("location should be " + id);

       File playerDataFileMain = new File(Bukkit.getWorlds().getFirst().getWorldFolder(), "playerdata" + File.separator + id + ".dat");

       playerDataFileMain.delete();

       File playerDataFileOld = new File(Bukkit.getWorlds().getFirst().getWorldFolder(), "playerdata" + File.separator + id + ".dat_old");

       playerDataFileOld.delete();

       main.getPlugin().getLogger().info("Main data file location should be " + playerDataFileMain);
       main.getPlugin().getLogger().info("Old data file location should be " + playerDataFileOld);
    }
}