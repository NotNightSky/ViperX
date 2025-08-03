package com.Nightsky.viperX;

import com.Nightsky.viperX.events.advancedBanEventListener;
import com.Nightsky.viperX.events.banListener;
import com.Nightsky.viperX.events.onJoinWarn;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {
    private static main plugin;

    @Override
    public void onEnable() {
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
        } else {
            getLogger().info("AdvancedBan not found therefore using the vanilla ban system");
            getServer().getPluginManager().registerEvents(new banListener(), this);
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


