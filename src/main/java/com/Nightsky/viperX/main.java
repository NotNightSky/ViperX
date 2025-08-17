package com.Nightsky.viperX;

import com.Nightsky.viperX.events.advancedBanEventListener;
import com.Nightsky.viperX.events.banListener;
import com.Nightsky.viperX.events.liteBansEventListener;
import com.Nightsky.viperX.events.onJoinWarn;
import com.Nightsky.viperX.utils.metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {
    private static main plugin;

    @Override
    public void onEnable() {
        int pluginId = 26811;
        metrics metrics = new metrics(this, pluginId);
        saveDefaultConfig();

        plugin = this;
        getLogger().info("up and running");
        this.registerListener();

    }

    public void registerListener(){
        getServer().getPluginManager().registerEvents(new onJoinWarn(), this);

        if (getServer().getPluginManager().getPlugin("AdvancedBan") != null){
            getServer().getPluginManager().registerEvents(new advancedBanEventListener(), this);
            getLogger().info("AdvancedBan is found and being used");
        } else if (getServer().getPluginManager().getPlugin("LiteBans") != null) {
            new liteBansEventListener().register();
            getLogger().info("LiteBans is found and being used");
        } else {
            getServer().getPluginManager().registerEvents(new banListener(), this);
            getLogger().info("Litebans not found therefore using the vanilla ban system");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("shutting down");
    }

    public static main getPlugin() {
        return plugin;
    }

}


