package me.parapenguin.overcast.scrimmage.player.commands;

import com.sk89q.minecraft.util.commands.CommandContext;
import me.parapenguin.overcast.scrimmage.Scrimmage;
import me.parapenguin.overcast.scrimmage.player.Client;
import me.parapenguin.overcast.scrimmage.player.PlayerChatEvent;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlobalCommand  {


	@com.sk89q.minecraft.util.commands.Command(aliases = { "g", "global"}, desc = "Speaks in Global Chat", usage = "[message]", min = 1, max = -1)
	public static void global(final CommandContext args, CommandSender sender) throws Exception {

		String message = "";

		message = args.getJoinedStrings(0);
		Scrimmage.callEvent(new PlayerChatEvent(Client.getClient((Player) sender), message, false));
	}
	
}
