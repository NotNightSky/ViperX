package com.Nightsky.viperX.events;
import com.Nightsky.viperX.main;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import me.leoko.advancedban.bukkit.event.PunishmentEvent;

public class advancedBanEventListener implements Listener {
    public void onEvent(PunishmentEvent event){
    String punishment = String.valueOf(event.getPunishment());
        System.out.println("detected by viper: " + punishment);
        main.getPlugin().getLogger().info("detected by viper: " + punishment);
    }
}
