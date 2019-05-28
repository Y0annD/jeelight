package fr.yoanndiquelou.jeelight.model;

/**
 * State of flow.
 * 
 * @author Y0annD
 *
 */
public class FlowColor {
	/** Flow color duration. */
	private int mDuration;
	/** Color Mode. */
	private ColorMode mMode;
	/** Color or temperature. */
	private int mColor;
	/** Brightness value. */
	private int mBrightness;

	/**
	 * Instance of flow state.
	 * 
	 * @param mode       color mode
	 * @param color      color to set
	 * @param brightness brightness to set
	 * @param duration   duration of the state
	 */
	public FlowColor(ColorMode mode, int color, int brightness, int duration) {
		mMode = mode;
		mColor = color;
		mBrightness = brightness;
		mDuration = duration;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(mDuration).append(",").append(mMode.getValue()).append(",").append(mColor).append(",")
				.append(mBrightness);
		return builder.toString();
	}
}
