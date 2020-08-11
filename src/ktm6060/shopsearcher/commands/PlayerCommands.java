package ktm6060.shopsearcher.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ktm6060.shopsearcher.factories.PlayerCommandFactory;
import ktm6060.shopsearcher.interfaces.Cmd;

public class PlayerCommands implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;
		
		Cmd command = PlayerCommandFactory.getCommand(cmd, args);
		if (command != null) {
			command.execute(player);
			return true;
		}
		
		return false;
	}
	
}
