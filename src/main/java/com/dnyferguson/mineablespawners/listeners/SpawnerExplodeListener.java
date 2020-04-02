package com.dnyferguson.mineablespawners.listeners;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpawnerExplodeListener implements Listener {
    private MineableSpawners plugin;
    private boolean dropOnExplode;
    private double dropChance;
    private String displayName;
    private List<String> lore;
    private boolean enableLore;


    public SpawnerExplodeListener(MineableSpawners plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        dropOnExplode = config.getBoolean("mining.drop-on-explode");
        dropChance = config.getDouble("mining.drop-on-explode-chance")/100;
        displayName = config.getString("displayname");
        lore = config.getStringList("lore");
        enableLore = config.getBoolean("enable-lore");
    }

    @EventHandler (ignoreCancelled = true)
    public void onSpawnerExplode(EntityExplodeEvent e) {
        if (!dropOnExplode) {
            return;
        }

        for (Block block : e.blockList()) {
            if (!block.getType().equals(Material.SPAWNER)) {
                continue;
            }

            if (dropChance != 1) {
                double random = Math.random();
                if (random >= dropChance) {
                    return;
                }
            }

            CreatureSpawner spawner = (CreatureSpawner) block.getState();

            ItemStack item = new ItemStack(Material.SPAWNER);
            ItemMeta meta = item.getItemMeta();
            String mobFormatted = Chat.uppercaseStartingLetters(spawner.getSpawnedType().toString());

            meta.setDisplayName(Chat.format(displayName.replace("%mob%", mobFormatted)));
            List<String> newLore = new ArrayList<>();
            if (lore != null && enableLore) {
                for (String line : lore) {
                    newLore.add(Chat.format(line).replace("%mob%", mobFormatted));
                }
                meta.setLore(newLore);
            }
            item.setItemMeta(meta);

            item = plugin.getNmsHandler().setType(item, spawner.getSpawnedType());

            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), item);
        }
    }
}
