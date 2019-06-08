package com.dnyferguson.mineablespawners.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerPlaceListener implements Listener {

    @EventHandler
    public void onSpawnerPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Material material = block.getType();

        if (material != Material.SPAWNER) {
            return;
        }

        ItemStack placed = e.getItemInHand();
        ItemMeta meta = placed.getItemMeta();
        try {
            String entityName = ChatColor.stripColor(meta.getDisplayName()).split(" Spawner")[0].replace("[", "").replace(" ", "_").toUpperCase();
            EntityType entity = EntityType.valueOf(entityName);
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            spawner.setSpawnedType(entity);
            spawner.update();
        } catch (NullPointerException ex) {
            System.out.println("[MineableSpawners] An error occured while placing a spawner. Please contact the author!");
        }
    }
}
