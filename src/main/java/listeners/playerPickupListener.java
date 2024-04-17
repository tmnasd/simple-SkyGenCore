package listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import grassminevn.skygencore.Main;
import grassminevn.skygencore.Utils;
import manager.PLAYERdatabaseManager;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;

public class playerPickupListener implements Listener {
	@SuppressWarnings("unused")
	private Main plugin;

	public playerPickupListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public HashMap<String, Long> cooldowns = new HashMap<String, Long>();

	@EventHandler
	public void event(EntityPickupItemEvent e) {

		if (!(e.getEntity() instanceof Player))
			return;
		e.setCancelled(true);

		ItemStack itemstack = e.getItem().getItemStack();
		String itemName = itemstack.getItemMeta().getDisplayName();
		ItemStack newItem = itemstack;
		boolean itemgeneratorItem = false;

		if (itemName != null)
			if (itemName.contains("MATERIAL_") || itemName.contains("MMOITEM_")) {
				itemgeneratorItem = true;
			}

		Player p = (Player) e.getEntity();

		if (itemgeneratorItem) {
			newItem.setAmount((int) (itemstack.getAmount() * Utils.finalBooster(p)));
		}

		if (fillItem(p, (itemgeneratorItem ? newItem : itemstack), newItem.getAmount())) {
			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 2);
			e.getItem().remove();

			if (itemgeneratorItem)
				PLAYERdatabaseManager.getData(p.getName()).addNhatItem(1);
		}

	}

	public boolean fillItem(Player p, ItemStack item, int amount) {
		String itemName = item.getItemMeta().getDisplayName();

		if (itemName != null) {
			if (itemName.contains("MMOITEM_")) {
				MMOItem MMOItem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(Main.getInstance().getConfig().getString("mmoitem-category")),
						item.getItemMeta().getDisplayName().replace("MMOITEM_", "").toUpperCase());

				if (MMOItem == null) {
					return false;
				}

				item = MMOItem.newBuilder().build();
				item.setAmount(amount);

			}

			if (itemName.contains("MATERIAL_")) {
				item = new ItemStack(Material.valueOf(itemName.replace("MATERIAL_", "")));
				item.setAmount(amount);
			}

		}

		p.getInventory().addItem(item);
		return true;
	}

}
