package storage.player;

public interface PLAYERstorage {
	playerData getData(String p0);

	void saveData(String p0, playerData p1);
}