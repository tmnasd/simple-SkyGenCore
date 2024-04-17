package storage.generator;

import java.io.File;

import grassminevn.skygencore.Main;

public class GENERATORdataStorage {

	private static GENERATORstorage STORAGE;

	public static void init() {
		File file = new File(Main.getInstance().getDataFolder() + "/generator/");
		if (!file.exists()) {
			file.mkdirs();
		}
		GENERATORdataStorage.STORAGE = new GENERATORfileStorage();
	}

	public static generatorData getData(String generator) {
		return GENERATORdataStorage.STORAGE.getData(generator);
	}

	public static void saveData(String generator, generatorData data) {
		GENERATORdataStorage.STORAGE.saveData(generator, data);
	}

}
