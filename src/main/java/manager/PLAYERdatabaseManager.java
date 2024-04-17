package manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import grassminevn.skygencore.Main;
import storage.player.PLAYERdataStorage;
import storage.player.playerData;

public class PLAYERdatabaseManager {

	public static List<String> openingInventory = new ArrayList<>();
	public static List<String> runtempbooster = new ArrayList<>();
	public static List<Player> autocompressorsPlayer = new ArrayList<Player>();

	public static HashMap<String, Double> KHBAmount = new HashMap<>();
	public static HashMap<String, Long> KHBTime = new HashMap<>();

	// Data sẽ được lưu
	public static Map<String, playerData> data = new HashMap<>();

	public static playerData getData(String name) {
		if (!PLAYERdatabaseManager.data.containsKey(name)) {
			load(name);
		}
		return PLAYERdatabaseManager.data.get(name);
	}

	public static void load(String player) {

		if (data.containsKey(player))
			return;

		PLAYERdatabaseManager.data.put(player, PLAYERdataStorage.getData(player));
	}

	public static void save(String player) {
		PLAYERdataStorage.saveData(player, PLAYERdatabaseManager.data.get(player));
	}

	public static void unload(String player) {
		PLAYERdataStorage.saveData(player, PLAYERdatabaseManager.data.get(player));
		PLAYERdatabaseManager.data.remove(player);
	}

	public static void loadTime() {

		File dataFolder = new File(Main.getInstance().getDataFolder() + "/player");
		File[] listOfFiles = dataFolder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {

				File playerFile = listOfFiles[i];
				YamlConfiguration playerFileYaml = YamlConfiguration.loadConfiguration(playerFile);

				if (playerFileYaml.getDouble("data.temp_booster_amount") > 0) {

					String playerName = removeExtension(playerFile.getName()).toString();
					PLAYERdatabaseManager.load(playerName);

					tempBoosterManager.runTempBooster(playerName);

				}
			}
		}
	}

	private static String removeExtension(String fileName) {

		String separator = System.getProperty("file.separator");
		String filename;
		int lastSeparatorIndex = fileName.lastIndexOf(separator);
		if (lastSeparatorIndex == -1) {
			filename = fileName;
		} else {
			filename = fileName.substring(lastSeparatorIndex + 1);
		}

		int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex == -1)
			return filename;

		return filename.substring(0, extensionIndex);
	}

}
