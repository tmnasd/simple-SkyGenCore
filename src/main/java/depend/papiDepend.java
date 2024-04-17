package depend;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import grassminevn.skygencore.Utils;
import manager.PLAYERdatabaseManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import storage.player.playerData;

public class papiDepend extends PlaceholderExpansion {
	public String getAuthor() {
		return "Cortez Romeo";
	}

	public String getIdentifier() {
		return "skygen";
	}

	public String getVersion() {
		return "1.0";
	}

	public String onPlaceholderRequest(Player player, String identifier) {
		if (player == null) {
			return "";
		}

		// playerData data = databaseManager.getData(player.getName());
		DecimalFormat df = new DecimalFormat("#.##");
		playerData data = PLAYERdatabaseManager.getData(player.getName());

		if (identifier.equalsIgnoreCase("nhatitem")) {
			return String.valueOf(data.getNhatItem());
		}

		if (identifier.equalsIgnoreCase("kill")) {
			return String.valueOf(data.getKill());
		}

		if (identifier.equalsIgnoreCase("die")) {
			return String.valueOf(data.getDie());
		}

		if (identifier.equalsIgnoreCase("kdr")) {
			double kill = data.getKill();
			double die = data.getDie();

			if (die == 0)
				die = 1;

			return df.format(kill / die);
		}

		if (identifier.equalsIgnoreCase("booster")) {
			return df.format(Utils.finalBooster(player));
		}

		return "";
	}
}