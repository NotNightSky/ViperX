package com.Nightsky.viperX.utils;

import com.Nightsky.viperX.main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class pendingManager {

    private final main plugin;
    private final File file;
    private FileConfiguration config;
    private final Set<UUID> pendingClear;

    public pendingManager(main plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "pending.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("[ViperX] Could not create pending.yml: " + e.getMessage());
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        this.pendingClear = new HashSet<>();

        // Load existing UUIDs
        if (config.isList("pending")) {
            for (String uuidStr : config.getStringList("pending")) {
                try {
                    pendingClear.add(UUID.fromString(uuidStr));
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("[ViperX] Invalid UUID in pending.yml: " + uuidStr);
                }
            }
        }
    }

    // Add a player UUID to the pending list

    public void add(UUID uuid) {
        pendingClear.add(uuid);
    }

    //Remove a player UUID from the pending list
    public void remove(UUID uuid) {
        pendingClear.remove(uuid);
    }

    //Check if a UUID is pending inventory clear

    public boolean isPending(UUID uuid) {
        return pendingClear.contains(uuid);
    }

    //Get all pending UUIDs

    public Set<UUID> getAll() {
        return new HashSet<>(pendingClear);
    }

    //Save the current list to file
    public void save() {
        try {
            config.set("pending", pendingClear.stream().map(UUID::toString).toList());
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("[ViperX] Failed to save pending.yml: " + e.getMessage());
        }
    }
}
