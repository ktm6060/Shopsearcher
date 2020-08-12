package ktm6060.shopsearcher.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.managers.ConfigManager;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class PlotSearchUI extends GUI {
	
	private static Inventory inv;
	public static String inventoryName;
	private static int invRows = 3;
	private static int invBoxes = invRows * 9;
	
	public static void initialize() {
		inventoryName = Utils.format("&8Plot Search | ");
		inv = Bukkit.createInventory(null, invBoxes);
	}
	
	public static Inventory GUI(Player player) {
		return GUI(player, 1);
	}
	
	public static Inventory GUI(Player player, int page) {
		String[] directions = {"NW", "NE", "SW", "SE", "N", "E", "S", "W"};
		String[] directionsFull = {"North West", "North East", "South West", "South East", "North", "East", "South", "West"};
		String plotOwner;
		int cnt = 1;
		
		inventoryName = Utils.format("&8Plot Search | " + directionsFull[page-1]);
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		inv.clear();
		
		// TODO build UI icons
		while (true) {
			plotOwner = ConfigManager.getPlotsConfig().getString(directions[page-1] + "." + cnt);
			if (plotOwner == null) break;
			
			//Utils.createItem(inv, "paper", 1, cnt-1, "&FPlot " + cnt + " ", "&5Owner: " + plotOwner);
			Utils.createItem(inv, "paper", 1, cnt-1, "&FPlot " + cnt + " " + Utils.convertToInvisibleString(directions[page-1]), "&5Owner: " + plotOwner);
			cnt++;
		}
		
		Tools.setPageSwitchingIcons(inv, directions.length, page);
		Utils.createItem(inv, "barrier", 1, inv.getSize()-5, "&CGo Back");
		
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
		else if (clicked.getItemMeta().getDisplayName().contains(Utils.format("Plot ")))
		{
			String itemName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName().substring(0, 5));
			itemName += clicked.getItemMeta().getDisplayName().substring(5).replaceAll("§", "");
			
			int target = Integer.parseInt(itemName.substring(5, itemName.lastIndexOf(" ")));
			String direction = Utils.getStringAfterLastNum(itemName).substring(2).toUpperCase();
			Bukkit.getConsoleSender().sendMessage(direction + target);
			//player.openInventory(PlotUI.GUI(player, target));
		}
	}

}
