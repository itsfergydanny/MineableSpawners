package com.dnyferguson.mineablespawners;

import com.dnyferguson.mineablespawners.api.API;
import com.dnyferguson.mineablespawners.commands.MineableSpawnersCommand;
import com.dnyferguson.mineablespawners.listeners.*;
import com.dnyferguson.mineablespawners.nms.*;
import com.dnyferguson.mineablespawners.utils.ConfigurationHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineableSpawners extends JavaPlugin {
    private ConfigurationHandler configurationHandler;
    private NMS_Handler nmsHandler;
    private Economy econ;
    private static API api;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        configurationHandler = new ConfigurationHandler(this);

        checkServerVersion();

        if (!setupEconomy()) {
            System.out.println("[MineableSpawners] vault not found, economy features disabled.");
        }

        getCommand("mineablespawners").setExecutor(new MineableSpawnersCommand(this));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new SpawnerMineListener(this), this);
        pm.registerEvents(new SpawnerPlaceListener(this), this);
        pm.registerEvents(new EggChangeListener(this), this);
        pm.registerEvents(new AnvilRenameListener(this), this);
        pm.registerEvents(new SpawnerExplodeListener(this), this);

        StringBuilder str = new StringBuilder("[MineableSpawners] Available mob types: \n");
        for (EntityType type : EntityType.values()) {
            str.append("- ");
            str.append(type.name());
            str.append("\n");
        }
        System.out.println(str.toString());

        api = new API(this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void checkServerVersion() {
        String version = Bukkit.getServer().getClass().getPackage().getName();
        if (version.contains("1_8")) {
            nmsHandler = new NMS_1_8();
            System.out.println("[MineableSpawners] loaded NMS class for version: " + version);
            return;
        }
        if (version.contains("1_9")) {
            nmsHandler = new NMS_1_9();
            System.out.println("[MineableSpawners] loaded NMS class for version: " + version);
            return;
        }
        if (version.contains("1_10")) {
            nmsHandler = new NMS_1_10();
            System.out.println("[MineableSpawners] loaded NMS class for version: " + version);
            return;
        }
        if (version.contains("1_11")) {
            nmsHandler = new NMS_1_11();
            System.out.println("[MineableSpawners] loaded NMS class for version: " + version);
            return;
        }
        if (version.contains("1_12")) {
            nmsHandler = new NMS_1_12();
            System.out.println("[MineableSpawners] loaded NMS class for version: " + version);
            return;
        }
        if (version.contains("1_13")) {
            nmsHandler = new NMS_1_13();
            System.out.println("[MineableSpawners] loaded NMS class for version: " + version);
            return;
        }
        if (version.contains("1_14")) {
            nmsHandler = new NMS_1_14();
            System.out.println("[MineableSpawners] loaded NMS class for version: " + version);
            return;
        }
        if (version.contains("1_15")) {
            nmsHandler = new NMS_1_15();
            System.out.println("[MineableSpawners] loaded NMS class for version: " + version);
            return;
        }

        nmsHandler = new NMS_1_15();
        System.out.println("[MineableSpawners] Warning: Current server minecraft version not explicitly supported, use at your own risk!");
    }

    public ConfigurationHandler getConfigurationHandler() {
        return configurationHandler;
    }

    public NMS_Handler getNmsHandler() {
        return nmsHandler;
    }

    public Economy getEcon() {
        return econ;
    }

    public static API getApi() {
        return api;
    }
}
