package fr.yoanndiquelou.jeelight.model;

public enum EffectType {
	/** Make value change suddenly. */
	SUDDEN("sudden"), 
	/** Make the value change smoothly. */
	SMOOTH("smooth");

	/** Command value. */
	private String mValue;

	/**
	 * Effect type constructor.
	 * 
	 * @param value value of effect
	 */
	EffectType(String value) {
		mValue = value;
	}

	/**
	 * Get effect value to put in command.
	 * 
	 * @return effect value
	 */
	public String getValue() {
		return mValue;
	}
}
