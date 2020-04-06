package com.dnyferguson.mineablespawners.commands;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SetSubCommand {

    public SetSubCommand() {}

    public void execute(MineableSpawners plugin, CommandSender sender, String type) {
        if (!(sender instanceof Player)) {
            System.out.println("[MineableSpawners] Only players can run this command!");
            return;
        }

        Player player = (Player) sender;

        if (plugin.getConfigurationHandler().getList("set", "blacklisted-worlds").contains(player.getWorld().getName())) {
            player.sendMessage(plugin.getConfigurationHandler().getMessage("set", "blacklisted"));
            return;
        }

        EntityType entityType;
        try {
            entityType = EntityType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(plugin.getConfigurationHandler().getMessage("set", "invalid-type"));
            return;
        }

        if (plugin.getConfigurationHandler().getBoolean("set", "require-individual-permission")) {
            if (!player.hasPermission("mineablespawners.set." + type.toLowerCase())) {
                player.sendMessage(plugin.getConfigurationHandler().getMessage("set", "no-individual-permission"));
                return;
            }
        }

        Block target = player.getTargetBlock(null, 5);

        if (target.getState().getBlock().getType() != Material.SPAWNER) {
            player.sendMessage(plugin.getConfigurationHandler().getMessage("set", "not-looking-at"));
            return;
        }

        CreatureSpawner spawner = (CreatureSpawner) target.getState();

        String from = Chat.uppercaseStartingLetters(spawner.getSpawnedType().name());
        String to = Chat.uppercaseStartingLetters(type);
        if (from.equals(to)) {
            player.sendMessage(plugin.getConfigurationHandler().getMessage("set", "already-type"));
            return;
        }

        spawner.setSpawnedType(entityType);
        spawner.update();

        player.sendMessage(plugin.getConfigurationHandler().getMessage("set", "success").replace("%from%", from).replace("%to%", to));
    }
}
