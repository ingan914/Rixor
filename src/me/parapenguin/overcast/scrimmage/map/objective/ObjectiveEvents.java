package me.parapenguin.overcast.scrimmage.map.objective;

import java.util.List;

import me.parapenguin.overcast.scrimmage.Scrimmage;
import me.parapenguin.overcast.scrimmage.map.Map;
import me.parapenguin.overcast.scrimmage.map.MapTeam;
import me.parapenguin.overcast.scrimmage.map.extras.SidebarType;
import me.parapenguin.overcast.scrimmage.map.filter.events.BlockChangeEvent;
import me.parapenguin.overcast.scrimmage.match.events.PlayerDiedEvent;
import me.parapenguin.overcast.scrimmage.player.Client;
import me.parapenguin.overcast.scrimmage.utils.FireworkUtil;

import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class ObjectiveEvents implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDiedEvent(PlayerDiedEvent event) {
		Client killer = Client.getClient(event.getKiller());
		Client died = Client.getClient(event.getKilled());
		
		if(killer == null) died.getTeam().addScore(-1);
		else {
			killer.getTeam().addScore(1);
			if(event.getMap().getSidebar() == SidebarType.SCORE && killer.getTeam().getScore() >= event.getMap().getScoreLimit() && event.getMap().getScoreLimit() > 0)
				Scrimmage.getRotation().getSlot().getMatch().end(killer.getTeam());
		}
		
		event.getMap().reloadSidebar(false, SidebarType.SCORE);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	
	public void onCoreBlockChange(BlockChangeEvent event) {
	//	Scrimmage.debug("First message", "core");
		if(event.getNewState().getWorld() != event.getMap().getWorld())
			return;

		else if (event.getCause() instanceof BlockPistonExtendEvent || event.getCause() instanceof BlockPistonRetractEvent){
			event.setCancelled(true);
			return;
		}

		if(event.getClient() != null) {
			Client client = event.getClient();
			List<CoreObjective> cores = event.getMap().getCores();
			
			if(event.getCause() instanceof BlockBreakEvent) {
				for(CoreObjective core : cores)
					if(core.isLocation(event.getNewState().getLocation()) && core.getTeam() == client.getTeam()) {
						event.setCancelled(true);
						client.getPlayer().sendMessage(ChatColor.RED + "You can't break your own core!");
						return;
					}
				for(CoreObjective core : cores)
					if(core.isLocation(event.getNewState().getLocation()) && !(core.getTeam() == client.getTeam())){
						MapTeam team = client.getTeam();
						MapTeam obs = Scrimmage.getRotation().getSlot().getMap().getObservers();
						String format = team.getColor() + "[Team] " + client.getStars() + team.getColor() + client.getPlayer().getName() + ChatColor.GRAY + " broke a piece of " + core.getName();
						Scrimmage.broadcast(format, team);
						Scrimmage.broadcast(format, obs);
			}
			
			if(event.getNewState().getType().equals(Material.LAVA)) {
			//	Scrimmage.debug("First message3", "core");
				event.setCancelled(true);
				return;
			}
		} else {
			if(event.getNewState().getType() == Material.LAVA && event.getMap().getCoreLeak(event.getNewState().getLocation()) != null) {
		//		Scrimmage.debug("First2 message", "core");
				CoreObjective core = event.getMap().getCoreLeak(event.getNewState().getLocation());
				core.setComplete(true, client.getTeam());
				
				event.getMap().reloadSidebar(true, SidebarType.OBJECTIVES);
				
				String who = core.getTeam().getColor() + core.getTeam().getDisplayName() + "'s";
				String leaked = ChatColor.DARK_AQUA + " " + core.getName();
				String has = ChatColor.RED + " has leaked!";
				String message = who + leaked + has;
				Scrimmage.broadcast(message);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlaceForWool(BlockPlaceEvent event) {
		Client client = Client.getClient(event.getPlayer());


		for(WoolObjective wool : client.getTeam().getMap().getWools())
			if(wool.isLocation(event.getBlockPlaced().getLocation()) && wool.getTeam() != client.getTeam() && !wool.getColor().equals(wool.getColor())) {
				event.setCancelled(true);
				return;
			}
		
		if(event.getBlockPlaced().getType() != Material.WOOL) {
			if(Scrimmage.getMap().getWool(event.getBlock().getLocation()) != null) {
				event.setCancelled(true);
				return;
			}
			return;
		}

		WoolObjective wool = client.getTeam().getWool(event.getBlockPlaced());
		if(wool == null)
			return;
		
		if(wool.isLocation(event.getBlockPlaced().getLocation()) && wool.getTeam() != client.getTeam()) {
			event.setCancelled(true);
			return;
		}

		if(!wool.isLocation(event.getBlockPlaced().getLocation()))
			return;

		Builder builder = FireworkEffect.builder();
		builder.withColor(wool.getWool().getColor());
		builder.with(Type.BALL);
		builder.withTrail();

		FireworkEffect effect = builder.build();
		
		try {
			new FireworkUtil().playFirework(event.getBlockPlaced().getWorld(), event.getBlockPlaced().getLocation(), effect);
		} catch(Exception e) {
			e.printStackTrace();
		}

		String who = client.getTeam().getColor() + client.getPlayer().getName();
		String placed = ChatColor.GRAY + " placed the " + wool.getName().toUpperCase();
		String team = ChatColor.GRAY + " WOOL for " + client.getTeam().getColor() + client.getTeam().getDisplayName();
		String message = who + placed + team;
		Scrimmage.broadcast(message);
		wool.setComplete(true, client.getTeam());
		client.getTeam().getMap().reloadSidebar(true, SidebarType.OBJECTIVES);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreakForWool(BlockBreakEvent event) {
		if(Scrimmage.getMap().getWool(event.getBlock().getLocation()) != null) {
			event.setCancelled(true);
			return;
		}

	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlaceForMonument(BlockChangeEvent event) {
		//Scrimmage.debug("monu1", "monu");
		Client client = event.getClient();
		if(event.getCause() instanceof BlockPlaceEvent) {
			List<MonumentObjective> monuments = event.getMap().getMonuments();
			for(MonumentObjective monument : monuments) {
				if(monument.isLocation(event.getNewState().getLocation()) && monument.getTeam() == client.getTeam() || monument.isDestroyed()) {
					event.setCancelled(true);
					client.getPlayer().sendMessage(ChatColor.RED + "You may not break this monument!");
					return;
				}
			}
		}
		else if (event.getCause() instanceof BlockPistonExtendEvent || event.getCause() instanceof BlockPistonRetractEvent){
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreakForMonument(BlockChangeEvent event) {
		Client client = event.getClient();
		if(event.getCause() instanceof BlockBreakEvent) {
			List<MonumentObjective> monuments = event.getMap().getMonuments();
			for(MonumentObjective monument : monuments) {
				if(monument.isLocation(event.getNewState().getLocation()) && monument.getTeam() == client.getTeam()) {
					event.setCancelled(true);
					client.getPlayer().sendMessage(ChatColor.RED + "You may not break this monument!");
					return;
				}

			}
		}
		else if (event.getCause() instanceof BlockPistonExtendEvent || event.getCause() instanceof BlockPistonRetractEvent){
			event.setCancelled(true);
			return;
		}
		//Scrimmage.debug("monu2", "monu");
		Map map = event.getMap();
		

		MonumentObjective monument = map.getMonument(event.getNewState().getLocation());
		//ServerLog.info("Monument == null (" + (monument == null) + ")");
		if(monument == null) return;
		monument.addBreak(event.getNewState().getLocation(), client);
	}

}
