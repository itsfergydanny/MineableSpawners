package net.momentonetwork.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SpawnerPlaceListener implements Listener {

    @EventHandler
    public void onSpawnerPlace(BlockPlaceEvent e) {

        Player player = e.getPlayer();
        Block block = e.getBlock();
        Material material = block.getType();

        if (material == Material.SPAWNER) {
            ItemStack placed = e.getItemInHand();
            try {
                EntityType entity = EntityType.valueOf(placed.getLore().toString().split(": §7")[1].split("]")[0].toUpperCase());
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                spawner.setSpawnedType(entity);
                spawner.update();
            } catch (NullPointerException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
