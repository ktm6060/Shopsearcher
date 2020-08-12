package ktm6060.shopsearcher.ui;

import ktm6060.shopsearcher.ShopSearcher;

public class UserInterfaces {
	public static void initialize(ShopSearcher plugin) {
		GUI.initialize(plugin);
		ItemSearchUI.initialize();
		MyShopUI.initialize();
		PlotSearchUI.initialize();
		ShopItemsUI.initialize();
		ShopSearchMenuUI.initialize();
	}
}
