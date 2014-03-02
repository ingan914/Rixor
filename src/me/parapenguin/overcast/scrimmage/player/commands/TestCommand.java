package me.parapenguin.overcast.scrimmage.player.commands;

import me.confuser.barapi.BarAPI;
import me.parapenguin.overcast.scrimmage.Scrimmage;
import me.parapenguin.overcast.scrimmage.Var;
import me.parapenguin.overcast.scrimmage.player.Client;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdl, String[] args) {
		float test = 1;
		Var.edHealth = Var.edHealth - 1;
		for (Player Online : Bukkit.getOnlinePlayers()) {
				BarAPI.setHealth(Online, test);
	}
		return false;
	}
}
