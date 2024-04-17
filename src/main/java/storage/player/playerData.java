package storage.player;

public class playerData {

	private int kill;
	private int die;
	private int nhatItem;
	private double temp_booster_amount;
	private long temp_booster_time;

	public playerData(int kill, int die, int nhatItem, double temp_booster_amount, long temp_booster_time) {
		this.kill = kill;
		this.die = die;
		this.nhatItem = nhatItem;
		this.temp_booster_amount = temp_booster_amount;
		this.temp_booster_time = temp_booster_time;
	}

	public int getKill() {
		return kill;
	}

	public void addKill(int kill) {

		if (kill < 0 && this.kill + kill < 0) {
			this.kill = 0;
			return;
		}

		this.kill = this.kill + kill;
	}

	public void removeKill(int die) {

		if (this.die - die < 0) {
			this.die = 0;
			return;
		}

		this.die = this.die - die;
	}

	public void setKill(int kill) {

		if (kill < 0) {
			this.kill = 0;
			return;
		}

		this.kill = kill;
	}

	//
	
	public int getDie() {
		return die;
	}

	public void addDie(int die) {

		if (die < 0 && this.die + die < 0) {
			this.die = 0;
			return;
		}

		this.die = this.die + die;
	}

	public void removeDie(int die) {

		if (this.die - die < 0) {
			this.die = 0;
			return;
		}

		this.die = this.die - die;
	}

	public void setDie(int die) {

		if (die < 0) {
			this.die = 0;
			return;
		}

		this.die = die;
	}

	//	
	public int getNhatItem() {
		return nhatItem;
	}

	public void addNhatItem(int nhatItem) {

		if (nhatItem < 0 && this.nhatItem + nhatItem < 0) {
			this.nhatItem = 0;
			return;
		}

		this.nhatItem = this.nhatItem + nhatItem;
	}

	public void removeNhatItem(int nhatItem) {

		if (this.nhatItem - nhatItem < 0) {
			this.nhatItem = 0;
			return;
		}

		this.nhatItem = this.nhatItem - nhatItem;
	}

	public void setNhatItem(int nhatItem) {

		if (nhatItem < 0) {
			this.nhatItem = 0;
			return;
		}

		this.nhatItem = nhatItem;
	}
	//
	
	public double getTempBoosterAmount() {
		return temp_booster_amount;
	}

	public void addTempBoosterAmount(double temp_booster_amount) {

		if (temp_booster_amount < 0 && this.temp_booster_amount + temp_booster_amount < 0) {
			this.temp_booster_amount = 0;
			return;
		}

		this.temp_booster_amount = this.temp_booster_amount + temp_booster_amount;
	}

	public void removeTempBoosterAmount(double temp_booster_amount) {

		if (this.temp_booster_amount - temp_booster_amount < 0) {
			this.temp_booster_amount = 0;
			return;
		}

		this.temp_booster_amount = this.temp_booster_amount - temp_booster_amount;
	}

	public void setTempBoosterAmount(double temp_booster_amount) {

		if (temp_booster_amount < 0) {
			this.temp_booster_amount = 0;
			return;
		}

		this.temp_booster_amount = temp_booster_amount;
	}

	public long getTempBoosterTime() {
		return temp_booster_time;
	}

	public void addTempBoosterTime(long temp_booster_time) {

		if (temp_booster_time < 0 && this.temp_booster_time + temp_booster_time < 0) {
			this.temp_booster_time = 0;
			return;
		}

		this.temp_booster_time = this.temp_booster_time + temp_booster_time;
	}

	public void removeTempBoosterTime(long temp_booster_time) {

		if (this.temp_booster_time - temp_booster_time < 0) {
			this.temp_booster_time = 0;
			return;
		}

		this.temp_booster_time = this.temp_booster_time - temp_booster_time;
	}

	public void setTempBoosterTime(long temp_booster_time) {

		if (temp_booster_time < 0) {
			this.temp_booster_time = 0;
			return;
		}

		this.temp_booster_time = temp_booster_time;
	}

}