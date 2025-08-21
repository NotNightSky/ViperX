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
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class advancedBanEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(PunishmentEvent punishment) {
        Instant start = Instant.ofEpochMilli(punishment.getPunishment().getStart());
        Instant end = Instant.ofEpochMilli(punishment.getPunishment().getEnd());

        Duration duration = Duration.between(start, end);
        long banDuration = duration.toMillis();
        List<String> durationList = main.getPlugin().getConfig().getStringList("advancedban.ban-durations.duration");
        List<Long> parsedDurations = new ArrayList<>();
        main.getPlugin().getLogger().info(String.valueOf(main.getPlugin().getConfig().getInt("advancedban.ban-Durations.grace-period")));
        if(punishment.getPunishment().getType() == PunishmentType.BAN){
            String playerName = punishment.getPunishment().getName();
            OfflinePlayer offPlayer= Bukkit.getServer().getOfflinePlayer(playerName);

            UUID id = offPlayer.getUniqueId();

            File playerDataFileMain = new File(Bukkit.getWorlds().getFirst().getWorldFolder(), "playerdata" + File.separator + id + ".dat");

            playerDataFileMain.delete();

            File playerDataFileOld = new File(Bukkit.getWorlds().getFirst().getWorldFolder(), "playerdata" + File.separator + id + ".dat_old");

            playerDataFileOld.delete();
        } else if (punishment.getPunishment().getType() == PunishmentType.TEMP_BAN) {
            for (String durationStr : durationList) {
                long millis = durationParser.parseToMillis(durationStr);
                parsedDurations.add(millis);
                main.getPlugin().getLogger().info("ban duration should be " + banDuration);
                main.getPlugin().getLogger().info("Parsed: \"" + durationStr + "\" = " + millis + " ms");
            }

            for (long parsed : parsedDurations) {
                if (banDuration == parsed || Math.abs(banDuration - parsed) <= main.getPlugin().getConfig().getInt("advancedban.ban-Durations.grace-period")) {
                    main.getPlugin().getLogger().info("Ban duration matches config value: " + parsed + " ms");
                    String playerName = punishment.getPunishment().getName();
                    OfflinePlayer offPlayer= Bukkit.getServer().getOfflinePlayer(playerName);

                    UUID id = offPlayer.getUniqueId();

                    File playerDataFileMain = new File(Bukkit.getWorlds().getFirst().getWorldFolder(), "playerdata" + File.separator + id + ".dat");

                    playerDataFileMain.delete();

                    File playerDataFileOld = new File(Bukkit.getWorlds().getFirst().getWorldFolder(), "playerdata" + File.separator + id + ".dat_old");

                    playerDataFileOld.delete();
                    break;
                }
            }
        }
    }
}