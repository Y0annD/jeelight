package fr.yoanndiquelou.jeelight.model;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import fr.yoanndiquelou.jeelight.communication.MessageManager;
import fr.yoanndiquelou.jeelight.exception.CommandException;
import fr.yoanndiquelou.jeelight.exception.ParameterException;

public class EasyLight {
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
		mMessaging = new MessageManager(light);
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
	 * Toggle Light status.
	 * 
	 * @return new status of light
	 * @throws CommandException unavailable method
	 */
	public boolean toggle() throws CommandException {
		executeCommand(Method.TOGGLE, new Object[0]);
		return mLight.isPower();
	}

	/**
	 * Start color flow.
	 * 
	 * @param end  end mode
	 * @param flow list of changes in flow
	 * @throws CommandException unavailable method
	 */
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
	public int adjustBright(int percentage, int duration) throws CommandException {
		if (percentage < -100 || percentage > 100) {
			throw new IllegalArgumentException("Percentage attribute must be positive and lower than 101%");
		}
		if (duration < 30) {
			throw new IllegalArgumentException("Duration must be higher than 29ms");
		}
		executeCommand(Method.ADJUST_BRIGHT, percentage, duration);
		return mLight.getBrightness();
	}

	/**
	 * Adjust color temperature.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	public int adjustColorTemperature(int percentage, int duration) throws CommandException {
		if (percentage < -100 || percentage > 100) {
			throw new IllegalArgumentException("Percentage attribute must be positive and lower than 101%");
		}
		if (duration < 30) {
			throw new IllegalArgumentException("Duration must be higher than 29ms");
		}
		executeCommand(Method.ADJUST_CT, percentage, duration);
		return mLight.getBrightness();
	}

	/**
	 * Adjust color.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	public int adjustColor(int percentage, int duration) throws CommandException {
		if (percentage < -100 || percentage > 100) {
			throw new IllegalArgumentException("Percentage attribute must be positive and lower than 101%");
		}
		if (duration < 30) {
			throw new IllegalArgumentException("Duration must be higher than 29ms");
		}
		executeCommand(Method.ADJUST_COLOR, percentage, duration);
		return mLight.getBrightness();
	}

	/**
	 * Adjust brightness.
	 * 
	 * @param percentage percentage of brightness
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	public int bgAdjustBright(int percentage, int duration) throws CommandException {
		if (percentage < -100 || percentage > 100) {
			throw new IllegalArgumentException("Percentage attribute must be positive and lower than 101%");
		}
		if (duration < 30) {
			throw new IllegalArgumentException("Duration must be higher than 29ms");
		}
		executeCommand(Method.BG_ADJUST_BRIGHT, percentage, duration);
		return mLight.getBrightness();
	}

	/**
	 * Adjust color temperature.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	public int bgAdjustColorTemperature(int percentage, int duration) throws CommandException {
		if (percentage < -100 || percentage > 100) {
			throw new IllegalArgumentException("Percentage attribute must be positive and lower than 101%");
		}
		if (duration < 30) {
			throw new IllegalArgumentException("Duration must be higher than 29ms");
		}
		executeCommand(Method.BG_ADJUST_CT, percentage, duration);
		return mLight.getBrightness();
	}

	/**
	 * Adjust color.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	public int bgAdjustColor(int percentage, int duration) throws CommandException {
		if (percentage < -100 || percentage > 100) {
			throw new IllegalArgumentException("Percentage attribute must be positive and lower than 101%");
		}
		if (duration < 30) {
			throw new IllegalArgumentException("Duration must be higher than 29ms");
		}
		executeCommand(Method.BG_ADJUST_COLOR, percentage, duration);
		return mLight.getBrightness();
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
	public int setCtAbx(int temperature) throws CommandException {
		if (temperature > 6500 || temperature < 1700) {
			throw new IllegalArgumentException("Color temperature should be between 1700K and 6500K");
		}

		executeCommand(Method.SET_CT_ABX, temperature, mEffect.getValue(), mDuration);
		return mLight.getCt();
	}

	public int[] setRgb(int red, int green, int blue) throws CommandException {
		int newColor = setRgb(red * 65536 + green * 256 + blue);
		return new int[] { newColor >> 4, newColor >> 2 & 0xFF, newColor & 0xFF };
	}

	/**
	 * set rgb value.
	 * 
	 * @param mergedValue rgb value as 0x(R)(G)(B)
	 * @return new color
	 * @throws CommandException unavailable method
	 */
	public int setRgb(int mergedValue) throws CommandException {
		executeCommand(Method.SET_RGB, mergedValue, mEffect.getValue(), mDuration);
		return mLight.getRGB();
	}

	public void setHsv(int hue, int sat) throws CommandException {
		if (hue < 0 || hue > 359) {
			throw new IllegalArgumentException("Hue must be positive and lower than 360");
		}
		if (sat < 0 || sat > 100) {
			throw new IllegalArgumentException("Sat must be positive and lower than 100");
		}
		executeCommand(Method.SET_HSV, hue, sat, mEffect.getValue(), mDuration);
	}

	/**
	 * 
	 * @param bright
	 * @return
	 * @throws CommandException 
	 */
	public int setBright(int bright) throws CommandException {
		if (bright < 0 || bright > 100) {
			throw new IllegalArgumentException("Bright must be positive and lower than 100");
		}
		executeCommand(Method.SET_BRIGHT, bright, mEffect.getValue(), mDuration);
		return mLight.getBrightness();
	}

	public boolean setPower(boolean power) throws CommandException {
		executeCommand(Method.SET_POWER, getPowerStr(power), mEffect.getValue(), mDuration);
		return mLight.isPower();
	}

	// TODO: Scene management
//	public void setScene() {
//		
//	}

	/**
	 * Set time before switch off.
	 * 
	 * @param time time before switch off
	 * @return new time
	 * @throws CommandException unavailable method
	 */
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
	public int getCron() throws CommandException {
		executeCommand(Method.CRON_GET, 0);
		return mLight.getCron();
	}

	/**
	 * Delete cron task.
	 * @throws CommandException unavailable method
	 */
	public void delCron() throws CommandException {
		executeCommand(Method.CRON_DEL, 0);
	}

	/**
	 * Adjust property.
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
	public void setAdjust(AdjustType adjust, String property) throws CommandException {
		if ("ct".equals(property) || "bright".equals(property) || "color".equals(property)) {
			if ("color".equals(property) && adjust != AdjustType.CIRCLE) {
				throw new IllegalArgumentException("When color property is set, adjust type must be AdjustType.CIRCLE");
			}
			executeCommand(Method.SET_ADJUST, adjust.getValue(), property);
		} else {
			throw new IllegalArgumentException("Bad property. One of \"ct\", \"bright\" or \"color\" expected");
		}
	}

	// TODO: Music

	/**
	 * Toggle Light status.
	 * 
	 * @return new status of light
	 * @throws CommandException unavailable method
	 */
	public boolean bgToggle() throws CommandException {
		executeCommand(Method.BG_TOGGLE);
		return mLight.isPower();
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
	public int setBgCtAbx(int temperature) throws CommandException {
		if (temperature > 6500 || temperature < 1700) {
			throw new IllegalArgumentException("Color temperature should be between 1700K and 6500K");
		}

		executeCommand(Method.BG_SET_CT_ABX, temperature, mEffect.getValue(), mDuration);
		return mLight.getCt();
	}

	public int[] setBgRgb(int red, int green, int blue) throws CommandException {
		int newColor = setBgRgb(red * 65536 + green * 256 + blue);
		return new int[] { newColor >> 4, newColor >> 2 & 0xFF, newColor & 0xFF };
	}

	public int setBgRgb(int mergedValue) throws CommandException {
		executeCommand(Method.BG_SET_RGB, mergedValue, mEffect.getValue(), mDuration);
		return mLight.getRGB();
	}

	public void setBgHsv(int hue, int sat) throws CommandException {
		if (hue < 0 || hue > 359) {
			throw new IllegalArgumentException("Hue must be positive and lower than 360");
		}
		if (sat < 0 || sat > 100) {
			throw new IllegalArgumentException("Sat must be positive and lower than 100");
		}
		executeCommand(Method.BG_SET_HSV, hue, sat, mEffect.getValue(), mDuration);
	}

	/**
	 * 
	 * @param bright
	 * @return
	 * @throws CommandException unavailable method
	 */
	public int setBgBright(int bright) throws CommandException{
		if (bright < 0 || bright > 100) {
			throw new IllegalArgumentException("Bright must be positive and lower than 100");
		}
		executeCommand(Method.BG_SET_BRIGHT, bright, mEffect.getValue(), mDuration);
		return mLight.getBrightness();
	}

	private String getPowerStr(boolean power) {
		return (power ? "On" : "Off");
	}

	public boolean setBgPower(boolean power) throws CommandException {
		executeCommand(Method.BG_SET_POWER, getPowerStr(power), mEffect.getValue(), mDuration);
		return mLight.isPower();
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
	public void setBgAdjust(AdjustType adjust, String property) throws CommandException {
		if ("ct".equals(property) || "bright".equals(property) || "color".equals(property)) {
			if ("color".equals(property) && adjust != AdjustType.CIRCLE) {
				throw new IllegalArgumentException("When color property is set, adjust type must be AdjustType.CIRCLE");
			}
			executeCommand(Method.BG_SET_ADJUST, adjust.getValue(), property);
		} else {
			throw new IllegalArgumentException("Bad property. One of \"ct\", \"bright\" or \"color\" expected");
		}
	}

	/**
	 * Set device name.
	 * 
	 * @param name device name
	 * @return new Device name
	 * @throws CommandException unavailable method
	 */
	public String setName(String name) throws CommandException {
		executeCommand(Method.SET_NAME, name);
		return mLight.getName();
	}

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
}
