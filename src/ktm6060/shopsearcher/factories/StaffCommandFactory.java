package ktm6060.shopsearcher.factories;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import ktm6060.shopsearcher.interfaces.Cmd;
import ktm6060.shopsearcher.managers.ConfigManager;
import ktm6060.shopsearcher.utils.Utils;

public class StaffCommandFactory {
	public static Cmd getCommand(Command commmand, String[] args) {
		String cmd = commmand.getLabel().toLowerCase();
		switch (cmd) {
		case "plot":
			return new PlotCmd(args);
		default:
			break;
		}
		
		if (cmd.equalsIgnoreCase("ss")) {
			switch (args[0]) {
			case "lockdown":
				return new LockdownCmd();
			default:
				break;
			}
		}
		
		return null;
	}
}

class LockdownCmd implements Cmd {
	@Override
	public void execute(Player player) {
		if (plugin.getConfig().getBoolean("lockdown"))
			player.sendMessage(Utils.format("&FPlugin is already on lockdown. Only an admin can unlock it."));
		else if (!plugin.getConfig().getBoolean("lockdown")) {
			plugin.getConfig().set("lockdown", true);
			plugin.saveConfig();
			plugin.reloadConfig();
			player.sendMessage(Utils.format("&ASuccessfully locked down shopsearcher plugin. Only staff can now use it."));
		}
		else
			player.sendMessage(Utils.format("&CFailed to read config."));
	}
}

class PlotCmd implements Cmd {
	String[] args;
	Player player;
	
	public PlotCmd(String[] args) {
		this.args = args;
	}
	
	@Override
	public void execute(Player player) {
		this.player = player;
		if (!validateArgs()) return;
		
		if (args[0].equals("set"))
			setPlot();
		else if (args[0].equals("clear"))
			clearPlot();
		else if (args[0].equals("view"))
			viewPlots();
	}
	
	private void setPlot() {
		try {
			ConfigManager.getPlotsConfig().set(args[1].toUpperCase() + "." + args[2], args[3]);
			ConfigManager.saveConfigs();
			ConfigManager.reloadConfigs();
			player.sendMessage(Utils.format("&ASuccessfully set " + args[3] + " as owner of plot " + args[1].toUpperCase() + args[2] + "."));
		} catch (Exception e) {
			player.sendMessage(Utils.format("&4Invalid command arguements."));
		}
	}
	
	private void clearPlot() {
		if (args[1].equalsIgnoreCase("all")) {
			String[] directions = {"NW", "NE", "SW", "SE", "N", "E", "S", "W"};
			int numPlotsMain = plugin.getConfig().getInt("numPlotsMain");
			int numPlotsTunnel = plugin.getConfig().getInt("numPlotsTunnel");
			int numPlots = 0;
			
			for (int i = 0; i < directions.length; i++) {
				if (i > 4)
					numPlots = numPlotsMain;
				else
					numPlots = numPlotsTunnel;
				
				for (int j = 1; j <= numPlots; j++)
					ConfigManager.getPlotsConfig().set(directions[i] + "." + j, "");
			}
			ConfigManager.saveConfigs();
			ConfigManager.reloadConfigs();
		}
		else {
			try {
				ConfigManager.getPlotsConfig().set(args[1].toUpperCase() + "." + args[2], "");
				ConfigManager.saveConfigs();
				ConfigManager.reloadConfigs();
				player.sendMessage(Utils.format("&ASuccessfully cleared ownership of plot " + args[1].toUpperCase() + args[2] + "."));
			} catch (Exception e) {
				player.sendMessage(Utils.format("&4Invalid command arguements."));
			}
		}
	}
	
	private void viewPlots() {
		String msg = "Plot owners for " + args[1].toUpperCase();
		int numPlotsMain = plugin.getConfig().getInt("numPlotsMain");
		int numPlotsTunnel = plugin.getConfig().getInt("numPlotsTunnel");
		int numPlots = 0;
		
		if (args[1].equalsIgnoreCase("N") || args[1].equalsIgnoreCase("E") || args[1].equalsIgnoreCase("S") || args[1].equalsIgnoreCase("W"))
			numPlots = numPlotsTunnel;
		else
			numPlots = numPlotsMain;
		
		for (int i = 1; i <= numPlots; i++)
			msg += "\n" + args[1].toUpperCase() + i + ": " + ConfigManager.getPlotsConfig().get(args[1].toUpperCase() + "." + i);
		
		player.sendMessage(Utils.format(msg));
	}
	
	private boolean validateArgs() {
		String[] directions = {"NW", "NE", "SW", "SE", "N", "E", "S", "W"};
		
		if (args.length > 1) {
			for (int i = 0; i <= directions.length; i++) {
				if (i == directions.length) {
					player.sendMessage(Utils.format("&4Invalid command arguements."));
					return false;
				}
				if (args[1].equalsIgnoreCase(directions[i])) break;
			}
		}
		
		if (args.length > 2) {
			if (args[1].equalsIgnoreCase("N") || args[1].equalsIgnoreCase("E") || args[1].equalsIgnoreCase("S") || args[1].equalsIgnoreCase("W")) {
				if (Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > plugin.getConfig().getInt("numPlotsTunnel")) {
					player.sendMessage(Utils.format("&4Invalid command arguements."));
					return false;
				}
			} else {
				if (Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > plugin.getConfig().getInt("numPlotsMain")) {
					player.sendMessage(Utils.format("&4Invalid command arguements."));
					return false;
				}
			}
		}
		
		return true;
	}
}
