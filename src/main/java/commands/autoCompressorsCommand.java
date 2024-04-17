package commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import grassminevn.skygencore.Main;
import grassminevn.skygencore.Utils;
import manager.PLAYERdatabaseManager;
import manager.autoCompressorsManager;
import net.md_5.bungee.api.ChatColor;

public class autoCompressorsCommand implements CommandExecutor {
	private final Main plugin;

	public autoCompressorsCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("autocompressors").setExecutor(this);
	}

	public List<String> queueCommand = new ArrayList<String>();

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		if (!(sender instanceof Player)) {
			errorstr(sender, "Lệnh này dành cho người chơi!");
			return false;
		}

		Player p = (Player) sender;

		if (queueCommand.contains(p.getName())) {
			p.sendMessage(Utils.color("&cVui lòng đợi một chút..."));
			return false;
		}

		if (!PLAYERdatabaseManager.autocompressorsPlayer.contains(p)) {
			autoCompressorsManager.runAdvancedAC(p);
			return false;
		}

		autoCompressorsManager.stopAutoCompressors(p);
		p.sendMessage(Utils.color(plugin.getConfig().getString("message.autocompressors-off")));

		return true;
	}

	public void errorstr(CommandSender sender, String str) {
		sender.sendMessage(ChatColor.RED + str);
	}

	public void successtr(CommandSender sender, String str) {
		sender.sendMessage(ChatColor.GREEN + str);
	}

}
