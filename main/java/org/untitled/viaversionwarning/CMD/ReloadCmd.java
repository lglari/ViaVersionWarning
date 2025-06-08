package org.untitled.viaversionwarning.CMD;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.untitled.viaversionwarning.ViaVersionWarning;

public class ReloadCmd implements CommandExecutor {

    private final ViaVersionWarning plugin;

    public ReloadCmd(ViaVersionWarning plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("vwarn.reload")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }

        plugin.reloadConfig();
        plugin.loadSettings();
        sender.sendMessage(ChatColor.GREEN + "[ViaVersionWarning] Config reloaded.");
        return true;
    }
}
