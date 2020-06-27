package com.dnyferguson.mineablespawners.commands;

import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MineableSpawnersCommand implements CommandExecutor {
    private MineableSpawners plugin;
    private GiveSubCommand giveSubCommand;
    private SetSubCommand setSubCommand;
    private TypesSubCommand typesSubCommand;

    public MineableSpawnersCommand(MineableSpawners plugin) {
        this.plugin = plugin;
        giveSubCommand = new GiveSubCommand();
        setSubCommand = new SetSubCommand();
        typesSubCommand = new TypesSubCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sendHelpMessage(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("give") && args.length == 4) {
            if (!plugin.getConfigurationHandler().getBoolean("give", "require-permission")) {
                giveSubCommand.execute(plugin, sender, args[1], args[2], args[3]);
                return true;
            }

            if (!sender.hasPermission("mineablespawners.give")) {
                plugin.getConfigurationHandler().sendMessage("give", "no-permission", sender);
                return true;
            }

            giveSubCommand.execute(plugin, sender, args[1], args[2], args[3]);
            return true;
        }

        if (subCommand.equals("set") && args.length == 2) {
            if (!plugin.getConfigurationHandler().getBoolean("set", "require-permission")) {
                setSubCommand.execute(plugin, sender, args[1]);
                return true;
            }

            if (!sender.hasPermission("mineablespawners.set")) {
                plugin.getConfigurationHandler().sendMessage("set", "no-permission", sender);
                return true;
            }

            setSubCommand.execute(plugin, sender, args[1]);
            return true;
        }

        if (subCommand.equals("types") && args.length == 1) {
            if (!plugin.getConfigurationHandler().getBoolean("types", "require-permission")) {
                typesSubCommand.execute(plugin, sender);
                return true;
            }

            if (!sender.hasPermission("mineablespawners.types")) {
                plugin.getConfigurationHandler().sendMessage("types", "no-permission", sender);
                return true;
            }

            typesSubCommand.execute(plugin, sender);
            return true;
        }

        if (subCommand.equals("reload") && sender.hasPermission("mineablespawners.reload")) {
            plugin.getConfigurationHandler().reload();
            sender.sendMessage(Chat.format("&e[MineableSpawners] &aYou have successfully reloaded the config!"));
            return true;
        }

        sendHelpMessage(sender);
        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        StringBuilder msg = new StringBuilder(plugin.getConfigurationHandler().getMessage("main", "title")).append("\n \n");
        if (!plugin.getConfigurationHandler().getBoolean("give", "require-permission")) {
            msg.append(plugin.getConfigurationHandler().getMessage("main", "give"));
            msg.append("\n \n");
        }
        if (plugin.getConfigurationHandler().getBoolean("give", "require-permission") && sender.hasPermission("mineablespawners.give")) {
            msg.append(plugin.getConfigurationHandler().getMessage("main", "give"));
            msg.append("\n \n");
        }
        if (!plugin.getConfigurationHandler().getBoolean("set", "require-permission")) {
            msg.append(plugin.getConfigurationHandler().getMessage("main", "set"));
            msg.append("\n \n");
        }
        if (plugin.getConfigurationHandler().getBoolean("set", "require-permission") && sender.hasPermission("mineablespawners.set")) {
            msg.append(plugin.getConfigurationHandler().getMessage("main", "set"));
            msg.append("\n \n");
        }
        if (!plugin.getConfigurationHandler().getBoolean("types", "require-permission")) {
            msg.append(plugin.getConfigurationHandler().getMessage("main", "types"));
            msg.append("\n \n");
        }
        if (plugin.getConfigurationHandler().getBoolean("types", "require-permission") && sender.hasPermission("mineablespawners.types")) {
            msg.append(plugin.getConfigurationHandler().getMessage("main", "types"));
            msg.append("\n \n");
        }
        if (sender.hasPermission("mineablespawners.reload")) {
            msg.append(plugin.getConfigurationHandler().getMessage("main", "reload"));
        }
        sender.sendMessage(msg.toString());
    }
}
