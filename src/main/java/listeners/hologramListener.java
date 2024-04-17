package listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import grassminevn.skygencore.Main;
import manager.GENERATORdatabaseManager;

public class hologramListener implements Listener {
	@SuppressWarnings("unused")
	private Main plugin;

	public hologramListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler

	public void manipulate(PlayerArmorStandManipulateEvent e) {
		if (GENERATORdatabaseManager.generatorHologramLine1.containsValue(e.getRightClicked())
				|| GENERATORdatabaseManager.generatorHologramLine2.containsValue(e.getRightClicked())
		/*
		 * || GENERATORdatabaseManager.generatorHologramLine3.containsValue(e.
		 * getRightClicked())
		 */) {
			e.setCancelled(true);
		}
	}

}
