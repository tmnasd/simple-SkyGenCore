package listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import grassminevn.skygencore.Main;
import manager.GENERATORdatabaseManager;
import manager.PLAYERdatabaseManager;
import manager.actionbarManager;
import manager.tempBoosterManager;
import storage.player.playerData;

public class playerJoinListener implements Listener {
	private Main plugin;

	public playerJoinListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void event(PlayerJoinEvent e) {

		String pName = e.getPlayer().getName();
		playerData data = PLAYERdatabaseManager.getData(pName);

		if (data == null) {
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> GENERATORdatabaseManager.load(pName));
		}

		new BukkitRunnable() {

			@Override
			public void run() {
				actionbarManager.runActionBar(e.getPlayer());

			}
		}.runTaskLater(plugin, 20 * 2);

		tempBoosterManager.runTempBooster(pName);

	}

}
