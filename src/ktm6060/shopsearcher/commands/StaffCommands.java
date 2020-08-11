package ktm6060.shopsearcher.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ktm6060.shopsearcher.factories.StaffCommandFactory;
import ktm6060.shopsearcher.interfaces.Cmd;
import ktm6060.shopsearcher.utils.Utils;

public class StaffCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;
		
		
		Cmd command = StaffCommandFactory.getCommand(cmd, args);
		if (command != null) {
			if (!sender.hasPermission("shopsearcher.staff")) {
				player.sendMessage(Utils.format("&4You do not have permission to execute this command."));
				return true;
			}
			
			command.execute(player);
			return true;
		}
		
		return false;
	}

}
