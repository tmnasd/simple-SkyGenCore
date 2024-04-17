package files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import grassminevn.skygencore.Main;

public class tuchetaoV2File {
	private static File file;
	private static FileConfiguration customFile;

	public static void setup() {
		file = new File(Main.getInstance().getDataFolder() + "/tuchetaov2.yml");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {

			}
		}
		customFile = YamlConfiguration.loadConfiguration(file);
	}

	public static FileConfiguration get() {
		return customFile;
	}

	public static void save() {
		try {
			customFile.save(file);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void reload() {
		customFile = YamlConfiguration.loadConfiguration(file);
	}

	public static void setupConfig() {

		tuchetaoV2File.get().addDefault("BEDROCK.amount", 64);
		tuchetaoV2File.get().addDefault("BEDROCK.new_item.name", "Bedrock 2");
		tuchetaoV2File.get().addDefault("BEDROCK.new_item.value", "MMOITEM_BEDROCK2");

		tuchetaoV2File.get().options().copyDefaults(true);
		tuchetaoV2File.save();
	}

}
