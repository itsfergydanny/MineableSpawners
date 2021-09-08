package com.dnyferguson.mineablespawners.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class SpawnerPlaceListener implements Listener {
    private final MineableSpawners plugin;
    private final Map<EntityType, Double> prices = new HashMap<>();
    private boolean allSamePrice = false;
    private double globalPrice = 0;
    private final DecimalFormat df = new DecimalFormat("##.##");

    public SpawnerPlaceListener(MineableSpawners plugin) {
        this.plugin = plugin;
        for (String line : plugin.getConfigurationHandler().getList("placing", "prices")) {
            try {
                String[] args = line.split(":");
                if (args[0].equalsIgnoreCase("all")) {
                    allSamePrice = true;
                    globalPrice = Double.parseDouble(args[1]);
                    break;
                }
                EntityType type = EntityType.valueOf(args[0].toUpperCase());
                double price = Double.parseDouble(args[1]);
                prices.put(type, price);
            } catch (Exception ignore) {
                plugin.getLogger().info("Error with placing price \"" + line + "\"");
            }
        }
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSpawnerPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Material material = block.getType();

        // check if item is spawner
        if (material != XMaterial.SPAWNER.parseMaterial()) {
            return;
        }

        // check if valid entity type
        ItemStack item = e.getItemInHand();
        EntityType entityType = MineableSpawners.getApi().getEntityTypeFromItemStack(item);
        if (entityType == null) {
            return;
        }

        // check if user is bypassing
        Player player = e.getPlayer();
        boolean bypassing = player.getGameMode().equals(GameMode.CREATIVE) || player.hasPermission("mineablespawners.bypass");
        if (bypassing) {
            handlePlacement(player, block, entityType, 0);
            return;
        }

        // check if blacklisted world
        if (plugin.getConfigurationHandler().getList("placing", "blacklisted-worlds").contains(player.getWorld().getName())) {
            player.sendMessage(plugin.getConfigurationHandler().getMessage("placing", "blacklisted"));
            e.setCancelled(true);
            return;
        }

        // check if charging/has enough
        double cost = 0;
        if (plugin.getEcon() != null && plugin.getConfigurationHandler().getBoolean("placing", "charge")) {
            if (!allSamePrice && prices.containsKey(entityType)) {
                cost = prices.getOrDefault(entityType, 0.0);
            } else {
                cost = globalPrice;
            }

            if (!plugin.getEcon().withdrawPlayer(player, cost).transactionSuccess()) {
                String missing = df.format(cost - plugin.getEcon().getBalance(player));
                player.sendMessage(plugin.getConfigurationHandler().getMessage("placing", "not-enough-money").replace("%missing%", missing).replace("%cost%", cost + ""));
                e.setCancelled(true);
                return;
            }
        }

        // place
        handlePlacement(player, block, entityType, cost);
    }

    private void handlePlacement(Player player, Block block, EntityType type, double cost) {
        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        spawner.setSpawnedType(type);
        spawner.update();

        if (plugin.getConfigurationHandler().getBoolean("placing", "log")) {
            Location loc = block.getLocation();
            plugin.getLogger().info("Player " + player.getName() + " placed a " + type.name().toLowerCase() + " spawner at x:" + loc.getX() + ", y:" + loc.getY() + ", z:" + loc.getZ() + " (" + loc.getWorld().getName() + ")");
        }

        if (cost > 0) {
            player.sendMessage(plugin.getConfigurationHandler().getMessage("placing", "transaction-success").replace("%type%", Chat.uppercaseStartingLetters(type.name())).replace("%cost%", df.format(cost).replace("%balance%", df.format(plugin.getEcon().getBalance(player)))));
        }
    }
}
