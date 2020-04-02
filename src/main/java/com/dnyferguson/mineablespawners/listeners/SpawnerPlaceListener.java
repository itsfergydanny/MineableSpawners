package com.dnyferguson.mineablespawners.listeners;

import com.dnyferguson.mineablespawners.MineableSpawners;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerPlaceListener implements Listener {
    private MineableSpawners plugin;
    private boolean compatibility;
    private boolean log;

    public SpawnerPlaceListener(MineableSpawners plugin) {
        this.plugin = plugin;
        compatibility = plugin.getConfig().getBoolean("enable-compatibility");
        log = plugin.getConfig().getBoolean("log");
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSpawnerPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Material material = block.getType();

        if (material != Material.SPAWNER) {
            return;
        }

        ItemStack placed = e.getItemInHand();
        ItemMeta meta = placed.getItemMeta();

//        boolean found = false;
//        EntityType entity = null;
//        if (compatibility) {
//            try {
//                entity = EntityType.valueOf(meta.getLore().toString().split(": §7")[1].split("]")[0].toUpperCase());
//                found = true;
//            } catch (NullPointerException|ArrayIndexOutOfBoundsException ignored) {}
//        }

        try {
            EntityType type = plugin.getNmsHandler().getType(placed);
//            String entityName = ChatColor.stripColor(meta.getDisplayName()).split(" Spawner")[0].replace("[", "").replace(" ", "_").toUpperCase();
//            if (!found) {
//                entity = EntityType.valueOf(entityName);
//            }
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            spawner.setSpawnedType(type);
            spawner.update();

            if (log) {
                Location loc = block.getLocation();
                System.out.println("[MineableSpawners] Player " + e.getPlayer().getName() + " placed a " + type.name().toLowerCase() + " spawner at x:" + loc.getX() + ", y:" + loc.getY() + ", z:" + loc.getZ() + " (" + loc.getWorld().getName() + ")");
            }
        } catch (NullPointerException|IllegalArgumentException ignored) {}
    }
}
