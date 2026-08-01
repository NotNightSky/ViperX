package com.Nightsky.viperX.utils;

import com.Nightsky.viperX.main;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class NBTClear {
    public static void clearNBT(UUID playerUUID) {
        Bukkit.getScheduler().runTaskLater(main.getPlugin(), () -> {
            try{
                if (Bukkit.getServer().getPlayer(playerUUID) != null){
                    Bukkit.getLogger().warning("[ViperX] Player " + Bukkit.getServer().getPlayer(playerUUID).getName() + " is online, skipping NBT clear.");
                    return;
                }

                World world = Bukkit.getWorlds().get(0);

                File playerFile = null;
                List<String> location = main.getPlugin().getConfig().getStringList("global.playerdata-path");
                for (String path : location) {
                    playerFile = new File(world.getWorldFolder(), path + File.separator + playerUUID + ".dat");
                    if (playerFile.exists()) {
                        break;
                    }
                }

                if (playerFile == null || !playerFile.exists()) {
                    Bukkit.getLogger().warning("[ViperX] Player data file for " + Bukkit.getServer().getOfflinePlayer(playerUUID).getName() + " does not exist or was not found.");
                    Bukkit.getLogger().warning("[ViperX] Was the playerdata directory set correctly in the config? If not, please set it correctly and try again.");
                    return;
                }

                ReadWriteNBT nbt = NBT.readFile(playerFile);

                List<String> keys = main.getPlugin().getConfig().getStringList("global.NBT-Tags");

                for (String key : keys) {
                    if (!nbt.hasTag(key)) {
                        Bukkit.getLogger().warning("[ViperX] NBT key '" + key + "' does not exist for player " + Bukkit.getServer().getOfflinePlayer(playerUUID).getName());
                        continue;
                    }
                    nbt.removeKey(key);
                    Bukkit.getLogger().info("[ViperX] Removed NBT key '" + key + "'");
                }

                NBT.writeFile(playerFile, nbt);
                Bukkit.getLogger().info("[ViperX] Cleared NBT data for player " + Bukkit.getServer().getOfflinePlayer(playerUUID).getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1L);
    }
}
