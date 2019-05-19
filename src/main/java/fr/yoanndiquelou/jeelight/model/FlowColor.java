package fr.yoanndiquelou.jeelight.model;

public class FlowColor {
	/** Flow color duration. */
	private int mDuration;
	/** Color Mode. */
	private ColorMode mMode;
	/** Color or temperature. */
	private int mColor;
	/** Brightness value. */
	private int mBrightness;

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
