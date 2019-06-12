package fr.yoanndiquelou.jeelight.model.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.Future;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import fr.yoanndiquelou.jeelight.annotation.CheckIntegerInterval;
import fr.yoanndiquelou.jeelight.annotation.Property;
import fr.yoanndiquelou.jeelight.communication.MessageManager;
import fr.yoanndiquelou.jeelight.exception.CommandException;
import fr.yoanndiquelou.jeelight.exception.ParameterException;
import fr.yoanndiquelou.jeelight.model.AdjustType;
import fr.yoanndiquelou.jeelight.model.ColorFlowEnd;
import fr.yoanndiquelou.jeelight.model.EffectType;
import fr.yoanndiquelou.jeelight.model.FlowColor;
import fr.yoanndiquelou.jeelight.model.ILight;
import fr.yoanndiquelou.jeelight.model.Light;
import fr.yoanndiquelou.jeelight.model.Method;
import fr.yoanndiquelou.jeelight.model.SceneClass;

/**
 * Light with actions managed in an easy way.
 * 
 * @author Y0annD
 *
 */
public class EasyLight implements ILight {
	/** associated light. */
	private Light mLight;
	/** Messaging to manage light. */
	private MessageManager mMessaging;
	/** Effect type. */
	private EffectType mEffect = EffectType.SMOOTH;
	/** duration of effect. */
	private int mDuration = 100;

	/**
	 * EasyLight constructor.
	 * 
	 * @param light     light to manage
	 * @param messaging messaging to manage light
	 * @throws ParameterException execption in parameter
	 * @throws IOException        network exception
	 */
	public EasyLight(Light light) throws IOException, ParameterException {
		mLight = light;
		mMessaging = MessageManager.getInstance(light);
	}

	/**
	 * EasyLight constructor for testing purposes.
	 * 
	 * @param light     light to manage
	 * @param messaging messaging that manager data
	 */
	public EasyLight(Light light, MessageManager messaging) {
		mLight = light;
		mMessaging = messaging;
	}

	/**
	 * Set type of effect.
	 * 
	 * @param effect effect
	 */
	public void setEffectType(EffectType effect) {
		mEffect = effect;
	}

	/**
	 * Return effect type.
	 * 
	 * @return effect type
	 */
	public EffectType getEffectType() {
		return mEffect;
	}

	/**
	 * Set effect duration.
	 * 
	 * @param duration duration of the effect
	 */
	public void setDuration(int duration) {
		if (duration < 30) {
			throw new IllegalArgumentException("Duration should be higher than 30 ms");
		}
		mDuration = duration;
	}

	/**
	 * Return effect duration.
	 * 
	 * @return effect duration
	 */
	public int getDuration() {
		return mDuration;
	}

	/**
	 * Get property method.
	 * 
	 * @param properties array of property to retrieve
	 * @return values of those properties, or empty string if not exist
	 * @throws CommandException command unavailable
	 */
	@Override
	public Object[] getProp(String... properties) throws CommandException {
		executeCommand(Method.GET_PROP, (Object[]) properties);
		Object[] result = new Object[properties.length];
		for (int i = 0; i < properties.length; i++) {
			result[i] = "";
			try {
				Field[] fields = mLight.getClass().getDeclaredFields();
				for (Field f : fields) {
					Property annotation = f.getAnnotation(Property.class);
					if (null != annotation && annotation.field().contentEquals(properties[i])) {
						f.setAccessible(true);
						result[i] = f.get(mLight);
						f.setAccessible(false);
					}
				}
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
				result[i] = "";
			}
		}
		return result;
	}

	/**
	 * Toggle Light status.
	 * 
	 * @return new status of light
	 * @throws CommandException unavailable method
	 */
	@Override
	public boolean toggle() throws CommandException {
		executeCommand(Method.TOGGLE);
		return mLight.isPower();
	}

	/**
	 * Set the current state as default. Persisted in light memory.
	 * 
	 * @throws CommandException unavailable method
	 */
	@Override
	public void setDefault() throws CommandException {
		executeCommand(Method.SET_DEFAULT);
	}

	/**
	 * Start color flow.
	 * 
	 * @param end  end mode
	 * @param flow list of changes in flow
	 * @throws CommandException unavailable method
	 */
	@Override
	public void startCf(ColorFlowEnd end, List<FlowColor> flow) throws CommandException {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < flow.size(); i++) {
			builder.append(flow.get(i).toString());
			if (i < flow.size() - 1) {
				builder.append(",");
			}
		}
		executeCommand(Method.START_CF, flow.size(), end.getValue(), builder.toString());
	}

	/**
	 * Stop color flow.
	 * 
	 * @throws CommandException unavailable method
	 */
	@Override
	public void stopCf() throws CommandException {
		executeCommand(Method.STOP_CF);
	}

	/**
	 * Adjust brightness.
	 * 
	 * @param percentage percentage of brightness
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	@Override
	public int adjustBright(int percentage, int duration) throws CommandException {
		return adjustBright(Method.ADJUST_BRIGHT, percentage, duration);
	}

	/**
	 * Adjust brightness.
	 * 
	 * @param percentage percentage of brightness
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	@Override
	public int bgAdjustBright(int percentage, int duration) throws CommandException {
		return adjustBright(Method.BG_ADJUST_BRIGHT, percentage, duration);
	}

	/**
	 * Adjust brightness.
	 * 
	 * @param method     Method.SET_BRIGHT or Method.BG_SET_BRIGHT
	 * @param percentage percentage of brightness
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	private int adjustBright(Method method, int percentage, int duration) throws CommandException {
		if (percentage < -100 || percentage > 100) {
			throw new IllegalArgumentException("Percentage attribute must be positive and lower than 101%");
		}
		if (duration < 30) {
			throw new IllegalArgumentException("Duration must be higher than 29ms");
		}

		executeCommand(method, percentage, duration);
		if (Method.ADJUST_BRIGHT.equals(method)) {
			return mLight.getBrightness();
		} else {
			return mLight.getBgBrightness();
		}
	}

	/**
	 * Adjust color temperature.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	@Override
	public int adjustColorTemperature(int percentage, int duration) throws CommandException {
		return adjustColorTemperature(Method.ADJUST_CT, percentage, duration);
	}

	/**
	 * Adjust color.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	@Override
	public int adjustColor(int percentage, int duration) throws CommandException {
		return adjustColor(Method.ADJUST_COLOR, percentage, duration);
	}

	/**
	 * Adjust color.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	private int adjustColor(Method method, int percentage, int duration) throws CommandException {
		if (percentage < -100 || percentage > 100) {
			throw new IllegalArgumentException("Percentage attribute must be positive and lower than 101%");
		}
		if (duration < 30) {
			throw new IllegalArgumentException("Duration must be higher than 29ms");
		}
		executeCommand(method, percentage, duration);
		if (Method.ADJUST_COLOR.equals(method)) {
			return mLight.getRGB();
		} else {
			return mLight.getBgRGB();
		}
	}

	/**
	 * Adjust color.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	@Override
	public int bgAdjustColor(int percentage, int duration) throws CommandException {
		return adjustColor(Method.BG_ADJUST_COLOR, percentage, duration);
	}

	/**
	 * Adjust color temperature.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	private int adjustColorTemperature(Method method, int percentage, int duration) throws CommandException {
		if (percentage < -100 || percentage > 100) {
			throw new IllegalArgumentException("Percentage attribute must be positive and lower than 101%");
		}
		if (duration < 30) {
			throw new IllegalArgumentException("Duration must be higher than 29ms");
		}
		executeCommand(method, percentage, duration);
		if (Method.ADJUST_CT.equals(method)) {
			return mLight.getCt();
		} else {
			return mLight.getBgCt();
		}

	}

	/**
	 * Adjust color temperature.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	@Override
	public int bgAdjustColorTemperature(int percentage, int duration) throws CommandException {
		return adjustColorTemperature(Method.BG_ADJUST_CT, percentage, duration);
	}

	/**
	 * Define color temperature.
	 * 
	 * @param temperature color temperature Should be:
	 *                    <ul>
	 *                    <li>Higher or equal to 1700K
	 *                    <li>
	 *                    <li>Lower or equal to 6500K</li>
	 *                    </ul>
	 * @param effect      effect
	 * @param duration    Should be higher or equals than 30 ms
	 * @throws CommandException unavailable method
	 */
	@Override
	public int setCtAbx(int temperature) throws CommandException {
		return setCtAbx(Method.SET_CT_ABX, temperature);
	}

	/**
	 * Define color temperature.
	 * 
	 * @param temperature color temperature Should be:
	 *                    <ul>
	 *                    <li>Higher or equal to 1700K
	 *                    <li>
	 *                    <li>Lower or equal to 6500K</li>
	 *                    </ul>
	 * @param effect      effect
	 * @param duration    Should be higher or equals than 30 ms
	 * @throws CommandException unavailable method
	 */
	private int setCtAbx(Method method, int temperature) throws CommandException {
		String attributeName;
		if (Method.BG_SET_CT_ABX.equals(method)) {
			attributeName = "bg_ct";
		} else {
			attributeName = "ct";
		}
		if (isAttributeInvalid(attributeName, temperature)) {
			throw new IllegalArgumentException("Color temperature should be between 1700K and 6500K");
		}

		executeCommand(method, temperature, mEffect.getValue(), mDuration);
		if (Method.SET_CT_ABX.equals(method)) {
			return mLight.getCt();
		} else {
			return mLight.getBgCt();
		}
	}

	/**
	 * Define background color temperature.
	 * 
	 * @param temperature color temperature Should be:
	 *                    <ul>
	 *                    <li>Higher or equal to 1700K
	 *                    <li>
	 *                    <li>Lower or equal to 6500K</li>
	 *                    </ul>
	 * @param effect      effect
	 * @param duration    Should be higher or equals than 30 ms
	 * @throws CommandException unavailable method
	 */
	@Override
	public int setBgCtAbx(int temperature) throws CommandException {
		return setCtAbx(Method.BG_SET_CT_ABX, temperature);
	}

	/**
	 * Set rgb color.
	 * 
	 * @param red   red value
	 * @param green green value
	 * @param blue  blue value
	 * @return new color as following [red, green, blue]
	 * @throws CommandException something goes wrong
	 */
	@Override
	public int[] setRgb(int red, int green, int blue) throws CommandException {
		return setRgb(Method.SET_RGB, red, green, blue);
	}

	/**
	 * Set rgb color.
	 * 
	 * @param red   red value
	 * @param green green value
	 * @param blue  blue value
	 * @return new color as following [red, green, blue]
	 * @throws CommandException something goes wrong
	 */
	private int[] setRgb(Method method, int red, int green, int blue) throws CommandException {
		int newColor = setRgb(method, red * 65536 + green * 256 + blue);
		return new int[] { newColor >> 4, newColor >> 2 & 0xFF, newColor & 0xFF };
	}

	/**
	 * set rgb value.
	 * 
	 * @param mergedValue rgb value as 0x(R)(G)(B)
	 * @return new color
	 * @throws CommandException unavailable method
	 */
	private int setRgb(Method method, int mergedValue) throws CommandException {
		String attributeName;
		if(Method.SET_RGB.equals(method)) {
			attributeName = "rgb";
		} else {
			attributeName = "bg_rgb";
		}
		if(isAttributeInvalid(attributeName, mergedValue)) {
			throw new IllegalArgumentException("Color temperature should be between 1 and 16777215");
		}
		executeCommand(method, mergedValue, mEffect.getValue(), mDuration);
		if (Method.SET_RGB.equals(method)) {
			return mLight.getRGB();
		} else {
			return mLight.getBgRGB();
		}
	}

	/**
	 * set rgb value.
	 * 
	 * @param mergedValue rgb value as 0x(R)(G)(B)
	 * @return new color
	 * @throws CommandException unavailable method
	 */
	@Override
	public int setRgb(int mergedValue) throws CommandException {
		return setRgb(Method.SET_RGB, mergedValue);
	}

	/**
	 * Set hue and saturation.
	 * 
	 * @param hue hue value
	 * @param sat saturation value
	 * @throws CommandException something goes wrong
	 */
	@Override
	public void setHsv(int hue, int sat) throws CommandException {
		setHsv(Method.SET_HSV, hue, sat);
	}

	/**
	 * Set hue and saturation.
	 * 
	 * @param hue hue value
	 * @param sat saturation value
	 * @throws CommandException something goes wrong
	 */
	@Override
	public void setHsv(Method method, int hue, int sat) throws CommandException {
		if (isAttributeInvalid("hue", hue)) {
			throw new IllegalArgumentException("Hue must be positive and lower than 360");
		}
		if (isAttributeInvalid("sat", sat)) {
			throw new IllegalArgumentException("Sat must be positive and lower than 100");
		}
		executeCommand(method, hue, sat, mEffect.getValue(), mDuration);
	}

	/**
	 * Set brightness.
	 * 
	 * @param bright brighness value
	 * @return new brightness
	 * @throws CommandException something goes wrong
	 */
	@Override
	public int setBright(int bright) throws CommandException {
		return setBright(Method.SET_BRIGHT, bright);
	}

	/**
	 * Set brightness.
	 * 
	 * @param bright brighness value
	 * @return new brightness
	 * @throws CommandException something goes wrong
	 */
	private int setBright(Method method, int bright) throws CommandException {
		if (isAttributeInvalid("bright", bright)) {
			throw new IllegalArgumentException("Bright must be positive and lower than 100");
		}
		executeCommand(method, bright, mEffect.getValue(), mDuration);
		if (Method.SET_BRIGHT.equals(method)) {
			return mLight.getBrightness();
		} else {
			return mLight.getBgBrightness();
		}
	}

	/**
	 * Set light power.
	 * 
	 * @param power true to switch on, false to switch off
	 * @return new power value
	 * @throws CommandException command could not be executed
	 */
	@Override
	public boolean setPower(boolean power) throws CommandException {
		executeCommand(Method.SET_POWER, getPowerStr(power), mEffect.getValue(), mDuration);
		return mLight.isPower();
	}

	/**
	 * Set scene with 2 arguments.
	 * 
	 * @param scClass    scene class
	 * @param val1
	 *                   <ul>
	 *                   <li>Color for COLOR class</li>
	 *                   <li>Color temperature for CT class</li>
	 *                   </ul>
	 * @param brightness brightness
	 * @throws CommandException command could not be executed
	 */
	@Override
	public void setScene(SceneClass scClass, int val1, int brightness) throws CommandException {
		setScene(Method.SET_SCENE, scClass, val1, brightness);
	}

	/**
	 * Set scene with 2 arguments.
	 * 
	 * @param scClass    scene class
	 * @param val1
	 *                   <ul>
	 *                   <li>Color for COLOR class</li>
	 *                   <li>Color temperature for CT class</li>
	 *                   </ul>
	 * @param brightness brightness
	 * @throws CommandException command could not be executed
	 */
	@Override
	public void setBgScene(SceneClass scClass, int val1, int brightness) throws CommandException {
		setScene(Method.BG_SET_SCENE, scClass, val1, brightness);
	}

	/**
	 * Set scene with 2 arguments.
	 * 
	 * @param method     Method.SET_SCENE or Method.BG_SET_SCENE
	 * @param scClass    scene class
	 * @param val1
	 *                   <ul>
	 *                   <li>Color for COLOR class</li>
	 *                   <li>Color temperature for CT class</li>
	 *                   </ul>
	 * @param brightness brightness
	 * @throws CommandException command could not be executed
	 */
	private void setScene(Method method, SceneClass scClass, int val1, int brightness) throws CommandException {
		if (scClass.equals(SceneClass.COLOR)) {
			if (isAttributeInvalid("rgb", val1)) {
				throw new CommandException("Color must be between 0 and 0xffffff");
			}
		} else if (scClass.equals(SceneClass.CT)) {
			if (isAttributeInvalid("ct", val1)) {
				throw new CommandException("Color temperature must be between 1500 and 5900");
			}
		} else if (scClass.equals(SceneClass.AUTO_DELAY_OFF)) {
			if (isAttributeInvalid("delayoff", val1)) {
				throw new CommandException("Timer must be at least 1");
			}
		} else {
			throw new CommandException(scClass.toString() + " could not be executed with this constructor");
		}
		if (isAttributeInvalid("bright", brightness)) {
			throw new CommandException("Brightness must be between 0 and 100");
		}
		if (scClass.equals(SceneClass.AUTO_DELAY_OFF)) {
			executeCommand(Method.SET_SCENE, scClass.getValue(), brightness, val1);

		} else {
			executeCommand(method, scClass.getValue(), val1, brightness);
		}
	}

	@Override
	public void setScene(SceneClass scClass, int val1, int val2, Object val3) throws CommandException {
		setScene(Method.SET_SCENE, scClass, val1, val2, val3);
	}

	@Override
	public void setBgScene(SceneClass scClass, int val1, int val2, Object val3) throws CommandException {
		setScene(Method.BG_SET_SCENE, scClass, val1, val2, val3);
	}

	/**
	 * Set scene with 3 arguments.
	 * 
	 * @param scClass class, might be color flow
	 * @param val1
	 *                <ul>
	 *                <li>Hue for HSV class</li>
	 *                <li>for CF class</li>
	 *                </ul>
	 * @param val2
	 *                <ul>
	 *                <li>Saturation for HSV class</li>
	 *                <li>Action for CF class</li>
	 *                </ul>
	 * @param val3
	 *                <ul>
	 *                <li>brightness for HSV class</li>
	 *                <li>Flow for CF class</li>
	 *                </ul>
	 * @throws CommandException
	 */
	@Override
	public void setScene(Method method, SceneClass scClass, int val1, int val2, Object val3) throws CommandException {
		if (scClass.equals(SceneClass.HSV)) {
			if (isAttributeInvalid("hue", val1)) {
				throw new CommandException("Hue must be between 0 and 359");
			}
			if (isAttributeInvalid("sat", val2)) {
				throw new CommandException("Saturation must be between 0 and 100");
			}
			if (!(val3 instanceof Integer) || isAttributeInvalid("bright", (int)val3)) {
				throw new CommandException("Brightness must be between 0 and 100");
			}
		} else if (scClass.equals(SceneClass.CF)) {
			if (val1 < 0) {
				throw new CommandException("Count must be 0 or more");
			}
			if (val2 < 0 || val2 > 2) {
				throw new CommandException("Action must be 0, 1 or 2");
			}
		} else {
			throw new CommandException(scClass.toString() + " could not be executed with this constructor");
		}
		executeCommand(method, val1, val2, val3);
	}

	/**
	 * Set time before switch off.
	 * 
	 * @param time time before switch off
	 * @return new time
	 * @throws CommandException unavailable method
	 */
	@Override
	public int setCron(int time) throws CommandException {
		executeCommand(Method.CRON_ADD, 0, time);
		return mLight.getCron();
	}

	/**
	 * Set time before switch off.
	 * 
	 * @return time before switch off
	 * @throws CommandException unavailable method
	 */
	@Override
	public int getCron() throws CommandException {
		executeCommand(Method.CRON_GET, 0);
		return mLight.getCron();
	}

	/**
	 * Delete cron task.
	 * 
	 * @throws CommandException unavailable method
	 */
	@Override
	public void delCron() throws CommandException {
		executeCommand(Method.CRON_DEL, 0);
	}

	/**
	 * Adjust property.
	 * 
	 * @param method   method Method.SET_ADJUST or Method.BG_SET_ADJUST
	 * @param adjust   adjust type
	 * @param property property to adjust. One of
	 *                 <ul>
	 *                 <li>ct</li>
	 *                 <li>bright</li>
	 *                 <li>color</li>
	 *                 </ul>
	 * @throws CommandException unavailable method
	 */
	private void setAdjust(Method method, AdjustType adjust, String property) throws CommandException {
		if ("ct".equals(property) || "bright".equals(property) || "color".equals(property)) {
			if ("color".equals(property) && adjust != AdjustType.CIRCLE) {
				throw new IllegalArgumentException("When color property is set, adjust type must be AdjustType.CIRCLE");
			}
			executeCommand(method, adjust.getValue(), property);
		} else {
			throw new IllegalArgumentException("Bad property. One of \"ct\", \"bright\" or \"color\" expected");
		}
	}

	/**
	 * Set music mode.
	 * <p>
	 * Do not forget to start a server.
	 * </p>
	 * 
	 * @param turnOn turn on or turn off
	 * @param host   host the IP address of the music server
	 * @param port   the TCP port music application is listening on
	 * @throws CommandException unavailable method
	 */
	@Override
	public void setMusic(boolean turnOn, String host, int port) throws CommandException {
		executeCommand(Method.SET_MUSIC, turnOn, host, port);
	}

	/**
	 * Adjust property.
	 * 
	 * @param adjust   adjust type
	 * @param property property to adjust. On eof
	 *                 <ul>
	 *                 <li>ct</li>
	 *                 <li>bright</li>
	 *                 <li>color</li>
	 *                 </ul>
	 * @throws CommandException unavailable method
	 */
	@Override
	public void setAdjust(AdjustType adjust, String property) throws CommandException {
		setAdjust(Method.SET_ADJUST, adjust, property);
	}

	/**
	 * Toggle Light status.
	 * 
	 * @return new status of light
	 * @throws CommandException unavailable method
	 */
	@Override
	public boolean bgToggle() throws CommandException {
		executeCommand(Method.BG_TOGGLE);
		return mLight.isBgPower();
	}

	/**
	 * Set background rgb value.
	 * 
	 * @param red   red value
	 * @param green green value
	 * @param blue  blue value
	 * @return new value as following [red, green, blue]
	 * @throws CommandException something goes wrong
	 */
	@Override
	public int[] setBgRgb(int red, int green, int blue) throws CommandException {
		return setRgb(Method.BG_SET_RGB, red, green, blue);
	}

	/**
	 * Set background rgb value.
	 * 
	 * @param mergedValue merged background rgb value
	 * @return new background rgb value
	 * @throws CommandException something goes wrong
	 */
	@Override
	public int setBgRgb(int mergedValue) throws CommandException {
		return setRgb(Method.BG_SET_RGB, mergedValue);
	}

	/**
	 * Set hue and saturation for background.
	 * 
	 * @param hue background hue
	 * @param sat background saturation
	 * @throws CommandException something goes wrong
	 */
	@Override
	public void setBgHsv(int hue, int sat) throws CommandException {
		setHsv(Method.BG_SET_HSV, hue, sat);
	}

	/**
	 * Set background brigness.
	 * 
	 * @param bright brightness
	 * @return background brightness
	 * @throws CommandException something goes wrong
	 */
	@Override
	public int setBgBright(int bright) throws CommandException {
		return setBright(Method.BG_SET_BRIGHT, bright);
	}

	/**
	 * Convert power boolean to power string.
	 * 
	 * @param power power status
	 * @return power status as string
	 */
	private String getPowerStr(boolean power) {
		return (power ? "on" : "off");
	}

	/**
	 * Set the background power status.
	 * 
	 * @param power power status
	 * @return background power status
	 * @throws CommandException something goes wrong
	 */
	@Override
	public boolean setBgPower(boolean power) throws CommandException {
		executeCommand(Method.BG_SET_POWER, getPowerStr(power), mEffect.getValue(), mDuration);
		return mLight.isBgPower();
	}

	/**
	 * Adjust bg property.
	 * 
	 * @param adjust   adjust type
	 * @param property property to adjust. On of
	 *                 <ul>
	 *                 <li>ct</li>
	 *                 <li>bright</li>
	 *                 <li>color</li>
	 *                 </ul>
	 * @throws CommandException unavailable method
	 */
	@Override
	public void setBgAdjust(AdjustType adjust, String property) throws CommandException {
		setAdjust(Method.BG_SET_ADJUST, adjust, property);
	}

	/**
	 * Set device name.
	 * 
	 * @param name device name
	 * @return new Device name
	 * @throws CommandException unavailable method
	 */
	@Override
	public String setName(String name) throws CommandException {
		executeCommand(Method.SET_NAME, name);
		return mLight.getName();
	}

	/**
	 * Execute a command.
	 * 
	 * @param method method to execute
	 * @param params parameters
	 * @throws CommandException something goes wrong in the command
	 */
	private void executeCommand(Method method, Object... params) throws CommandException {
		if (mLight.isMethodAvailable(method)) {
			Future<Boolean> result = mMessaging.send(method, params);
			if (null != result) {
				while (!result.isDone()) {
					// Wait response
				}
			}
		} else {
			throw new CommandException(String.format("%s is not an available method for this light", method.getName()));
		}
	}

	/**
	 * Return the associated light.
	 * 
	 * @return associated light
	 */
	@Override
	public Light getLight() {
		return mLight;
	}

	/**
	 * Check if an integer attribute is in the defined interval.
	 * 
	 * @param attributeName attribute to check
	 * @param value         value to check
	 * @return true if in interval, false otherwise
	 */
	private boolean isAttributeInvalid(final String attributeName, int value) {
		boolean result = false;
		for (Field field : mLight.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Property.class)
					&& field.getAnnotation(Property.class).field().contentEquals(attributeName)) {
				if (field.isAnnotationPresent(CheckIntegerInterval.class)
						&& (value < field.getAnnotation(CheckIntegerInterval.class).min()
								|| value > field.getAnnotation(CheckIntegerInterval.class).max())) {
					result = true;
				}
			}
		}
		return result;
	}
}
