package ktm6060.shopsearcher.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import ktm6060.shopsearcher.ShopSearcher;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class ConfigManager {

	private static ShopSearcher plugin = ShopSearcher.getPlugin(ShopSearcher.class);
	
	//Files
	public static FileConfiguration plotsConfig, menusConfig;
	public static File plotsFile, menusFile;
	
	public ConfigManager(ShopSearcher p) {
		plugin = p;
		
		//create plugin directory
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		
		buildConfig();
		buildPlotsConfig();
		buildMenusConfig();
	}
	
	private void buildConfig() {
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveDefaultConfig();
		plugin.getConfig().set("lastScanned.year", 0);
		plugin.saveConfig();
		Tools.setPlugin(plugin);
	}
	
	private void buildPlotsConfig() {
		createPlotsFile();
		
		int numPlotsMain = plugin.getConfig().getInt("numPlotsMain");
		int numPlotsTunnel = plugin.getConfig().getInt("numPlotsTunnel");
		int numPlots = 0;
		String[] directions = {"NW", "NE", "SW", "SE", "N", "E", "S", "W"};
		
		for (int i = 0; i < directions.length; i++) {
			if (i < 4)
				numPlots = numPlotsMain;
			else
				numPlots = numPlotsTunnel;
			
			for (int j = 1; j <= numPlots; j++)
				plotsConfig.addDefault(directions[i] + "." + j, "");
		}
		plotsConfig.options().header("List of owners of each plot.");
		plotsConfig.options().copyDefaults(true);
		savePlotsConfig();
	}
	
	private void buildMenusConfig() {
		createMenusFile();
		
		menusConfig.addDefault("Search-By-Plot-Icon", "BARRIER");
		menusConfig.addDefault("Search-By-Item-Icon", "PAPER");
		
		menusConfig.options().copyDefaults(true);
		saveMenusConfig();
	}
	
	public void createPlotsFile() {
		plotsFile = new File(plugin.getDataFolder(), "plots.yml");
		
		if (!plotsFile.exists()) {
			try {
				plotsFile.createNewFile();
			} catch (IOException e) {
				Utils.print("Could not create plots.yml file.");
			}
		}
		
		plotsConfig = YamlConfiguration.loadConfiguration(plotsFile);
		Utils.print("plots.yml file created successfully.");
	}
	
	public void createMenusFile() {
		menusFile = new File(plugin.getDataFolder(), "menus.yml");
		
		if (!menusFile.exists()) {
			try {
				menusFile.createNewFile();
			} catch (IOException e) {
				Utils.print("Could not create menus.yml file.");
			}
		}
		
		menusConfig = YamlConfiguration.loadConfiguration(menusFile);
		Utils.print("menus.yml file created successfully.");
	}
	
	public static FileConfiguration getPlotsConfig() {
		return plotsConfig;
	}
	
	public static FileConfiguration getMenusConfig() {
		return menusConfig;
	}
	
	public static void saveConfigs() {
		try {
			plotsConfig.save(plotsFile);
		} catch (IOException e) {
			Utils.print("Could not save plots.yml file.");
		}
		try {
			menusConfig.save(menusFile);
		} catch (IOException e) {
			Utils.print("Could not save menus.yml file.");
		}
	}
	
	public static void savePlotsConfig() {
		try {
			plotsConfig.save(plotsFile);
		} catch (IOException e) {
			Utils.print("Could not save plots.yml file.");
		}
	}
	
	public static void saveMenusConfig() {
		try {
			menusConfig.save(menusFile);
		} catch (IOException e) {
			Utils.print("Could not save menus.yml file.");
		}
	}
	
	public static void reloadConfigs() {
		plotsConfig = YamlConfiguration.loadConfiguration(plotsFile);
		menusConfig = YamlConfiguration.loadConfiguration(menusFile);
	}
	
	public static boolean isOnLockdown() {
		return plugin.getConfig().getBoolean("lockdown");
	}
}
