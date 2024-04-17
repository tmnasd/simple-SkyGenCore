package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import grassminevn.skygencore.Main;
import grassminevn.skygencore.Utils;

public class genlistCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private Main plugin;

	public genlistCommand(final Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("genlist").setExecutor((CommandExecutor) this);
	}

	public boolean onCommand(final CommandSender sender, final Command arg1, final String arg2, final String[] args) {

		for (String str : Main.getInstance().getConfig().getStringList("message.genlist")) {
			sender.sendMessage(Utils.color(str));
		}

		return false;
	}
}