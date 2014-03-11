package com.projectrixor.rixor.scrimmage.player.commands;

import com.projectrixor.rixor.scrimmage.Scrimmage;
import com.projectrixor.rixor.scrimmage.utils.DownloadMapUtil;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;

/**
 * @author MasterEjay
 */
public class DownloadMapCommand {

	@Command(aliases = { "downloadmap", "dm"}, desc = "Downloads a map from the map repo", usage = "<map name>", min = 1, max = -1)
	public static void downloadmap(final CommandContext args, CommandSender sender) throws Exception {

		boolean bool = DownloadMapUtil.checkForMap(args.getJoinedStrings(0));
		if (bool){
			boolean bool1 = new File(Scrimmage.getMapRoot() + File.separator + args.getJoinedStrings(0)).exists();
			if (!bool1){
				boolean successs = DownloadMapUtil.downloadMap(args.getJoinedStrings(0));
				if (successs){
					sender.sendMessage(ChatColor.GREEN + "Your map is ready! Restart the server for the map to be loaded!");
				}
				else {
					sender.sendMessage(ChatColor.RED + "Oops! The map could not download / unzip for some reason! Check the logs for more info!");
				}
			}
			else {
				throw new CommandException("That map exists in the repo, but you already have it!");
			}

		}
		else {
			throw new CommandException(ChatColor.RED + "That map doesn't exist. Check your spelling and " + ChatColor.GOLD + "update.masterejay.us/maps"
					+ ChatColor.RED + " to see if the map exists!");
		}

	}
}
