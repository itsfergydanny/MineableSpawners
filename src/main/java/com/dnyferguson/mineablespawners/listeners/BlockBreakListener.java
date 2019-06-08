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
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockBreakListener implements Listener {

    private boolean requirePerm;
    private String noPerm;
    private boolean requireSilk;
    private String noSilk;
    private boolean dropExp;
    private boolean dropInInventory;
    private String inventoryFull;
    private boolean stillBreak;
    private String stillBreakMsg;
    private String itemName;

    public BlockBreakListener(MineableSpawners plugin) {
        FileConfiguration config = plugin.getConfig();
        requirePerm = config.getBoolean("mining.require-permission");
        noPerm = config.getString("mining.no-permission");
        requireSilk = config.getBoolean("mining.require-silktouch");
        noSilk = config.getString("mining.no-silktouch");
        dropExp = config.getBoolean("mining.drop-exp");
        dropInInventory = config.getBoolean("mining.drop-in-inventory");
        inventoryFull = config.getString("mining.inventory-full");
        stillBreak = config.getBoolean("mining.still-break");
        stillBreakMsg = config.getString("mining.still-break-message");
        itemName = config.getString("item-name");
    }

    @EventHandler (ignoreCancelled = true)
    public void onSpawnerMine(BlockBreakEvent e) {
        Block block = e.getBlock();
        Material material = block.getType();

        if (material != Material.SPAWNER) {
            return;
        }

        Player player = e.getPlayer();

        if (!dropExp) {
            e.setExpToDrop(0);
        }

        if (requirePerm) {
            if (!player.hasPermission("mineablespawners.break")) {
                if (!stillBreak) {
                    e.setCancelled(true);
                    player.sendMessage(Chat.format(noPerm));
                }
                player.sendMessage(Chat.format(stillBreakMsg));
                return;
            }
        }

        if (requireSilk) {
            if (!player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH) && !player.hasPermission("mineablespawners.nosilk")) {
                if (!stillBreak) {
                    e.setCancelled(true);
                    player.sendMessage(Chat.format(noSilk));
                }
                player.sendMessage(Chat.format(stillBreakMsg));
                return;
            }
        }

        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        String mob = spawner.getSpawnedType().toString().replace("_", " ");
        meta.setDisplayName(Chat.format(itemName.replace("%mob%", mob.substring(0, 1).toUpperCase() + mob.substring(1).toLowerCase())));
        item.setItemMeta(meta);

        if (dropInInventory) {
            if (player.getInventory().firstEmpty() == -1) {
                e.setCancelled(true);
                player.sendMessage(Chat.format(inventoryFull));
                return;
            }
            player.getInventory().addItem(item);
            block.getDrops().clear();
        }

        Location loc = block.getLocation();
        loc.getWorld().dropItemNaturally(loc, item);
    }
}
