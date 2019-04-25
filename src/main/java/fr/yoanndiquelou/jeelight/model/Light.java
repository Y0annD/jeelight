package fr.yoanndiquelou.jeelight.model;

import java.util.List;

/**
 * Class that describe Light.
 * 
 * @author yoann Diquélou
 *
 */
public class Light {
	/** Light unique identifier. */
	private long mId;
	/** IP of light. */
	private String mIp;
	/** Light model. */
	private String mModel;
	/** Firmware version. */
	private int mFirmware;
	/** Supported tasks. */
	private List<String> mTasks;
	/** Power status. */
	private boolean mPower;
	/** Brightness. */
	private int mBrightness;
	/** Color mode. */
	private int mColorMode;
	/** Ct. */
	private int mCt;
	/** rgb. */
	private long mRGB;
	/** Hue. */
	private int mHue;
	/** Saturation. */
	private int mSaturation;
	/** Name. */
	private String mName;
	
	
	/**
	 * Empty constructor.
	 */
	public Light() {
		
	}
	
	public void setIp(String ip) {
		mIp = ip;
	}
	
	public String getIp() {
		return mIp;
	}

	/** Get Id.
	 * 
	 * @return id of the light
	 */
	public long getId() {
		return mId;
	}

	/**
	 * Set Id.
	 * @param id id
	 */
	public void setId(long id) {
		mId = id;
	}

	/**
	 * Get light model.
	 * @return light model
	 */
	public String getModel() {
		return mModel;
	}

	/**
	 * Set light model.
	 * @param model model
	 */
	public void setModel(String model) {
		mModel = model;
	}

	/**
	 * Get firmware version.
	 * @return firmware version
	 */
	public int getFirmware() {
		return mFirmware;
	}

	/**
	 * Set firmware version.
	 * @param firmware firmware version
	 */
	public void setFirmware(int firmware) {
		mFirmware = firmware;
	}

	/**
	 * Get allowed tasks.
	 * @return allowed tasks
	 */
	public List<String> getTasks() {
		return mTasks;
	}

	/**
	 * Set allowed tasks.
	 * @param tasks tasks
	 */
	public void setTasks(List<String> tasks) {
		this.mTasks = tasks;
	}

	/**
	 * Get power status.
	 * @return power status
	 */
	public boolean isPower() {
		return mPower;
	}


	/**
	 * Set power status.
	 * @param power power status
	 */
	public void setPower(boolean power) {
		mPower = power;
	}

	/**
	 * Set the brightness.
	 * @return light brightness
	 */
	public int getBrightness() {
		return mBrightness;
	}

	/**
	 * Set brightness.
	 * @param brightness brightness
	 */
	public void setBrightness(int brightness) {
		mBrightness = brightness;
	}

	/**
	 * Get the color mode.
	 * @return color mode
	 */
	public int getColorMode() {
		return mColorMode;
	}

	/**
	 * Color mode.
	 * @param colorMode colormode
	 */
	public void setColorMode(int colorMode) {
		mColorMode = colorMode;
	}

	/**
	 * CT?
	 * @return ct
	 */
	public int getCt() {
		return mCt;
	}

	/**
	 * CT?
	 * @param ct ct
	 */
	public void setCt(int ct) {
		this.mCt = ct;
	}

	/**
	 * Get RGB value.
	 * @return rgb value
	 */
	public long getRGB() {
		return mRGB;
	}

	/**
	 * Set RGB value.
	 * @param RGB rgb
	 */
	public void setRGB(long RGB) {
		mRGB = RGB;
	}

	/**
	 * Get Hue.
	 * @return hue
	 */
	public int getHue() {
		return mHue;
	}

	/**
	 * Set Hue.
	 * @param hue hue
	 */
	public void setHue(int hue) {
		mHue = hue;
	}

	/**
	 * Get saturation.
	 * @return saturation
	 */
	public int getSaturation() {
		return mSaturation;
	}

	/**
	 * Set saturation.
	 * @param saturation saturation
	 */
	public void setSaturation(int saturation) {
		mSaturation = saturation;
	}

	/**
	 * Get name of the light.
	 * @return light name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Set light name.
	 * @param name name of the light
	 */
	public void setName(String name) {
		mName = name;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Name: ").append(mName).append("\r\n");
		builder.append("power: ").append((true?"on":"off")).append("\r\n");
		builder.append("Brightness: ").append(mBrightness).append("\r\n");
		return builder.toString();
	}
	
}
