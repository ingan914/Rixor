package me.parapenguin.overcast.scrimmage.player.commands;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import me.confuser.barapi.BarAPI;
import me.parapenguin.overcast.scrimmage.Scrimmage;
import me.parapenguin.overcast.scrimmage.Var;
import me.parapenguin.overcast.scrimmage.map.Map;
import me.parapenguin.overcast.scrimmage.map.MapLoader;
import me.parapenguin.overcast.scrimmage.map.MapTeam;
import me.parapenguin.overcast.scrimmage.map.extras.SidebarType;
import me.parapenguin.overcast.scrimmage.match.Match;
import me.parapenguin.overcast.scrimmage.player.Client;
import me.parapenguin.overcast.scrimmage.rotation.Rotation;
import me.parapenguin.overcast.scrimmage.rotation.RotationSlot;
import me.parapenguin.overcast.scrimmage.utils.ConversionUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
public class MatchCommand implements CommandExecutor {
	@Override
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdl, String[] args) {
		/*Current Map*/
		String mapname = Scrimmage.getRotation().getSlot().getMap().getName();
		
		Client client = Client.getClient((Player) sender);	
		
		String score = ChatColor.AQUA + "Score: ";
		
		Rotation rot = Scrimmage.getRotation();
		
		for(MapTeam team : Scrimmage.getMap().getTeams())
		score += team.getColor() + "" + team.getScore() + " ";
		
		sender.sendMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "---------------------" + ChatColor.DARK_AQUA + "Info" + ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "---------------------");
		sender.sendMessage(ChatColor.RED + "Map " + ChatColor.GRAY + ": " + mapname);
		
		sender.sendMessage(ChatColor.RED + "Next Map " + ChatColor.GRAY + ": " + Var.nextMap);
		return true;
	}
	
}
/*
 * public void reloadSidebar(boolean objectives, SidebarType sidebar) {
		if(getSidebar() == SidebarType.OBJECTIVES && (sidebar == SidebarType.OBJECTIVES || sidebar == null)) {
			if(boardObjective != null)
				this.boardObjective.unregister();
			
			this.boardObjective = board.registerNewObjective("Objectives", "dummy");
			this.boardObjective.setDisplayName(ChatColor.GOLD + "Objectives");
			this.boardObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			if(objectives) {
				int i = 1;
				for(MapTeam team : teams) {
					if(team.getObjectives() == null || team.getObjectives().size() == 0)
						i = team.loadTeamObjectives(true, i);
					else i = team.loadTeamObjectives(false, i);
					if(teams.get(teams.size() - 1) != team) {
						i++;
						OfflinePlayer player = Scrimmage.getInstance().getServer().getOfflinePlayer(getSpaces(i));
						getBoardObjective().getScore(player).setScore(i);
						i++;
						
					}
				}
			}
		} else if(getSidebar() == SidebarType.SCORE && (sidebar == SidebarType.SCORE || sidebar == null)) {
			if(objectives) {
				if(boardObjective != null)
					this.boardObjective.unregister();
				
				
				this.boardObjective = board.registerNewObjective("Score", "dummy");
				this.boardObjective.setDisplayName(ChatColor.GOLD + "Score");
				this.boardObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
				
				for(MapTeam team : getTeams()) {
					OfflinePlayer player = Scrimmage.getInstance().getServer().getOfflinePlayer(team.getColor() + team.getDisplayName());
					this.boardObjective.getScore(player).setScore(1);
				}
				
			}
			
			for(MapTeam team : getTeams()) {
				OfflinePlayer player = Scrimmage.getInstance().getServer().getOfflinePlayer(team.getColor() + team.getDisplayName());
				this.boardObjective.getScore(player).setScore(team.getScore());
				
			}
			
		}
		
	}
 */
