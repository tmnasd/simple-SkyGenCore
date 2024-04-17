package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import grassminevn.skygencore.Main;
import grassminevn.skygencore.Utils;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class autoCompressorsManager {

	public static List<String> queueCommand = new ArrayList<String>();
	public static List<Player> runTask = new ArrayList<Player>();

	public static int getCooldown(Player p) {

		if (p.isOp()) {
			return 10;
		}

		FileConfiguration config = Main.getInstance().getConfig();

		for (String str : config.getConfigurationSection("tuchetao-permission").getKeys(false)) {
			if (p.hasPermission(config.getString("tuchetao-permission." + str + ".permission")))
				return config.getInt("tuchetao-permission." + str + ".time");
		}

		return config.getInt("tuchetao-default");

	}

	public static void runAdvancedAC(Player p) {
		if (queueCommand.contains(p.getName())) {
			p.sendMessage(Utils.color("&cVui lòng đợi một chút..."));
			return;
		}

		if (!PLAYERdatabaseManager.autocompressorsPlayer.contains(p)) {

			p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
					TextComponent.fromLegacyText(Utils.color("&3Vui lòng đợi...")));
			queueCommand.add(p.getName());

			new BukkitRunnable() {

				@Override
				public void run() {

					double d = (autoCompressorsManager.getCooldown(p) / 20.0);
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent
							.fromLegacyText(Utils.color("&3Bắt đầu quá trình tự chế tạo &e(" + d + " giây)")));
					autoCompressorsManager.runAutoCompressors(p);
					queueCommand.remove(p.getName());
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2);

				}

			}.runTaskLater(Main.getInstance(), 20 * 2);

			double d = (autoCompressorsManager.getCooldown(p) / 20.0);
			for (String str : Main.getInstance().getConfig().getStringList("message.autocompressors-on")) {
				str = str.replace("%time%", String.valueOf(d));
				p.sendMessage(Utils.color(str));
			}
		}
	}

	public static void runAutoCompressors(Player p) {

		if (!PLAYERdatabaseManager.autocompressorsPlayer.contains(p))
			PLAYERdatabaseManager.autocompressorsPlayer.add(p);

		int cooldown = getCooldown(p);

		new BukkitRunnable() {

			@Override
			public void run() {

				if (!PLAYERdatabaseManager.autocompressorsPlayer.contains(p)) {
					cancel();
					return;
				}

				if (cooldown == getCooldown(p))
					;
				else {
					cancel();
					runAutoCompressors(p);
					return;
				}

				doAutoCompressors(p);
			}
		}.runTaskTimer(Main.getInstance(), 0L, getCooldown(p));
	}

	public static void stopAutoCompressors(Player p) {
		if (PLAYERdatabaseManager.autocompressorsPlayer.contains(p))
			while (PLAYERdatabaseManager.autocompressorsPlayer.contains(p))
				PLAYERdatabaseManager.autocompressorsPlayer.remove(p);
	}

	public static void doAutoCompressors(Player p) {

		if (!PLAYERdatabaseManager.autocompressorsPlayer.contains(p))
			return;

		FileConfiguration config = Main.getInstance().getConfig();

		c: for (String compressors : config.getConfigurationSection("auto-compressors.compressors").getKeys(false)) {

			if (!p.hasPermission(config.getString("auto-compressors.compressors." + compressors + ".permission")))
				continue c;

			List<ItemStack> is = new ArrayList<ItemStack>();
			HashMap<ItemStack, Integer> is_chiaduoc = new HashMap<ItemStack, Integer>();
			for (String itemRequired : config
					.getConfigurationSection("auto-compressors.compressors." + compressors + ".item-required")
					.getKeys(false)) {

				ItemStack i = new ItemStack(Material.BEDROCK);
				String itemRequired_name = itemRequired;

				if (itemRequired.contains("MATERIAL_")) {

					itemRequired_name = itemRequired_name.replace("MATERIAL_", "");
					i = new ItemStack(Material.valueOf(itemRequired_name));

				} else if (itemRequired.contains("MMOITEM_")) {

					itemRequired_name = itemRequired_name.replace("MMOITEM_", "");
					MMOItem mmitem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(Main.getInstance().getConfig().getString("mmoitem-category")),
							itemRequired_name.toUpperCase());

					if (mmitem == null) {
						Bukkit.getConsoleSender().sendMessage(
								"[TU CHE TAO ERROR] Item MMOITEM:" + itemRequired_name + " KHÔNG TỒN TẠI!");
						return;
					}

					i = mmitem.newBuilder().build();

				}

				int amount = Utils.getPlayerAmount(p, i);
				int amount_required = config
						.getInt("auto-compressors.compressors." + compressors + ".item-required." + itemRequired);

				if (amount >= amount_required) {

					i.setAmount(amount_required);
					is_chiaduoc.put(i, amount / amount_required);

					is.add(i);
				} else
					continue c;

			}

			Integer sum = is_chiaduoc.size();
			boolean fastC = false;
			ItemStack fastC_I = new ItemStack(Material.WHEAT);
			if (sum == 1)
				fastC = true;

			if (is != null) {

				for (ItemStack i : is)
					if (fastC) {
						Utils.removeItems(p, i, i.getAmount() * is_chiaduoc.get(i));
						fastC_I = i;
					} else {
						Utils.removeItems(p, i, i.getAmount());
					}

				MMOItem newmmoitem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(Main.getInstance().getConfig().getString("mmoitem-category")),
						config.getString("auto-compressors.compressors." + compressors + ".new-item"));

				if (newmmoitem == null) {
					Bukkit.getConsoleSender()
							.sendMessage("[TU CHE TAO ERROR] Item MMOITEM: "
									+ config.getString("auto-compressors.compressors." + compressors + ".new-item")
									+ " KHÔNG TỒN TẠI! (Config: " + "auto-compressors.compressors." + compressors
									+ ".new-item");
					return;
				}

				ItemStack newItem = newmmoitem.newBuilder().build();

				if (fastC)
					newItem.setAmount(is_chiaduoc.get(fastC_I));

				p.getInventory().addItem(newItem);
				is.clear();

			}
		}
	}
}
