package fr.yoanndiquelou.jeelight.model;

/**
 * Type of adjustment.
 * 
 * @author Y0annD
 *
 */
public enum AdjustType {
	INCREASE("increase"), DECREASE("decrease"), CIRCLE("circle");

	/** Value of adjustment. */
	private String mValue;

	/**
	 * Adjust type constructor.
	 * 
	 * @param value value of adjustment
	 */
	AdjustType(String value) {
		mValue = value;
	}

	/**
	 * Return string value of adjustment.
	 */
	public String getValue() {
		return mValue;
	}
}
