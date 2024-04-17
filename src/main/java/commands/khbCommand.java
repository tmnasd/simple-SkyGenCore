package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import grassminevn.skygencore.Main;
import grassminevn.skygencore.Utils;
import manager.PLAYERdatabaseManager;
import manager.actionbarManager;
import manager.tempBoosterManager;
import storage.player.playerData;

public class khbCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private Main plugin;

	public khbCommand(final Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("kichhoatbooster").setExecutor((CommandExecutor) this);
	}

	public boolean onCommand(final CommandSender sender, final Command arg1, final String arg2, final String[] args) {

		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Console không xài được lệnh này");
			return false;
		}

		Player p = (Player) sender;
		playerData data = PLAYERdatabaseManager.getData(p.getName());

		if (!PLAYERdatabaseManager.KHBAmount.containsKey(p.getName())) {
			p.sendMessage(Utils.color("&cBạn chưa có Booster nào cần chờ để kích hoạt!"));
			return false;
		}

		data.setTempBoosterAmount(PLAYERdatabaseManager.KHBAmount.get(p.getName()));
		data.setTempBoosterTime(PLAYERdatabaseManager.KHBTime.get(p.getName()));
		tempBoosterManager.runTempBooster(p.getName());
		actionbarManager.runActionBar(p);

		tempBoosterManager.removeKHBBoster(p);

		p.sendMessage(Utils.color(Main.getInstance().getConfig().getString("message.temp-booster-actived")
				.replaceAll("%amount%", String.valueOf(data.getTempBoosterAmount()))
				.replaceAll("%timeformat%", Utils.timeFormat(data.getTempBoosterTime()))));

		return false;
	}
}