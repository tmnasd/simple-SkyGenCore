package manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import grassminevn.skygencore.Main;
import grassminevn.skygencore.Utils;
import storage.player.playerData;

public class tempBoosterManager {

	public static HashMap<String, Double> runnableAmountPlayer = new HashMap<>();
	public static HashMap<String, Long> runnableTimePlayer = new HashMap<>();

	public static void giveAllBooster(CommandSender sender, double amount, long time, String giver) {

		String name = (giver != null ? giver : sender.getName());

		Bukkit.broadcastMessage(Utils.color("&5[&dBOOSTER TOÀN SERVER&5] &e" + name
				+ "&f đã kích hoạt booster tạm thời (&6x" + amount + "&f) trong vòng &r" + Utils.timeFormat(time)));

		for (Player p : Bukkit.getOnlinePlayers()) {
			playerData data = PLAYERdatabaseManager.getData(p.getName());

			if (PLAYERdatabaseManager.runtempbooster.contains(p.getName())) {

				addKHBBooster(p, amount, time);

				p.sendMessage(Utils.color("&5[&dBOOSTER TOÀN SERVER&5] &6BẠN CẦN XÁC NHẬN!"));
				p.sendMessage(Utils.color("&7- Booster tạm thời hiện tại của bạn:"));
				p.sendMessage(Utils.color("   &3Booster x" + Utils.finalBooster(p)));
				p.sendMessage(Utils.color("   &fThời gian: " + Utils.timeFormat(data.getTempBoosterTime())));
				p.sendMessage(Utils.color("&7- Booster toàn máy chủ:"));
				p.sendMessage(Utils.color("   &3Booster x" + amount + " "
						+ (amount > Utils.finalBooster(p) ? "&a&l(LỚN HƠN BOOSTER HIỆN TẠI CỦA BẠN)"
								: "&c&l(THẤP HƠN BOOSTER HIỆN TẠI CỦA BẠN)")));
				p.sendMessage(Utils.color("   &fThời gian: " + Utils.timeFormat(time)));
				p.sendMessage(Utils.color(""));
				p.sendMessage(Utils.color(
						"&fĐể chấp nhận kích hoạt Booster tạm thời mới, sử dụng lệnh &e&l/kichhoatbooster &e(hoặc /khb) &fđể kích hoạt Booster mới"));
				p.sendMessage(Utils.color(
						"&fBạn có &a1 phút&f để chấp nhận kích hoạt booster này, nếu không thì booster mới sẽ biến mất"));

				new BukkitRunnable() {

					@Override
					public void run() {

						if ((runnableAmountPlayer.get(p.getName()) != amount)
								&& (runnableTimePlayer.get(p.getName()) != time)) {
							cancel();
						} else {

							removeKHBBoster(p);

							if (p.isOnline())
								p.sendMessage(Utils.color("&c&lBooster chờ kích hoạt (&ax" + amount + "&r "
										+ Utils.timeFormat(time) + "&r&c&l) vừa biến mất vì không có sự chấp nhận"));
						}

					}
				}.runTaskLater(Main.getInstance(), 20 * 60);

			} else {
				if (Utils.finalBooster(p) < amount) {
					data.setTempBoosterTime(time);
					data.setTempBoosterAmount(amount);

					runTempBooster(p.getName());

					p.sendMessage(
							Utils.color("&5[&dBOOSTER TOÀN SERVER&5] &aVì số Booster hiện tại của bạn &2bé&a hơn &6"
									+ amount + " &anên sẽ nhận được Booster tạm thời!"));
				} else {
					p.sendMessage(Utils.color(
							"&5[&dBOOSTER TOÀN SERVER&5] &cVì số Booster hiện tại của bạn &4lớn hơn (hoặc bằng) &6"
									+ amount + " &cnên sẽ không nhận được Booster tạm thời!"));
				}
			}

		}

	}

	public static void addTempBooster(String playerName, CommandSender sender, double amount, long time,
			Boolean sendMessage) {

		if (amount == Utils.getPlayerBonusPermission(Bukkit.getPlayer(playerName))) {
			return;
		}

		playerData data = PLAYERdatabaseManager.getData(playerName);
		Player p = Bukkit.getPlayer(playerName);
		FileConfiguration config = Main.getInstance().getConfig();

		if (p == null) {
			sender.sendMessage("Dữ liệu của " + playerName + " không tồn tại!");
			return;
		}

		// Trùng booster amount & booster type => Cộng thêm booster time
		if (data.getTempBoosterAmount() == amount) {

			if ((!sendMessage && sender == Bukkit.getPlayer(playerName)) || sendMessage) {
				p.sendMessage(Utils.color(config.getString("message.temp-booster-actived-add")
						.replaceAll("%amount%", String.valueOf(data.getTempBoosterAmount()))
						.replaceAll("%timeformat1%", Utils.timeFormat(time))
						.replaceAll("%timeformat2%", Utils.timeFormat(data.getTempBoosterTime() + time))));

			}

			if (sendMessage) {
				sender.sendMessage(Utils.color("&fCộng thêm &e" + time + "s&f cho &dBooster tạm thời &ex"
						+ data.getTempBoosterAmount() + " &fcủa &5" + playerName + " &fthành công &e(Số giây cũ: "
						+ data.getTempBoosterTime() + ")"));
			}

			data.addTempBoosterTime(time);
			return;

		}

		data.setTempBoosterAmount(amount);
		data.setTempBoosterTime(time);

		if ((!sendMessage && sender == Bukkit.getPlayer(playerName)) || sendMessage) {
			p.sendMessage(Utils.color(config.getString("message.temp-booster-actived")
					.replaceAll("%amount%", String.valueOf(data.getTempBoosterAmount()))
					.replaceAll("%timeformat%", Utils.timeFormat(data.getTempBoosterTime()))));
		}

		if (sendMessage)
			sender.sendMessage(Utils.color("&fKích hoạt &dBooster tạm thời &ex" + amount + " &ftrong thời gian "
					+ Utils.timeFormat(time) + " cho &5" + playerName + " &fthành công"));

		runTempBooster(playerName);
		actionbarManager.runActionBar(p);
	}

	public static void addKHBBooster(Player p, double amount, long time) {
		PLAYERdatabaseManager.KHBAmount.put(p.getName(), amount);
		PLAYERdatabaseManager.KHBTime.put(p.getName(), time);
		runnableAmountPlayer.put(p.getName(), amount);
		runnableTimePlayer.put(p.getName(), time);
	}

	public static void removeKHBBoster(Player p) {
		if (PLAYERdatabaseManager.KHBAmount.containsKey(p.getName())) {
			PLAYERdatabaseManager.KHBAmount.remove(p.getName());
		}
		if (PLAYERdatabaseManager.KHBTime.containsKey(p.getName())) {
			PLAYERdatabaseManager.KHBTime.remove(p.getName());
		}
		if (runnableAmountPlayer.containsKey(p.getName())) {
			runnableAmountPlayer.remove(p.getName());
		}
		if (runnableTimePlayer.containsKey(p.getName())) {
			runnableTimePlayer.remove(p.getName());
		}
	}

	public static void runTempBooster(String playerName) {

		playerData data = PLAYERdatabaseManager.getData(playerName);

		if (PLAYERdatabaseManager.runtempbooster.contains(playerName) || data.getTempBoosterAmount() == 0)
			return;
		else
			PLAYERdatabaseManager.runtempbooster.add(playerName);

		new BukkitRunnable() {
			public void run() {

				if (data.getTempBoosterTime() > 0) {
					data.removeTempBoosterTime(1);
				}

				if (data.getTempBoosterTime() <= 1 || !PLAYERdatabaseManager.runtempbooster.contains(playerName)) {

					if (Bukkit.getPlayer(playerName) != null)
						Bukkit.getPlayer(playerName).sendMessage(
								Utils.color(Main.getInstance().getConfig().getString("message.temp-booster-expired")
										.replace("%amount%", String.valueOf(data.getTempBoosterAmount()))));

					data.setTempBoosterAmount(0.0);
					data.setTempBoosterTime(0);
					PLAYERdatabaseManager.runtempbooster.remove(playerName);

					if (Bukkit.getPlayer(playerName) == null)
						PLAYERdatabaseManager.save(playerName);

					cancel();
					return;

				}

			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}

}
