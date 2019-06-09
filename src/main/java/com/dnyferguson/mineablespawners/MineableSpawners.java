package com.dnyferguson.mineablespawners;

import com.dnyferguson.mineablespawners.commands.SpawnerCommand;
import com.dnyferguson.mineablespawners.commands.SpawnerGiveCommand;
import com.dnyferguson.mineablespawners.listeners.AnvilRenameListener;
import com.dnyferguson.mineablespawners.listeners.BlockBreakListener;
import com.dnyferguson.mineablespawners.listeners.EggChangeListener;
import com.dnyferguson.mineablespawners.listeners.SpawnerPlaceListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineableSpawners extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getCommand("spawner").setExecutor(new SpawnerCommand(this));
        getCommand("spawnergive").setExecutor(new SpawnerGiveCommand(this));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockBreakListener(this), this);
        pm.registerEvents(new SpawnerPlaceListener(this), this);
        pm.registerEvents(new EggChangeListener(this), this);
        pm.registerEvents(new AnvilRenameListener(this), this);
    }
}
