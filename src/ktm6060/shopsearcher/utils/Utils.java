package ktm6060.shopsearcher.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@SuppressWarnings("deprecation")
public class Utils {
	
	public static void print(String str) {
		Bukkit.getConsoleSender().sendMessage("[Shopsearcher] " + str);
	}
	
	public static String format(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public static ItemStack displayItem(Inventory inv, String materialString, int amount, int invSlot, ItemMeta meta) {
		ItemStack item = new ItemStack(Material.matchMaterial(materialString), amount);
		item.setItemMeta(meta);
		inv.setItem(invSlot, item);
		return item;
	}
	
	public static ItemStack createItem(Inventory inv, String materialString, int amount, int invSlot, String displayName, String... loreString) {
		ItemStack item;
		List<String> lore = new ArrayList<String>();
		
		item = new ItemStack(Material.matchMaterial(materialString), amount);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.format(displayName));
		
		for (String str : loreString) {
			lore.add(Utils.format(str));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		inv.setItem(invSlot, item);
		
		return item;
	}
	
	public static ItemStack createItem(Inventory inv, Material material, int amount, int invSlot, String displayName, String... loreString) {
		ItemStack item;
		List<String> lore = new ArrayList<String>();
		
		item = new ItemStack(material, amount);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.format(displayName));
		
		for (String str : loreString) {
			lore.add(Utils.format(str));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		inv.setItem(invSlot, item);
		
		return item;
	}
	
	public static ItemStack createItemByte(Inventory inv, String materialString, int byteId, int amount, int invSlot, String displayName, String... loreString) {
		ItemStack item;
		List<String> lore = new ArrayList<String>();
		
		item = new ItemStack(Material.matchMaterial(materialString), amount, (short) byteId);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.format(displayName));
		
		for (String str : loreString) {
			lore.add(Utils.format(str));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		inv.setItem(invSlot, item);
		
		return item;
	}
	
	public static ItemStack createPlayerSkull(Inventory inv, int amount, int invSlot, String title, String playerName, String... loreString) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        List<String> lore = new ArrayList<String>();
		 
		meta.setOwner(playerName);
		meta.setDisplayName(Utils.format(title));
		
		for (String str : loreString) {
			lore.add(Utils.format(str));
		}
		meta.setLore(lore);
		skull.setItemMeta(meta);
		
		inv.setItem(invSlot, skull);
		
		return skull;
	}
	
	public static String convertToInvisibleString(String str) {
        String hidden = "";
        for (char c : str.toCharArray()) hidden += ChatColor.COLOR_CHAR + "" + c;
        return hidden;
    }
	
	public static String getStringAfterLastNum(String str) {
		for (int i = str.length()-1; i >= 0; i--) {
			if (str.charAt(i) >= 48 && str.charAt(i) <= 57)
				return Utils.format(str.substring(i+1));
		}
		return str;
	}
	
}
