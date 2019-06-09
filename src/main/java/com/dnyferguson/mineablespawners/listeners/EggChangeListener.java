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

    private boolean requirePerm;
    private String noPerm;
    private String success;
    private String alreadyType;
    private boolean requireIndividualPerm;
    private String noIndividualPerm;

    public EggChangeListener(MineableSpawners plugin) {
        FileConfiguration config = plugin.getConfig();
        requirePerm = config.getBoolean("eggs.require-permission");
        noPerm = config.getString("eggs.no-permission");
        success = config.getString("eggs.success");
        alreadyType = config.getString("eggs.already-this-type");
        requireIndividualPerm = config.getBoolean("eggs.require-individual-permission");
        noIndividualPerm = config.getString("eggs.no-individual-permission");
        if (!requirePerm) {
            requireIndividualPerm = false;
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onEggChange(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player player = e.getPlayer();
        ItemStack itemInHand = e.getItem();
        String itemName = itemInHand.getType().name();
        Material targetBlock = e.getClickedBlock().getType();

        if (targetBlock != Material.SPAWNER || !itemName.contains("SPAWN_EGG")) {
            return;
        }

        if (requirePerm) {
            if (!player.hasPermission("mineablespawners.eggchange")) {
                e.setCancelled(true);
                player.sendMessage(Chat.format(noPerm));
                return;
            }
        }

        String to = itemName.split("_SPAWN_EGG")[0].replace("_", " ").toLowerCase();

        if (requireIndividualPerm) {
            if (!player.hasPermission("mineablespawners.eggchange." + to.replace(" ", "_"))) {
                e.setCancelled(true);
                player.sendMessage(Chat.format(noIndividualPerm));
                return;
            }
        }

        CreatureSpawner spawner = (CreatureSpawner) e.getClickedBlock().getState();
        String from = spawner.getSpawnedType().toString().replace("_", " ").toLowerCase();

        if (from.equals(to)) {
            e.setCancelled(true);
            player.sendMessage(Chat.format(alreadyType));
            return;
        }

        player.sendMessage(Chat.format(success.replace("%from%", from).replace("%to%", to)));
    }
}
