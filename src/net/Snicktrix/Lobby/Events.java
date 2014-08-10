package net.Snicktrix.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Luke on 8/9/14.
 */
public class Events implements Listener {
	private Lobby lobby;

	public Events(Lobby lobby) {
		this.lobby = lobby;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		//final so runnable can use it
		final Player player = event.getPlayer();

		//We will use a custom broadcast
		event.setJoinMessage("");

		//Create the message for everyone else
		String excludeMsg = ChatColor.GREEN + player.getName() + ChatColor.YELLOW
				+ " has entered the " + ChatColor.AQUA + ChatColor.BOLD.toString() + "ARCADE";

		//Send message to everyone but this player
		lobby.excludeBroadcast(player, excludeMsg);

		//Create the message specifically for the player
		String msg = ChatColor.YELLOW + "Hey " + ChatColor.GREEN + player.getName()
				+ ChatColor.YELLOW + "! Welcome to the " + ChatColor.AQUA + ChatColor.BOLD.toString() + "ARCADE";

		//Send message to the player with a nifty sound
		player.sendMessage(msg);
		player.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 1);

		//Delay firework. Dumb Bukkit bug
		Bukkit.getScheduler().scheduleSyncDelayedTask(lobby, new Runnable() {
			@Override
			public void run() {
				//Set off a random firework
				lobby.randomFirework(player.getLocation());
			}
		}, 20);

	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (!event.getPlayer().isOp()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (!event.getPlayer().isOp()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamageFromEntity(EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
		event.setFoodLevel(20);
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (!event.getPlayer().isOp()) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("You cannot use that command on this server");
		}
	}
}
