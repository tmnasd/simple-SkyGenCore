package listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import grassminevn.skygencore.Main;
import grassminevn.skygencore.Utils;

public class playerCraftListener implements Listener {
	@SuppressWarnings("unused")
	private Main plugin;

	public playerCraftListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void event(CraftItemEvent e) {

		Player p = (Player) e.getWhoClicked();

		if (!p.isOp()) {
			p.sendMessage(Utils.color("&cBạn không thể craft trong SkyGen &4(Quy định của Admin)"));
			e.setCancelled(true);
		} else
			p.sendMessage("Vì có OP nên bạn được phép craft");

	}

}
