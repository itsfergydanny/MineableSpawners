package com.dnyferguson.mineablespawners.commands;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import com.dnyferguson.mineablespawners.utils.XMaterial;
import org.bukkit.Bukkit;
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
        if (target == null || targetPlayer == null) {
            plugin.getConfigurationHandler().sendMessage("give", "player-does-not-exist", sender);
            return;
        }

        EntityType entityType;
        try {
            entityType = EntityType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getConfigurationHandler().sendMessage("give", "invalid-type", sender);
            return;
        }

        int amount = 0;
        try {
            amount = Integer.parseInt(amt);
        } catch (NumberFormatException e) {
            plugin.getConfigurationHandler().sendMessage("give", "invalid-amount", sender);
            return;
        }

        if (targetPlayer.getInventory().firstEmpty() == -1) {
            plugin.getConfigurationHandler().sendMessage("give", "inventory-full", sender);
            return;
        }

        ItemStack item = new ItemStack(XMaterial.SPAWNER.parseMaterial());
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
        plugin.getConfigurationHandler().getMessage("give", "success").replace("%mob%", mobFormatted).replace("%target%", targetPlayer.getName()).replace("%amount%", amount + "");
        targetPlayer.sendMessage(plugin.getConfigurationHandler().getMessage("give", "received").replace("%mob%", mobFormatted).replace("%target%", targetPlayer.getName()).replace("%amount%", amount + ""));
    }
}
