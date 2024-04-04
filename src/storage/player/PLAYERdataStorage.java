package storage.player;

import java.io.File;

import grassminevn.skygencore.Main;

public class PLAYERdataStorage {

	private static PLAYERstorage STORAGE;

	public static void init() {
		File file = new File(Main.getInstance().getDataFolder() + "/player/");
		if (!file.exists()) {
			file.mkdirs();
		}
		PLAYERdataStorage.STORAGE = new PLAYERfileStorage();
	}

	public static playerData getData(String player) {
		return PLAYERdataStorage.STORAGE.getData(player);
	}

	public static void saveData(String player, playerData data) {
		PLAYERdataStorage.STORAGE.saveData(player, data);
	}

}
