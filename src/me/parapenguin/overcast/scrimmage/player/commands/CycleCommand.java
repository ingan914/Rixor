package me.parapenguin.overcast.scrimmage.player.commands;

import me.parapenguin.overcast.scrimmage.Scrimmage;
import me.parapenguin.overcast.scrimmage.match.Match;
import me.parapenguin.overcast.scrimmage.player.Client;
import me.parapenguin.overcast.scrimmage.utils.ConversionUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CycleCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdl, String[] args) {
		if(sender instanceof Player) {
			if(!Client.getClient((Player) sender).isRanked()) {
				sender.sendMessage(ChatColor.RED + "No permission!");
				return false;
			}
		}
		
		Match match = Scrimmage.getRotation().getSlot().getMatch();
		if(match.isCurrentlyRunning()) {
			match.end(true);
		}
		
		int time = 0;
		if(args.length == 1)
			if(ConversionUtil.convertStringToInteger(args[0], 0) >= 1)
				time = ConversionUtil.convertStringToInteger(args[0], 0);
			else {
				sender.sendMessage(ChatColor.RED + "Please supply a valid time greater than or equal to 1.");
				return false;
			}
		
		if(!match.isCurrentlyCycling()) Scrimmage.getRotation().getSlot().getMatch().stop();
		else Scrimmage.getRotation().getSlot().getMatch().setCycling(time);
		
		Scrimmage.getRotation().getSlot().getMatch().cycle(time);
		for (Player Online : Bukkit.getOnlinePlayers()) {
			if (Client.getClient((Player) Online).isRanked()) {
				Online.sendMessage(ChatColor.WHITE + "[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + (Client.getClient((Player) sender).getStars()) + (Client.getClient((Player) sender).getTeam().getColor()) + sender.getName() + ChatColor.WHITE + " has started the cycle at " + ChatColor.GOLD + time);
			}
		}
		return true;
	}
	
}
