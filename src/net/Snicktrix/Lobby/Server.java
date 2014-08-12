package net.Snicktrix.Lobby;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Luke on 8/11/14.
 */
public class Server {
	private String name;
	private int playerCount;
	private ItemStack icon;

	public Server(String name, ItemStack icon) {
		this.name = name;
		this.icon = icon;
		//Default to 0
		this.playerCount = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public void setIcon(ItemStack icon) {
		this.icon = icon;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(int players) {
		this.playerCount = players;
	}

}
