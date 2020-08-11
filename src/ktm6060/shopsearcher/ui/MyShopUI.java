package ktm6060.shopsearcher.ui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.objects.ShopKeeperData;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class MyShopUI extends GUI {
	
	private static Inventory inv;
	public static String inventoryName;
	private static int invRows = 6;
	private static int invBoxes = invRows * 9;
	private static int currPage = 1;
	private static int numPages = 0;
	private static ArrayList<ShopKeeperData> shopkeepers = new ArrayList<ShopKeeperData>();
	
	public static void initialize() {
		inventoryName = Utils.format("&8My Shop (Page " + currPage + " of " + numPages + ")");
		inv = Bukkit.createInventory(null, invBoxes);
	}
	
	public static Inventory GUI(Player player, int page) {
		currPage = page;
		return GUI(player);
	}

	public static Inventory GUI(Player player) {
		shopkeepers = Tools.getShopKeeperDataByOwner(player.getName());
		numPages = Tools.getNumPages(shopkeepers);
		inventoryName = Utils.format("&8My Shop (Page " + currPage + " of " + numPages + ")");
		
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		inv.clear();
		
		Tools.displayShopKeeperItems(inv, shopkeepers, currPage);
		shopkeepers.clear();
		
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
			
			player.openInventory(MyShopUI.GUI(player));
		}
	}
	
}
