//1.8.9 comp
package com.Nightsky.viperX.events;

import com.Nightsky.viperX.main;
import com.Nightsky.viperX.utils.NBTClear;
import com.Nightsky.viperX.utils.pendingManager;
import com.Nightsky.viperX.utils.durationParser;
import litebans.api.Entry;
import litebans.api.Events;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class liteBansEventListener {
    private final pendingManager pendingManager;

    public liteBansEventListener(pendingManager pendingManager) {
        this.pendingManager = pendingManager;
    }

    public void register() {
        Events.get().register(new Events.Listener() {
            @Override
            public void entryAdded(Entry entry) {
                if ("ban".equalsIgnoreCase(entry.getType())) {
                    UUID uuid = UUID.fromString(entry.getUuid());
                    Player player = Bukkit.getPlayer(uuid);

                    if (entry.isPermanent()) {
                        if (player != null && player.isOnline()) {
                            NBTClear.clearNBT(uuid);
                        } else {
                            pendingManager.add(uuid);
                            pendingManager.save();
                            main.getPlugin().getLogger().info("[ViperX] Queued permanent ban inventory clear for: " + entry.getUuid());
                        }
                    } else {
                        long banDuration = entry.getDuration();
                        List<String> durationList = main.getPlugin().getConfig().getStringList("global.ban-durations.duration");
                        List<Long> parsedDurations = new ArrayList<>();

                        for (String durationStr : durationList) {
                            long millis = durationParser.parseToMillis(durationStr);
                            parsedDurations.add(millis);
                        }

                        for (long parsed : parsedDurations) {
                            if (banDuration == parsed){
                                if (player != null && player.isOnline()) {
                                    NBTClear.clearNBT(uuid);
                                } else {
                                    pendingManager.add(uuid);
                                    pendingManager.save();
                                    main.getPlugin().getLogger().info("[ViperX] Queued temp ban inventory clear for: " + entry.getUuid());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        });
    }
}
