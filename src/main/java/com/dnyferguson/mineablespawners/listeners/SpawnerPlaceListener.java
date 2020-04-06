package com.dnyferguson.mineablespawners.listeners;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import com.dnyferguson.mineablespawners.utils.XMaterial;
import net.milkbowl.vault.economy.EconomyResponse;
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
    private MineableSpawners plugin;
    private Map<EntityType, Double> prices = new HashMap<>();
    private boolean allSamePrice = false;
    private double globalPrice = 0;
    private DecimalFormat df = new DecimalFormat("##.##");

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
                System.out.println("[MineableSpawners] Error with placing price \"" + line + "\"");
            }
        }
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSpawnerPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Material material = block.getType();

        if (material != XMaterial.SPAWNER.parseMaterial()) {
            return;
        }

        Player player = e.getPlayer();
        boolean bypassing = player.getGameMode().equals(GameMode.CREATIVE) || player.hasPermission("mineablespawners.bypass");

        if (plugin.getConfigurationHandler().getList("placing", "blacklisted-worlds").contains(player.getWorld().getName())) {
            if (!bypassing) {
                player.sendMessage(plugin.getConfigurationHandler().getMessage("placing", "blacklisted"));
                e.setCancelled(true);
                return;
            }
        }

        ItemStack placed = e.getItemInHand();

        try {
            EntityType type = plugin.getNmsHandler().getType(placed);

            if (bypassing || plugin.getEcon() == null) {
                handlePlacement(e, block, type, false, 0);
                return;
            }

            if (!plugin.getConfigurationHandler().getBoolean("placing", "charge")) {
                handlePlacement(e, block, type, false, 0);
                return;
            }

            if (allSamePrice) {
                if (!plugin.getEcon().withdrawPlayer(player, globalPrice).transactionSuccess()) {
                    String missing = df.format(globalPrice - plugin.getEcon().getBalance(player));
                    player.sendMessage(plugin.getConfigurationHandler().getMessage("placing", "not-enough-money").replace("%missing%", missing).replace("%cost%", globalPrice + ""));
                }

                handlePlacement(e, block, type, true, globalPrice);
                return;
            }

            if (!prices.containsKey(type)) {
                handlePlacement(e, block, type, false, 0);
                return;
            }

            if (!plugin.getEcon().withdrawPlayer(player, prices.get(type)).transactionSuccess()) {
                String missing = df.format(prices.get(type) - plugin.getEcon().getBalance(player));
                player.sendMessage(plugin.getConfigurationHandler().getMessage("placing", "not-enough-money").replace("%missing%", missing).replace("%cost%", prices.get(type).toString()));
                e.setCancelled(true);
                return;
            }

            handlePlacement(e, block, type, true, prices.get(type));
        } catch (NullPointerException|IllegalArgumentException ignored) {}
    }

    private void handlePlacement(BlockPlaceEvent e, Block block, EntityType type, boolean charged, double cost) {
        Player player = e.getPlayer();

        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        spawner.setSpawnedType(type);
        spawner.update();

        if (plugin.getConfigurationHandler().getBoolean("placing", "log")) {
            Location loc = block.getLocation();
            System.out.println("[MineableSpawners] Player " + e.getPlayer().getName() + " placed a " + type.name().toLowerCase() + " spawner at x:" + loc.getX() + ", y:" + loc.getY() + ", z:" + loc.getZ() + " (" + loc.getWorld().getName() + ")");
        }

        if (charged) {
            player.sendMessage(plugin.getConfigurationHandler().getMessage("placing", "transaction-success").replace("%type%", Chat.uppercaseStartingLetters(type.name())).replace("%cost%", df.format(cost).replace("%balance%", df.format(plugin.getEcon().getBalance(player)))));
        }
    }
}
