package storage.player;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import grassminevn.skygencore.Main;

class PLAYERfileStorage implements PLAYERstorage {
	private static File getFile(String player) {
		final File file = new File(Main.getInstance().getDataFolder() + "/player/" + player + ".yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static playerData fromFile(File file) {
		final YamlConfiguration storage = YamlConfiguration.loadConfiguration(file);
		final playerData data = new playerData(0, 0, 0, 0, 0);

		if (!storage.contains("data"))
			return data;

		data.setKill(storage.getInt("data.kill"));
		data.setDie(storage.getInt("data.die"));
		data.setNhatItem(storage.getInt("data.nhatItem"));

		data.setTempBoosterAmount(storage.getDouble("data.temp_booster_amount"));
		data.setTempBoosterTime(storage.getLong("data.temp_booster_time"));

		return data;
	}

	@Override
	public void saveData(String player, playerData data) {
		saveDataDirect(player, data);
	}

	public static void saveDataDirect(String player, playerData data) {
		final File file = getFile(player);
		final YamlConfiguration storage = YamlConfiguration.loadConfiguration(file);

		storage.set("data.kill", data.getKill());
		storage.set("data.die", data.getDie());
		storage.set("data.nhatItem", data.getNhatItem());		

		storage.set("data.temp_booster_amount", data.getTempBoosterAmount());
		storage.set("data.temp_booster_time", data.getTempBoosterTime());

		try {
			storage.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public playerData getData(String player) {
		final File file = getFile(player);
		return fromFile(file);
	}

}