package com.Nightsky.viperX;

import com.Nightsky.viperX.commands.clearInv;
import com.Nightsky.viperX.commands.hello;
import com.Nightsky.viperX.events.banListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("up and running");
        this.getCommand("hello").setExecutor(new hello());
        this.getCommand("clearinv").setExecutor(new clearInv());
        this.registerListener();

    }

    public void registerListener(){
    getServer().getPluginManager().registerEvents(new banListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("shuting down");
    }
}
