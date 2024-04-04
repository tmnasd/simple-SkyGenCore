package grassminevn.skygencore;

import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import commands.autoCompressorsCommand;
import commands.coreCommand;
import commands.genlistCommand;
import commands.khbCommand;
import depend.papiDepend;
import files.tuchetaoV2File;
import listeners.hologramListener;
import listeners.playerDeathListener;
import listeners.playerDropListener;
import listeners.playerJoinListener;
import listeners.playerPickupListener;
import manager.GENERATORdatabaseManager;
import manager.PLAYERdatabaseManager;
import manager.actionbarManager;
import manager.generatorManager;
import manager.hologramManager;
import manager.tempBoosterManager;
import storage.generator.GENERATORdataStorage;
import storage.generator.generatorData;
import storage.player.PLAYERdataStorage;

public class Main extends JavaPlugin {

	private static Main instance;
	private generatorManager manager;

	@Override
	public void onLoad() {
		Main.instance = this;
	}

	@Override
	public void onEnable() {

		GENERATORdataStorage.init();
		PLAYERdataStorage.init();

		tuchetaoV2File.setup();
		tuchetaoV2File.setupConfig();
		tuchetaoV2File.reload();

		new coreCommand(this);
		// new pvpCommand(this);
		new playerPickupListener(this);
		new khbCommand(this);
		new genlistCommand(this);
		new playerDropListener(this);
		new playerDeathListener(this);
		new autoCompressorsCommand(this);
		new hologramListener(this);
		new playerJoinListener(this);

		manager = new generatorManager(this);
		GENERATORdatabaseManager.loadAllDatabase();

		getConfig().options().copyDefaults(true);
		saveDefaultConfig();

		generatorManager.radiusX = getConfig().getDouble("generator.radius.x");
		generatorManager.radiusY = getConfig().getDouble("generator.radius.y");
		generatorManager.radiusZ = getConfig().getDouble("generator.radius.z");

		Set<String> generatorData = GENERATORdatabaseManager.data.keySet();
		for (String generator : generatorData)
			Main.getInstance().getManager().addGenerator(GENERATORdatabaseManager.getData(generator));

		PLAYERdatabaseManager.loadTime();

		if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
			new papiDepend().register();

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!PLAYERdatabaseManager.data.containsKey(p.getName())) {
				PLAYERdatabaseManager.load(p.getName());
			}
			actionbarManager.runActionBar(p);
			tempBoosterManager.runTempBooster(p.getName());
		}

		new BukkitRunnable() {

			@Override
			public void run() {

				for (Entry<String, generatorData> data : GENERATORdatabaseManager.data.entrySet()) {

					if (data.getValue() == null) {
						continue;
					}

					Block block = data.getValue().getLocation().getBlock();

					if (block == null)
						continue;

					boolean canDrop = false;

					for (Entity e : block.getLocation().getWorld().getNearbyEntities(block.getLocation(),
							generatorManager.radiusX, generatorManager.radiusY, generatorManager.radiusZ)) {

						if (e.getType() == EntityType.PLAYER) {

							Player p = Bukkit.getPlayer(e.getName());

							if (p == null)
								continue;

							/*
							 * if (Utils.getEmptySlot(p) <= 1) { canDrop = true;
							 * generatorManager.canDrop.put(data.getValue(),
							 * GeneratorDropStatus.FULL_INVENTORY); continue main; } else {
							 * generatorManager.canDrop.put(data.getValue(), GeneratorDropStatus.CAN_DROP);
							 * canDrop = true; block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY,
							 * block.getLocation().add(0.5, 1.5, 0.5), 1); }
							 */
							generatorManager.canDrop.put(data.getValue(), GeneratorDropState.CAN_DROP);
							canDrop = true;
							block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY,
									block.getLocation().add(0.5, 1.5, 0.5), 1);
						}
					}

					if (!canDrop) {
						generatorManager.canDrop.put(data.getValue(), GeneratorDropState.FALSE);
						block.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, block.getLocation().add(0.5, 1.5, 0.5),
								1);
					}
				}

			}
		}.runTaskTimer(this, 0, 20 * 3);

		new BukkitRunnable() {

			@Override
			public void run() {

				for (String generatorName : GENERATORdatabaseManager.data.keySet()) {
					generatorData generatorData = GENERATORdatabaseManager.getData(generatorName);
					if (GENERATORdatabaseManager.generatorHologramLine1.containsKey(generatorData)) {
						hologramManager.deleteHologram(generatorData);

						for (Entity entity : generatorData.getWorld().getNearbyEntities(generatorData.getLocation(), 2,
								6, 2)) {

							if (entity.getType() == EntityType.ARMOR_STAND) {
								entity.remove();
							}

						}

						hologramManager.createHologram(generatorData);
					}
				}

			}
		}.runTaskLater(instance, 20 * 15);
		
		Bukkit.getConsoleSender().sendMessage(Utils.color("&a&lSkyGenCore by Cortez Romeo được chia sẻ miễn phí tại minecraftvn.net"));

	}

	@Override
	public void onDisable() {
		Set<String> generatorData = GENERATORdatabaseManager.data.keySet();
		for (String generator : generatorData)
			GENERATORdatabaseManager.save(generator);

		Set<String> keys = PLAYERdatabaseManager.data.keySet();
		for (String k : keys)
			if (PLAYERdatabaseManager.getData(k) != null) {
				try {
					PLAYERdatabaseManager.save(k);
				} catch (Exception e) {}
			}

		if (!GENERATORdatabaseManager.generatorHologramLine1.isEmpty()) {
			for (generatorData data : GENERATORdatabaseManager.generatorHologramLine1.keySet()) {
				GENERATORdatabaseManager.generatorHologramLine1.get(data).remove();
			}
		}
		if (!GENERATORdatabaseManager.generatorHologramLine2.isEmpty()) {
			for (generatorData data : GENERATORdatabaseManager.generatorHologramLine2.keySet()) {
				GENERATORdatabaseManager.generatorHologramLine2.get(data).remove();
			}
		}
		if (!GENERATORdatabaseManager.generatorHologramLine3.isEmpty()) {
			for (generatorData data : GENERATORdatabaseManager.generatorHologramLine3.keySet()) {
				GENERATORdatabaseManager.generatorHologramLine3.get(data).remove();
			}
		}

	}

	public static Main getInstance() {
		return Main.instance;
	}

	public generatorManager getManager() {
		return manager;
	}

}