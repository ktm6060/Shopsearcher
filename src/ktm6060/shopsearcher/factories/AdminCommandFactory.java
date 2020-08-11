package ktm6060.shopsearcher.factories;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import ktm6060.shopsearcher.interfaces.Cmd;
import ktm6060.shopsearcher.managers.ConfigManager;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class AdminCommandFactory {
	public static Cmd getCommand(Command command, String[] args) {
		String cmd = command.getLabel().toLowerCase();
		if (!(cmd.equalsIgnoreCase("ss") || cmd.equalsIgnoreCase("shopsearcher")))
			return null;
			
		switch (args[0]) {
		case "reload":
			return new ReloadCmd();
		case "scan":
			return new ScanCmd();
		case "unlock":
			return new UnlockCmd();
		default:
			break;
		}
		return null;
	}
}

class ReloadCmd implements Cmd {
	@Override
	public void execute(Player player) {
		plugin.reloadConfig();
		plugin.saveConfig();
		ConfigManager.saveConfigs();
		ConfigManager.reloadConfigs();
		player.sendMessage(Utils.format("&AShopSearcher plugin reloaded!"));
	}
}

class ScanCmd implements Cmd {
	@Override
	public void execute(Player player) {
		Tools.forceUpdateShopItems();
		player.sendMessage(Utils.format("&ASuccessfully scanned shopkeeper data!"));
	}
}

class UnlockCmd implements Cmd {
	@Override
	public void execute(Player player) {
		plugin.getConfig().set("lockdown", false);
		plugin.saveConfig();
		plugin.reloadConfig();
		player.sendMessage(Utils.format("&ASuccessfully unlocked shopsearcher plugin. Everyone can now use it."));
	}
}
