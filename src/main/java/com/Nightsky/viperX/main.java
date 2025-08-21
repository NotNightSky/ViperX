package com.Nightsky.viperX;

import com.Nightsky.viperX.events.*;
import com.Nightsky.viperX.utils.pendingManager;
import com.Nightsky.viperX.utils.metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {

    private static main plugin;
    private pendingManager pendingManager;

    @Override
    public void onEnable() {
        int pluginId = 26811;
        metrics metrics = new metrics(this, pluginId);
        saveDefaultConfig();

        plugin = this;
        pendingManager = new pendingManager(this);

        getLogger().info("[ViperX] up and running");
        this.registerListener();
    }

    public void registerListener() {
        getServer().getPluginManager().registerEvents(new onJoinWarn(), this);
        getServer().getPluginManager().registerEvents(new onJoinPendingBanner(pendingManager), this);

        if (getServer().getPluginManager().getPlugin("AdvancedBan") != null) {
            getServer().getPluginManager().registerEvents(new advancedBanEventListener(), this);
            getLogger().info("[ViperX] AdvancedBan is found and being used");
        } else if (getServer().getPluginManager().getPlugin("LiteBans") != null) {
            new liteBansEventListener(pendingManager).register();
            getLogger().info("[ViperX] LiteBans is found and being used");
        } else {
            getServer().getPluginManager().registerEvents(new banListener(), this);
            getLogger().info("[ViperX] Litebans not found therefore using the vanilla ban system");
        }
    }

    @Override
    public void onDisable() {
        pendingManager.save();
        getLogger().info("[ViperX] shutting down");
    }

    public static main getPlugin() {
        return plugin;
    }

    public pendingManager getPendingManager() {
        return pendingManager;
    }
}
