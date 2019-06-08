package com.dnyferguson.mineablespawners.commands;

import com.dnyferguson.mineablespawners.MineableSpawners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class SpawnerGiveCommand implements CommandExecutor {

    private FileConfiguration config;

    public SpawnerGiveCommand(MineableSpawners plugin) {
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("mineablespawners.give") || !config.getBoolean("require_permission.spawnergive_command")) {
            if (args.length == 3) {
                Player target = (Player) Bukkit.getPlayer(args[0]);
                String type = args[1].toString().toLowerCase();
                int amount = Integer.valueOf(args[2]);
                if (target.getInventory().firstEmpty() != -1) {
                    ItemStack item = new ItemStack(Material.SPAWNER);
                    ItemMeta meta = item.getItemMeta();
                    item.setAmount(amount);
                    meta.setLore(Collections.singletonList(ChatColor.YELLOW + "type: " + ChatColor.GRAY + type));
                    item.setItemMeta(meta);
                    target.getInventory().addItem(item);
                    sender.sendMessage(ChatColor.GREEN + "Gave " + target.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " " + amount + "x " + type + " spawners!");
                    target.sendMessage(ChatColor.GREEN + "Received " + amount + "x " + type + " spawners!");
                } else {
                    sender.sendMessage(ChatColor.RED + "The player " + target.getDisplayName() + ChatColor.RED + " doesn't have enough inventory space!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid format, please use: /spawnergive <player> <spawner type> <# of spawners>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Sorry, you don't have permission to do this!");
        }
        return false;
    }
}
