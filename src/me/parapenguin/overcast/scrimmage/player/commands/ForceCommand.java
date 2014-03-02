package me.parapenguin.overcast.scrimmage.player.commands;

import me.parapenguin.overcast.scrimmage.Scrimmage;
import me.parapenguin.overcast.scrimmage.map.Map;
import me.parapenguin.overcast.scrimmage.map.MapTeam;
import me.parapenguin.overcast.scrimmage.player.Client;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdl, String[] args) {
		if(sender instanceof Player == false) {
			sender.sendMessage(ChatColor.RED + "This command is for players only!");
			return false;
		}
		
		if(args.length < 1) {
			sender.sendMessage(ChatColor.RED + "/force <name> <team>");
			return false;
		}
		Player target = Bukkit.getPlayer(args[0]);
		Map map = Scrimmage.getRotation().getSlot().getMap();
		Client targetClient = Client.getClient((Player) target);
		
		MapTeam team = map.getObservers();
		if(args.length == 2) {
			team = map.getTeam(args[1]);
			if(team == null) {
				sender.sendMessage(ChatColor.RED + "No teams matched query.");
				return false;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "/force <name> <team>");
			return false;
		}
		if (targetClient.getTeam().equals(team)) {
			sender.sendMessage(ChatColor.RED + "That person is already on that team!");
		} else {
			Scrimmage.broadcast(team.getColor() + target.getName() + ChatColor.GRAY + " has been forced to join " + team.getColor() + team.getDisplayName() + ChatColor.GRAY + ".");
			for (Player Online : Bukkit.getOnlinePlayers()) {
				if (Client.getClient((Player) Online).isRanked()) {
					Online.sendMessage(ChatColor.WHITE + "[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + (Client.getClient((Player) sender).getStars()) + (targetClient.getClient((Player) sender).getTeam().getColor()) + sender.getName() + ChatColor.WHITE + " forced " + (targetClient.getClient((Player) sender).getStars()) + team.getColor() + target.getName() + ChatColor.WHITE + ".");
				}
			}
	    }
		targetClient.setTeam(team);
		
		return false;
	}
	
}
