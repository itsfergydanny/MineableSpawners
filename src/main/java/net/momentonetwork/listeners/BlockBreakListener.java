package net.momentonetwork.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onSpawnerMine(BlockBreakEvent e) {

        Player player = e.getPlayer();
        Block block = e.getBlock();
        Material material = block.getType();

        if (material == Material.SPAWNER) {

            CreatureSpawner spawner = (CreatureSpawner) block.getState();

            if (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {

                // Cancel exp drop
                e.setExpToDrop(0);

                // Bring block straight to inv
                if (player.getInventory().firstEmpty() != -1) {
                    ItemStack item = new ItemStack(material);
                    item.setLore(Collections.singletonList(ChatColor.YELLOW + "type: " + ChatColor.GRAY + spawner.getSpawnedType().toString().toLowerCase()));
                    player.getInventory().addItem(item);
                    block.getDrops().clear();
                } else {
                    player.sendMessage(ChatColor.RED + "Please clear up some inventory space before mining a spawner!");
                    e.setCancelled(true);
                }


            } else {
                player.sendMessage(ChatColor.RED + "You must use a pickaxe with silk touch to mine a spawner!");
                e.setCancelled(true);
            }
        }
    }
}
