package listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import grassminevn.skygencore.Main;
import manager.PLAYERdatabaseManager;

public class playerDeathListener implements Listener {
	public Main plugin;

	public playerDeathListener(final Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) plugin);
	}

	@EventHandler
	public void event(PlayerDeathEvent e) {
		Entity killer = e.getEntity().getKiller();
		if (e.getEntity().getKiller() instanceof Player && e.getEntity() instanceof Player) {
			Player p = (Player) killer;
			PLAYERdatabaseManager.getData(p.getName()).addKill(1);
			PLAYERdatabaseManager.getData(e.getEntity().getName()).addDie(1);
		}
	}

}