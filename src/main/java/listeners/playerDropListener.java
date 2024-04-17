package listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import grassminevn.skygencore.Main;

public class playerDropListener implements Listener {
	@SuppressWarnings("unused")
	private Main plugin;

	public playerDropListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public List<Player> confirmDrop = new ArrayList<>();

	@EventHandler
	public void event(PlayerDropItemEvent e) {

		@SuppressWarnings("unused")
		Player p = e.getPlayer();

		/*
		 * if (!confirmDrop.contains(p)) { confirmDrop.add(p); e.setCancelled(true);
		 * p.sendMessage(Utils.
		 * color("&eVui lòng &a&lDROP&e lại 1 lần nữa để xác nhận, bạn có 10 giây để thực hiện"
		 * ));
		 * 
		 * new BukkitRunnable() {
		 * 
		 * @Override public void run() {
		 * 
		 * if (confirmDrop.contains(p)) { confirmDrop.remove(p); }
		 * 
		 * } }.runTaskLater(plugin, 20 * 10); } else { confirmDrop.remove(p);
		 * p.sendMessage(Utils.color("&aDrop thành công")); }
		 */

	}

}
