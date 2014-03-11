package com.projectrixor.rixor.scrimmage.player.commands;

import com.projectrixor.rixor.scrimmage.Scrimmage;
import com.projectrixor.rixor.scrimmage.Var;
import com.projectrixor.rixor.scrimmage.player.Client;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopTheServer implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdl, String[] args) {
		if(sender instanceof Player) {
			if(!Client.getClient((Player)sender).isRanked()) {
				sender.sendMessage(ChatColor.RED + "No permission!");
				return false;
			}
		}
		if (sender.getName().equals("ShinyDialgaa45") && Var.stopConfirmation) {
		    	Scrimmage.getInstance().getServer().shutdown();
		} else if (Var.stopConfirmation) {
		    	if(args[0].equalsIgnoreCase("yes")) {
		    		for (Player Online : Bukkit.getOnlinePlayers()) {
		    			if (Client.getClient((Player) Online).isRanked()) {
		    				Online.sendMessage(ChatColor.WHITE + "[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + (Client.getClient((Player) sender).getStars()) + (Client.getClient((Player) sender).getTeam().getColor()) + sender.getName() + ChatColor.WHITE + " has stopped the server.");
		    			}	
		    		}
		    		Scrimmage.getInstance().getServer().shutdown();
		    	} else if (args[0].equalsIgnoreCase("no")) {
		    		for (Player Online : Bukkit.getOnlinePlayers()) {
		    			if (Client.getClient((Player) Online).isRanked()) {
		    				Online.sendMessage(ChatColor.WHITE + "[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + (Client.getClient((Player) sender).getStars()) + (Client.getClient((Player) sender).getTeam().getColor()) + sender.getName() + ChatColor.WHITE + " has cancelled the stop.");
		    				Var.stopConfirmation = false;
		    			}
		    		}
		    	} else {
		    		sender.sendMessage(ChatColor.RED + "Invalid arguments!");
		    	}
		} else {
			for (Player Online : Bukkit.getOnlinePlayers()) {
				if (Client.getClient((Player) Online).isRanked()) {
					Online.sendMessage(ChatColor.WHITE + "[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + (Client.getClient((Player) sender).getStars()) + (Client.getClient((Player) sender).getTeam().getColor()) + sender.getName() + ChatColor.WHITE + " has typed /stoptheserver.");
				}
			}
			Var.stopConfirmation = true;
			sender.sendMessage(ChatColor.RED + "Are you sure you wish to stop the server?");
			sender.sendMessage(ChatColor.RED + "/stoptheserver <yes/no>");
		}

		return false;
	}
	
}