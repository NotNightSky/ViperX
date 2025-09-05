package com.Nightsky.viperX.events;

import com.Nightsky.viperX.main;
import com.Nightsky.viperX.utils.durationParser;
import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.leoko.advancedban.utils.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class advancedBanEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(PunishmentEvent punishment) {
        long start = punishment.getPunishment().getStart();
        long end = punishment.getPunishment().getEnd();
        long banDuration = end - start;

        List<String> durationList = main.getPlugin().getConfig().getStringList("global.ban-durations.duration");
        List<Long> parsedDurations = new ArrayList<>();

        if (punishment.getPunishment().getType() == PunishmentType.BAN) {
            clearPlayerData(punishment.getPunishment().getName());
        } else if (punishment.getPunishment().getType() == PunishmentType.TEMP_BAN) {
            for (String durationStr : durationList) {
                long millis = durationParser.parseToMillis(durationStr);
                parsedDurations.add(millis);
                main.getPlugin().getLogger().info("[ViperX] ban duration should be " + banDuration);
                main.getPlugin().getLogger().info("[ViperX] Parsed: \"" + durationStr + "\" = " + millis + " ms");
            }

            int grace = main.getPlugin().getConfig().getInt("global.ban-durations.grace-period");

            for (long parsed : parsedDurations) {
                if (banDuration == parsed || Math.abs(banDuration - parsed) <= grace) {
                    main.getPlugin().getLogger().info("[ViperX] Ban duration matches config value: " + parsed + " ms");
                    clearPlayerData(punishment.getPunishment().getName());
                    break;
                }
            }
        }
    }

    private void clearPlayerData(String playerName) {
        OfflinePlayer offPlayer = Bukkit.getServer().getOfflinePlayer(playerName);
        UUID id = offPlayer.getUniqueId();

        File worldFolder = Bukkit.getWorlds().get(0).getWorldFolder();

        File playerDataFileMain = new File(worldFolder, "playerdata" + File.separator + id + ".dat");
        File playerDataFileOld = new File(worldFolder, "playerdata" + File.separator + id + ".dat_old");

        if (playerDataFileMain.exists()) playerDataFileMain.delete();
        if (playerDataFileOld.exists()) playerDataFileOld.delete();
    }
}
