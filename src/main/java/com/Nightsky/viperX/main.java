package com.Nightsky.viperX;

//import com.Nightsky.viperX.events.advancedBanEventListener;
import com.Nightsky.viperX.events.banListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    private static main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("up and running");
        this.registerListener();
        //this.registerCommand();

    }

    public void registerListener(){
        //getServer().getPluginManager().registerEvents(new advancedBanEventListener(), this);
        getServer().getPluginManager().registerEvents(new banListener(), this);
    }

    public void registerCommand() {
    }

    @Override
    public void onDisable() {
        getLogger().info("shutting down");
    }

    public static main getPlugin() {
        return plugin;
    }
}


