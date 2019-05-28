package fr.yoanndiquelou.jeelight.model;

/**
 * Color mode.
 * 
 * @author Y0annD
 *
 */
public enum ColorMode {
	/** Mode RGB color. */
	COLOR(1),
	/** Mode white temperature. */
	TEMPERATURE(2),
	/** Mode off. */
	SLEEP(7);

	private int mType;

	/**
	 * Color mode enum.
	 * 
	 * @param type color type
	 */
	ColorMode(int type) {
		mType = type;
	}

	/**
	 * Get color mode.
	 * 
	 * @return color mode
	 */
	public int getValue() {
		return mType;
	}
}
