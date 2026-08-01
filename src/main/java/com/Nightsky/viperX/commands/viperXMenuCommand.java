package com.Nightsky.viperX.commands;

import com.Nightsky.viperX.menu.mainMenu;
import com.Nightsky.viperX.menu.linksMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class viperXMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (player.hasPermission("viperx.mainMenu")) {
            mainMenu.open(player);
        } else {
            linksMenu.open(player);
        }
        return true;
    }
}
