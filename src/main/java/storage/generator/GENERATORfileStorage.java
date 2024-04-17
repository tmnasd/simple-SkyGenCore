package storage.generator;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import grassminevn.skygencore.Main;

class GENERATORfileStorage implements GENERATORstorage {
	private static File getFile(String generator) {
		final File file = new File(Main.getInstance().getDataFolder() + "/generator/" + generator + ".yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static generatorData fromFile(File file) {
		final YamlConfiguration storage = YamlConfiguration.loadConfiguration(file);
		final generatorData data = new generatorData(null, null, null, null, null, 0);

		if (!storage.contains("data"))
			return data;

		data.setName(storage.getString("data.name"));

		data.setWorld(storage.getString("data.location.world"));
		double x = storage.getDouble("data.location.x");
		double y = storage.getDouble("data.location.y");
		double z = storage.getDouble("data.location.z");
		data.setLocation(new Location(data.getWorld(), x, y, z));

		try {
			data.setItemType(storage.getString("data.type"));
		} catch (Exception e) {
			data.setItemType(generatorType.MATERIAL);
		}

		data.setMaterial(storage.getString("data.material"));
		data.setTime(storage.getInt("data.time"));

		return data;
	}

	@Override
	public void saveData(String generator, generatorData data) {
		saveDataDirect(generator, data);
	}

	public static void saveDataDirect(String player, generatorData data) {
		final File file = getFile(player);
		final YamlConfiguration storage = YamlConfiguration.loadConfiguration(file);

		storage.set("data.name", data.getName());
		storage.set("data.location.world", data.getWorld().getName());
		storage.set("data.location.x", data.getLocation().getX());
		storage.set("data.location.y", data.getLocation().getY());
		storage.set("data.location.z", data.getLocation().getZ());
		storage.set("data.type", data.getItemType().toString().toUpperCase());
		storage.set("data.material", data.getMaterial().toString());
		storage.set("data.time", data.getTime());

		try {
			storage.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public generatorData getData(String player) {
		final File file = getFile(player);
		return fromFile(file);
	}

}