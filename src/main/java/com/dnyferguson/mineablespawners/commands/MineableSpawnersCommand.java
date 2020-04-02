package com.dnyferguson.mineablespawners.commands;

import com.dnyferguson.mineablespawners.MineableSpawners;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class MineableSpawnersCommand implements CommandExecutor {
    private MineableSpawners plugin;
    private GiveSubCommand giveSubCommand;
    private boolean giveCommandRequiresPerm;
    private boolean setCommandRequiresPerm;

    public MineableSpawnersCommand(MineableSpawners plugin) {
        this.plugin = plugin;
        giveSubCommand = new GiveSubCommand(plugin);

        FileConfiguration config = plugin.getConfig();
        giveCommandRequiresPerm = config.getBoolean("give.require-permission");
        setCommandRequiresPerm = config.getBoolean("set.require-permission");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sendHelpMessage(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("give") && args.length == 4) {
            if (!giveCommandRequiresPerm) {
                giveSubCommand.execute(plugin, sender, args[1], args[2], args[3]);
                return true;
            }

            if (!sender.hasPermission("mineablespawners.give")) {
                sender.sendMessage(plugin.getConfigurationHandler().getMessage("give", "no-permission"));
                return true;
            }

            giveSubCommand.execute(plugin, sender, args[1], args[2], args[3]););
            return true;
        }

        if (subCommand.equals("set") && args.length == 2) {

        }

        if (subCommand.equals("types") && args.length == 1) {

        }
        sendHelpMessage(sender);
        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        StringBuilder msg = new StringBuilder(plugin.getConfigurationHandler().getMessage("main", "title")).append("\n");
        if (giveCommandRequiresPerm && sender.hasPermission("mineablespawners.give")) {
            msg.append(plugin.getConfigurationHandler().getMessage("main", "give"));
            msg.append("\n");
        }
        if (setCommandRequiresPerm && sender.hasPermission("mineablespawners.set")) {
            msg.append(plugin.getConfigurationHandler().getMessage("main", "set"));
            msg.append("\n");
        }
        msg.append(plugin.getConfigurationHandler().getMessage("main", "types"));
        sender.sendMessage(msg.toString());
    }
}
