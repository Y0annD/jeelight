package fr.yoanndiquelou.jeelight.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.yoanndiquelou.jeelight.annotation.Property;
import fr.yoanndiquelou.jeelight.exception.ParameterException;
import fr.yoanndiquelou.jeelight.utils.AttributeParser;

/**
 * Class that describe Light.
 * 
 * @author yoann Diquélou
 *
 */
public class Light {
	/** Location. */
	private String mLocation;
	/** Light unique identifier. */
	private long mId;
	/** IP of light. */
	private String mIp;
	/** Light model. */
	private String mModel;
	/** Firmware version. */
	private int mFirmware;
	/** Power status. */
	@Property("power")
	private boolean mPower;
	/** Background power status. */
	@Property("bg_power")
	private boolean mBgPower;
	/** Brightness. */
	@Property("bright")
	private int mBrightness;
	/** Background brightness. */
	@Property("bg_bright")
	private int mBgBrightness;
	/** Color mode. */
	@Property("color_mode")
	private int mColorMode;
	/** Background color mode. */
	@Property("bg_lmode")
	private int mBgColorMode;
	/** Color temperature. */
	@Property("ct")
	private int mCt;
	/** Background color temperature. */
	@Property("bg_ct")
	private int mBgCt;
	/** rgb. */
	@Property("rgb")
	private int mRgb;
	/** Background color. */
	@Property("bg_rgb")
	private int mBgRgb;
	/** Hue. */
	@Property("hue")
	private int mHue;
	/** Background hue. */
	@Property("bg_hue")
	private int mBgHue;
	/** Saturation. */
	@Property("sat")
	private int mSaturation;
	/** Background saturation. */
	@Property("bg_sat")
	private int mBgSaturation;
	/** Name. */
	@Property("name")
	private String mName;
	/** Delay. */
	@Property("delayoff")
	private int mCron;
	/** Flowing. */
	@Property("flowing")
	private int mFlowing;
	/** Background flowing. */
	@Property("bg_flowing")
	private int mBgFlowing;
	/** Flow params. */
	@Property("flow_params")
	private String mFlowParams = "";
	/** Background flow params. */
	@Property("bg_flow_params")
	private String mBgFlowParams = "";
	/** Music On. */
	@Property("music_on")
	private int mMusic;
	/** Brightness of night mode. */
	@Property("nl_br")
	private int mNightBright;
	/**
	 * Active mode.
	 * <p>
	 * Only for ceiling light.
	 * </p>
	 */
	@Property("active_mode")
	private int mActiveMode;
	/** List of listeners. */
	private Set<PropertyChangeListener> mListeners;
	/** Available methods. */
	private Set<Method> mAvailableMethods;

	/**
	 * Empty constructor.
	 */
	public Light() {
		mListeners = new HashSet<>();
		mAvailableMethods = new HashSet<Method>();
		mCron = 0;
	}

	/**
	 * Fill light from datagram packet.
	 * 
	 * @param address    adress of light
	 * @param packetData received packet
	 * @return light object
	 */
	public static Light fromDatagramPacket(InetAddress address, byte[] packetData) {
		Light l = new Light();
		Map<String, String> headers = new HashMap<>();
		Pattern pattern = Pattern.compile("(.*): (.*)");

		String[] lines = new String(packetData).split("\r\n");
		for (String line : lines) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				headers.put(matcher.group(1).toUpperCase(), matcher.group(2));
			}
		}
		l.setIp(address.getHostAddress());
		l.setLocation(headers.get("LOCATION"));
		l.setName(address.getHostName());
		l.setId(new BigInteger(headers.get("ID").substring(2), 16).longValue());
		l.setModel(headers.get("MODEL"));
		l.setFirmware(Integer.valueOf(headers.get("FW_VER")));
		l.setPower("on".equalsIgnoreCase(headers.get("POWER")));
		l.setBrightness(Integer.valueOf(headers.get("BRIGHT")));
		l.setColorMode(Integer.valueOf(headers.get("COLOR_MODE")));
		l.setCt(Integer.valueOf(headers.get("CT")));
		l.setRGB(Integer.valueOf(headers.get("RGB")));
		l.setHue(Integer.valueOf(headers.get("HUE")));
		l.setSaturation(Integer.valueOf(headers.get("SAT")));
		// Add allowed methods
		String tasks = headers.get("SUPPORT");
		if (null != tasks) {
			String[] taskArray = tasks.split(" ");
			for (String task : taskArray) {
				l.addMethod(Method.mMethodMap.get(task.trim()));
			}
		}

		return l;
	}

	/**
	 * Add a new available method.
	 * 
	 * @param method method to add
	 */
	public void addMethod(Method method) {
		mAvailableMethods.add(method);
	}

	/**
	 * Get all available methods.
	 * 
	 * @return set of available methods
	 */
	public Set<Method> getAvailableMethods() {
		return mAvailableMethods;
	}

	/**
	 * Is the method available for this light.
	 * 
	 * @param method method to check
	 * @return true if available, false else
	 */
	public boolean isMethodAvailable(Method method) {
		return mAvailableMethods.contains(method);
	}

	/**
	 * Set light IP.
	 * 
	 * @param ip ip of light
	 */
	public void setIp(String ip) {
		mIp = ip;
	}

	/**
	 * Get IP of light.
	 * 
	 * @return ip of light
	 */
	public String getIp() {
		return mIp;
	}

	/**
	 * Get Id.
	 * 
	 * @return id of the light
	 */
	public long getId() {
		return mId;
	}

	/**
	 * Set Id.
	 * 
	 * @param id id
	 */
	public void setId(long id) {
		mId = id;
	}

	/**
	 * Set device location.
	 * 
	 * @param location device location
	 */
	public void setLocation(String location) {
		mLocation = location;
	}

	/**
	 * Get Device location.
	 * 
	 * @return location
	 */
	public String getLocation() {
		return mLocation;
	}

	/**
	 * Get light model.
	 * 
	 * @return light model
	 */
	public String getModel() {
		return mModel;
	}

	/**
	 * Set light model.
	 * 
	 * @param model model
	 */
	public void setModel(String model) {
		mModel = model;
	}

	/**
	 * Get firmware version.
	 * 
	 * @return firmware version
	 */
	public int getFirmware() {
		return mFirmware;
	}

	/**
	 * Set firmware version.
	 * 
	 * @param firmware firmware version
	 */
	public void setFirmware(int firmware) {
		mFirmware = firmware;
	}

	/**
	 * Remove an available method.
	 * 
	 * @param method method to remove
	 */
	public void removeMethod(Method method) {
		mAvailableMethods.remove(method);
	}

	/**
	 * Get power status.
	 * 
	 * @return power status
	 */
	public boolean isPower() {
		return mPower;
	}

	/**
	 * Set power status.
	 * 
	 * @param power power status
	 */
	public void setPower(boolean power) {
		mPower = power;
	}

	/**
	 * Get background power status.
	 * 
	 * @return background power status
	 */
	public boolean isBgPower() {
		return mBgPower;
	}

	/**
	 * Set background power status.
	 * 
	 * @param power background power status
	 */
	public void setBgPower(boolean power) {
		mBgPower = power;
	}

	/**
	 * Set the brightness.
	 * 
	 * @return light brightness
	 */
	public int getBrightness() {
		return mBrightness;
	}

	/**
	 * Set brightness.
	 * 
	 * @param brightness brightness
	 */
	public void setBrightness(int brightness) {
		mBrightness = brightness;
	}

	/**
	 * Set the background brightness.
	 * 
	 * @return light background brightness
	 */
	public int getBgBrightness() {
		return mBgBrightness;
	}

	/**
	 * Set background brightness.
	 * 
	 * @param brightness background brightness
	 */
	public void setBgBrightness(int brightness) {
		mBgBrightness = brightness;
	}

	/**
	 * Get the color mode.
	 * 
	 * @return color mode
	 */
	public int getColorMode() {
		return mColorMode;
	}

	/**
	 * Color mode.
	 * 
	 * @param colorMode colormode
	 */
	public void setColorMode(int colorMode) {
		mColorMode = colorMode;
	}

	/**
	 * Get the background color mode.
	 * 
	 * @return background color mode
	 */
	public int getBgColorMode() {
		return mBgColorMode;
	}

	/**
	 * Background Color mode.
	 * 
	 * @param background colorMode colormode
	 */
	public void setBgColorMode(int colorMode) {
		mBgColorMode = colorMode;
	}

	/**
	 * Color temperature.
	 * 
	 * @return ct
	 */
	public int getCt() {
		return mCt;
	}

	/**
	 * Color temperature.
	 * 
	 * @param ct ct
	 */
	public void setCt(int ct) {
		this.mCt = ct;
	}

	/**
	 * Background Color temperature.
	 * 
	 * @return background ct
	 */
	public int getBgCt() {
		return mBgCt;
	}

	/**
	 * Background Color temperature.
	 * 
	 * @param ct ct
	 */
	public void setBgCt(int ct) {
		this.mBgCt = ct;
	}

	/**
	 * Get RGB value.
	 * 
	 * @return rgb value
	 */
	public int getRGB() {
		return mRgb;
	}

	/**
	 * Set RGB value.
	 * 
	 * @param rgb rgb
	 */
	public void setRGB(int rgb) {
		mRgb = rgb;
	}

	/**
	 * Get background RGB value.
	 * 
	 * @return rgb value
	 */
	public int getBgRGB() {
		return mBgRgb;
	}

	/**
	 * Set background RGB value.
	 * 
	 * @param rgb rgb
	 */
	public void setBgRGB(int rgb) {
		mBgRgb = rgb;
	}

	/**
	 * Get Hue.
	 * 
	 * @return hue
	 */
	public int getHue() {
		return mHue;
	}

	/**
	 * Set Hue.
	 * 
	 * @param hue hue
	 */
	public void setHue(int hue) {
		mHue = hue;
	}

	/**
	 * Get background Hue.
	 * 
	 * @return background hue
	 */
	public int getBgHue() {
		return mBgHue;
	}

	/**
	 * Set background Hue.
	 * 
	 * @param hue hue
	 */
	public void setBgHue(int hue) {
		mBgHue = hue;
	}

	/**
	 * Get saturation.
	 * 
	 * @return saturation
	 */
	public int getSaturation() {
		return mSaturation;
	}

	/**
	 * Set saturation.
	 * 
	 * @param saturation saturation
	 */
	public void setSaturation(int saturation) {
		mSaturation = saturation;
	}

	/**
	 * Get background saturation.
	 * 
	 * @return saturation
	 */
	public int getBgSaturation() {
		return mBgSaturation;
	}

	/**
	 * Set background saturation.
	 * 
	 * @param saturation saturation
	 */
	public void setBgSaturation(int saturation) {
		mBgSaturation = saturation;
	}

	/**
	 * Get name of the light.
	 * 
	 * @return light name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Set light name.
	 * 
	 * @param name name of the light
	 */
	public void setName(String name) {
		mName = name;
	}

	/**
	 * Set active mode.
	 * <p>
	 * Only for ceiling light.
	 * </p>
	 * 
	 * @param mode active mode.
	 */
	public void setActiveMode(int mode) {
		mActiveMode = mode;
	}

	/**
	 * Return active mode.
	 * 
	 * @return active mode
	 */
	public int getActiveMode() {
		return mActiveMode;
	}

	/**
	 * Add light listener.
	 * 
	 * @param listener listener to add
	 */
	public void addListener(PropertyChangeListener listener) {
		mListeners.add(listener);
	}

	/**
	 * Remove listener.
	 * 
	 * @param listener listener to remove
	 */
	public void removeListener(PropertyChangeListener listener) {
		mListeners.remove(listener);
	}

	public void notifyListeners(String property, Object oldValue, Object newValue) {
		for (PropertyChangeListener listener : mListeners) {
			listener.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
		}
	}

	/**
	 * Update property from Yeelight response.
	 * 
	 * @param property property to update
	 * @param param    new value
	 * @return the light
	 * @throws ParameterException parameter exception
	 */
	public Light updateFromMethod(String property, String param) throws ParameterException {
		for (Field field : this.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Property.class)
					&& field.getAnnotation(Property.class).value().contentEquals(property)) {
				try {
					Object newValue = AttributeParser.parse(param);
					notifyListeners(property, field.get(this), newValue);
					field.set(this, newValue);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new ParameterException("Unable to set field: " + field.getName() + " with value: " + param,
							e);
				}
			}
		}
		return this;
	}

	/**
	 * Return delay before light switch off. 0 if no sleep mode set.
	 * 
	 * @return delay before switch off
	 */
	public int getCron() {
		return mCron;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Field field : this.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Property.class)) {
				try {
					builder.append(field.getAnnotation(Property.class).value()).append(": ")
							.append(field.get(this).toString()).append("\r\n");
				} catch (IllegalAccessException e) {
				}
			}
		}
		return builder.toString();
	}

}
