package com.dnyferguson.mineablespawners;

import com.dnyferguson.mineablespawners.api.API;
import com.dnyferguson.mineablespawners.commands.MineableSpawnersCommand;
import com.dnyferguson.mineablespawners.listeners.*;
import com.dnyferguson.mineablespawners.metrics.Metrics;
import com.dnyferguson.mineablespawners.nms.*;
import com.dnyferguson.mineablespawners.utils.ConfigurationHandler;
import io.github.portlek.versionmatched.VersionMatched;
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
        getConfig().options().copyDefaults(true);
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

        if (getConfigurationHandler().getBoolean("global", "show-available")) {
            StringBuilder str = new StringBuilder("[MineableSpawners] Available mob types: \n");
            for (EntityType type : EntityType.values()) {
                str.append("- ");
                str.append(type.name());
                str.append("\n");
            }
            System.out.println(str.toString());
        }

        api = new API(this);
        int pluginId = 7354;
        Metrics metrics = new Metrics(this, pluginId);
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

        this.nmsHandler = new VersionMatched<>(
                NMS_1_8_R1.class,
                NMS_1_8_R2.class,
                NMS_1_8_R3.class,
                NMS_1_9_R1.class,
                NMS_1_9_R2.class,
                NMS_1_10_R1.class,
                NMS_1_11_R1.class,
                NMS_1_12_R1.class,
                NMS_1_13_R1.class,
                NMS_1_13_R2.class,
                NMS_1_15_R1.class,
                NMS_1_16_R1.class
        ).of().create().orElse(null);

        System.out.println("[MineableSpawners] loaded NMS class for version: " + version);
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
