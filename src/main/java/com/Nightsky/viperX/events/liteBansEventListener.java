package com.Nightsky.viperX.events;

import litebans.api.Entry;
import litebans.api.Events;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.UUID;

public class liteBansEventListener{
    public void register() {
        Events.get().register(new Events.Listener() {
            @Override
            public void entryAdded(Entry entry) {
                if ("ban".equals(entry.getType())) {
                    if (entry.isPermanent()) {
                        UUID id = UUID.fromString(entry.getUuid());

                        File playerDataFileMain = new File(Bukkit.getWorlds().getFirst().getWorldFolder(), "playerdata" + File.separator + id + ".dat");

                        playerDataFileMain.delete();

                        File playerDataFileOld = new File(Bukkit.getWorlds().getFirst().getWorldFolder(), "playerdata" + File.separator + id + ".dat_old");

                        playerDataFileOld.delete();
                    } else {
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
