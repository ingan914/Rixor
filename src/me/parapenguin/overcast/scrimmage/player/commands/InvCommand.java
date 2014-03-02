package me.parapenguin.overcast.scrimmage.player.commands;

import me.parapenguin.overcast.scrimmage.Scrimmage;
import me.parapenguin.overcast.scrimmage.map.Map;
import me.parapenguin.overcast.scrimmage.map.MapTeam;
import me.parapenguin.overcast.scrimmage.player.Client;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InvCommand implements CommandExecutor {
	
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
			sender.sendMessage(ChatColor.RED + "No player name provided!");
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

		client.setTeam(team);
		sender.sendMessage(ChatColor.GRAY + "You have joined the " + team.getColor() + team.getDisplayName() + ChatColor.GRAY + ".");
		
		return false;
	}
	protected void run(final Server server, final Player user, final String commandLabel, final String[] args) throws Exception
    {
            if (args.length < 1)
            {
            	user.sendMessage(ChatColor.RED + "No player name provided!");
            }

            final Player invUser = getPlayer(server, user, args, 0);
            Inventory inv;

            if (args.length == 1)
            {
            	Player target = Bukkit.getServer().getPlayer(args[0]);
                    inv = server.createInventory((InventoryHolder) target.getInventory(), 9, "Equipped");
                    inv.setContents(invUser.getInventory().getArmorContents());
            }
            else
            {
                    inv = invUser.getInventory();
            }
            user.closeInventory();
            user.openInventory(inv);
    }
	private Player getPlayer(Server server, Player user, String[] args, int i) {
		// TODO Auto-generated method stub
		return null;
	}
}
