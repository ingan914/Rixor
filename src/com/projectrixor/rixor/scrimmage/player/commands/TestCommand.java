package com.projectrixor.rixor.scrimmage.player.commands;

import com.projectrixor.rixor.scrimmage.Scrimmage;
import com.projectrixor.rixor.scrimmage.utils.UpdateUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdl, String[] args) {
		UpdateUtil.checkForUpdate("http://192.241.139.19/Version.txt","http://192.241.139.19/Rixor.jar",
				Scrimmage.getInstance().getDescription());
		return false;
	}
}
