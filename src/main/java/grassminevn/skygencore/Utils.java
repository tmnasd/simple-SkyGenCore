package grassminevn.skygencore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import manager.PLAYERdatabaseManager;
import net.md_5.bungee.api.ChatColor;

public class Utils {

	public static final Pattern HEX_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-fA-F])");

	public static String color(String textToTranslate) {

		if (textToTranslate == null)
			return "NULL_TEXT";

		Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
		StringBuffer buffer = new StringBuffer();

		while (matcher.find()) {
			matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
		}

		return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());

	}

	private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('&') + "[0-9A-FK-OR]");

	public static String stripColor(String input) {
		return input == null ? null : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	public static String timeFormat(long totalSeconds) {

		FileConfiguration config = Main.getInstance().getConfig();

		if (totalSeconds > 604800) {
			String str = config.getString("time-format.wwddhhmmss");
			str = str.replace("%w%", String.valueOf(totalSeconds / 604800));
			str = str.replace("%d%", String.valueOf(totalSeconds % 604800 / 86400));
			str = str.replace("%h%", String.valueOf((totalSeconds % 86400) / 3600));
			str = str.replace("%m%", String.valueOf((totalSeconds % 3600) / 60));
			str = str.replace("%s%", String.valueOf(totalSeconds % 60));

			return Utils.color(str);
		}

		if (totalSeconds > 86400) {
			String str = config.getString("time-format.ddhhmmss");
			str = str.replace("%d%", String.valueOf(totalSeconds / 86400));
			str = str.replace("%h%", String.valueOf((totalSeconds % 86400) / 3600));
			str = str.replace("%m%", String.valueOf((totalSeconds % 3600) / 60));
			str = str.replace("%s%", String.valueOf(totalSeconds % 60));

			return Utils.color(str);
		}

		if (totalSeconds > 3600) {

			String str = config.getString("time-format.hhmmss");
			str = str.replace("%h%", String.valueOf(totalSeconds / 3600));
			str = str.replace("%m%", String.valueOf((totalSeconds % 3600) / 60));
			str = str.replace("%s%", String.valueOf(totalSeconds % 60));

			return Utils.color(str);
		}

		if (totalSeconds >= 60) {
			String str = config.getString("time-format.mmss");
			str = str.replace("%m%", String.valueOf((totalSeconds % 3600) / 60));
			str = str.replace("%s%", String.valueOf(totalSeconds % 60));

			return Utils.color(str);
		}

		return Utils.color(config.getString("time-format.ss").replace("%s%", String.valueOf(totalSeconds)));

	}

	public static double getPlayerBonusPermission(Player p) {

		double total = 1;

		for (String permissionN : Main.getInstance().getConfig().getConfigurationSection("pickup-permission.")
				.getKeys(false)) {

			if (p.hasPermission(
					Main.getInstance().getConfig().getString("pickup-permission." + permissionN + ".permission"))) {
				if (total < Main.getInstance().getConfig().getDouble("pickup-permission." + permissionN + ".bonus")) {
					total = Main.getInstance().getConfig().getDouble("pickup-permission." + permissionN + ".bonus");
				}
			}

		}

		return total;
	}

	public static double finalBooster(Player p) {

		if (getPlayerBonusPermission(p) > PLAYERdatabaseManager.getData(p.getName()).getTempBoosterAmount()) {
			return getPlayerBonusPermission(p);
		} else
			return PLAYERdatabaseManager.getData(p.getName()).getTempBoosterAmount();

	}

	public static void dispatchCommand(Player player, String command) {
		String MATCH = "(?ium)^(player:|op:|console:|message:|)(.*)$";
		new BukkitRunnable() {
			@Override
			public void run() {
				final String type = command.replaceAll(MATCH, "$1").replace(":", "").toLowerCase();
				final String cmd = command.replaceAll(MATCH, "$2").replaceAll("(?ium)([{]Player[}])", player.getName());
				switch (type) {
				case "op":
					if (player.isOp()) {
						player.performCommand(cmd);
					} else {
						player.setOp(true);
						player.performCommand(cmd);
						player.setOp(false);
					}
					break;
				case "":
				case "player":
					player.performCommand(cmd);
					break;
				case "message":
					player.sendMessage(color(cmd));
					break;
				case "console":
				default:
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
					break;
				}
			}
		}.runTask(Main.getInstance());
	}

	public static int getEmptySlot(Player p) {
		int emptySlot = 0;
		for (int i2 = 0; i2 <= 35; i2++) {
			if (p.getInventory().getItem(i2) == null) {
				emptySlot = emptySlot + 1;
			}
		}
		return emptySlot;
	}
	
	public static boolean isLegalItem(ItemStack item) {
		if (item == null)
			return false;

		if (item.getType() == Material.AIR) {
			return false;
		}

		return true;
	}
	
	public static void removeItems(Player player, ItemStack item, long amount) {
		item = item.clone();
		final PlayerInventory inv = player.getInventory();
		final ItemStack[] items = inv.getContents();
		int c = 0;
		for (int i = 0; i < items.length; ++i) {
			final ItemStack is = items[i];
			if (is != null) {
				if (is.isSimilar(item)) {
					if (c + is.getAmount() > amount) {
						final long canDelete = amount - c;
						is.setAmount((int) (is.getAmount() - canDelete));
						items[i] = is;
						break;
					}
					c += is.getAmount();
					items[i] = null;
				}
			}
		}
		inv.setContents(items);
		player.updateInventory();
	}

	public static void removeItemsDisplayName(Player player, ItemStack item, long amount) {
		item = item.clone();
		final PlayerInventory inv = player.getInventory();
		final ItemStack[] items = inv.getContents();
		int c = 0;
		for (int i = 0; i < items.length; ++i) {
			final ItemStack is = items[i];
			if (Utils.isLegalItem(is)) {
				if (Utils.checkItem(item, is)) {
					if (c + is.getAmount() > amount) {
						final long canDelete = amount - c;
						is.setAmount((int) (is.getAmount() - canDelete));
						items[i] = is;
						break;
					}
					c += is.getAmount();
					items[i] = null;
				}
			}
		}
		inv.setContents(items);
		player.updateInventory();
	}
	
	public static String getDisplayNameAsID(ItemStack item) {

		if (item == null)
			return "@null";

		ItemMeta itemMeta = item.getItemMeta();

		if (itemMeta != null)
			if (itemMeta.hasDisplayName()) {
				String ID = ChatColor.stripColor(itemMeta.getDisplayName());
				ID = ID.replace(" ", "");

				return ID;
			}

		return item.getType().toString().toUpperCase();
	}

	public static int getPlayerAmount(HumanEntity player, ItemStack item) {
		final PlayerInventory inv = player.getInventory();
		final ItemStack[] items = inv.getContents();
		int c = 0;
		for (final ItemStack is : items) {
			if (is != null) {
				if (is.isSimilar(item)) {
					c += is.getAmount();
				}
			}
		}
		return c;
	}
	
	public static boolean checkItem(ItemStack playerItem, ItemStack item) {
		if (!getDisplayNameAsID(playerItem).equals(getDisplayNameAsID(item))) {
			return false;
		}

		if (playerItem.hasItemMeta() != item.hasItemMeta()) {
			return false;
		}

		if (item.hasItemMeta() && playerItem.hasItemMeta()) {
			if (item.getItemMeta().hasLore() != playerItem.getItemMeta().hasLore()) {
				return false;
			}
		}

		return item.getType() == playerItem.getType();
	}

	public static int getPlayerAmountDisplayName(HumanEntity player, ItemStack item) {
		final PlayerInventory inv = player.getInventory();
		final ItemStack[] items = inv.getContents();
		int c = 0;
		for (final ItemStack is : items) {
			if (Utils.isLegalItem(is)) {
				if (Utils.checkItem(item, is))
					c += is.getAmount();
			}
		}
		return c;
	}
	
}
