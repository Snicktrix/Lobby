package net.Snicktrix.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

	public void addServer(String name, String materialName) {
		Material material = Material.getMaterial(materialName);

		if (material == null) {
			//Freak out
			System.out.println("[LOBBY ERROR] MATERIAL NOT LOADED CORRECTLY");
		}

		ItemStack icon = new ItemStack(material);

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
		Bukkit.broadcastMessage(name + " has " + Integer.toString(playerCount) + " players(s).");
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

}
