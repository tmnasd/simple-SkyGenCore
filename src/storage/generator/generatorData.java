package storage.generator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class generatorData {

	private String name;
	private Location location;
	private World world;
	private generatorType type;
	private String material;
	private double time;

	public generatorData(String name, Location location, World world, generatorType type, String material,
			double time) {
		this.name = name;
		this.location = location;
		this.world = world;
		this.type = type;
		this.material = material;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = Bukkit.getServer().getWorld(world);
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public generatorType getItemType() {
		return type;
	}

	public void setItemType(generatorType type) {
		this.type = type;
	}

	public void setItemType(String type) {

		try {
			this.type = generatorType.valueOf(type.toUpperCase());
		} catch (Exception e) {
			this.type = generatorType.MATERIAL;
		}
		
	}

}