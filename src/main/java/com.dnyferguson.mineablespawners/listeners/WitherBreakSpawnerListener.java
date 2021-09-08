package com.dnyferguson.mineablespawners.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class WitherBreakSpawnerListener implements Listener {
    private MineableSpawners plugin;

    public WitherBreakSpawnerListener(MineableSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onWitherBlockChange(EntityChangeBlockEvent event) {
        if (!event.getEntity().getType().equals(EntityType.WITHER)) {
            return;
        }

        if (event.getBlock().getLocation().getWorld() != null && plugin.getConfigurationHandler().getList("wither", "blacklisted-worlds").contains(event.getBlock().getWorld().getName())) {
            return;
        }

        if (!plugin.getConfigurationHandler().getBooleanOrDefault("wither", "drop", false)) {
            return;
        }

        Block block = event.getBlock();

        if (!block.getType().equals(XMaterial.SPAWNER.parseMaterial())) {
            return;
        }

        double dropChance = plugin.getConfigurationHandler().getDoubleOrDefault("wither", "chance", 100)/100;

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

        if (meta != null) {
            meta.setDisplayName(plugin.getConfigurationHandler().getMessage("global", "name").replace("%mob%", mobFormatted));
            List<String> newLore = new ArrayList<>();
            if (plugin.getConfigurationHandler().getList("global", "lore") != null && plugin.getConfigurationHandler().getBoolean("global", "lore-enabled")) {
                for (String line : plugin.getConfigurationHandler().getList("global", "lore")) {
                    newLore.add(Chat.format(line).replace("%mob%", mobFormatted));
                }
                meta.setLore(newLore);
            }
            item.setItemMeta(meta);
        }

        NBTItem nbti = new NBTItem(item);
        nbti.setString("ms_mob", spawner.getSpawnedType().name());

        item = nbti.getItem();

        if (block.getLocation().getWorld() != null) {
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), item);
        }
    }
}
