package ktm6060.shopsearcher.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.objects.ShopItem;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class ShopItemsUI extends GUI {

	private static Inventory inv;
	public static String inventoryName;
	private static int invRows = 6;
	private static int invBoxes = invRows * 9;
	private static int currPage = 1;
	private static HashMap<String, Integer> userBackToPage = new HashMap<String, Integer>();
	private static HashMap<String, ArrayList<ShopItem>> userShopItems = new HashMap<String, ArrayList<ShopItem>>();
	
	public static void initialize() {
		inventoryName = Utils.format("&8Offers");
		inv = Bukkit.createInventory(null, invBoxes);
	}
	
	public static Inventory GUI(Player player, ArrayList<ShopItem> list, int backToPage) {
		currPage = 1;
		userBackToPage.put(player.getName(), backToPage);
		userShopItems.put(player.getName(), list);
		return GUI(player);
	}
	
	public static Inventory GUI(Player player, int page) {
		currPage = page;
		return GUI(player);
	}
	
	public static Inventory GUI(Player player) {
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		inv.clear();
		
		numPages = Tools.getNumPagesItems(userShopItems.get(player.getName()));
		Tools.displayShopItems(inv, userShopItems.get(player.getName()), currPage);
		Tools.setPageSwitchingIcons(inv, numPages, currPage);
		
		Utils.createItem(inv, "barrier", 1, 49, "&CGo Back");
		
		toReturnInventory.setContents(inv.getContents());
		return toReturnInventory;
	}
	
	public static Inventory limitedGUI(Player player, int page) {
		return limitedGUI(player, userShopItems.get(player.getName()), page);
	}
	
	public static Inventory limitedGUI(Player player, ArrayList<ShopItem> list, int page) {
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		inv.clear();
		
		currPage = page;
		userShopItems.put(player.getName(), list);
		
		numPages = Tools.getNumPagesItems(list);
		Tools.displayShopItems(inv, list, currPage);
		Tools.setPageSwitchingIcons(inv, numPages, currPage);
		
		toReturnInventory.setContents(inv.getContents());
		return toReturnInventory;
	}
	
	public void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {	
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.format("&CGo Back"))) {
			int backToPage = userBackToPage.get(player.getName());
			userBackToPage.remove(player.getName());
			userShopItems.get(player.getName()).clear();
			userShopItems.remove(player.getName());
			
			player.openInventory(ItemSearchUI.GUI(player, backToPage));
		}
		else if (clicked.getItemMeta().getDisplayName().contains(Utils.format("Page ")))
		{
			//Change page of UI
			int target = Integer.parseInt(clicked.getItemMeta().getDisplayName().substring(7));
			inventoryName = Utils.format("&8Offers");
			if (inv.getItem(49) == null)
				player.openInventory(ShopItemsUI.limitedGUI(player, target));
			else
				player.openInventory(ShopItemsUI.GUI(player, target));
		}
	}
}
