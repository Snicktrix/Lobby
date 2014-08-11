package net.Snicktrix.Lobby;

/**
 * Created by Luke on 8/11/14.
 */
public class Server {
	private String name;
	private int players;
	private int maxPlayers;

	public Server(String name, int players, int maxPlayers) {
		this.name = name;
		this.players = players;
		this.maxPlayers = maxPlayers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPlayers() {
		return players;
	}

	public void setPlayers(int players) {
		this.players = players;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
}
