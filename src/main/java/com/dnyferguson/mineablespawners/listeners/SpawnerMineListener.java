package com.dnyferguson.mineablespawners.listeners;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpawnerMineListener implements Listener {
    private MineableSpawners plugin;

    public SpawnerMineListener(MineableSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onSpawnerMine(BlockBreakEvent e) {
        Block block = e.getBlock();
        Material material = block.getType();

        if (material != Material.SPAWNER || e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();

        if (plugin.getConfigurationHandler().getList("mining", "blacklisted-worlds").contains(player.getWorld().getName())) {
            player.sendMessage(plugin.getConfigurationHandler().getMessage("mining", "blacklisted"));
            return;
        }

        if (!plugin.getConfigurationHandler().getBoolean("mining", "drop-exp")) {
            e.setExpToDrop(0);
        }

        if (plugin.getConfigurationHandler().getBoolean("mining", "require-permission")) {
            if (!player.hasPermission("mineablespawners.mine")) {
                handleStillBreak(e, player, plugin.getConfigurationHandler().getMessage("mining", "no-permission"), plugin.getConfigurationHandler().getMessage("minings", "requirements.permission"));
                return;
            }
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (plugin.getConfigurationHandler().getBoolean("mining", "require-silktouch") && !player.hasPermission("mineablespawners.nosilk")) {
            int silkTouchLevel = 0;
            if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH)) {
                silkTouchLevel = itemInHand.getEnchantmentLevel(Enchantment.SILK_TOUCH);
            }
            if (plugin.getConfigurationHandler().getBoolean("mining", "require-silktouch-level")) {
                int requiredLevel = plugin.getConfigurationHandler().getInteger("mining", "required-level");
                if (silkTouchLevel < requiredLevel) {
                    handleStillBreak(e, player, plugin.getConfigurationHandler().getMessage("mining", "not-level-required"), plugin.getConfigurationHandler().getMessage("minings", "requirements.silktouch-level"));
                    return;
                }
            } else {
                if (silkTouchLevel != 1) {
                    handleStillBreak(e, player, plugin.getConfigurationHandler().getMessage("mining", "no-silktouch"), plugin.getConfigurationHandler().getMessage("minings", "requirements.silktouch"));
                    return;
                }
            }
        }

        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        ItemStack item = new ItemStack(material);
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

        double dropChance = plugin.getConfigurationHandler().getDouble("mining", "chance");

        if (dropChance != 1) {
            double random = Math.random();
            if (random >= dropChance) {
                return;
            }
        }

        if (plugin.getConfigurationHandler().getBoolean("mining", "drop-to-inventory")) {
            if (player.getInventory().firstEmpty() == -1) {
                e.setCancelled(true);
                player.sendMessage(plugin.getConfigurationHandler().getMessage("mining", "inventory-full"));
                return;
            }
            player.getInventory().addItem(item);
            block.getDrops().clear();
            return;
        }

        Location loc = block.getLocation();
        loc.getWorld().dropItemNaturally(loc, item);
    }

    private void handleStillBreak(BlockBreakEvent e, Player player, String msg, String reason) {
        if (!plugin.getConfigurationHandler().getBoolean("mining", "still-break")) {
            e.setCancelled(true);
            player.sendMessage(Chat.format(msg));
            return;
        }

        if (msg.length() > 0) {
            player.sendMessage(plugin.getConfigurationHandler().getMessage("mining", "still-break").replace("%requirement%", reason));
        }
    }
}
