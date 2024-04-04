package listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.Plugin;

import grassminevn.skygencore.Main;
import grassminevn.skygencore.Utils;

public class playerFishListener implements Listener {
	public Main plugin;

	public playerFishListener(final Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) plugin);
	}

	@EventHandler
	public void onFish(PlayerFishEvent e) {

		if (e.getCaught() instanceof Player) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(Utils.color("&cKhông thể câu người, hãy câu cá <3"));
		}
	}

}