package ktm6060.shopsearcher;

import org.bukkit.plugin.java.JavaPlugin;

import ktm6060.shopsearcher.commands.Commands;
import ktm6060.shopsearcher.listeners.Listeners;
import ktm6060.shopsearcher.managers.Managers;
import ktm6060.shopsearcher.ui.UserInterfaces;

public class ShopSearcher extends JavaPlugin {
	
	@Override
	public void onEnable() {
		Commands.initialize(this);
		Managers.initialize(this);
		UserInterfaces.initialize(this);
		Listeners.initialize(this);
	}
	
}
