package fr.yoanndiquelou.jeelight.model;

public enum ColorMode {
	COLOR(1),
	TEMPERATURE(2),
	SLEEP(7);
	
	private int mType;
	
	ColorMode(int type) {
		mType = type;
	}
	
	public int getValue() {
		return mType;
	}
}
