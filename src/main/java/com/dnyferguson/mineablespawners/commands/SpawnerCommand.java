package com.dnyferguson.mineablespawners.commands;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnerCommand implements CommandExecutor {

    private boolean requirePerm;
    private String noPerm;
    private String wrongCommand;
    private boolean requireIndividualPerm;
    private String noIndividualPerm;
    private String notLookingAt;
    private String success;
    private String invalidType;
    private String alreadyType;

    public SpawnerCommand(MineableSpawners plugin) {
        FileConfiguration config = plugin.getConfig();
        requirePerm = config.getBoolean("spawner.require-permission");
        noPerm = config.getString("spawner.no-permission");
        wrongCommand = config.getString("spawner.wrong-command");
        requireIndividualPerm = config.getBoolean("spawner.require-individual-permission");
        noIndividualPerm = config.getString("spawner.no-individual-permission");
        notLookingAt = config.getString("spawner.not-looking-at-spawner");
        success = config.getString("spawner.success");
        invalidType = config.getString("spawner.invalid-type");
        if (!requirePerm) {
            requireIndividualPerm = false;
        }
        alreadyType = config.getString("spawner.already-this-type");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("[MineableSpawners] Only players can run this command!");
            return true;
        }

        Player player = (Player) sender;

        if (requirePerm) {
            if (!player.hasPermission("mineablespawners.spawner")) {
                player.sendMessage(Chat.format(noPerm));
                return true;
            }
        }

        if (args.length < 1) {
            player.sendMessage(Chat.format(wrongCommand));
            return true;
        }

        EntityType type = null;
        try {
            type = EntityType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            StringBuilder str = new StringBuilder();
            for (EntityType entity : EntityType.values()) {
                str.append(entity.name().toLowerCase());
                str.append(", ");
            }
            player.sendMessage(Chat.format(invalidType.replace("%mobs%", str.toString())));
            return true;
        }

        if (requireIndividualPerm) {
            if (!player.hasPermission("mineablespawners.spawner." + args[0].toLowerCase())) {
                player.sendMessage(Chat.format(noIndividualPerm));
                return true;
            }
        }

        Block target = player.getTargetBlock(null, 5);

        if (target.getState().getBlock().getType() != Material.SPAWNER) {
            player.sendMessage(Chat.format(notLookingAt));
            return true;
        }

        CreatureSpawner spawner = (CreatureSpawner) target.getState();
        spawner.setSpawnedType(type);
        spawner.update();
        String from = spawner.getSpawnedType().toString().replace("_", " ").toLowerCase();
        String to = args[0].replace("_", " ").toLowerCase();
        if (from.equals(to)) {
            player.sendMessage(Chat.format(alreadyType));
            return true;
        }
        player.sendMessage(Chat.format(success.replace("%from%", from).replace("%to%", to)));
        return true;
    }
}
