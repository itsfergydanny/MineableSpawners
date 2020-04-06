package com.dnyferguson.mineablespawners.listeners;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class SpawnerMineListener implements Listener {
    private MineableSpawners plugin;
    private Set<Location> minedSpawners = new HashSet<>();
    private Map<String, Double> permissionChances = new HashMap<>();

    public SpawnerMineListener(MineableSpawners plugin) {
        this.plugin = plugin;
        for (String line : plugin.getConfigurationHandler().getList("mining", "perm-based-chances")) {
            String[] args = line.split(":");
            try {
                String permission = args[0];
                double chance = Double.parseDouble(args[1]);
                permissionChances.put(permission, chance);
            } catch (Exception ignore) {}
        }
    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpawnerMine(BlockBreakEvent e) {
        try {
            Block block = e.getBlock();
            Location loc = block.getLocation();
            Material material = block.getType();

            if (!material.equals(Material.SPAWNER)) {
                return;
            }

            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            String type = spawner.getSpawnedType().name();

            Player player = e.getPlayer();

            if (!plugin.getConfigurationHandler().getBoolean("mining", "drop-exp") || minedSpawners.contains(loc)) {
                e.setExpToDrop(0);
            }

            if (player.getGameMode().equals(GameMode.CREATIVE) || player.hasPermission("mineablespawners.bypass")) {
                handleGivingSpawner(e, material, spawner, loc, player, block);
                return;
            }

            if (plugin.getConfigurationHandler().getList("mining", "blacklisted-worlds").contains(player.getWorld().getName())) {
                player.sendMessage(plugin.getConfigurationHandler().getMessage("mining", "blacklisted"));
                return;
            }

            Material tool = player.getInventory().getItemInMainHand().getType();
            System.out.println("DEBUG: tool name = " + tool.name());
            if (!plugin.getConfigurationHandler().getList("mining", "tools").contains(tool.name())) {
                handleStillBreak(e, player, plugin.getConfigurationHandler().getMessage("mining", "wrong-tool"), plugin.getConfigurationHandler().getMessage("mining", "requirements.wrong-tool"));
                return;
            }

            if (plugin.getConfigurationHandler().getBoolean("mining", "require-permission")) {
                if (!player.hasPermission("mineablespawners.mine")) {
                    handleStillBreak(e, player, plugin.getConfigurationHandler().getMessage("mining", "no-permission"), plugin.getConfigurationHandler().getMessage("mining", "requirements.permission"));
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
                        handleStillBreak(e, player, plugin.getConfigurationHandler().getMessage("mining", "not-level-required"), plugin.getConfigurationHandler().getMessage("mining", "requirements.silktouch-level"));
                        return;
                    }
                } else {
                    if (silkTouchLevel != 1) {
                        handleStillBreak(e, player, plugin.getConfigurationHandler().getMessage("mining", "no-silktouch"), plugin.getConfigurationHandler().getMessage("mining", "requirements.silktouch"));
                        return;
                    }
                }
            }

            if (plugin.getConfigurationHandler().getBoolean("mining", "require-individual-permission")) {
                if (!player.hasPermission("mineablespawners.mine." + type.toLowerCase())) {
                    System.out.println("DEBUG 1 = " + plugin.getConfigurationHandler().getMessage("mining", "no-individual-permission"));
                    System.out.println("DEBUG 2 = " + plugin.getConfigurationHandler().getMessage("mining", "requirements.individual-permission"));
                    handleStillBreak(e, player, plugin.getConfigurationHandler().getMessage("mining", "no-individual-permission"), plugin.getConfigurationHandler().getMessage("mining", "requirements.individual-permission"));
                    return;
                }
            }

            double dropChance = 0;
            if (plugin.getConfigurationHandler().getBoolean("mining", "use-perm-based-chances") && permissionChances.size() > 0) {
                for (String perm : permissionChances.keySet()) {
                    if (player.hasPermission(perm)) {
                        dropChance = permissionChances.get(perm)/100;
                        break;
                    }
                }
            } else {
                dropChance = plugin.getConfigurationHandler().getDouble("mining", "chance")/100;
                if (dropChance != 1) {
                    double random = Math.random();
                    if (random >= dropChance) {
                        return;
                    }
                }
            }

            double random = Math.random();
            System.out.println("DEBUG: random = " + random + ", DROP CHANCE = " + dropChance);
            if (random >= dropChance) {
                return;
            }

            handleGivingSpawner(e, material, spawner, loc, player, block);
        } catch (Exception ex) {
            ex.printStackTrace();
            e.setCancelled(true);
        }
    }

    private void handleGivingSpawner(BlockBreakEvent e, Material material, CreatureSpawner spawner, Location loc, Player player, Block block) {
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

        minedSpawners.add(loc);

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
