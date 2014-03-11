package com.projectrixor.rixor.scrimmage.player.commands;

import com.projectrixor.rixor.scrimmage.Scrimmage;
import com.projectrixor.rixor.scrimmage.utils.DownloadMapUtil;
import com.projectrixor.rixor.scrimmage.utils.UpdateUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdl, String[] args) {

		boolean bool = DownloadMapUtil.checkForMap(args[0] + " " + args[1]);
		if (bool){
			sender.sendMessage("YES");

		}
		else {
			sender.sendMessage("NO");
		}
		return false;
	}
}
