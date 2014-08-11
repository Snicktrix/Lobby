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
		//Setup config
		saveDefaultConfig();

		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		this.serverManager = new ServerManager(this);

		this.events = new Events(this);
		Bukkit.getPluginManager().registerEvents(this.events, this);
	}

	private void loadConfig() {
		//Set up the config
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();

		for (String name : getConfig().getStringList("Servers")) {
//			serverManager.addServer(name);
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
