package com.Nightsky.viperX.events;

import com.Nightsky.viperX.main;
import com.Nightsky.viperX.utils.NBTClear;
import com.Nightsky.viperX.utils.durationParser;
import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.leoko.advancedban.utils.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class advancedBanEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(PunishmentEvent punishment) {
        long start = punishment.getPunishment().getStart();
        long end = punishment.getPunishment().getEnd();
        long banDuration = end - start;

        UUID playerUUID = Bukkit.getServer().getOfflinePlayer(punishment.getPunishment().getName()).getUniqueId();

        List<String> durationList = main.getPlugin().getConfig().getStringList("global.ban-durations.duration");
        List<Long> parsedDurations = new ArrayList<>();

        if (punishment.getPunishment().getType() == PunishmentType.BAN) {
            NBTClear.clearNBT(playerUUID);
        } else if (punishment.getPunishment().getType() == PunishmentType.TEMP_BAN) {
            for (String durationStr : durationList) {
                long millis = durationParser.parseToMillis(durationStr);
                parsedDurations.add(millis);
            }

            int grace = main.getPlugin().getConfig().getInt("global.ban-durations.grace-period");

            for (long parsed : parsedDurations) {
                if (banDuration == parsed || Math.abs(banDuration - parsed) <= grace) {
                    NBTClear.clearNBT(playerUUID);
                    break;
                }
            }
        }
    }
}
