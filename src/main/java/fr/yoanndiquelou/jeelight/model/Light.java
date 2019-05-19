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
	/** Supported tasks. */
	private Set<String> mTasks;
	/** Power status. */
	@Property("power")
	private boolean mPower;
	/** Brightness. */
	@Property("bright")
	private int mBrightness;
	/** Color mode. */
	@Property("color_mode")
	private int mColorMode;
	/** Ct. */
	@Property("ct")
	private int mCt;
	/** rgb. */
	@Property("rgb")
	private int mRgb;
	/** Hue. */
	@Property("hue")
	private int mHue;
	/** Saturation. */
	@Property("sat")
	private int mSaturation;
	/** Name. */
	@Property("name")
	private String mName;
	/** Delay. */
	@Property("delayoff")
	private int mCron;
	/** List of listeners. */
	private Set<PropertyChangeListener> mListeners;

	/**
	 * Empty constructor.
	 */
	public Light() {
		mTasks = new HashSet<>();
		mListeners = new HashSet<>();
		mCron = 0;
	}

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
		// Add allowed tasks
		String tasks = headers.get("SUPPORT");
		if (null != tasks) {
			String[] taskArray = tasks.split(" ");
			for (String task : taskArray) {
				l.addTask(task);
			}
		}

		return l;
	}

	public void setIp(String ip) {
		mIp = ip;
	}

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
	 * Get allowed tasks.
	 * 
	 * @return allowed tasks
	 */
	public Set<String> getTasks() {
		return mTasks;
	}

	/**
	 * Add a task.
	 * 
	 * @param task task name
	 */
	public void addTask(String task) {
		mTasks.add(task);
	}

	/**
	 * Remove a task.
	 * 
	 * @param task task name to remove
	 */
	public void removeTask(String task) {
		mTasks.remove(task);
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
	 * CT?
	 * 
	 * @return ct
	 */
	public int getCt() {
		return mCt;
	}

	/**
	 * CT?
	 * 
	 * @param ct ct
	 */
	public void setCt(int ct) {
		this.mCt = ct;
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

	public void addListener(PropertyChangeListener listener) {
		mListeners.add(listener);
	}

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
		builder.append("Name: ").append(mName).append("\r\n");
		builder.append("IP: ").append(mIp).append("\r\n");
		builder.append("Location: ").append(mLocation).append("\r\n");
		builder.append("Id: ").append(mId).append("\r\n");
		builder.append("Model: ").append(mModel).append("\r\n");
		builder.append("Firmware: ").append(mFirmware).append("\r\n");
		builder.append("power: ").append((mPower ? "on" : "off")).append("\r\n");
		builder.append("Brightness: ").append(mBrightness).append("\r\n");
		builder.append("Color mode: ").append(mColorMode).append("\r\n");
		builder.append("CT: ").append(mCt).append("\r\n");
		builder.append("RGB: ").append(mRgb).append("\r\n");
		builder.append("Hue: ").append(mHue).append("\r\n");
		builder.append("Saturation: ").append(mSaturation).append("\r\n");
		builder.append("delay off: ").append(mCron).append("\r\n");
		builder.append("tasks:").append("\r\n");
		for (String task : mTasks) {
			builder.append("\t").append(task).append("\r\n");
		}
		return builder.toString();
	}

}
