package com.dnyferguson.mineablespawners.listeners;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import com.dnyferguson.mineablespawners.utils.XMaterial;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpawnerExplodeListener implements Listener {
    private MineableSpawners plugin;

    public SpawnerExplodeListener(MineableSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler (ignoreCancelled = true)
    public void onSpawnerExplode(EntityExplodeEvent e) {
        if (!plugin.getConfigurationHandler().getBoolean("explode", "drop")) {
            return;
        }

        for (Block block : e.blockList()) {
            if (!block.getType().equals(XMaterial.SPAWNER.parseMaterial())) {
                continue;
            }

            double dropChance = plugin.getConfigurationHandler().getDouble("explode", "chance")/100;

            if (dropChance != 1) {
                double random = Math.random();
                if (random >= dropChance) {
                    return;
                }
            }

            CreatureSpawner spawner = (CreatureSpawner) block.getState();

            ItemStack item = new ItemStack(XMaterial.SPAWNER.parseMaterial());
            ItemMeta meta = item.getItemMeta();
            String mobFormatted = Chat.uppercaseStartingLetters(spawner.getSpawnedType().toString());

            meta.setDisplayName(plugin.getConfigurationHandler().getMessage("global", "name").replace("%mob%", mobFormatted));
            List<String> newLore = new ArrayList<>();
            if (plugin.getConfigurationHandler().getList("global", "lore") != null && plugin.getConfigurationHandler().getBoolean("global", "lore-enabled")) {
                for (String line : plugin.getConfigurationHandler().getList("global", "lore")) {
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
