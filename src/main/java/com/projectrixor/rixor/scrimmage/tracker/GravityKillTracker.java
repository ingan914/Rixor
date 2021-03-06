package com.projectrixor.rixor.scrimmage.tracker;

import com.projectrixor.rixor.scrimmage.match.events.PlayerDiedEvent;
import com.projectrixor.rixor.scrimmage.Scrimmage;
import com.projectrixor.rixor.scrimmage.map.MapTeam;
import com.projectrixor.rixor.scrimmage.player.Client;
import com.projectrixor.rixor.scrimmage.utils.SchedulerUtil;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;

import java.util.*;

public class GravityKillTracker implements Listener {

	public static final long MAX_KNOCKBACK_TIME = 20, // Max ticks between
														// getting attacked and
														// leaving the ground
			MAX_SPLEEF_TIME = 10, // Max ticks between block breaking and
									// leaving the ground
			MAX_ON_GROUND_TIME = 5, // Max ticks to be on ground without ending
										// the fall
			MAX_SWIMMING_TIME = 10, // Max ticks to be in water without ending
									// the fall
			MAX_CLIMBING_TIME = 5; // Max ticks to be on a ladder/vine without
									// ending the fall

	/**
	 * Represents a block that was recently broken by a player. Created for all
	 * blocks broken, indexed by the block's location, and discarded after
	 * MAX_SPLEEF_TIME.
	 */
	static class BrokenBlock {
		final public Block block;
		final public Player breaker;
		final public long time;

		BrokenBlock(Block block, Player breaker, long time) {
			this.block = block;
			this.breaker = breaker;
			this.time = time;
		}
	}

	private HashMap<BlockVector, BrokenBlock> brokenBlocks = new HashMap<BlockVector, BrokenBlock>();

	/**
	 * Represents a player potentially falling due to an attack. Whenever an
	 * attack is detected, one of these is created and indexed by victim. It is
	 * discarded when the player lands somewhere safe or dies. A player can only
	 * have one fall record at any time. While a fall is in progress, the
	 * attacker and cause don't change, and no new falls can begin.
	 */
	static class Attack {
		static enum Cause {
			HIT, SHOOT, SPLEEF
		}

		static enum From {
			FLOOR, LADDER, WATER
		}

		final public Player victim; // Who is falling
		final public Entity attacker; // Who is responsible
		final public Cause cause; // What caused the fall
		final public From from; // Where they are falling from
		final public long time; // Time of the attack
		public boolean wasAttacked; // Set true when we're sure the fall was
									// caused by an attack
		public long onGroundTime; // Time the victim landed on the ground
		public boolean isSwimming; // If the victim is currently in water
		public long swimmingTime; // Time the victim entered water
		public boolean isClimbing; // If the victim is currently on a
									// ladder/vine
		public long climbingTime; // Time the victim grabbed a ladder/vine
		public boolean isInLava; // If the victim is currently in lava
		public long inLavaTime; // Time the victim entered lava

		Attack(Entity attacker, Cause cause, Player victim, From from, long time) {
			this.attacker = attacker;
			this.cause = cause;
			this.victim = victim;
			this.from = from;
			this.time = this.swimmingTime = this.climbingTime = this.onGroundTime = time;
		}
	}

	private HashMap<Player, Attack> attacks = new HashMap<Player, Attack>();

	private TickTimer timer;
	private PlayerBlockChecker playerBlockChecker;

	public GravityKillTracker(TickTimer timer, PlayerBlockChecker playerBlockChecker) {
		this.timer = timer;
		this.playerBlockChecker = playerBlockChecker;
	}

	public void clear() {
		brokenBlocks.clear();
		attacks.clear();
	}

	/**
	 * Find the block most recently broken under a player's feet within the last
	 * MAX_SPLEEF_TIME, or null if there is no such block.
	 * 
	 * @param player
	 *            A player
	 * @param time
	 *            Time the player started to fall
	 * @return Last block that was recently broken under the player, or null if
	 *         no such blocks were found
	 */
	private BrokenBlock findBlockBrokenUnderPlayer(Player player, long time) {
		Location location = player.getLocation();

		int y = (int) Math.floor(location.getY() - 0.1);

		int x1 = (int) Math.floor(location.getX() - PlayerBlockChecker.PLAYER_RADIUS);
		int z1 = (int) Math.floor(location.getZ() - PlayerBlockChecker.PLAYER_RADIUS);

		int x2 = (int) Math.floor(location.getX() + PlayerBlockChecker.PLAYER_RADIUS);
		int z2 = (int) Math.floor(location.getZ() + PlayerBlockChecker.PLAYER_RADIUS);

		BrokenBlock lastBrokenBlock = null;

		for (int x = x1; x <= x2; ++x) {
			for (int z = z1; z <= z2; ++z) {
				BlockVector bv = new BlockVector(x, y, z);

				if (brokenBlocks.containsKey(bv)) {
					BrokenBlock brokenBlock = brokenBlocks.get(bv);
					if (lastBrokenBlock == null || brokenBlock.time > lastBrokenBlock.time) {
						lastBrokenBlock = brokenBlock;
					}
				}
			}
		}

		if (lastBrokenBlock != null && time - lastBrokenBlock.time <= MAX_SPLEEF_TIME) {
			return lastBrokenBlock;
		} else {
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	private boolean isSupported(final Attack attack) {
		return attack.isClimbing || attack.isSwimming || attack.victim.isOnGround();
	}

	private void cancelFall(Attack attack, String reason) {
		attacks.remove(attack.victim);
	}

	/**
	 * Check various timeouts
	 * 
	 * @param attack
	 *            Fall to check
	 * @param time
	 *            Time of the check
	 */
	@SuppressWarnings("deprecation")
	private void checkAttackCancelled(final Attack attack, long time) {
		if (attacks.get(attack.victim) == attack) {
			// Ensure the given fall is still in progress

			if (attack.wasAttacked) {
				// If the attack has been confirmed, check for various timeouts
				// that can cancel it. Don't cancel the attack if the victim is
				// in lava.

				if (!attack.isInLava) {
					if (attack.victim.isOnGround() && time - attack.onGroundTime > MAX_ON_GROUND_TIME)
						cancelFall(attack, "on ground too long during fall");

					if (attack.isSwimming && time - attack.swimmingTime > MAX_SWIMMING_TIME)
						cancelFall(attack, "in water too long during fall");

					if (attack.isClimbing && time - attack.climbingTime > MAX_CLIMBING_TIME)
						cancelFall(attack, "on ladder/vine too long during fall");
				}
			} else {
				// If attack has not been confirmed yet, check if the player has
				// been
				// supported long enough to assume that they weren't knocked
				// back.

				if (attack.victim.isOnGround() && time - attack.time > MAX_KNOCKBACK_TIME)
					cancelFall(attack, "on ground too long after attack");

				if (attack.isSwimming && time - attack.time > MAX_KNOCKBACK_TIME)
					cancelFall(attack, "in water too long after attack");

				if (attack.isClimbing && time - attack.time > MAX_KNOCKBACK_TIME)
					cancelFall(attack, "on ladder/vine too long after attack");
			}
		}
	}

	private void scheduleCheckAttackCancelled(final Attack attack, long delay) {
		new SchedulerUtil() {
			@Override
			public void runnable() {
				checkAttackCancelled(attack, timer.getTicks());
			}
		}.later(delay + 1); // CraftBukkit runs tasks a tick early, so add one
	}

	private void checkKnockback(final Attack attack, long time) {
		if (!attack.wasAttacked && !isSupported(attack) && time - attack.time <= MAX_KNOCKBACK_TIME) attack.wasAttacked = true;
	}

	/**
	 * Called when a tracked player lands on the ground. Schedule an attack
	 * timeout check.
	 * 
	 * @param attack
	 *            Fall being tracked
	 * @param time
	 *            Time they hit the ground
	 */
	private void victimOnGround(final Attack attack, long time) {
		scheduleCheckAttackCancelled(attack, MAX_ON_GROUND_TIME + 1);
	}

	/**
	 * Called when a tracked player leaves the ground. If this happens soon
	 * after an attack, confirm that knockback has happened.
	 * 
	 * @param attack
	 *            Fall being tracked
	 * @param time
	 *            Time they left the ground
	 */
	private void victimOffGround(Attack attack, long time) {
		checkKnockback(attack, time);
	}

	/**
	 * Called when a tracked player grabs a ladder or vine. Schedule an attack
	 * timeout check.
	 * 
	 * @param attack
	 *            Fall being tracked
	 * @param time
	 *            Time they first touched the ladder/vine
	 */
	private void victimOnLadder(Attack attack, long time) {
		scheduleCheckAttackCancelled(attack, MAX_CLIMBING_TIME + 1);
	}

	/**
	 * Called when a tracked player lets go of a ladder or vine. If this happens
	 * soon after an attack, confirm that knockback has happened.
	 * 
	 * @param attack
	 *            Fall being tracked
	 * @param time
	 *            Time they let go of the ladder/vine
	 */
	private void victimOffLadder(Attack attack, long time) {
		checkKnockback(attack, time);
	}

	/**
	 * Called when a tracked player enters water. Schedule an attack timeout
	 * check.
	 * 
	 * @param attack
	 *            Fall being tracked
	 * @param time
	 *            Time they first touched the water
	 */
	private void victimInWater(Attack attack, long time) {
		scheduleCheckAttackCancelled(attack, MAX_SWIMMING_TIME + 1);
	}

	/**
	 * Called when a tracked player leaves water. If they were in the water for
	 * more than MAX_SWIMMING_TIME, the fall is discarded.
	 * 
	 * @param attack
	 *            Fall being tracked
	 * @param time
	 *            Time they stopped touching the water
	 */
	private void victimOutOfWater(Attack attack, long time) {
		checkKnockback(attack, time);
	}

	/**
	 * Called when a tracked player enters lava.
	 * 
	 * @param attack
	 *            Fall being tracked
	 * @param time
	 *            Time they first touched the lava
	 */
	private void victimInLava(Attack attack, long time) {
	}

	/**
	 * Called when a tracked player leaves lava. Check for the attack being
	 * cancelled, which may have been deferred while they were in the lava.
	 * 
	 * @param attack
	 *            Fall being tracked
	 * @param time
	 *            Time they stopped touching the lava
	 */
	private void victimOutOfLava(Attack attack, long time) {
		checkAttackCancelled(attack, time);
	}

	/**
	 * Called when a player who is NOT being tracked leaves the ground. If we
	 * can find a block that was broken under them recently, we start tracking
	 * the fall as a spleefing.
	 * 
	 * @param player
	 *            Player who left the ground
	 * @param time
	 *            Time they left the ground
	 */
	private void nonVictimOffGround(Player player, long time) {
		BrokenBlock brokenBlock = findBlockBrokenUnderPlayer(player, time);
		if (brokenBlock != null) {
			Attack attack = new Attack(brokenBlock.breaker, Attack.Cause.SPLEEF, player, Attack.From.FLOOR, brokenBlock.time);

			attack.isClimbing = playerBlockChecker.isPlayerClimbing(player);
			attack.isSwimming = playerBlockChecker.isPlayerSwimming(player, Material.WATER);
			attack.isInLava = playerBlockChecker.isPlayerSwimming(player, Material.LAVA);

			attack.wasAttacked = true; // Confirm the spleef attack immediately

			attacks.put(player, attack);
		}
	}

	/**
	 * Called when a player is damaged by another entity (which could be
	 * anything, not necessarily living). Ignore if the player is in lava,
	 * because there is a good chance they are already doomed.
	 * 
	 * @param player
	 *            Player who was attacked
	 * @param damager
	 *            Entity that attacked/damaged them
	 * @param time
	 *            Time of the attack
	 */
	private void playerAttacked(Player player, Entity damager, long time) {
		if (attacks.containsKey(player))
			return;

		boolean isInLava = playerBlockChecker.isPlayerSwimming(player, Material.LAVA);

		if (isInLava)
			return;

		boolean isClimbing = playerBlockChecker.isPlayerClimbing(player);
		boolean isSwimming = playerBlockChecker.isPlayerSwimming(player, Material.WATER);

		Attack.Cause cause;
		if (damager instanceof Projectile) {
			damager =(Entity)((Projectile) damager).getShooter();
			cause = Attack.Cause.SHOOT;
		} else {
			cause = Attack.Cause.HIT;
		}

		Attack.From from = null;
		if (isClimbing) {
			from = Attack.From.LADDER;
		} else if (isSwimming) {
			from = Attack.From.WATER;
		} else {
			from = Attack.From.FLOOR;
		}

		Attack attack = new Attack(damager, cause, player, from, time);

		attack.isClimbing = isClimbing;
		attack.isSwimming = isSwimming;
		attack.isInLava = isInLava;

		// Confirm the attack immediately if the victim is already falling
		attack.wasAttacked = !isSupported(attack);

		attacks.put(player, attack);

		if (!attack.wasAttacked)
			scheduleCheckAttackCancelled(attack, MAX_KNOCKBACK_TIME + 1);
	}

	/**
	 * Called when a falling player dies to decide if the death was caused by
	 * the fall.
	 * 
	 * @param attack
	 *            The fall being tracked
	 * @param damageCause
	 *            Cause of death
	 * @param time
	 *            Time of death
	 * @return true if death was caused by the fall
	 */
	private boolean wasAttackFatal(Attack attack, EntityDamageEvent.DamageCause damageCause, long time) {
		switch (damageCause) {
		case VOID:
		case FALL:
		case LAVA:
		case SUICIDE:
			return true;

		case FIRE_TICK:
			return attack.isInLava;

		default:
			return false;
		}
	}

	/**
	 * Generates a death message based on the various circumstances of a falling
	 * kill.
	 * 
	 * @param attack
	 *            The fall that resulted in a kill
	 * @param damageCause
	 *            The final cause of death
	 * @return A text message describing the kill
	 */
	@SuppressWarnings("deprecation")
	private String makeDeathMessage(Attack attack, EntityDamageEvent.DamageCause damageCause) {
		String attackerText;
		Client victim = Client.getClient(attack.victim);
		String victimText = victim.getTeam().getColor() + attack.victim.getName();
		ChatColor c = ChatColor.GRAY;
		if (attack.attacker instanceof Player) {
			Client attacker = Client.getClient((Player) attack.attacker);
			attackerText = attacker.getTeam().getColor() + attacker.getPlayer().getName();
			if (wasTeamKill(attack, damageCause)) c = ChatColor.DARK_RED;
		} else {
			attackerText = attack.attacker.getType().getName();
		}

		String attackText;
		switch (attack.cause) {
		case HIT:
			attackText = "knocked";
			break;

		case SHOOT:
			attackText = "shot";
			break;

		case SPLEEF:
			attackText = "spleefed";
			break;

		default:
			attackText = "";
			break;
		}

		String siteText;
		switch (attack.from) {
		case LADDER:
			siteText = " off a ladder";
			break;

		case WATER:
			siteText = " out of the water";
			break;

		default:
			siteText = "";
			break;
		}

		String damageText;
		if (attack.from == Attack.From.FLOOR) {
			switch (damageCause) {
			case VOID:
				damageText = " out of the world";
				break;

			case FALL:
				damageText = " off a high place";
				break;

			case LAVA:
			case FIRE_TICK:
				damageText = " into lava";
				break;

			case SUICIDE:
				damageText = " to their death (suicide/battle log)";
				break;

			default:
				damageText = " to their death";
			}
		} else {
			switch (damageCause) {
			case VOID:
				damageText = " and into the void";
				break;

			case FALL:
				damageText = "";
				break;

			case LAVA:
			case FIRE_TICK:
				damageText = " and into lava";
				break;

			case SUICIDE:
				damageText = " to their death (suicide/battle log)";
				break;

			default:
				damageText = " to their death";
			}
		}

		return victimText + c + " was " + attackText + siteText + damageText + c + " by " + attackerText;
	}
	
	private boolean wasTeamSpleef(Attack attack, EntityDamageEvent.DamageCause damageCause) {
		Client victim = Client.getClient(attack.victim);
		Client attacker = null;
		if (attack.attacker instanceof Player) {
			attacker = Client.getClient((Player) attack.attacker);
		}
		
		boolean was = false;

		switch (attack.cause) {
			case SPLEEF:
				was = true;
				break;
			case HIT: break;
			case SHOOT: break;
			default: break;
		}
		
		try {
			if (was) {
				if (!victim.getTeam().equals(attacker.getTeam()) && !(victim.getPlayer().getDisplayName().equals(attacker.getPlayer().getDisplayName()))) was = true;
			}
		} catch (NullPointerException e) {
			return false;
		}

		return was;
	}
	
	private boolean wasTeamKill(Attack attack, EntityDamageEvent.DamageCause damageCause) {
		Client victim = Client.getClient(attack.victim);
		Client attacker = null;
		if (attack.attacker instanceof Player) {
			attacker = Client.getClient((Player) attack.attacker);
		}
		
		boolean was = false;
		
		try {
			if (was) {
				if (!victim.getTeam().equals(attacker.getTeam())) was = true;
			}
		} catch (NullPointerException e) {
			return false;
		}

		return was;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerOnGroundChanged(PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		long time = timer.getTicks();

		if (attacks.containsKey(player)) {
			Attack attack = attacks.get(player);
			if (event.getPlayer().isOnGround()) {
				attack.onGroundTime = time;
				victimOnGround(attack, time);
			} else {
				victimOffGround(attack, time);
			}

		} else {
			nonVictimOffGround(player, time);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		double xmove = event.getFrom().getX() - event.getTo().getX();
		double ymove = event.getFrom().getY() - event.getTo().getY();
		double zmove = event.getFrom().getZ() - event.getTo().getZ();
		if (xmove >= Scrimmage.MINIMUM_MOVEMENT && xmove <= -Scrimmage.MINIMUM_MOVEMENT
				&& ymove >= Scrimmage.MINIMUM_MOVEMENT && ymove <= -Scrimmage.MINIMUM_MOVEMENT
				&& zmove >= Scrimmage.MINIMUM_MOVEMENT && zmove <= -Scrimmage.MINIMUM_MOVEMENT) return;
		Player player = event.getPlayer();

		if (attacks.containsKey(player)) {
			Attack attack = attacks.get(player);
			long time = timer.getTicks();
			boolean isClimbing = playerBlockChecker.isPlayerClimbing(player);
			boolean isSwimming = playerBlockChecker.isPlayerSwimming(player, Material.WATER);
			boolean isInLava = playerBlockChecker.isPlayerSwimming(player, Material.LAVA);

			if (isClimbing != attack.isClimbing) {
				if ((attack.isClimbing = isClimbing)) {
					attack.climbingTime = time;
					victimOnLadder(attack, time);
				} else {
					victimOffLadder(attack, time);
				}
			}

			if (isSwimming != attack.isSwimming) {
				if ((attack.isSwimming = isSwimming)) {
					attack.swimmingTime = time;
					victimInWater(attack, time);
				} else {
					victimOutOfWater(attack, time);
				}
			}

			if (isInLava != attack.isInLava) {
				if ((attack.isInLava = isInLava)) {
					attack.inLavaTime = time;
					victimInLava(attack, time);
				} else {
					victimOutOfLava(attack, time);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) return;
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			long time = timer.getTicks();
			playerAttacked(player, event.getDamager(), time);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Client client = Client.getClient(player);
		EntityDamageEvent.DamageCause damageCause = player.getLastDamageCause().getCause();
		if (attacks.containsKey(player)) {
			long time = timer.getTicks();
			Attack attack = attacks.remove(player);

			if (wasAttackFatal(attack, damageCause, time) && attack.attacker instanceof Player) {
				Player attacker = (Player) attack.attacker;
				String deathMessage = makeDeathMessage(attack, damageCause);
				event.setDeathMessage(deathMessage);
				if (!wasTeamSpleef(attack, damageCause)) {
					Scrimmage.getInstance();
					Scrimmage.callEvent(new PlayerDiedEvent(Scrimmage.getMap(), player, attacker));
				}
				return;
			}
		}

		if (damageCause.equals(DamageCause.DROWNING)) {
			event.setDeathMessage(client.getTeam().getColor() + player.getName() + ChatColor.GRAY + " drowned");
			Scrimmage.getInstance();
			Scrimmage.callEvent(new PlayerDiedEvent(Scrimmage.getMap(), player, null));
			return;
		} else if (damageCause.equals(DamageCause.VOID)) {
			event.setDeathMessage(client.getTeam().getColor() + player.getName() + ChatColor.GRAY + " fell into the void");
			Scrimmage.getInstance();
			Scrimmage.callEvent(new PlayerDiedEvent(Scrimmage.getMap(), player, null));
			return;
		} else if (damageCause.equals(DamageCause.STARVATION)) {
			event.setDeathMessage(client.getTeam().getColor() + player.getName() + ChatColor.GRAY + " starved to death");
			Scrimmage.getInstance();
			Scrimmage.callEvent(new PlayerDiedEvent(Scrimmage.getMap(), player, null));
			return;
		} else if (damageCause.equals(DamageCause.FALLING_BLOCK)) {
			event.setDeathMessage(client.getTeam().getColor() + player.getName() + ChatColor.GRAY + " was squished by a block");
			Scrimmage.getInstance();
			Scrimmage.callEvent(new PlayerDiedEvent(Scrimmage.getMap(), player, null));
			return;
		} else if (damageCause.equals(DamageCause.FALL)) {
			event.setDeathMessage(client.getTeam().getColor() + player.getName() + ChatColor.GRAY + " fell to their death");
			Scrimmage.callEvent(new PlayerDiedEvent(Scrimmage.getMap(), player, null));
			return;
		}

		Player t = event.getEntity();
		Player p = null;
		boolean shot = false;
		
		Entity e = t.getKiller();
		if (e instanceof Player) {
			p = (Player) e;
			if (damageCause.equals(DamageCause.PROJECTILE)) {
				shot = true;
			}
		}

		if (p != null) {
			Client attacked = Client.getClient(t);
			Client damager = Client.getClient(p);
			Player killer = player.getKiller();
			MapTeam attackedTeam = attacked.getTeam();
			MapTeam damagerTeam = damager.getTeam();
            Material item = killer.getItemInHand().getType();
            String itemstring = item.toString();
            itemstring = itemstring.replaceAll("_", " ").toLowerCase();
			String tName = attackedTeam.getColor() + attacked.getPlayer().getName();
			ChatColor c = ChatColor.GRAY;
			String how = " was slain by ";
			if (shot) {
				how = " was shot by ";
			} else if(itemstring.equals("air")) {
				how = " felt the fury of ";
				itemstring = "fists";
			}
			String dName = damagerTeam.getColor() + damager.getPlayer().getName();
			if (shot) {
				Player killed = event.getEntity().getPlayer();
				double xDifference = Math.abs(killed.getLocation().getX() - killer.getLocation().getX());
				double yDifference = Math.abs(killed.getLocation().getY() - killer.getLocation().getY());
				double zDifference = Math.abs(killed.getLocation().getZ() - killer.getLocation().getZ());
				double firstNumber = Math.sqrt((xDifference * xDifference) + (zDifference * zDifference));
				double blocksAway = Math.sqrt((firstNumber * firstNumber) + (yDifference * yDifference));
				long blocks = Math.round(blocksAway);
				int distance = (int)blocks;
			event.setDeathMessage(tName + c + how + dName + c + " (" + distance + " blocks)");
			} else {
				event.setDeathMessage(tName + c + how + dName + c + "'s " + itemstring);	
			}
			event.setDroppedExp(0);
			Scrimmage.getInstance();
			Scrimmage.callEvent(new PlayerDiedEvent(Scrimmage.getMap(), player, p));
			return;
		}
		
		event.setDeathMessage(null);
		Scrimmage.getInstance();
		Scrimmage.callEvent(new PlayerDiedEvent(Scrimmage.getMap(), player, null));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		// Detect battle logging and credit the kill

		Player player = event.getPlayer();

		if (attacks.containsKey(player)) {
			long time = timer.getTicks();
			Attack attack = attacks.remove(player);
			EntityDamageEvent.DamageCause damageCause = EntityDamageEvent.DamageCause.SUICIDE;

			if (wasAttackFatal(attack, damageCause, time)) {
				Vector<ItemStack> inventory = new Vector<ItemStack>();
				Collections.addAll(inventory, player.getInventory().getContents());

				PlayerDeathEvent deathEvent = new PlayerDeathEvent(player, inventory, 0, makeDeathMessage(attack, damageCause));
				Scrimmage.callEvent(deathEvent);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		final Player player = event.getPlayer();
		long time = timer.getTicks();

		// Start tracking this broken block
		final BrokenBlock brokenBlock = new BrokenBlock(event.getBlock(), player, time);
		final BlockVector bv = new BlockVector(brokenBlock.block.getLocation().toVector());
		brokenBlocks.put(bv, brokenBlock);

		// Schedule the tracking to end after MAX_SPLEEF_TIME
		new SchedulerUtil() {
			public void runnable() {
				if (brokenBlocks.containsKey(bv) && brokenBlocks.get(bv) == brokenBlock) {
					brokenBlocks.remove(bv);
				}
			}
		}.laterAsync(MAX_SPLEEF_TIME + 1);
	}
}
