package fr.yoanndiquelou.jeelight.model;

public enum ColorFlowEnd {
	/** Recover old state. */
	RECOVER(0),
	/** Keep last flow state. */
	KEEP(1),
	/** Switch light off. */
	OFF(2);
	/** Value to send. */
	private int mValue;
	
	/**
	 * Color flow end constructor.
	 * @param value value
	 */
	ColorFlowEnd(int value) {
		mValue = value;
	}
	
	/**
	 * Get end value.
	 * @return end value
	 */
	public int getValue() {
		return mValue;
	}
}
