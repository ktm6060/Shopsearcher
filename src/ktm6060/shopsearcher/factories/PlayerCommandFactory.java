package ktm6060.shopsearcher.factories;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.interfaces.Cmd;
import ktm6060.shopsearcher.ui.ShopSearchMenuUI;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class PlayerCommandFactory {
	public static Cmd getCommand(Command commmand, String[] args) {
		String cmd = commmand.getLabel().toLowerCase();
		switch (cmd) {
		case "myshop":
			return new MyShopCmd();
		case "search":
			return new SearchCmd(args);
		case "shop":
			return new ShopCmd();
		default:
			break;
		}
		return null;
	}
}

class MyShopCmd implements Cmd {
	@Override
	public void execute(Player player) {
		Tools.openMyShop(player);
	}
}

class SearchCmd implements Cmd {
	String material;
	
	public SearchCmd(String[] args) {
		this.material = args[0];
	}
	
	@Override
	public void execute(Player player) {
		ItemStack item = null;
		
		try {
			item = new ItemStack(Material.matchMaterial(material));
		} catch (Exception e) {
			player.sendMessage(Utils.format("&4Invalid material ID. Use valid namespaced ID."));
			return;
		}
		
		Tools.viewOffers(player, item, -1);
	}
}

class ShopCmd implements Cmd {
	@Override
	public void execute(Player player) {
		player.openInventory(ShopSearchMenuUI.GUI(player));
	}
}
