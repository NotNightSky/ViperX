package com.Nightsky.viperX.events;

import com.Nightsky.viperX.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoinWarn implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event){
        boolean offlineModeWarn = main.getPlugin().getConfig().getBoolean("offline-mode-warning");
        Player player = event.getPlayer();

        if (offlineModeWarn && player.isOp()){
            player.sendMessage("§l§4[ViperX]§4§l " + "§l§o§4The server is currently in offline mode(aka online-mode = false) which can lead to some issues with the plugin although it has been tested with offline clients§4§o§l");
            player.sendMessage("§l§4[ViperX]§4§l " + "§eTo disable this warning, open setting menu(/viperx or /vx)");
        }

    }

}
