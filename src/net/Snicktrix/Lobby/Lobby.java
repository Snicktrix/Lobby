package net.Snicktrix.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Luke on 8/9/14.
 */
public class Lobby extends JavaPlugin {
	private Events events;
	private ServerManager serverManager;

	@Override
	public void onEnable() {
		this.serverManager = new ServerManager(this);

		Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this.serverManager);
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		this.events = new Events(this);
		Bukkit.getPluginManager().registerEvents(this.events, this);

		loadServersFromConfig();
	}

	private void loadConfig() {
		//Set up the config
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();

		for (String s : getConfig().getKeys(false)) {
			if (!s.equalsIgnoreCase("Spawn")) {

			}
		}

	}

	private void loadServersFromConfig() {
		//Set up the config
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();

		for (String s : getConfig().getKeys(false)) {
			if (!s.equalsIgnoreCase("Spawn")) {
				String name = getConfig().getString(s + ".name");
				String materialName = getConfig().getString(s + ".material");
				serverManager.addServer(name, materialName);
				Bukkit.broadcastMessage("added server " + name + " with material " + materialName);
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("testconnect")) {
			serverManager.connectPlayerToServer((Player) sender, "KD-1");
			return true;
		}
		return false;
	}

	//**********  UTILS **********//

	public void excludeBroadcast(Player exclude, String msg) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			//Make sure it isn't the excluded player
			if (player != exclude) {
				player.sendMessage(msg);
			}
		}
	}
}
