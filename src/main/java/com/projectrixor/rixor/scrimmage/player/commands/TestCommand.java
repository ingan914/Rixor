package com.projectrixor.rixor.scrimmage.player.commands;

import com.projectrixor.rixor.scrimmage.Scrimmage;
import com.projectrixor.rixor.scrimmage.utils.DownloadMapUtil;
import com.sk89q.minecraft.util.commands.CommandContext;

import org.bukkit.command.CommandSender;

import java.io.File;

public class TestCommand  {

	@com.sk89q.minecraft.util.commands.Command(aliases = { "test"}, desc = "test", usage = "", min = 1, max = -1)
	public static void test(final CommandContext args, CommandSender sender) throws Exception {

		boolean bool = DownloadMapUtil.checkForMap(args.getJoinedStrings(0));

		if (bool){


		}
		else {
			sender.sendMessage("NO");
		}

	}
}
