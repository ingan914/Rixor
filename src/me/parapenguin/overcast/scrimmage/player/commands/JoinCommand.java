package me.parapenguin.overcast.scrimmage.player.commands;

import me.parapenguin.overcast.scrimmage.Scrimmage;
import me.parapenguin.overcast.scrimmage.map.Map;
import me.parapenguin.overcast.scrimmage.map.MapTeam;
import me.parapenguin.overcast.scrimmage.player.Client;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdl, String[] args) {
		if(sender instanceof Player == false) {
			sender.sendMessage(ChatColor.RED + "This command is for players only!");
			return false;
		}
		
		Map map = Scrimmage.getRotation().getSlot().getMap();
		Client client = Client.getClient((Player) sender);
		
		MapTeam team = map.getObservers();
		if(args.length == 0) {
			team = map.getLowest();
		} else if(args.length == 1) {
			team = map.getTeam(args[0]);
			if(team == null) {
				sender.sendMessage(ChatColor.RED + "No teams matched query.");
				return false;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "/join (team)");
			return false;
		}
		if (client.getTeam().equals(team)) {
			sender.sendMessage(ChatColor.RED + "You are already on that team!");
		} else {
			Scrimmage.broadcast(team.getColor() + sender.getName() + ChatColor.GRAY + " has joined the " + team.getColor() + team.getDisplayName() + ChatColor.GRAY + ".");
	    }
		client.setTeam(team);
		
		return false;
	}
	
}
