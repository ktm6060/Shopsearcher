package ktm6060.shopsearcher.interfaces;

import org.bukkit.entity.Player;

import ktm6060.shopsearcher.ShopSearcher;

public interface Cmd {
	ShopSearcher plugin = ShopSearcher.getPlugin(ShopSearcher.class);
	public void execute(Player player);
}
