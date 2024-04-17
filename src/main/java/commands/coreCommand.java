package commands;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import files.tuchetaoV2File;
import grassminevn.skygencore.Main;
import grassminevn.skygencore.Utils;
import manager.GENERATORdatabaseManager;
import manager.PLAYERdatabaseManager;
import manager.hologramManager;
import manager.tempBoosterManager;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.md_5.bungee.api.ChatColor;
import storage.generator.generatorData;
import storage.generator.generatorType;
import storage.player.playerData;

public class coreCommand implements CommandExecutor, TabCompleter {
	private Main plugin;

	public coreCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("skygencore").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		if (sender instanceof Player) {
			if (!((Player) sender).hasPermission("skygen.admin")) {
				sender.sendMessage("Không đủ quyền");
				return false;
			}
		}

		if (args.length >= 6) {
			if (args[0].equalsIgnoreCase("generator") && (sender instanceof Player)) {

				Player p = (Player) sender;

				if (args[1].equalsIgnoreCase("set")) {

					Block b = p.getTargetBlock(null, 10);
					if (!b.getType().isSolid()) {
						sender.sendMessage("Block chỉ định không hợp lệ");
						return false;
					}

					if (GENERATORdatabaseManager.data.containsKey(args[2])) {
						sender.sendMessage("Generator tên " + args[2] + " đã tồn tại");
						return false;
					}

					args[3] = args[3].toUpperCase();
					if (generatorType.valueOf(args[3]) == null) {
						sender.sendMessage("Type " + args[3] + " không tồn tại");
						sender.sendMessage("Type bao gồm: MATERIAL, MMOITEM");
						return false;
					}

					args[4] = args[4].toUpperCase();
					if (generatorType.valueOf(args[3]) == generatorType.MATERIAL) {
						Material m = Material.matchMaterial(args[4].toUpperCase());
						if (m == null) {
							sender.sendMessage(args[4] + " không phải material");
							return false;
						}
					} else {
						MMOItem MMOItem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes()
								.get(Main.getInstance().getConfig().getString("mmoitem-category")), args[4]);
						if (MMOItem == null) {
							sender.sendMessage(args[4] + " không tồn tại trong mục MATERIAL của MMOITEM");
							return false;
						}
					}

					if (!isDouble(args[5])) {
						sender.sendMessage("<time (s)> không hợp lệ");
						return false;
					}

					generatorData data = GENERATORdatabaseManager.getData(args[2]);

					try {
						data.setName(args[2]);
						data.setWorld(b.getLocation().getWorld());
						data.setItemType(generatorType.valueOf(args[3]));
						data.setMaterial(args[4]);
						data.setLocation(b.getLocation());
						data.setTime(Double.parseDouble(args[5]));
					} catch (Exception e) {
						e.printStackTrace();
					}
					GENERATORdatabaseManager.save(args[2]);

					plugin.getManager().addGenerator(data);

					sender.sendMessage("-------");
					sender.sendMessage("Tạo generator thành công!");
					sender.sendMessage("Name: " + args[2]);
					sender.sendMessage("World: " + b.getLocation().getWorld().getName());
					sender.sendMessage("Location: " + formatLocation(b.getLocation()));
					sender.sendMessage("Item type: " + args[3]);
					sender.sendMessage("Material: " + args[4]);
					sender.sendMessage("Time: " + args[5] + "s");
					sender.sendMessage("-------");
					return false;

				}
			}
		}

		if (args.length == 5) {
			if (args[0].equalsIgnoreCase("tempbooster") && args[1].equalsIgnoreCase("add")) {
				try {
					tempBoosterManager.addTempBooster(args[2], sender, Double.parseDouble(args[3]),
							Long.parseLong(args[4]), true);
				} catch (Exception e) {
					sender.sendMessage("Nhập sai, vui lòng kiểm tra lại");
				}
				return false;
			}
		}

		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("tempbooster") && args[1].equalsIgnoreCase("remove")) {

				if (PLAYERdatabaseManager.data.containsKey(args[2]) && Bukkit.getPlayer(args[2]) != null) {
					playerData data = PLAYERdatabaseManager.getData(args[2]);

					sender.sendMessage("Đã xóa booster của " + args[2] + " thành công (x" + data.getTempBoosterAmount()
							+ " " + data.getTempBoosterTime() + ")");

					data.setTempBoosterAmount(0.0);
					data.setTempBoosterTime(0);

				} else {

					sender.sendMessage(Utils.color("&eVì " + args[2]
							+ " không có trong máy chủ nên hệ thống sẽ tìm kiếm dữ liệu của người này và xử lý, vui lòng đợi một chút..."));

					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
						File dataFolder = new File(Main.getInstance().getDataFolder() + "/player");
						File[] listOfFilesData = dataFolder.listFiles();

						for (int i = 0; i < listOfFilesData.length; i++) {
							if (listOfFilesData[i].isFile()) {

								File dataFile = listOfFilesData[i];
								String playerName = FilenameUtils.removeExtension(dataFile.getName()).toString();

								if (playerName.equalsIgnoreCase(args[2])) {
									PLAYERdatabaseManager.load(playerName);
									playerData data = PLAYERdatabaseManager.getData(playerName);

									sender.sendMessage("Đã xóa booster của " + args[2] + " thành công (x"
											+ data.getTempBoosterAmount() + " " + data.getTempBoosterTime() + ")");

									data.setTempBoosterAmount(0.0);
									data.setTempBoosterTime(0);
									PLAYERdatabaseManager.save(playerName);

									return;
								}
							}
						}
						sender.sendMessage(Utils.color("&cDữ liệu của &e" + args[2] + "&c không tồn tại"));
					});

				}
				return false;
			}
		}

		if (args.length == 4 && args[0].equalsIgnoreCase("giveallbooster")) {
			tempBoosterManager.giveAllBooster(sender, Double.parseDouble(args[1]), Long.parseLong(args[2]), args[3]);
			return false;
		}

		if (args.length == 3 && args[0].equalsIgnoreCase("giveallbooster")) {
			tempBoosterManager.giveAllBooster(sender, Double.parseDouble(args[1]), Long.parseLong(args[2]), null);
			return false;
		}

		if (args.length == 3 && args[0].equals("generator") && args[1].equals("remove")) {
			if (!GENERATORdatabaseManager.data.containsKey(args[2])) {
				sender.sendMessage("Generator " + args[2] + " không tồn tại");
				return false;
			}
			plugin.getManager().removeGenerator(args[2]);
			sender.sendMessage("Xóa generator " + args[2] + " thành công!");
			return false;
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("generator") && (sender instanceof Player)) {

				Player p = (Player) sender;

				if (args[1].equalsIgnoreCase("remove")) {

					try {
						Block b = p.getTargetBlock(null, 10);
						if (!b.getType().isSolid()) {
							sender.sendMessage("Block chỉ định không hợp lệ");
							return false;
						}

						if (!plugin.getManager().containsGenerator(b.getLocation())) {
							sender.sendMessage("Block chỉ định chưa được set item generator");
							return false;
						}

						plugin.getManager().removeGenerator(b);
						sender.sendMessage("Xóa item generator ở [" + formatLocation(b.getLocation()) + "] thành công");

					} catch (Exception e) {
						sender.sendMessage(Utils.color("&eVui lòng thử lại..."));
					}
					return false;
				}
				if (args[1].equalsIgnoreCase("list")) {
					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
						int total = 0;
						for (String generator : GENERATORdatabaseManager.data.keySet()) {
							total = total + 1;
							sender.sendMessage(Utils.color("* [name:&a" + generator + "&r] [location:&a"
									+ formatLocation(GENERATORdatabaseManager.getData(generator).getLocation())
									+ "&r] [world:&a" + GENERATORdatabaseManager.getData(generator).getWorld().getName()
									+ "&r]" + " [material:&a"
									+ GENERATORdatabaseManager.getData(generator).getMaterial().toString() + "&r]"
									+ " [time:&a" + GENERATORdatabaseManager.getData(generator).getTime() + "s&r]"));
						}
						sender.sendMessage(Utils.color("&eTổng dữ liệu: " + total));
					});
					return false;
				}
			}

			if (args[0].equalsIgnoreCase("viewtempbooster")) {
				if (PLAYERdatabaseManager.data.containsKey(args[1]) && Bukkit.getPlayer(args[1]) != null) {
					playerData data = PLAYERdatabaseManager.getData(args[1]);

					sender.sendMessage("Data booster tạm thời của " + args[1] + ":");
					sender.sendMessage("temp boooster: " + data.getTempBoosterAmount());
					sender.sendMessage("temp booster time: " + data.getTempBoosterTime() + "s");
					sender.sendMessage("active run time: "
							+ (PLAYERdatabaseManager.runtempbooster.contains(args[1]) ? "ACTIVE" : "NON-ACTIVE"));

				} else {

					sender.sendMessage(Utils.color("&eVì " + args[1]
							+ " không có trong máy chủ nên hệ thống sẽ tìm kiếm dữ liệu của người này và xử lý, vui lòng đợi một chút..."));

					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
						File dataFolder = new File(Main.getInstance().getDataFolder() + "/player");
						File[] listOfFilesData = dataFolder.listFiles();

						for (int i = 0; i < listOfFilesData.length; i++) {
							if (listOfFilesData[i].isFile()) {

								File dataFile = listOfFilesData[i];
								String playerName = FilenameUtils.removeExtension(dataFile.getName()).toString();

								if (playerName.equalsIgnoreCase(args[1])) {
									PLAYERdatabaseManager.load(playerName);
									playerData data = PLAYERdatabaseManager.getData(playerName);

									sender.sendMessage("Data booster tạm thời của " + args[1] + ":");
									sender.sendMessage("temp boooster: " + data.getTempBoosterAmount());
									sender.sendMessage("temp booster time: " + data.getTempBoosterTime() + "s");
									sender.sendMessage("active run time: "
											+ (PLAYERdatabaseManager.runtempbooster.contains(args[1]) ? "ACTIVE"
													: "NON-ACTIVE"));

									return;
								}
							}
						}
						sender.sendMessage(Utils.color("&cDữ liệu của &e" + args[1] + "&c không tồn tại"));
					});

				}
			}

			if (args[0].equalsIgnoreCase("resetnhatitem")) {
				if (PLAYERdatabaseManager.data.containsKey(args[1]) && Bukkit.getPlayer(args[1]) != null) {
					playerData data = PLAYERdatabaseManager.getData(args[1]);

					sender.sendMessage("Đã reset nhặt item của " + args[1] + " thành công");

					data.setNhatItem(0);

				} else {

					sender.sendMessage(Utils.color("&eVì " + args[1]
							+ " không có trong máy chủ nên hệ thống sẽ tìm kiếm dữ liệu của người này và xử lý, vui lòng đợi một chút..."));

					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
						File dataFolder = new File(Main.getInstance().getDataFolder() + "/player");
						File[] listOfFilesData = dataFolder.listFiles();

						for (int i = 0; i < listOfFilesData.length; i++) {
							if (listOfFilesData[i].isFile()) {

								File dataFile = listOfFilesData[i];
								String playerName = FilenameUtils.removeExtension(dataFile.getName()).toString();

								if (playerName.equalsIgnoreCase(args[1])) {
									PLAYERdatabaseManager.load(playerName);
									playerData data = PLAYERdatabaseManager.getData(playerName);

									sender.sendMessage("Đã reset nhặt item của " + args[1] + " thành công");

									data.setNhatItem(0);
									PLAYERdatabaseManager.save(playerName);

									return;
								}
							}
						}
						sender.sendMessage(Utils.color("&cDữ liệu của &e" + args[1] + "&c không tồn tại"));
					});

				}
			}

			return false;
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				Main.getInstance().reloadConfig();
				tuchetaoV2File.reload();
				hologramManager.reloadHologram();
				sender.sendMessage("Reload thành công");
				return false;
			}

			if (args[0].equalsIgnoreCase("resetallnhatitem")) {

				sender.sendMessage("Tiến hành reset nhặt item all...");
				File dataFolder = new File(Main.getInstance().getDataFolder() + "/player");
				File[] listOfFilesData = dataFolder.listFiles();

				for (int i = 0; i < listOfFilesData.length; i++) {
					if (listOfFilesData[i].isFile()) {

						File dataFile = listOfFilesData[i];
						String playerName = FilenameUtils.removeExtension(dataFile.getName()).toString();

						playerData data = PLAYERdatabaseManager.getData(playerName);
						data.setNhatItem(0);
						if (Bukkit.getPlayer(playerName) == null)
							PLAYERdatabaseManager.unload(playerName);
						else
							PLAYERdatabaseManager.save(playerName);
					}
				}
				sender.sendMessage("Reset thành công");
			}
		}

		sender.sendMessage("SkyGenCore - Cortez Romeo");
		sender.sendMessage(" ");
		sender.sendMessage("        - Generator -");
		sender.sendMessage(
				"/skygencore generator set <generator name> <type (MATERIAL/MMOITEM)> <material> <time (s)>");
		sender.sendMessage("/skygencore generator remove (nhìn vào block để xóa)");
		sender.sendMessage("/skygencore generator remove <generator name>");
		sender.sendMessage("/skygencore generator list");
		sender.sendMessage(" ");
		sender.sendMessage("        - Player -");
		sender.sendMessage("/skygencore tempbooster add <player> <booster> <time>");
		sender.sendMessage("/skygencore tempbooster remove <player>");
		sender.sendMessage("/skygencore giveallbooster <booster> <time> <tên người give (không ghi cũng được)>");
		sender.sendMessage("/skygencore viewtempbooster <player>");
		sender.sendMessage(" ");
		sender.sendMessage("/skygencore reload");
		sender.sendMessage("/skygencore resetnhatitem <player>");

		return true;
	}

	private String formatLocation(Location l) {
		return l.getX() + ", " + l.getY() + ", " + l.getZ();
	}

	private boolean isDouble(String text) {
		try {
			Double.parseDouble(text);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		List<String> s1 = Arrays.asList("generator", "tempbooster", "giveallbooster", "reload", "viewtempbooster ");
		List<String> flist = Lists.newArrayList();

		if (args.length == 1) {
			for (String s : s1) {
				if (s.toLowerCase().startsWith(args[0].toLowerCase()))
					flist.add(s);
			}
			return flist;
		}
		return null;
	}

	public void errorstr(CommandSender sender, String str) {
		sender.sendMessage(ChatColor.RED + str);
	}

	public void successtr(CommandSender sender, String str) {
		sender.sendMessage(ChatColor.GREEN + str);
	}

}
