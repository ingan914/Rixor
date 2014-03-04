package me.parapenguin.overcast.scrimmage.player.commands;

import me.parapenguin.overcast.scrimmage.Scrimmage;
import me.parapenguin.overcast.scrimmage.match.Match;
import me.parapenguin.overcast.scrimmage.player.Client;
import me.parapenguin.overcast.scrimmage.utils.ConversionUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdl, String[] args) {

		if(sender instanceof Player) {
			if(!Client.getClient((Player) sender).isRanked()) {
				sender.sendMessage(ChatColor.RED + "No permission!");
				return false;
			}
		}

		Match match = Scrimmage.getRotation().getSlot().getMatch();
		//Scrimmage.getInstance().getLogger().info(match.isCurrentlyCycling() + "  " + match.isCurrentlyRunning() + "  " + match.isCurrentlyStarting() + " ");
		if(!match.isCurrentlyStarting()) {
			sender.sendMessage(ChatColor.RED + "A match is already running!");
			return false;
		}
		
		int time = 30;
		if(args.length == 1)
			if(ConversionUtil.convertStringToInteger(args[0], -1) > -1)
				time = ConversionUtil.convertStringToInteger(args[0], -1);
			else {
				sender.sendMessage(ChatColor.RED + "Please supply a valid time greater than -1");
				return false;
			}
		Scrimmage.getRotation().getSlot().getMatch().start(time);
		/*Scrimmage.broadcast(ChatColor.RED + sender.getName() + ChatColor.DARK_PURPLE + " has started the countdown.");*/
		for (Player Online : Bukkit.getOnlinePlayers()) {
			if (Client.getClient((Player) Online).isRanked()) {

				Online.sendMessage(ChatColor.WHITE + "[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + (Client.getClient((Player) sender).getStars()) + (Client.getClient((Player) sender).getTeam().getColor()) + sender.getName() + ChatColor.WHITE + " has started the countdown at " + time + ".");

			}
		}

		//Bukkit.dispatchCommand(sender, "weather clear 999999999");
		/*Scrimmage.broadcast(ChatColor.RED + "The match you are playing has the current ID of" + ChatColor.GREEN + " #" + ChatColor.BLUE + world.getName() + ChatColor.RED + "! If you would like a copy of this match, ask " + ChatColor.RED + "*" + ChatColor.YELLOW + "*" + ChatColor.AQUA + "ShinyDialga45 " + ChatColor.RED + "for this ID.");
		*/
		
		return true;
	}
	
}
