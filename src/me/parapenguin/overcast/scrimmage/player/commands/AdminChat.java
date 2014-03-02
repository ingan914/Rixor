package me.parapenguin.overcast.scrimmage.player.commands;

import lombok.Getter;
import me.parapenguin.overcast.scrimmage.Scrimmage;
import me.parapenguin.overcast.scrimmage.map.Map;
import me.parapenguin.overcast.scrimmage.player.Client;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class AdminChat implements CommandExecutor {
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdl, String[] args) {
		if(sender instanceof Player) {
			if(!Client.getClient((Player) sender).isRanked()) {
				sender.sendMessage(ChatColor.RED + "No permission!");
				return false;
			}
		}
		if(args.length < 1) {
			sender.sendMessage(ChatColor.RED + "/a <message>" + Map.mapcommand);
			return false;
		}
		
		String message = "";
		int i = 0;
		while(i < args.length) {
			message += " " + args[i];
			i++;
		}
		message = message.substring(1);
		for (Player Online : Bukkit.getOnlinePlayers()) {
			if (Client.getClient((Player) Online).isRanked()) {
				Online.sendMessage(ChatColor.WHITE + "[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + (Client.getClient((Player) sender).getStars()) + (Client.getClient((Player) sender).getTeam().getColor()) + sender.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + message);
			}
		}
		return false;
	}
	
}
