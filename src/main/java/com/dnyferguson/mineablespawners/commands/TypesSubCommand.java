package com.dnyferguson.mineablespawners.commands;

import com.dnyferguson.mineablespawners.MineableSpawners;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

public class TypesSubCommand {

    public TypesSubCommand() {}

    public void execute(MineableSpawners plugin, CommandSender sender) {
        StringBuilder msg = new StringBuilder(plugin.getConfigurationHandler().getMessage("types", "title"));
        for (EntityType entityType : EntityType.values()) {
            msg.append(plugin.getConfigurationHandler().getMessage("types", "entries").replace("%mob%", entityType.name().toLowerCase()));
        }
        sender.sendMessage(msg.toString());
    }
}
