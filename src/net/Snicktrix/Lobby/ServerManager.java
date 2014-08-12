package net.Snicktrix.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Luke on 8/11/14.
 */
public class ServerManager implements PluginMessageListener {
	private Lobby lobby;

	private ArrayList<Server> servers;

	public ServerManager(Lobby lobby) {
		this.lobby = lobby;
		this.servers = new ArrayList<Server>();
	}

	public void addServer(String name, String materialName, String itemName) {
		Material material = Material.getMaterial(materialName);

		if (material == null) {
			//Freak out
			System.out.println("[LOBBY ERROR] MATERIAL NOT LOADED CORRECTLY");
		}

		ItemStack icon = new ItemStack(material);
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName(itemName);
		icon.setItemMeta(meta);

		Server server = new Server(name, icon);
		this.servers.add(server);
		this.pingServerPlayers(name);
	}

	private void pingServerPlayers(String name) {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);

			out.writeUTF("PlayerCount");
			out.writeUTF(name);

			Bukkit.getServer().sendPluginMessage(this.lobby, "BungeeCord", b.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Get server by name
	private Server getServer(String name) {
		for (Server server : this.servers) {
			if (server.getName().equals(name)) {
				return server;
			}
		}
		return null;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) return;

		try {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
			String command = in.readUTF();

			if (command.equals("PlayerCount")) {
				String server = in.readUTF();
				int playerCount = in.readInt();

				System.out.println("Server " + server + " has " + playerCount + " player(s).");
				setServerPlayerCount(server, playerCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setServerPlayerCount(String name, int playerCount) {
		Server server = getServer(name);
		server.setPlayerCount(playerCount);
	}

	public void connectPlayerToServer(Player player, String serverName) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);

		try {
			out.writeUTF("Connect");
			out.writeUTF(serverName); // Target Server
		} catch (IOException e) {
			// Can never happen
		}
		player.sendPluginMessage(lobby, "BungeeCord", b.toByteArray());
	}

	public void setDefaultInventory(Player player) {
		PlayerInventory inv = player.getInventory();

		player.setHealth(20);
		player.setFoodLevel(20);

		inv.setArmorContents(null);

		int i = 0;
		for (Server server : servers) {
			inv.setItem(i, server.getIcon());
			i++;
		}

	}

	public Server getServer(ItemStack itemStack) {
		for (Server server : servers) {
			if (server.getIcon().equals(itemStack)) {
				return server;
			}
		}
		return null;
	}

	public void startPlayerCountUpdater() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(lobby, new Runnable() {
			@Override
			public void run() {
					for (Server server : servers) {
						pingServerPlayers(server.getName());
					}
			}
		}, 0, 20 * 5);
	}

}
