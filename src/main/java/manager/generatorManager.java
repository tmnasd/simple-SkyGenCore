package manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.block.Block;

import grassminevn.skygencore.Generator;
import grassminevn.skygencore.GeneratorDropState;
import grassminevn.skygencore.Main;
import storage.generator.generatorData;

public class generatorManager {
	private Main plugin;

	private Map<Location, Generator> gens;
	public static double radiusX;
	public static double radiusY;
	public static double radiusZ;
	
	public static HashMap<generatorData, GeneratorDropState> canDrop = new HashMap<>();

	public generatorManager(Main plugin) {
		gens = new HashMap<>();
		this.plugin = plugin;
	}

	public void addGenerator(generatorData data) {
		gens.put(data.getLocation(), new Generator(data));
		gens.get(data.getLocation()).runTaskTimer(plugin, 0, 20);
	}

	public void removeGenerator(Block b) {
		if (gens.containsKey(b.getLocation())) {

			for (Entry<String, generatorData> data : GENERATORdatabaseManager.data.entrySet()) {
				if (data.getValue().getLocation().getX() == b.getLocation().getX()
						&& data.getValue().getLocation().getY() == b.getLocation().getY()
						&& data.getValue().getLocation().getZ() == b.getLocation().getZ()) {
					hologramManager.deleteHologram(data.getValue());
					GENERATORdatabaseManager.delete(data.getKey());
				}
			}

			gens.get(b.getLocation()).cancel();
			gens.remove(b.getLocation());
		}
	}
	
	public void removeGenerator(String generatorName) {
		
		if (!GENERATORdatabaseManager.data.containsKey(generatorName))
			return;
		
		generatorData data = GENERATORdatabaseManager.getData(generatorName);
		
		if (gens.containsKey(data.getLocation())) {
			hologramManager.deleteHologram(data);
			gens.get(data.getLocation()).cancel();
			gens.remove(data.getLocation());			
			GENERATORdatabaseManager.delete(generatorName);
		}
	}

	public boolean containsGenerator(Location location) {
		if (gens.containsKey(location))
			return true;
		return false;
	}

}
