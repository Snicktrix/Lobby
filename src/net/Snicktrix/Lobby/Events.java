package net.Snicktrix.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

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
		Player player = event.getPlayer();
		event.setJoinMessage("");

		//Create the message for everyone else
		String excludeMsg = ChatColor.GREEN + player.getName() + ChatColor.YELLOW
				+ " has entered the " + ChatColor.AQUA + ChatColor.BOLD.toString() + "ARCADE";
		lobby.excludeBroadcast(player, excludeMsg);

		//Create the message specifically for the player
		String msg = ChatColor.YELLOW + "Hey " + ChatColor.GREEN + player.getName()
				+ ChatColor.YELLOW + "! Welcome to the " + ChatColor.AQUA + ChatColor.BOLD.toString() + "ARCADE";
		player.sendMessage(msg);
		player.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 1);

		lobby.serverManager.setDefaultInventory(player);

	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ItemStack item = event.getItem();

			//If the item is an icon for a server
			if (lobby.serverManager.getServer(item) != null) {
				event.setCancelled(true);
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_PLING, 10, 1);
				Server server = lobby.serverManager.getServer(item);
				lobby.serverManager.connectPlayerToServer(event.getPlayer(), server.getName());
				Bukkit.broadcastMessage("Connected player to server");
			} else {
				Bukkit.broadcastMessage("ITEM WAS NULL");
			}
		} else {
			Bukkit.broadcastMessage("ACTION WAS NOT RIGHT");
		}
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
