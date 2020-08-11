package ktm6060.shopsearcher.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.SkullMeta;

import ktm6060.shopsearcher.ShopSearcher;
import ktm6060.shopsearcher.managers.ConfigManager;
import ktm6060.shopsearcher.managers.Managers;
import ktm6060.shopsearcher.objects.ShopItem;
import ktm6060.shopsearcher.objects.ShopKeeperData;
import ktm6060.shopsearcher.ui.ItemSearchUI;
import ktm6060.shopsearcher.ui.MyShopUI;
import ktm6060.shopsearcher.ui.ShopItemsUI;

public class Tools {
	
	//private static ArrayList<ShopItem> allShopItems = new ArrayList<ShopItem>();
	private static HashMap<Integer, ArrayList<ShopItem>> allShopItemsMap = new HashMap<Integer, ArrayList<ShopItem>>();
	private static ShopSearcher plugin;
	
	public static void setPlugin(ShopSearcher p) {
		plugin = p;
	}
	
	public static FileConfiguration getSKConfig() {
		FileConfiguration skConfig = null;
		if (Bukkit.getPluginCommand("Shopkeepers").getPlugin().getDataFolder().exists()) {
			File file = new File(Bukkit.getPluginCommand("Shopkeepers").getPlugin().getDataFolder(), "config.yml");
			if (file.exists())
				skConfig = YamlConfiguration.loadConfiguration(file);
		}
		return skConfig;
		
	}

	public static FileConfiguration getSKSaveConfig() {
		FileConfiguration skSaveConfig = null;
		if (Bukkit.getPluginCommand("Shopkeepers").getPlugin().getDataFolder().exists()) {
			File saveFile = new File(Bukkit.getPluginCommand("Shopkeepers").getPlugin().getDataFolder(), "save.yml");
			if (saveFile.exists())
				skSaveConfig = YamlConfiguration.loadConfiguration(saveFile);
		}
		return skSaveConfig;
	}
	
	public static ArrayList<ShopKeeperData> getShopKeeperDataByOwner(String owner) {
		FileConfiguration skSaveConfig = getSKSaveConfig();
		ArrayList<ShopKeeperData> shopkeepers = new ArrayList<ShopKeeperData>();
		String str = "", str2 = "";
		int limit =  10000;
		int breakCnt = 0;
		int breakCntLimit = 50, breakCntExecption = 2500;
		
		boolean breakFlag;
		for (int i = 0; i < limit; i++) {
			breakFlag = true;
			
			try {
				skSaveConfig.get(i + ".owner").equals(null);
			} catch (NullPointerException e) {
				breakFlag = false;
			}
			
			if (breakFlag)
			{
				breakCnt = 0;
				
				//check if owner owns a shop
				if (skSaveConfig.getString(i + ".owner").equals(owner)) {
					
					//check if shop is a sell or trade shop
					if (skSaveConfig.getString(i + ".type").equals("sell") || skSaveConfig.getString(i + ".type").equals("trade")) {
						
						//check if shop has items for sale (at least 1), if so then add Shopkeeper to shopkeepers ArrayList
						str = "" + skSaveConfig.get(i + ".offers.0");
						str2 = "" + skSaveConfig.get(i + ".offers.1");
						if (!str2.equals("null")) {
							shopkeepers.add(new ShopKeeperData(skSaveConfig, i));
						}
						else {
							if (!str.equals("null")) {
								shopkeepers.add(new ShopKeeperData(skSaveConfig, i));
							}
						}
					}
				}
			}
			else if (++breakCnt >= breakCntLimit && i >= breakCntExecption) break;
		}
		
		return shopkeepers;
	}
	
	public static ArrayList<ShopItem> getUniqueShopItems() {
		Utils.print("Scanning for shop items...");
		FileConfiguration skSaveConfig = getSKSaveConfig();
		ArrayList<ShopItem> items = new ArrayList<ShopItem>();
		ShopItem shopItem;
		String str = "", str2 = "";
		int limit =  10000;
		int breakCnt = 0, cnt = 0;
		int breakCntLimit = 50, breakCntExecption = 2500;
		boolean breakFlag, isOldShopkeeper;
		allShopItemsMap.clear();

		for (int i = 0; i < limit; i++) {
			breakFlag = true;
			isOldShopkeeper = false;
			try {	//Check if shopkeeper has owner
				skSaveConfig.get(i + ".owner").equals(null);
			} catch (NullPointerException e) {
				breakFlag = false;
			}
			/*
			 * Check if shopkeeper has at least 1 item for sale/trade
			 * 
			 * Note: Shopkeepers from 2.9.0 offers start at 0, 2.9.2 and above start at 1
			 */
			try {	
				skSaveConfig.getItemStack(i + ".offers.1.item").getItemMeta();
			} catch (NullPointerException e1) {
				try {
					skSaveConfig.getItemStack(i + ".offers.1.item1").getItemMeta();
				} catch (NullPointerException e2) {
					isOldShopkeeper = true;
					try {
						skSaveConfig.getItemStack(i + ".offers.0.item").getItemMeta();
					} catch (NullPointerException e3) {
						try {
							skSaveConfig.getItemStack(i + ".offers.0.item1").getItemMeta();
						} catch (NullPointerException e4) {
							breakFlag = false;
						}
					}
				}
			}
			
			if (breakFlag)
			{
				breakCnt = 0;
				if (isOldShopkeeper)
					cnt = 0;
				else
					cnt = 1;
				
				//add all shopItems to items list
				do {
					shopItem = new ShopItem(skSaveConfig, i, cnt);
					
					if (items.size() == 0)
						items.add(shopItem);
					else {
						for (int j = 0; j < items.size(); j++) {
							
							if (shopItem.toString().equals("ENCHANTED_BOOK")) {
								breakFlag = true;
								EnchantmentStorageMeta meta = (EnchantmentStorageMeta) shopItem.getItemMeta();
								Map<Enchantment, Integer> enchants = meta.getStoredEnchants();
								EnchantmentStorageMeta lMeta = null;
								Map<Enchantment, Integer> lEnchants;
								ArrayList<ShopItem> list;
								
								//check map if book with same enchants has already been found
								if (allShopItemsMap.containsKey(HashString(shopItem.toString()))) {
									list = allShopItemsMap.get(HashString(shopItem.toString()));
									
									if (list.size() > 1) {
										//check list of enchanted books
										for (int index = 0; index < list.size(); index++) {
											try {
												lMeta = (EnchantmentStorageMeta) list.get(index).getItemMeta();
											} catch (ClassCastException e) {
												Utils.print(list.get(index).toString());
												Utils.print(list.toString());
												Utils.print("Enchanted Book HashCode: " + HashString(shopItem.toString()));
												Utils.print(list.get(index).toString() + " HashCode: " + HashString(list.get(index).toString()));
												throw e;
											}
											
											lEnchants = lMeta.getStoredEnchants();
											
											if (enchants.equals(lEnchants)) {
												breakFlag = false;
												break;
											}
										}
									}
								}
								
								
								if (breakFlag) {
									items.add(shopItem);
									break;
								}
								
							} else if (items.get(j).getItemStringSort().equals(shopItem.getItemStringSort()))
								break;
							else if (j == items.size()-1) {
								items.add(shopItem);
								break;
							}
						}
					}
					
					addToMap(shopItem);
					str = "" + skSaveConfig.getString(i + ".offers." + ++cnt + ".item");
					str2 = "" + skSaveConfig.getString(i + ".offers." + cnt + ".item1");
				} while (!str.equals("null") || !str2.equals("null"));
			}
			else if (++breakCnt >= breakCntLimit && i >= breakCntExecption) break;
		}
		items.sort(ShopItem::compareTo);
		Utils.print("Scanning complete.");
		return items;
	}
	
	public static void openMyShop(Player player) {
		FileConfiguration plotConfig = Managers.getPlotsConfig();
		int numPlots = 0;
		String[] directions = {"NW", "NE", "SW", "SE", "N", "E", "S", "W"};
		
		for (int d = 0; d < directions.length; d++) {
			if (d < 4)
				numPlots = plugin.getConfig().getInt("numPlotsMain");
			else
				numPlots = plugin.getConfig().getInt("numPlotsTunnel");
			
			for (int p = 1; p <= numPlots; p++) {
				if (plotConfig.getString(directions[d] + "." + p) != null) {
					if (plotConfig.getString(directions[d] + "." + p).equals(player.getDisplayName()) || plotConfig.getString(directions[d] + "." + p).equals(player.getName())) {
						player.openInventory(MyShopUI.GUI(player, 1));
						return;
					}
				}
			}
		}
		player.sendMessage(Utils.format("&CYou do not own a shop plot."));
	}
	
	public static boolean allowCommandExecution(CommandSender sender) {
		if (ConfigManager.isOnLockdown() && !(sender.hasPermission("shopsearcher.staff") || sender.hasPermission("shopsearcher.admin")))
			return false;
		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static void viewOffers(Player player, ItemStack item, int page) {
		@SuppressWarnings("unchecked")
		ArrayList<ShopItem> list = (ArrayList<ShopItem>) Tools.getAllShopItemsMap().get(Tools.HashString(item.getType().toString())).clone();
		//handle special cases for player heads, enchanted books and renamed items
		for (int i = 0; i < list.size(); i++) {
			if (item.getType().toString().equals("ENCHANTED_BOOK")) {
				if (!list.get(i).getItemMeta().equals(item.getItemMeta()))
					list.remove(i--);
			} else if (item.getType().toString().equals("PLAYER_HEAD")) {
				if (!((SkullMeta) list.get(i).getItemMeta()).getOwner().equals(((SkullMeta) item.getItemMeta()).getOwner()))
					list.remove(i--);
			} else if (!list.get(i).toString().equals(item.getType().toString())) {
				list.remove(i--);
			} else if (!list.get(i).getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
				list.remove(i--);
			}
		}
		
		list.sort(ShopItem::compareToDeal);
		ArrayList<ShopItem> temp = new ArrayList<ShopItem>();
		
		for (int i = 0; i < list.size(); i++)
			temp.add(list.get(i));
		
		if (page == -1)
			player.openInventory(ShopItemsUI.limitedGUI(player, temp, 1));
		else
			player.openInventory(ShopItemsUI.GUI(player, temp, page));
	}
	
	@SuppressWarnings("deprecation")
	public static void updateShopItems() {
		boolean updateItems = false;
		Date date = new Date();
		
		//if no date has yet been recorded, updates once hour has changed (updates every hour)
		if (plugin.getConfig().getInt("lastScanned.year") == 0) {
			updateItems = true;
		} else {	
			if (plugin.getConfig().getInt("lastScanned.year") == date.getYear()) {
				if (plugin.getConfig().getInt("lastScanned.month") >= date.getMonth()) {
					if (plugin.getConfig().getInt("lastScanned.day") >= date.getDate()) {
						if (plugin.getConfig().getInt("lastScanned.hour") < date.getHours())
							updateItems = true;
					}
					else updateItems = true;
				}
				else updateItems = true;
			}
			else updateItems = true;
		}
		
		//update lastScanned date
		if (updateItems) {
			plugin.getConfig().set("lastScanned.minute", date.getMinutes());
			plugin.getConfig().set("lastScanned.hour", date.getHours());
			plugin.getConfig().set("lastScanned.day", date.getDate());
			plugin.getConfig().set("lastScanned.month", date.getMonth());
			plugin.getConfig().set("lastScanned.year", date.getYear());
			plugin.saveConfig();
			plugin.reloadConfig();
			ItemSearchUI.setShopItems(Tools.getUniqueShopItems());
		}
	}
	
	public static void forceUpdateShopItems() {
		ItemSearchUI.setShopItems(Tools.getUniqueShopItems());
	}
	
	public static void displayShopKeeperItems(Inventory inv, ArrayList<ShopKeeperData> shopkeepers, int currPage) {
		displayShopItemInfo(inv, getShopItemsFromShopKeepers(shopkeepers), currPage);
	}
	
	private static int getStock(ShopItem shopItem) {
		Location chestLocation = new Location(shopItem.getChestW(), shopItem.getChestX(), shopItem.getChestY(), shopItem.getChestZ());
		Block chestBlock = chestLocation.getBlock();
		Chest chest = null;
		try {
			chest = (Chest) chestBlock.getState();
		} catch (ClassCastException e) {
			Bukkit.getConsoleSender().sendMessage("[Shopsearcher]: Could not find chest at W:" + shopItem.getChestW().getName() + " X:" + shopItem.getChestX() + " Y:" + shopItem.getChestY() + " Z:" + shopItem.getChestZ());
			return 0;
		}
		Inventory chestInv = chest.getInventory();
		ItemStack[] chestItems = chestInv.getContents();		
		
		int amountItemsInChest = 0;
		for (int j = 0; j < chestItems.length; j++) {
			if (chestItems[j] != null) {
				if (chestItems[j].getType().equals(shopItem.getType()) && chestItems[j].getItemMeta().equals(shopItem.getItemMeta()))
					amountItemsInChest += chestItems[j].getAmount();
			}
		}
		return amountItemsInChest;
	}
	
	public static void displayShopItemsOnly(Inventory inv, ArrayList<ShopItem> shopItems, int currPage, int max) {
		int itemsDisplayed = 0;
		for (int i = (currPage-1)*max; i < shopItems.size(); i++) {
			if (itemsDisplayed >= max) break;
			
			Utils.displayItem(inv, shopItems.get(i).toString(), 1, i - (max*(i/max)), shopItems.get(i).getItemMeta());
			itemsDisplayed++;
		}
	}
	
	public static void displayShopItems(Inventory inv, ArrayList<ShopItem> shopItems, int currPage) {
		displayShopItemInfo(inv, shopItems, currPage);
		displayShopItemLocation(inv, shopItems, currPage);
	}
	
	private static void displayShopItemInfo(Inventory inv, ArrayList<ShopItem> shopItems, int currPage) {
		ShopItem shopItem;
		int cnt = 0, itemsDisplayed = 0, highCurrency, currency;
		//display all items, prices and stock for current page
		for (int i = (currPage-1)*9; i < shopItems.size(); i++) {
			if (itemsDisplayed >= 9) break;
			shopItem = shopItems.get(i);
			Utils.displayItem(inv, shopItem.toString(), shopItem.getAmount(), cnt++, shopItem.getItemMeta());
			
			//set price
			if (shopItem.getPriceItemStack() == null) {
				if (shopItem.getPrice() > 20) {
					highCurrency = shopItem.getPrice() / 9;
					currency = shopItem.getPrice() % 9;
					Utils.createItem(inv, getSKConfig().getString("high-currency-item"), highCurrency, cnt+8, formatMaterialString(getSKConfig().getString("high-currency-item")));
					Utils.createItem(inv, getSKConfig().getString("currency-item"), currency, cnt+17, formatMaterialString(getSKConfig().getString("currency-item")));
				} else
					Utils.createItem(inv, getSKConfig().getString("currency-item"), shopItem.getPrice(), cnt+8, formatMaterialString(getSKConfig().getString("currency-item")));
			}
			else
				Utils.displayItem(inv, shopItem.getPriceItemStack().getType().toString(), shopItem.getPrice(), cnt+8, shopItem.getPriceItemStack().getItemMeta());
			
			
			//get and display stock
			if (getStock(shopItem) / shopItem.getAmount() > 0)
				Utils.createItem(inv, "LIME_CONCRETE", 1, i+27 - (9*(i/9)), "&AIn Stock!");
			else
				Utils.createItem(inv, "RED_CONCRETE", 1, i+27 - (9*(i/9)), "&COut of Stock!");
			
			itemsDisplayed++;
		}
	}
	
	private static void displayShopItemLocation(Inventory inv, ArrayList<ShopItem> shopItems, int currPage) {
		ShopItem shopItem;
		int itemsDisplayed = 0, plot = 0, numPlots = 0;
		FileConfiguration plotConfig = Managers.getPlotsConfig();
		String direction = "Plot ";
		String[] directions = {"NW", "NE", "SW", "SE", "N", "E", "S", "W"};
		
		//display all items, prices and stock for current page
		for (int i = (currPage-1)*9; i < shopItems.size(); i++) {
			if (itemsDisplayed >= 9) break;
			shopItem = shopItems.get(i);
			
			//get floor and plot of owner
			direction = "Plot ";
			plot = 0;
			for (int d = 0; d < directions.length; d++) {
				if (d > 4)
					numPlots = plugin.getConfig().getInt("numPlotsMain");
				else
					numPlots = plugin.getConfig().getInt("numPlotsTunnel");
				
				for (int p = 1; p <= numPlots; p++) {
					if ((plotConfig.getString(directions[d] + "." + p) + "").equals(shopItem.getOwner())) {
						direction = directions[d];
						plot = p;
						break;
					}
				}
			}
			
			Utils.createItem(inv, "PAPER", 1, i+36 - (9*(i/9)), "&F" + direction + plot, "&5Owner: " + shopItem.getOwner());
			itemsDisplayed++;
		}
	}
	
	public static ArrayList<ShopItem> getShopItemsFromShopKeepers(ArrayList<ShopKeeperData> shopkeepers) {
		ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();
		for (int i = 0; i < shopkeepers.size(); i++) {
			for (int j = 0; j < shopkeepers.get(i).getItemsForSale(); j++)
				shopItems.add(shopkeepers.get(i).getItems().get(j));
		}
		return shopItems;
	}
	
	public static void setPageSwitchingIcons(Inventory inv, int numPages, int currPage) {
		if (numPages > 1) {
			if (currPage == 1)
				Utils.createItem(inv, "writable_book", 1, 53, "&6Page " + (currPage + 1));
			else if (currPage == numPages)
				Utils.createItem(inv, "writable_book", 1, 45, "&6Page " + (currPage - 1));
			else {
				Utils.createItem(inv, "writable_book", 1, 53, "&6Page " + (currPage + 1));
				Utils.createItem(inv, "writable_book", 1, 45, "&6Page " + (currPage - 1));
			}
		}
	}
	
	public static int getNumPages(ArrayList<ShopKeeperData> shopkeepers) {
		int totalItemsForSale = 0, pagesNeeded = 0;
		
		for (int i = 0; i < shopkeepers.size(); i++) {
			for (int j = 0; j < (shopkeepers.get(i)).getItemsForSale(); j++)
				totalItemsForSale++;
		}
		
		pagesNeeded = totalItemsForSale / 9;
		if (totalItemsForSale % 9 > 0) pagesNeeded++;
		return pagesNeeded;
	}
	
	public static int getNumPagesItems(ArrayList<ShopItem> shopItems) {
		return shopItems.size() % 9 > 0 ? shopItems.size() / 9 + 1 : shopItems.size() / 9;
	}
	
	public static String formatMaterialString(String materialString) {
		materialString = assignRarityColor(materialString);
		String formatedStr = materialString.substring(0,2) + materialString.substring(2,3);
		for (int i = 3; i < materialString.length(); i++) {
			if (materialString.substring(i,i+1).equals("_"))
				formatedStr += " " + materialString.substring(++i,i+1);
			else
				formatedStr += materialString.substring(i,i+1).toLowerCase();
		}
		return formatedStr;
	}
	
	private static void addToMap(ShopItem shopItem) {
		ArrayList<ShopItem> list = allShopItemsMap.get(HashString(shopItem.toString()));
		if (list == null)
			list = new ArrayList<ShopItem>();
		
		list.add(shopItem);
		allShopItemsMap.put(HashString(shopItem.toString()), list);
	}
	
	@SuppressWarnings("deprecation")
	public static Integer HashString(String str) {
		try {
			return Material.matchMaterial(str).getId();
		} catch (IllegalArgumentException e) {
			return HashString(str, str.length()-1) / 3;
		}
	}
	
	private static Integer HashString(String str, int num) {
		if (num < 0) return 0;
		else
			return str.charAt(num)*(num+3) + HashString(str.substring(0, num), --num);
	}
	
	private static String assignRarityColor(String str) {
		
		switch (str.toLowerCase()) {
		case "creeper_canner_pattern":
		case "skull_banner_pattern":
		case "experience_bottle":
		case "dragon_breath":
		case "elytra":
		case "enchanted_book":
		case "skeleton_skull":
		case "wither_skeleton_skull":
		case "zombie_head":
		case "player_head":
		case "creeper_head":
		case "dragon_head":
		case "heart_of_the_sea":
		case "nether_star":
		case "totem_of_undying":
			str = "&E" + str;
			break;
		case "beacon":
		case "conduit":
		case "end_crystal":
		case "golden_apple":
		case "music_disc_13":
		case "music_disc_cat":
		case "music_disc_blocks":
		case "music_disc_chirp":
		case "music_disc_far":
		case "music_disc_mall":
		case "music_disc_mellohi":
		case "music_disc_stal":
		case "music_disc_strad":
		case "music_disc_ward":
		case "music_disc_11":
		case "music_disc_wait":
			str = "&B" + str;
			break;
		case "mojang_banner_pattern":
		case "command_block":
		case "chain_command_block":
		case "repeating_command_block":
		case "dragon_egg":
		case "enchanted_golden_apple":
		case "structure_block":
		case "jigsaw":
			str = "&O" + str;
			//str = "&5" + str;		megenta, but no color code
			break;
		case "ominous_banner":
			str = "&O" + str;
			//str = "&6" + str;		orange, but no color code
			break;
		default:
			str = "&F" + str;
		}
		
		return str;
	}
	
	public static int plotPrice(int plot) {
		switch (plot) {
		case 1:
		case 7:
		case 13:
		case 19:
			return 48;
		case 4:
		case 10:
		case 16:
		case 22:
			return 16;
		default:
			return 32;
		}
	}
	
	public static HashMap<Integer, ArrayList<ShopItem>> getAllShopItemsMap() {
		return allShopItemsMap;
	}
}
