package com.dnyferguson.mineablespawners.listeners;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EggChangeListener implements Listener {
    private MineableSpawners plugin;

    public EggChangeListener(MineableSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler (ignoreCancelled = true)
    public void onEggChange(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player player = e.getPlayer();
        ItemStack itemInHand = e.getItem();

        if (itemInHand == null || itemInHand.getType().equals(Material.AIR)) {
            return;
        }

        String itemName = itemInHand.getType().name();
        Material targetBlock = e.getClickedBlock().getType();

        if (targetBlock != Material.SPAWNER || !itemName.contains("SPAWN_EGG")) {
            return;
        }

        if (plugin.getConfigurationHandler().getList("eggs", "blacklisted-worlds").contains(player.getWorld().getName())) {
            player.sendMessage(plugin.getConfigurationHandler().getMessage("eggs", "blacklisted"));
            return;
        }

        if (plugin.getConfigurationHandler().getBoolean("eggs", "require-permission")) {
            if (!player.hasPermission("mineablespawners.eggchange")) {
                e.setCancelled(true);
                player.sendMessage(plugin.getConfigurationHandler().getMessage("eggs", "no-permission"));
                return;
            }
        }

        String to = itemName.split("_SPAWN_EGG")[0].replace("_", " ").toLowerCase();

        if (plugin.getConfigurationHandler().getBoolean("eggs", "require-individual-permission")) {
            if (!player.hasPermission("mineablespawners.eggchange." + to.replace(" ", "_"))) {
                e.setCancelled(true);
                player.sendMessage(plugin.getConfigurationHandler().getMessage("eggs", "no-individual-permission"));
                return;
            }
        }

        CreatureSpawner spawner = (CreatureSpawner) e.getClickedBlock().getState();
        String from = spawner.getSpawnedType().toString().replace("_", " ").toLowerCase();

        if (from.equals(to)) {
            e.setCancelled(true);
            player.sendMessage(plugin.getConfigurationHandler().getMessage("eggs", "already-type"));
            return;
        }

        player.sendMessage(plugin.getConfigurationHandler().getMessage("eggs", "success").replace("%from%", from).replace("%to%", to));
    }
}
