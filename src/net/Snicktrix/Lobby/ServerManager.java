package net.Snicktrix.Lobby;

import org.bukkit.entity.Player;
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
