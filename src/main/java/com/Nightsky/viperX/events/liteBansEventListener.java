package com.Nightsky.viperX.events;

import litebans.api.Entry;
import litebans.api.Events;
import org.bukkit.Bukkit;

import org.bukkit.entity.Player;

import java.util.UUID;

public class liteBansEventListener{
    public void register() {
        Events.get().register(new Events.Listener() {
            @Override
            public void entryAdded(Entry entry) {
                if ("ban".equals(entry.getType())) {
                    if (entry.isPermanent()) {
                        Player player = Bukkit.getPlayer(UUID.fromString(entry.getUuid()));

                        player.getInventory().clear();
                        player.updateInventory();
                    }else {
                        System.out.println("[LiteBans] Player temporarily banned: "
                                + entry.getUuid()
                                + " Reason: " + entry.getReason()
                                + " Duration: " + entry.getDurationString());
                    }
                }
            }
        });
    }
}
