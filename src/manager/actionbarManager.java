package manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import grassminevn.skygencore.Main;
import grassminevn.skygencore.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import storage.player.playerData;

public class actionbarManager {

	public static List<String> actionbar = new ArrayList<>();

	public static void runActionBar(Player p) {

		if (actionbar.contains(p.getName()))
			return;

		runTask(p);

	}

	private static void runTask(Player p) {
		String pName = p.getName();

		if (actionbar.contains(pName))
			return;
		actionbar.add(pName);

		new BukkitRunnable() {

			@Override
			public void run() {

				if (!p.isOnline()) {
					actionbar.remove(pName);
					cancel();
					return;
				}

				try {
					playerData data = PLAYERdatabaseManager.getData(pName);
					FileConfiguration config = Main.getInstance().getConfig();

					if (data.getTempBoosterTime() > 0) {
						String message = config.getString("message.actionbar-1");
						message = message.replace("%booster%", String.valueOf(Utils.finalBooster(p)));
						message = message.replace("%timeformat%", Utils.timeFormat(data.getTempBoosterTime()));

						p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
								TextComponent.fromLegacyText(Utils.color(message)));
					} else {
						String message = config.getString("message.actionbar");
						message = message.replace("%booster%", String.valueOf(Utils.finalBooster(p)));
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
								TextComponent.fromLegacyText(Utils.color(message)));
					}

				} catch (Exception e) {
					actionbar.remove(pName);
					cancel();
					return;
				}

			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);

	}

}
