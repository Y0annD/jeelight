package fr.yoanndiquelou.jeelight.model;

/**
 * Type of Scene.
 * @author Y0annD
 *
 */
public enum SceneClass {
	/** Color scene. */
	COLOR("color"),
	/** Hue and saturation. */
	HSV("hsv"),
	/** Color temperature. */
	CT("ct"),
	/** Color flow. */
	CF("cf"),
	/** Turn on and set brightness, then turn off*/
	AUTO_DELAY_OFF("auto_delay_off");
	
	/**
	 * String value of the scene class.
	 */
	String mValue;
	
	/**
	 * Scene class constructor.
	 * @param val class
	 */
	SceneClass(String val) {
		mValue = val;
	}
	
	/**
	 * Return scene class value.
	 * @return value
	 */
	public String getValue() {
		return mValue;
	}
}
