package manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import grassminevn.skygencore.Main;
import grassminevn.skygencore.Utils;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import storage.generator.generatorData;

public class hologramManager {

	public static void createHologram(generatorData data) {

		Block b = data.getLocation().getBlock();

		ArmorStand hologramLine1 = (ArmorStand) b.getWorld().spawnEntity(b.getLocation().add(0.5, 2, 0.5),
				EntityType.ARMOR_STAND);
		hologramLine1.setVisible(false);
		hologramLine1.setGravity(false);
		hologramLine1.setCustomNameVisible(true);
		if (Main.getInstance().getConfig().getString("message.blockName." + data.getMaterial()) != null) {
			hologramLine1.setCustomName(
					Utils.color(Main.getInstance().getConfig().getString("message.blockName." + data.getMaterial())));
		} else {
			hologramLine1.setCustomName(Utils.color(data.getMaterial()));
		}
		GENERATORdatabaseManager.generatorHologramLine1.put(data, hologramLine1);

		ArmorStand hologramLine2 = (ArmorStand) b.getWorld().spawnEntity(b.getLocation().add(0.5, 1.5, 0.5),
				EntityType.ARMOR_STAND);
		hologramLine2.setVisible(false);
		hologramLine2.setGravity(false);
		hologramLine2.setCustomNameVisible(true);
		hologramLine2.setCustomName(Utils.color("&fĐang tải..."));
		GENERATORdatabaseManager.generatorHologramLine2.put(data, hologramLine2);

		/*
		 * ArmorStand hologramLine3 = (ArmorStand)
		 * b.getWorld().spawnEntity(b.getLocation().add(0.5, 2.25, 0.5),
		 * EntityType.ARMOR_STAND); hologramLine3.setSmall(true);
		 * hologramLine3.setVisible(false); hologramLine3.setGravity(false);
		 * hologramLine3.setCustomNameVisible(false); try { hologramLine3.setHelmet(new
		 * ItemStack(Material.valueOf(data.getMaterial()))); } catch (Exception e) {
		 * MMOItem MMOItem =
		 * MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get("MATERIAL"),
		 * data.getMaterial().toUpperCase()); if (MMOItem == null) {
		 * Bukkit.getLogger().severe("Error while creating generator: MMOITEM " +
		 * data.getMaterial().toUpperCase() + " can not be null!"); } else {
		 * hologramLine3.setHelmet(new
		 * ItemStack(MMOItem.newBuilder().build().getType())); } }
		 * GENERATORdatabaseManager.generatorHologramLine3.put(data, hologramLine3);
		 */

		createItemHead(data);

	}

	public static void createItemHead(generatorData data) {
		Material headItem = Material.BEDROCK;

		try {
			headItem = Material.valueOf(data.getMaterial());
		} catch (Exception e) {
			MMOItem MMOItem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(Main.getInstance().getConfig().getString("mmoitem-category")),
					data.getMaterial().toUpperCase());
			if (MMOItem == null) {
				Bukkit.getLogger().severe("Error while creating generator: MMOITEM " + data.getMaterial().toUpperCase()
						+ " can not be null!");
			} else {
				headItem = MMOItem.newBuilder().build().getType();
			}
		}

		Block b = data.getLocation().getBlock();

		Item item = (Item) b.getLocation().getWorld().dropItem(b.getLocation().add(0.5, 3, 0.5),
				new ItemStack(headItem, 1));
		item.setGravity(false);
		item.setVelocity(item.getVelocity().zero());
		item.setPickupDelay(Integer.MAX_VALUE);
		GENERATORdatabaseManager.generatorHologramLine3.put(data, item);
	}

	public static void reloadHologram() {
		if (!GENERATORdatabaseManager.generatorHologramLine1.isEmpty()) {
			for (generatorData data : GENERATORdatabaseManager.generatorHologramLine1.keySet()) {

				ArmorStand hologramLine1 = GENERATORdatabaseManager.generatorHologramLine1.get(data);

				if (Main.getInstance().getConfig().getString("message.blockName." + data.getMaterial()) != null) {
					hologramLine1.setCustomName(Utils.color(
							Main.getInstance().getConfig().getString("message.blockName." + data.getMaterial())));
				} else {
					hologramLine1.setCustomName(Utils.color(data.getMaterial()));
				}
			}
		}
	}

	public static void deleteHologram(generatorData data) {

		if (GENERATORdatabaseManager.generatorHologramLine1.containsKey(data)) {
			GENERATORdatabaseManager.generatorHologramLine1.get(data).remove();
		}

		if (GENERATORdatabaseManager.generatorHologramLine2.containsKey(data)) {
			GENERATORdatabaseManager.generatorHologramLine2.get(data).remove();
		}

		if (GENERATORdatabaseManager.generatorHologramLine3.containsKey(data)) {
			GENERATORdatabaseManager.generatorHologramLine3.get(data).remove();
		}

	}


}
