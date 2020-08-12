package ktm6060.shopsearcher.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import ktm6060.shopsearcher.ShopSearcher;
import ktm6060.shopsearcher.factories.UIFactory;
import ktm6060.shopsearcher.ui.ItemSearchUI;
import ktm6060.shopsearcher.ui.MyShopUI;
import ktm6060.shopsearcher.ui.PlotSearchUI;
import ktm6060.shopsearcher.ui.ShopItemsUI;
import ktm6060.shopsearcher.ui.ShopSearchMenuUI;

public class InventoryClickListener implements Listener {
	
	public InventoryClickListener(ShopSearcher plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		String title = trimStr(event.getView().getTitle()), guiName = "";
		
		if(title.equals(trimStr(ShopSearchMenuUI.inventoryName)))
			guiName = "ShopSearcherMenu";
		else if(title.equals(trimStr(PlotSearchUI.inventoryName)))
			guiName = "PlotSearch";
		else if(title.equals(trimStr(ItemSearchUI.inventoryName)))
			guiName = "ItemSearch";
		else if(title.equals(trimStr(ShopItemsUI.inventoryName)))
			guiName = "ShopItems";
		else if(title.equals(trimStr(MyShopUI.inventoryName)))
			guiName = "MyShop";
		
		Player player = (Player) event.getWhoClicked();
		
		if (!guiName.equals("")) event.setCancelled(true);
		
		if (event.getCurrentItem() == null || guiName.equals("")) {
			return;
		}
		
		UIFactory.getUI(guiName).clicked(player, event.getSlot(), event.getCurrentItem(), event.getInventory());
		event.setCancelled(true);
	}

	private String trimStr(String str) {
		String[] breakpoints = {"|", "("};
		
		for (int i = 0; i < breakpoints.length; i++) {
			if (str.contains(breakpoints[i]))
				return str.substring(0,str.indexOf(breakpoints[i]));
		}
		
		return str;
	}
	
}
