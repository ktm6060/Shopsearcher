package ktm6060.shopsearcher.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.utils.Utils;

public class PlotSearchUI extends GUI {
	
	private static Inventory inv;
	public static String inventoryName;
	private static int invRows = 6;
	private static int invBoxes = invRows * 9;
	
	public static void initialize() {
		inventoryName = Utils.format("&8Plot Search");
		inv = Bukkit.createInventory(null, invBoxes);
	}
	
	public static Inventory GUI(Player player) {
		return GUI(player, 0);
	}
	
	public static Inventory GUI(Player player, int page) {
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		inv.clear();
		
		String[] directions = {"NW", "NE", "SW", "SE", "N", "E", "S", "W"};
		String[] directionsFull = {"North West", "North East", "South West", "South East", "North", "East", "South", "West"};
		
		// TODO build UI icons
		
		Utils.createItem(inv, "barrier", 1, 49, "&CGo Back");
		
		toReturnInventory.setContents(inv.getContents());
		return toReturnInventory;
	}
	
	public void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.format("&CGo Back"))) {
			player.openInventory(ShopSearchMenuUI.GUI(player));
		}
		else if (clicked.getItemMeta().getDisplayName().contains(Utils.format("Page ")))
		{
			int target = Integer.parseInt(clicked.getItemMeta().getDisplayName().substring(7));
			player.openInventory(PlotSearchUI.GUI(player, target));
		}
		
		// TODO handle clicked icons for any plot
	}

}
