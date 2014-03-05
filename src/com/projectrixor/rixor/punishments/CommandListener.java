package com.projectrixor.rixor.punishments;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String command = cmd.getName();
		Player player = (Player) sender;
		if (command.equalsIgnoreCase("k") && player.isOp()) {
			try {
			if (args.length > 1) {
				int orderOfString = 0;
				String playerArgs[] = args;
				String punishmentString = "";
				for (String arg : args) {
					if (orderOfString > 0) {
					punishmentString += (arg + " ");
					}
					orderOfString += 1;
				}
				String kickedPlayer = playerArgs[0];
				ChatColor kickedColor = ChatColor.AQUA;
				Bukkit.dispatchCommand(sender, "kick " + kickedPlayer + " " + punishmentString);
				Bukkit.broadcastMessage(ChatColor.AQUA + player.getDisplayName() + ChatColor.GOLD + " � Kicked � " + kickedColor + playerArgs[0] + ChatColor.GOLD + " � " + punishmentString);
			}
			} catch (Exception e) {
				player.sendMessage(ChatColor.RED + "The player requested to be kicked is not online!");
			}
	    }
		return false;
	}
}
