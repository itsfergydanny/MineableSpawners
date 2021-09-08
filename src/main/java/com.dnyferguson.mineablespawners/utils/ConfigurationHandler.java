package com.dnyferguson.mineablespawners.utils;

import com.dnyferguson.mineablespawners.MineableSpawners;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationHandler {
    private MineableSpawners plugin;

    private Map<String, Map<String, String>> messages = new HashMap<>();
    private Map<String, Map<String, Boolean>> booleans = new HashMap<>();
    private Map<String, Map<String, List<String>>> lists = new HashMap<>();
    private Map<String, Map<String, Double>> doubles = new HashMap<>();
    private Map<String, Map<String, Integer>> integers = new HashMap<>();

    public ConfigurationHandler(MineableSpawners plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        firstTime(config);
    }

    public void firstTime(FileConfiguration config) {
        loadSections(config);
    }

    public void reload() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        loadSections(config);
    }

    private void loadSections(FileConfiguration config) {
        globalSection(config);
        mainSection(config);
        giveSection(config);
        setSection(config);
        typesSection(config);
        anvilSection(config);
        eggsSection(config);
        explodeSection(config);
        miningSection(config);
        placingSection(config);
        witherSection(config);
    }

    private void placingSection(FileConfiguration config) {
        Map<String, String> msgs = new HashMap<>();
        Map<String, Boolean> bools = new HashMap<>();
        Map<String, List<String>> lsts = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("placing");

        bools.put("log", section.getBoolean("log"));
        bools.put("charge", section.getBoolean("charge"));

        msgs.put("blacklisted", section.getString("messages.blacklisted"));
        msgs.put("not-enough-money", section.getString("messages.not-enough-money"));
        msgs.put("transaction-success", section.getString("messages.transaction-success"));

        lsts.put("blacklisted-worlds", section.getStringList("blacklisted-worlds"));
        lsts.put("prices", section.getStringList("prices"));

        messages.put("placing", msgs);
        booleans.put("placing", bools);
        lists.put("placing", lsts);
    }

    private void miningSection(FileConfiguration config) {
        Map<String, String> msgs = new HashMap<>();
        Map<String, Boolean> bools = new HashMap<>();
        Map<String, Double> dbls = new HashMap<>();
        Map<String, List<String>> lsts = new HashMap<>();
        Map<String, Integer> ints = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("mining");

        msgs.put("blacklisted", section.getString("messages.blacklisted"));
        msgs.put("no-permission", section.getString("messages.no-permission"));
        msgs.put("no-individual-permission", section.getString("messages.no-individual-permission"));
        msgs.put("wrong-tool", section.getString("messages.wrong-tool"));
        msgs.put("no-silktouch", section.getString("messages.no-silktouch"));
        msgs.put("not-level-required", section.getString("messages.not-level-required"));
        msgs.put("inventory-full", section.getString("messages.inventory-full"));
        msgs.put("still-break", section.getString("messages.still-break"));
        msgs.put("requirements.permission", section.getString("requirements.permission"));
        msgs.put("requirements.individual-permission", section.getString("requirements.individual-permission"));
        msgs.put("requirements.silktouch", section.getString("requirements.silktouch"));
        msgs.put("requirements.silktouch-level", section.getString("requirements.silktouch-level"));
        msgs.put("requirements.wrong-tool", section.getString("requirements.wrong-tool"));
        msgs.put("not-enough-money", section.getString("messages.not-enough-money"));
        msgs.put("transaction-success", section.getString("messages.transaction-success"));
        msgs.put("out-of-luck", section.getString("messages.out-of-luck"));

        lsts.put("blacklisted-worlds", section.getStringList("blacklisted-worlds"));
        lsts.put("perm-based-chances", section.getStringList("perm-based-chances"));
        lsts.put("tools", section.getStringList("tools"));
        lsts.put("prices", section.getStringList("prices"));

        bools.put("drop-exp", section.getBoolean("drop-exp"));
        bools.put("require-permission", section.getBoolean("require-permission"));
        bools.put("require-silktouch", section.getBoolean("require-silktouch"));
        bools.put("require-silktouch-level", section.getBoolean("require-silktouch-level"));
        bools.put("drop-to-inventory", section.getBoolean("drop-to-inventory"));
        bools.put("still-break", section.getBoolean("still-break"));
        bools.put("require-individual-permission", section.getBoolean("require-individual-permission"));
        bools.put("use-perm-based-chances", section.getBoolean("use-perm-based-chances"));
        bools.put("charge", section.getBoolean("charge"));

        ints.put("required-level", section.getInt("required-level"));

        dbls.put("chance", section.getDouble("chance"));

        messages.put("mining", msgs);
        booleans.put("mining", bools);
        doubles.put("mining", dbls);
        lists.put("mining", lsts);
        integers.put("mining", ints);
    }

    private void explodeSection(FileConfiguration config) {
        Map<String, String> msgs = new HashMap<>();
        Map<String, Boolean> bools = new HashMap<>();
        Map<String, Double> dbls = new HashMap<>();
        Map<String, List<String>> lsts = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("explode");

        bools.put("drop", section.getBoolean("drop"));

        dbls.put("chance", section.getDouble("chance"));

        lsts.put("blacklisted-worlds", section.getStringList("blacklisted-worlds"));

        messages.put("explode", msgs);
        booleans.put("explode", bools);
        doubles.put("explode", dbls);
        lists.put("explode", lsts);
    }

    private void witherSection(FileConfiguration config) {
        Map<String, String> msgs = new HashMap<>();
        Map<String, Boolean> bools = new HashMap<>();
        Map<String, Double> dbls = new HashMap<>();
        Map<String, List<String>> lsts = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("wither");

        bools.put("drop", section.getBoolean("drop", false));

        dbls.put("chance", section.getDouble("chance", 100));

        lsts.put("blacklisted-worlds", section.getStringList("blacklisted-worlds"));

        messages.put("wither", msgs);
        booleans.put("wither", bools);
        doubles.put("wither", dbls);
        lists.put("wither", lsts);
    }

    private void eggsSection(FileConfiguration config) {
        Map<String, String> msgs = new HashMap<>();
        Map<String, Boolean> bools = new HashMap<>();
        Map<String, List<String>> lsts = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("eggs");

        msgs.put("no-permission", section.getString("messages.no-permission"));
        msgs.put("no-individual-permission", section.getString("messages.no-individual-permission"));
        msgs.put("already-type", section.getString("messages.already-type"));
        msgs.put("success", section.getString("messages.success"));
        msgs.put("blacklisted", section.getString("messages.blacklisted"));

        bools.put("require-permission", section.getBoolean("require-permission"));
        bools.put("require-individual-permission", section.getBoolean("require-individual-permission"));

        lsts.put("blacklisted-worlds", section.getStringList("blacklisted-worlds"));

        messages.put("eggs", msgs);
        booleans.put("eggs", bools);
        lists.put("eggs", lsts);

    }

    private void anvilSection(FileConfiguration config) {
        Map<String, String> msgs = new HashMap<>();
        Map<String, Boolean> bools = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("anvil");

        msgs.put("prevented", section.getString("messages.prevented"));

        bools.put("prevent-anvil", section.getBoolean("prevent-anvil"));

        messages.put("anvil", msgs);
        booleans.put("anvil", bools);
    }

    private void globalSection(FileConfiguration config) {
        Map<String, String> msgs = new HashMap<>();
        Map<String, Boolean> bools = new HashMap<>();
        Map<String, List<String>> lsts = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("global");

        msgs.put("name", section.getString("display.name"));

        lsts.put("lore", section.getStringList("display.lore"));

        bools.put("lore-enabled", section.getBoolean("display.lore-enabled"));
        bools.put("backwards-compatibility", section.getBoolean("backwards-compatibility"));
        bools.put("show-available", section.getBoolean("show-available"));

        messages.put("global", msgs);
        lists.put("global", lsts);
        booleans.put("global", bools);
    }

    private void mainSection(FileConfiguration config) {
        Map<String, String> msgs = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("main");

        msgs.put("title", section.getString("help-message.title"));
        msgs.put("give", section.getString("help-message.give"));
        msgs.put("set", section.getString("help-message.set"));
        msgs.put("types", section.getString("help-message.types"));
        msgs.put("reload", section.getString("help-message.reload"));

        messages.put("main", msgs);
    }

    private void giveSection(FileConfiguration config) {
        Map<String, String> msgs = new HashMap<>();
        Map<String, Boolean> bools = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("give");

        msgs.put("no-permission", section.getString("messages.no-permission"));
        msgs.put("player-does-not-exist", section.getString("messages.player-does-not-exist"));
        msgs.put("invalid-type", section.getString("messages.invalid-type"));
        msgs.put("invalid-amount", section.getString("messages.invalid-amount"));
        msgs.put("inventory-full", section.getString("messages.inventory-full"));
        msgs.put("success", section.getString("messages.success"));
        msgs.put("received", section.getString("messages.received"));

        bools.put("require-permission", section.getBoolean("require-permission"));
        bools.put("drop-if-full", section.getBoolean("drop-if-full"));

        messages.put("give", msgs);
        booleans.put("give", bools);
    }

    private void setSection(FileConfiguration config) {
        Map<String, String> msgs = new HashMap<>();
        Map<String, Boolean> bools = new HashMap<>();
        Map<String, List<String>> lsts = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("set");

        msgs.put("no-permission", section.getString("messages.no-permission"));
        msgs.put("no-individual-permission", section.getString("messages.no-individual-permission"));
        msgs.put("invalid-type", section.getString("messages.invalid-type"));
        msgs.put("not-looking-at", section.getString("messages.not-looking-at"));
        msgs.put("already-type", section.getString("messages.already-type"));
        msgs.put("success", section.getString("messages.success"));
        msgs.put("blacklisted", section.getString("messages.blacklisted"));

        bools.put("require-permission", section.getBoolean("require-permission"));
        bools.put("require-individual-permission", section.getBoolean("require-individual-permission"));

        lsts.put("blacklisted-worlds", section.getStringList("blacklisted-worlds"));

        messages.put("set", msgs);
        booleans.put("set", bools);
        lists.put("set", lsts);
    }

    private void typesSection(FileConfiguration config) {
        Map<String, String> msgs = new HashMap<>();
        Map<String, Boolean> bools = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("types");

        msgs.put("no-permission", section.getString("messages.no-permission"));
        msgs.put("title", section.getString("messages.title"));
        msgs.put("entries", section.getString("messages.entries"));

        bools.put("require-permission", section.getBoolean("require-permission"));

        messages.put("types", msgs);
        booleans.put("types", bools);
    }

    public void sendMessage(String section, String key, CommandSender sender) {
        if (getMessage(section, key) == null) {
            return;
        }

        String message = getMessage(section, key);
        if (message.equals("") || message.equals(" ")) {
            return;
        }

        sender.sendMessage(message);
    }

    public void sendMessage(String section, String key, Player player) {
        if (getMessage(section, key) == null) {
            return;
        }

        String message = getMessage(section, key);
        if (message.equals("") || message.equals(" ")) {
            return;
        }

        player.sendMessage(message);
    }

    public List<String> getList(String section, String key) {
        return lists.get(section).get(key);
    }

    public String getMessage(String section, String key) {
        return Chat.format(messages.get(section).get(key));
    }

    public boolean getBoolean(String section, String key) {
        return booleans.get(section).get(key);
    }

    public boolean getBooleanOrDefault(String section, String key, boolean defaultValue) {
        try {
            return booleans.get(section).get(key);
        } catch (Exception ignore) {}
        return defaultValue;
    }

    public double getDouble(String section, String key) {
        return doubles.get(section).get(key);
    }

    public int getInteger(String section, String key) {
        return integers.get(section).get(key);
    }
}
