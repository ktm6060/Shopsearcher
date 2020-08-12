package ktm6060.shopsearcher.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.managers.Managers;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class ShopSearchMenuUI extends GUI {
	
	private static Inventory inv;
	public static String inventoryName;
	private static int invRows = 1;
	private static int invBoxes = invRows * 9;
	
	public static void initialize() {
		inventoryName = Utils.format("&8Shop Searcher Menu");
		inv = Bukkit.createInventory(null, invBoxes);
	}
	
	public static Inventory GUI(Player player) {
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		
		Utils.createItem(inv, Managers.getMenusConfig().getString("Search-By-Plot-Icon"), 1, 0, "&FSearch By Plot", "&3See what items a plot sells!");
		Utils.createItem(inv, Managers.getMenusConfig().getString("Search-By-Item-Icon"), 1, 1, "&FSearch By Item", "&5See all items being sold!");
		Utils.createPlayerSkull(inv, 1, 2, "&FMy Shop", player.getName(), "&7Check your shops items and stock!");
		
		toReturnInventory.setContents(inv.getContents());
		return toReturnInventory;
	}
	
	public void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if (clicked.getItemMeta().getDisplayName().contains(Utils.format("Search By Plot"))) {
			
			//Bukkit.getConsoleSender().sendMessage(ConfigManager.getPlotsConfig().getString("NW.1"));
			//Bukkit.getConsoleSender().sendMessage(ConfigManager.getPlotsConfig().getString("NW.15"));
			
			player.openInventory(PlotSearchUI.GUI(player));
			//player.sendMessage(Utils.format("&4Plot search not yet implemented."));
		} else if (clicked.getItemMeta().getDisplayName().contains(Utils.format("Search By Item"))) {
			player.openInventory(ItemSearchUI.GUI(player, 1));
		} else if (clicked.getItemMeta().getDisplayName().contains(Utils.format("My Shop"))) {
			Tools.openMyShop(player);
		}
	}

}
