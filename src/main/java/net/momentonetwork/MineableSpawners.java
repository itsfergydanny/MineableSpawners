package net.momentonetwork;

import net.momentonetwork.commands.SpawnerCommand;
import net.momentonetwork.commands.SpawnerGiveCommand;
import net.momentonetwork.listeners.BlockBreakListener;
import net.momentonetwork.listeners.SpawnerPlaceListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineableSpawners extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Register Commands
        this.getCommand("spawner").setExecutor(new SpawnerCommand());
        this.getCommand("spawnergive").setExecutor(new SpawnerGiveCommand());

        // Register Listeners
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnerPlaceListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
