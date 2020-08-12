package ktm6060.shopsearcher.ui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.objects.ShopItem;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class ItemSearchUI extends GUI {
	
	private static Inventory inv;
	public static String inventoryName;
	private static int invRows = 6;
	private static int invBoxes = invRows * 9;
	private static int currPage = 1;
	public static int numPages = 0;
	private static ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();
	
	public static void initialize() {
		inventoryName = Utils.format("&8Item Search (Page " + currPage + " of " + numPages + ")");
		inv = Bukkit.createInventory(null, invBoxes);
	}

	public static Inventory GUI(Player player, int page) {
		currPage = page;
		return GUI(player);
	}

	public static Inventory GUI(Player player) {
		numPages = shopItems.size() / 45;
		numPages += shopItems.size() % 45 > 0 ? 1 : 0;
		inventoryName = Utils.format("&8Item Search (Page " + currPage + " of " + numPages + ")");
		
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		inv.clear();
		
		Tools.displayShopItemsOnly(inv, shopItems, currPage, 45);
		Tools.setPageSwitchingIcons(inv, numPages, currPage);
		
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
			//Change page of UI
			int target = Integer.parseInt(clicked.getItemMeta().getDisplayName().substring(7));
			
			if (target < currPage)
				currPage--;
			else if (target > currPage)
				currPage++;
			
			player.openInventory(ItemSearchUI.GUI(player, target));
		}
		else
		{
			Tools.viewOffers(player, clicked, currPage);
		}
	}
	
	public static void setShopItems(ArrayList<ShopItem> shopItems) {
		ItemSearchUI.shopItems = shopItems;
	}
}
