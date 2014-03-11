package com.projectrixor.rixor.scrimmage.player.commands;

import com.projectrixor.rixor.scrimmage.utils.DownloadMapUtil;
<<<<<<< HEAD
=======
import com.projectrixor.rixor.scrimmage.utils.UpdateUtil;
import com.sk89q.minecraft.util.commands.CommandContext;
>>>>>>> FETCH_HEAD
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestCommand  {

	@com.sk89q.minecraft.util.commands.Command(aliases = { "test"}, desc = "test", usage = "", min = 1, max = -1)
	public static void test(final CommandContext args, CommandSender sender) throws Exception {

		boolean bool = DownloadMapUtil.checkForMap(args.getJoinedStrings(0));
		if (bool){
			DownloadMapUtil.downloadMap(args.getJoinedStrings(0));

		}
		else {
			sender.sendMessage("NO");
		}

	}
}
