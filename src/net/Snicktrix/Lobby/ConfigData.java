package net.Snicktrix.Lobby;

/**
 * Created by Luke on 8/1/14.
 */
public class ConfigData {
    private Lobby lobby;

    public ConfigData (Lobby lobby) {
        this.lobby = lobby;

        this.loadConfig();
    }

    private void loadConfig() {
        //Set up the config
        this.lobby.getConfig().options().copyDefaults(true);
        this.lobby.saveDefaultConfig();

		for (String name : lobby.getConfig().getStringList("Servers")) {

		}

    }

}

