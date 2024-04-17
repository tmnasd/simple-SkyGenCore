package grassminevn.skygencore;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import manager.GENERATORdatabaseManager;
import manager.generatorManager;
import manager.hologramManager;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import storage.generator.generatorData;
import storage.generator.generatorType;

public class Generator extends BukkitRunnable {

	private generatorData data;
	private Block b;
	private ItemStack item;
	private double time;
	private double timeX;

	public Generator(generatorData data) {

		this.data = data;

		try {
			this.b = data.getLocation().getBlock();

			ItemStack itemSetting = new ItemStack(Material.DIRT);
			ItemMeta itemMeta = itemSetting.getItemMeta();

			if (data.getItemType() == generatorType.MATERIAL) {
				itemSetting = new ItemStack(Material.matchMaterial(data.getMaterial()));
				itemMeta.setDisplayName("MATERIAL_" + itemSetting.getType());
				itemSetting.setItemMeta(itemMeta);
			} else {

				MMOItem MMOItem = MMOItems.plugin.getMMOItem(
						MMOItems.plugin.getTypes().get(Main.getInstance().getConfig().getString("mmoitem-category")),
						data.getMaterial().toUpperCase());
				if (MMOItem == null) {
					Bukkit.getLogger().severe("Error while creating generator: MMOITEM "
							+ data.getMaterial().toUpperCase() + " can not be null!");
					return;
				}

				itemSetting = new ItemStack(MMOItem.newBuilder().build().getType());
				itemMeta.setDisplayName("MMOITEM_" + data.getMaterial());
				itemSetting.setItemMeta(itemMeta);

			}

			item = itemSetting;

			if (!GENERATORdatabaseManager.generatorItemGenerator.containsKey(data))
				GENERATORdatabaseManager.generatorItemGenerator.put(data, item);

			time = data.getTime();
			timeX = data.getTime();

			hologramManager.createHologram(data);

		} catch (Exception e) {
			Bukkit.getLogger().severe("Error while creating generator: " + e);
			return;
		}

	}

	@Override
	public void run() {

		if (item == null) {
			cancel();
			return;
		}

		if (GENERATORdatabaseManager.generatorHologramLine3.get(data).isDead()) {
			hologramManager.createItemHead(data);
		}

		time = time - 1;

		if (time <= 0) {

			boolean canDrop = true;

			for (Entity e : b.getLocation().getWorld().getNearbyEntities(b.getLocation(), generatorManager.radiusX,
					generatorManager.radiusY, generatorManager.radiusZ)) {

				if (e.getType() == EntityType.DROPPED_ITEM) {
					ItemStack item = ((Item) e).getItemStack();
					if (GENERATORdatabaseManager.generatorItemGenerator.containsKey(data)) {
						if (GENERATORdatabaseManager.generatorItemGenerator.get(data).isSimilar(item)) {
							generatorManager.canDrop.put(data, GeneratorDropState.ALREADY_GENERATED);
							try {
								b.getWorld().spawnParticle(Particle.valueOf("BARRIER"), b.getLocation().add(0.5, 1.5, 0.5), 1);
							} catch (IllegalArgumentException exception){
								b.getWorld().spawnParticle(Particle.BLOCK_MARKER, b.getLocation().add(0.5, 1.5, 0.5), 1, Bukkit.createBlockData(Material.LIGHT));
							}

							canDrop = false;
						}
					}
				}
			}

			if (canDrop)
				try {
					if (generatorManager.canDrop.get(data) == GeneratorDropState.CAN_DROP) {
						Item dropitem = b.getWorld().dropItem(b.getLocation().clone().add(0.5, 1.2, 0.5), item);
						dropitem.setVelocity(dropitem.getVelocity().zero());
						// b.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, b.getLocation().add(0.5,
						// 1.5, 0.5), 1);
					} else {
						// b.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, b.getLocation().add(0.5,
						// 1, 0.5), 1);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			time = timeX;
		}

		try {
			ArmorStand armorStand = GENERATORdatabaseManager.generatorHologramLine2.get(data);

			if (generatorManager.canDrop.get(data) == GeneratorDropState.ALREADY_GENERATED) {
				armorStand.setCustomName(Utils.color("&eGiới hạn một vật phẩm được thả"));
				return;
			}

			if (generatorManager.canDrop.get(data) == GeneratorDropState.CAN_DROP) {
				armorStand.setCustomName(Utils.color("&fXuất hiện trong &e" + Math.round(time) + " giây"));
			} else if (generatorManager.canDrop.get(data) == GeneratorDropState.FULL_INVENTORY)
				armorStand.setCustomName(Utils.color("&e&lTúi đồ của những người xung quanh đã đầy!"));
			else {
				armorStand.setCustomName(Utils.color("&cCần thêm người ở gần để xuất hiện vật phẩm"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * if (GENERATORdatabaseManager.generatorHologramLine3.containsKey(data)) {
		 * ArmorStand armorStand =
		 * GENERATORdatabaseManager.generatorHologramLine3.get(data);
		 * armorStand.setHeadPose((armorStand.getHeadPose().add(0, 0.05, 0))); //
		 * armorStand.setRotation(1, 5);
		 * 
		 * }
		 */
	}

}
