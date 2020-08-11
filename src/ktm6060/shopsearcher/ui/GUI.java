package ktm6060.shopsearcher.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.ShopSearcher;
import ktm6060.shopsearcher.utils.Utils;

public abstract class GUI {
	
	protected static ShopSearcher plugin;
	protected static Inventory inv;
	protected static String inventoryName;
	protected static int invRows = 6;
	protected static int invBoxes = invRows * 9;
	protected static int currPage = 1;
	protected static int numPages = 0;
	
	public static void initialize(ShopSearcher p) {
		plugin = p;
	}
	
	public static void initialize() {
		inventoryName = Utils.format("&8GUI (Page " + currPage + " of " + numPages + ")");
		inv = Bukkit.createInventory(null, invBoxes);
	}
	
	public static void setCurrPage(int cp) {
		currPage = cp;
	}
	
	public static void setInvName(String name) {
		inventoryName = name;
	}
	
	public static String getInvName() {
		return inventoryName;
	}
	
	public abstract void clicked(Player player, int slot, ItemStack clicked, Inventory inv);
}
