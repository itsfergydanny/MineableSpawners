package com.dnyferguson.mineablespawners.commands;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpawnerGiveCommand implements CommandExecutor {

    private boolean requirePerm;
    private String noPerm;
    private String wrongCommand;
    private String noExists;
    private String wrongAmount;
    private String inventoryFull;
    private String invalidType;
    private String mobNameColor;
    private List<String> lore;
    private boolean enableLore;
    private String successSender;
    private String successTarget;

    public SpawnerGiveCommand(MineableSpawners plugin) {
        FileConfiguration config = plugin.getConfig();
        requirePerm = config.getBoolean("spawnergive.require-permission");
        noPerm = config.getString("spawnergive.no-permission");
        wrongCommand = config.getString("spawnergive.wrong-command");
        noExists = config.getString("spawnergive.no-exists");
        wrongAmount = config.getString("spawnergive.wrong-amount");
        inventoryFull = config.getString("spawnergive.insufficient-space");
        invalidType = config.getString("spawnergive.invalid-type");
        mobNameColor = config.getString("mob-name-color");
        lore = config.getStringList("lore");
        enableLore = config.getBoolean("enable-lore");
        successSender = config.getString("spawnergive.success-sender");
        successTarget = config.getString("spawnergive.success-target");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (requirePerm) {
            if (!sender.hasPermission("mineablespawners.give")) {
                sender.sendMessage(Chat.format(noPerm));
                return true;
            }
        }

        if (args.length != 3) {
            sender.sendMessage(Chat.format(wrongCommand));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Chat.format(noExists));
            return true;
        }

        String type = args[1].toLowerCase();
        try {
            EntityType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            StringBuilder str = new StringBuilder();
            for (EntityType entity : EntityType.values()) {
                str.append(entity.name().toLowerCase());
                str.append(", ");
            }
            sender.sendMessage(Chat.format(invalidType.replace("%mobs%", str.toString())));
            return true;
        }

        int amount = 0;
        try {
            amount = Integer.valueOf(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Chat.format(wrongAmount));
            return true;
        }

        if (target.getInventory().firstEmpty() == -1) {
            sender.sendMessage(Chat.format(inventoryFull));
            return true;
        }

        ItemStack item = new ItemStack(Material.SPAWNER);
        ItemMeta meta = item.getItemMeta();
        item.setAmount(amount);

        String mobFormatted = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
        meta.setDisplayName(Chat.format("&8[" + mobNameColor + "%mob% &7Spawner&8]".replace("%mob%", mobFormatted)));
        List<String> newLore = new ArrayList<>();
        if (lore != null && enableLore) {
            for (String line : lore) {
                newLore.add(Chat.format(line).replace("%mob%", mobFormatted));
            }
            meta.setLore(newLore);
        }

        item.setItemMeta(meta);

        target.getInventory().addItem(item);
        sender.sendMessage(Chat.format(successSender.replace("%mob%", mobFormatted).replace("%target%", target.getName()).replace("%amount%", amount + "")));
        target.sendMessage(Chat.format(successTarget.replace("%mob%", mobFormatted).replace("%amount%", amount + "")));
        return true;
    }
}
