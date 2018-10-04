package net.momentonetwork;

import net.momentonetwork.commands.SpawnerCommand;
import net.momentonetwork.commands.SpawnerGiveCommand;
import net.momentonetwork.listeners.BlockBreakListener;
import net.momentonetwork.listeners.EggChangeListener;
import net.momentonetwork.listeners.SpawnerPlaceListener;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineableSpawners extends JavaPlugin {
    FileConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // If no plugins/MineableSpawners/config.yml exists, make one
        saveDefaultConfig();
        // Load config
        config = getConfig();

        // Register Commands
        this.getCommand("spawner").setExecutor(new SpawnerCommand(this));
        this.getCommand("spawnergive").setExecutor(new SpawnerGiveCommand(this));

        // Register Listeners
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new SpawnerPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new EggChangeListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public FileConfiguration getConfigInstance() {
        return config;
    }
}
