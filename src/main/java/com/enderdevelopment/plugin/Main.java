package com.enderdevelopment.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("ChestGolemPlugin has been enabled!");
        getServer().getPluginManager().registerEvents(new ChestGolemListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("ChestGolemPlugin has been disabled!");
    }
}