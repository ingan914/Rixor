package me.parapenguin.overcast.scrimmage.player.commands;

import java.util.ArrayList;
import java.util.List;

import me.parapenguin.overcast.scrimmage.Var;
import me.parapenguin.overcast.scrimmage.player.Client;
import me.parapenguin.overcast.scrimmage.utils.ConversionUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopOverride implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdl, String[] args) {
		if(sender instanceof Player) {
			if(!Client.getClient((Player) sender).isRanked()) {
				sender.sendMessage(ChatColor.RED + "No permission!");
				return false;
			}
		}
		sender.sendMessage(ChatColor.RED + "Are you sure you wish to turn off the server?");
		sender.sendMessage(ChatColor.RED + "Use /stoptheserver if you would like to.");
		return false;
	}
	
}