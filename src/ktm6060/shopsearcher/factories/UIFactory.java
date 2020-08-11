package ktm6060.shopsearcher.factories;

import ktm6060.shopsearcher.ui.GUI;
import ktm6060.shopsearcher.ui.ItemSearchUI;
import ktm6060.shopsearcher.ui.MyShopUI;
import ktm6060.shopsearcher.ui.ShopItemsUI;
import ktm6060.shopsearcher.ui.ShopSearchMenuUI;

public class UIFactory {
	public static GUI getUI(String invName) {
		switch (invName) {
		case "ShopSearcherMenu":
			return new ShopSearchMenuUI();
		case "ItemSearch":
			return new ItemSearchUI();
		case "ShopItems":
			return new ShopItemsUI();
		case "MyShop":
			return new MyShopUI();
		}
		return null;
	}
}
