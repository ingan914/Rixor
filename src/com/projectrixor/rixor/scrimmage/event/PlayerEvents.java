package com.projectrixor.rixor.scrimmage.event;

import com.projectrixor.rixor.scrimmage.ServerLog;
import com.projectrixor.rixor.scrimmage.player.Client;
import com.projectrixor.rixor.scrimmage.Scrimmage;
import com.projectrixor.rixor.scrimmage.map.Map;
import com.projectrixor.rixor.scrimmage.map.MapTeam;
import com.projectrixor.rixor.scrimmage.map.MapTeamSpawn;
import com.projectrixor.rixor.scrimmage.match.Match;
import com.projectrixor.rixor.scrimmage.player.PlayerChatEvent;
import com.projectrixor.rixor.scrimmage.utils.Characters;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*import sun.management.counter.Variability;
*/
public class PlayerEvents implements Listener {
	
	@EventHandler
	public void onBlockAttempt(BlockCanBuildEvent event) {
		event.setBuildable(true);
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		//player.sendMessage(ChatColor.DARK_AQUA + "ParaPGM v1.0 made by " + ChatColor.GOLD + "ParaPenguin" + ChatColor.DARK_AQUA + ".");
		Client client = new Client(player);
		
		Client.getClients().add(client);
		client.setTeam(Scrimmage.getRotation().getSlot().getMap().getObservers(), true, true, true);
		
		event.setJoinMessage(client.getStars() + client.getTeam().getColor() + event.getPlayer().getName() + ChatColor.YELLOW + " joined the game");
		Client clients = Client.getClient(event.getPlayer());
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		Client client = Client.getClient(event.getPlayer());
		if (client.getTeam().isObserver()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage();
		if (command.equals("staff")) {
			Bukkit.broadcastMessage("test");
		}
	}

	

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		List<ItemStack> itemStackL = event.getDrops();
		List<ItemStack> itemsToDelete = new ArrayList<>();
		for (ItemStack i : itemStackL){
			if (i != null){
				for (ItemStack i2 : Scrimmage.getRotation().getSlot().getMap().getItemRemove()){
					if (i2 != null){
						if (i.getType().toString().equals(i2.getType().toString())){
							itemsToDelete.add(i);
						}
					}

				}
			}
		}
		for (ItemStack i : itemsToDelete){
			itemStackL.remove(i);
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Client client = Client.getClient(event.getPlayer());
		/*if (clients.getClient(player).isRanked()) {	
			Var.staffNumber = Var.staffNumber - 1;
		}*/
		try {
			event.setQuitMessage(client.getStars() + client.getTeam().getColor() + event.getPlayer().getName() + ChatColor.YELLOW + " left the game.");
			onPlayerExit(event.getPlayer());
		} catch(NullPointerException e) {}
	}
	
	public void onPlayerExit(Player player) {
		Client.getClients().remove(Client.getClient(player));
	}
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent event) {
		Map map = Scrimmage.getRotation().getSlot().getMap();

		ChatColor color = ChatColor.GRAY;
		
		String team = "";
		if(!Scrimmage.isPublic())
			team = ChatColor.GRAY + "Server Owner: (" + ChatColor.GOLD + Scrimmage.getTeam() + ChatColor.GRAY + ")";
		
		event.setMotd(color + " " + Characters.raquo + " " + ChatColor.AQUA + map.getName() + color + " " + Characters.laquo + " " + "\n" + team);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Client client = Client.getClient(event.getPlayer());
		event.setCancelled(true);
		
		PlayerChatEvent chat = new PlayerChatEvent(client, event.getMessage(), true);
		Scrimmage.callEvent(chat);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location Loc = player.getLocation();
		Loc.setY(Loc.getY() + .5);
		Match match = Scrimmage.getRotation().getSlot().getMatch();
		Client client = Client.getClient(event.getPlayer());
		if ((Loc.getY() < -30 && client.isObserver()) || (!match.isCurrentlyRunning() && Loc.getY() < -30)) {
			event.getPlayer().teleport(Scrimmage.getMap().getObservers().getSpawn().getSpawn());
		}
		
	}
	
	@EventHandler
	public void onArrowPickup(PlayerPickupItemEvent event) {
		Item arrow = event.getItem();
		Client client = Client.getClient(event.getPlayer());
		if(arrow == new ItemStack(Material.ARROW) && (client.isObserver() || !Scrimmage.getRotation().getSlot().getMatch().isCurrentlyRunning())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Client client = Client.getClient(event.getPlayer());
		MapTeamSpawn spawn = client.getTeam().loadout(client, false, true);
		event.setRespawnLocation(spawn.getSpawn());
		if(!client.getTeam().isObserver() && spawn.getKit() != null)
			spawn.getKit().load(client);
		else client.getTeam().loadout(client, false, true);
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		if(event.isCancelled())
			return;
		
		Client client = event.getClient();
		String message = event.getMessage();
		
		MapTeam team = client.getTeam();
		String format = team.getColor() + "[Team] " + client.getStars() + team.getColor() + client.getPlayer().getName() + ChatColor.WHITE + ": " + message;
		if(!event.isTeam()) {
			format = "<" + client.getStars() + team.getColor() + client.getPlayer().getName() + ChatColor.WHITE + "> " + message;
			team = null;
		}
		
		Scrimmage.broadcast(format, team);
		ServerLog.info(format);
	}

	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player == false && event.getDamager() instanceof Projectile == false)
			return;
		
		if(event.getEntity() instanceof Player == false)
			return;
		
		Client damaged = Client.getClient((Player) event.getEntity());
		Player attackerPlayer = null;
		if(event.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) event.getDamager();
			if(proj.getShooter() instanceof Player == false)
				return;
			
			attackerPlayer = (Player) proj.getShooter();
		} else attackerPlayer = (Player) event.getDamager();
		Client attacker = Client.getClient(attackerPlayer);
		
		if(attacker.getTeam() == damaged.getTeam() || attacker.getTeam().isObserver() || damaged.getTeam().isObserver())
			event.setCancelled(true);
	}
	
}
