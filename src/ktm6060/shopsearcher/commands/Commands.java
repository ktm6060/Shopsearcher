package ktm6060.shopsearcher.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import ktm6060.shopsearcher.ShopSearcher;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class Commands implements CommandExecutor, TabExecutor{
	
	public static void initialize(ShopSearcher plugin) {
		String[] commands = {"shop", "myshop", "search", "shopsearcher", "ss", "plot"};
		
		for (int i = 0; i < commands.length; i++)
			plugin.getCommand(commands[i]).setExecutor(new Commands());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Tools.updateShopItems();
		
		if (!Tools.allowCommandExecution(sender)) {
			sender.sendMessage(Utils.format("&CPlugin is currently on lockdown. Something has gone wrong and\ntherefor cannot be used at the moment. We hope to have it\nback soon."));
			return false;
		}
		
		if (new PlayerCommands().onCommand(sender, cmd, label, args))
			return true;
		else if (new StaffCommands().onCommand(sender, cmd, label, args))
			return true;
		else if (new AdminCommands().onCommand(sender, cmd, label, args))
			return true;
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> cmds = new ArrayList<String>();
		if (cmd.getName().equalsIgnoreCase("shopsearcher") || cmd.getName().equalsIgnoreCase("ss")) {
			if (args.length == 1) {
				cmds.add("lockdown");
				cmds.add("plot");
				if (sender.hasPermission("shopsearcher.admin")) {
					cmds.add("reload");
					cmds.add("scan");
					cmds.add("unlock");
				}
			}
			else if (args.length == 2) {
				cmds.add("clear");
				cmds.add("set");
				cmds.add("view");
			}
			
			Collections.sort(cmds);
			return cmds;
		}
		else if (cmd.getName().equalsIgnoreCase("plot")) {
			if (args.length == 1) {
				cmds.add("clear");
				cmds.add("set");
				cmds.add("view");
			}
			
			Collections.sort(cmds);
			return cmds;
		}
		return null;
	}
}
