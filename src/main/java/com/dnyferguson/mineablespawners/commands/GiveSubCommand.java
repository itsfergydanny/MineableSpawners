package com.dnyferguson.mineablespawners.commands;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GiveSubCommand {

    public GiveSubCommand() {}

    public void execute(MineableSpawners plugin, CommandSender sender, String target, String type, String amt) {
        Player targetPlayer = Bukkit.getPlayer(target);
        if (target == null) {
            sender.sendMessage(plugin.getConfigurationHandler().getMessage("give", "player-does-not-exist"));
            return;
        }

        EntityType entityType;
        try {
            entityType = EntityType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(plugin.getConfigurationHandler().getMessage("give", "invalid-type"));
            return;
        }

        int amount = 0;
        try {
            amount = Integer.parseInt(amt);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getConfigurationHandler().getMessage("give", "invalid-amount"));
            return;
        }

        if (targetPlayer.getInventory().firstEmpty() == -1) {
            sender.sendMessage(plugin.getConfigurationHandler().getMessage("give", "inventory-full"));
            return;
        }

        ItemStack item = new ItemStack(Material.SPAWNER);
        ItemMeta meta = item.getItemMeta();
        item.setAmount(amount);

        String mobFormatted = Chat.uppercaseStartingLetters(entityType.name());
        meta.setDisplayName(Chat.format(plugin.getConfigurationHandler().getMessage("global", "name").replace("%mob%", mobFormatted)));
        List<String> newLore = new ArrayList<>();
        if (plugin.getConfigurationHandler().getList("global", "lore") != null && plugin.getConfigurationHandler().getBoolean("global", "lore-enabled")) {
            for (String line : plugin.getConfigurationHandler().getList("global", "lore")) {
                newLore.add(Chat.format(line).replace("%mob%", mobFormatted));
            }
            meta.setLore(newLore);
        }
        item.setItemMeta(meta);
        item = plugin.getNmsHandler().setType(item, entityType);

        targetPlayer.getInventory().addItem(item);
        sender.sendMessage(plugin.getConfigurationHandler().getMessage("give", "success").replace("%mob%", mobFormatted).replace("%target%", targetPlayer.getName()).replace("%amount%", amount + ""));
        targetPlayer.sendMessage(plugin.getConfigurationHandler().getMessage("give", "received").replace("%mob%", mobFormatted).replace("%target%", targetPlayer.getName()).replace("%amount%", amount + ""));
    }
}
