package manager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import grassminevn.skygencore.GeneratorDropState;
import grassminevn.skygencore.Main;
import storage.generator.GENERATORdataStorage;
import storage.generator.generatorData;

public class GENERATORdatabaseManager {

	public static HashMap<generatorData, ArmorStand> generatorHologramLine1 = new HashMap<>();
	public static HashMap<generatorData, ArmorStand> generatorHologramLine2 = new HashMap<>();
	public static HashMap<generatorData, Item> generatorHologramLine3 = new HashMap<>();
	public static HashMap<generatorData, ItemStack> generatorItemGenerator = new HashMap<>();

	public static Map<String, generatorData> data = new HashMap<>();

	public static generatorData getData(String name) {
		if (!GENERATORdatabaseManager.data.containsKey(name)) {
			load(name);
		}
		return GENERATORdatabaseManager.data.get(name);
	}

	public static void load(String generator) {
		GENERATORdatabaseManager.data.put(generator, GENERATORdataStorage.getData(generator));
		generatorManager.canDrop.put(getData(generator), GeneratorDropState.FALSE);
	}

	public static void save(String generator) {
		GENERATORdataStorage.saveData(generator, GENERATORdatabaseManager.data.get(generator));
	}

	public static void unload(String generator) {
		GENERATORdataStorage.saveData(generator, GENERATORdatabaseManager.data.get(generator));
		GENERATORdatabaseManager.data.remove(generator);
	}

	public static void delete(String generator) {
		unload(generator);

		File myObj = new File(Main.getInstance().getDataFolder() + "/generator/" + generator + ".yml");
		if (myObj.delete())
			;
		else
			Bukkit.getLogger().severe("Gặp lỗi khi xóa generator: " + generator);
	}

	public static void loadAllDatabase() {
		File dataFolder = new File(Main.getInstance().getDataFolder() + "/generator");
		File[] listOfFiles = dataFolder.listFiles();

		try {
			if (listOfFiles.length == 0)
				return;

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {

					File dataFile = listOfFiles[i];
					String dataName = removeExtension(dataFile.getName()).toString();
					GENERATORdatabaseManager.load(dataName);

				}
			}
		} catch (Exception e) {
			Bukkit.getLogger().severe("Error while loading database: " + e);
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
