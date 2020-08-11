package ktm6060.shopsearcher.listeners;

import ktm6060.shopsearcher.ShopSearcher;

public class Listeners {
	public static void initialize(ShopSearcher plugin) {
		new InventoryClickListener(plugin);
	}	
}
