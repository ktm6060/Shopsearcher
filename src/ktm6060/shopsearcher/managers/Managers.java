package ktm6060.shopsearcher.managers;

import org.bukkit.configuration.file.FileConfiguration;

import ktm6060.shopsearcher.ShopSearcher;

public class Managers {
	
	@SuppressWarnings("unused")
	private static ConfigManager configManager;
	private static ShopSearcher plugin;
	
	public static void initialize(ShopSearcher p) {
		plugin = p;
		configManager = new ConfigManager(plugin);	
	}
	
	public static FileConfiguration getPlotsConfig() {
		return ConfigManager.getPlotsConfig();
	}	
	
	public static FileConfiguration getMenusConfig() {
		return ConfigManager.getMenusConfig();
	}
}
