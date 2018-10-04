package net.momentonetwork.listeners;

import net.momentonetwork.MineableSpawners;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EggChangeListener implements Listener {
    // Make a class variable plugin with main class instance
    private MineableSpawners plugin;
    // Class config variable
    private FileConfiguration config;

    // Constructor
    public EggChangeListener(MineableSpawners plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfigInstance();
    }

    @EventHandler
    public void onEggChange(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        String handBlock = e.getMaterial().name();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Material targetBlock = e.getClickedBlock().getType();
            if (targetBlock == Material.SPAWNER && handBlock.contains("SPAWN_EGG")) {
                if (player.hasPermission("mineablespawners.eggchange") || !config.getBoolean("require_permission.egg_change")) {
                    player.sendMessage(ChatColor.GREEN + "You have successfully changed the spawner with your spawn egg.");
                } else {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "Sorry, you may not change spawner types using spawn eggs.");
                }
            }
        }
    }
}
