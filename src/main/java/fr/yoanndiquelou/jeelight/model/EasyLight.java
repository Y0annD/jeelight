package fr.yoanndiquelou.jeelight.model;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import fr.yoanndiquelou.jeelight.communication.MessageManager;
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
	 */
	public boolean toggle() {
		executeCommand(Method.TOGGLE, new Object[0]);
		return mLight.isPower();
	}

	/**
	 * Start color flow.
	 * 
	 * @param end  end mode
	 * @param flow list of changes in flow
	 */
	public void startCf(ColorFlowEnd end, List<FlowColor> flow) {
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
	 */
	public void stopCf() {
		executeCommand(Method.STOP_CF);
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
	 */
	public int setCtAbx(int temperature) {
		if (temperature > 6500 || temperature < 1700) {
			throw new IllegalArgumentException("Color temperature should be between 1700K and 6500K");
		}

		executeCommand(Method.SET_CT_ABX, temperature, mEffect.getValue(), mDuration);
		return mLight.getCt();
	}

	public int[] setRgb(int red, int green, int blue) {
		int newColor = setRgb(red * 65536 + green * 256 + blue);
		return new int[] { newColor >> 4, newColor >> 2 & 0xFF, newColor & 0xFF };
	}

	public int setRgb(int mergedValue) {
		executeCommand(Method.SET_RGB, mergedValue, mEffect.getValue(), mDuration);
		return mLight.getRGB();
	}

	public void setHsv(int hue, int sat) {
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
	 */
	public int setBright(int bright) {
		if (bright < 0 || bright > 100) {
			throw new IllegalArgumentException("Bright must be positive and lower than 100");
		}
		executeCommand(Method.SET_BRIGHT, bright, mEffect.getValue(), mDuration);
		return mLight.getBrightness();
	}

	public boolean setPower(boolean power) {
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
	 */
	public int setCron(int time) {
		executeCommand(Method.CRON_ADD, 0, time);
		return mLight.getCron();
	}

	/**
	 * Set time before switch off.
	 * 
	 * @return time before switch off
	 */
	public int getCron() {
		executeCommand(Method.CRON_GET, 0);
		return mLight.getCron();
	}
	
	/**
	 * Delete cron task.
	 */
	public void delCron() {
		executeCommand(Method.CRON_DEL, 0);
	}

	// TODO: Adjust

	// TODO: Music

	/**
	 * Toggle Light status.
	 * 
	 * @return new status of light
	 */
	public boolean bgToggle() {
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
	 */
	public int setBgCtAbx(int temperature) {
		if (temperature > 6500 || temperature < 1700) {
			throw new IllegalArgumentException("Color temperature should be between 1700K and 6500K");
		}

		executeCommand(Method.BG_SET_CT_ABX, temperature, mEffect.getValue(), mDuration);
		return mLight.getCt();
	}

	public int[] setBgRgb(int red, int green, int blue) {
		int newColor = setBgRgb(red * 65536 + green * 256 + blue);
		return new int[] { newColor >> 4, newColor >> 2 & 0xFF, newColor & 0xFF };
	}

	public int setBgRgb(int mergedValue) {
		executeCommand(Method.BG_SET_RGB, mergedValue, mEffect.getValue(), mDuration);
		return mLight.getRGB();
	}

	public void setBgHsv(int hue, int sat) {
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
	 */
	public int setBgBright(int bright) {
		if (bright < 0 || bright > 100) {
			throw new IllegalArgumentException("Bright must be positive and lower than 100");
		}
		executeCommand(Method.BG_SET_BRIGHT, bright, mEffect.getValue(), mDuration);
		return mLight.getBrightness();
	}

	private String getPowerStr(boolean power) {
		return (power ? "On" : "Off");
	}

	public boolean setBgPower(boolean power) {
		executeCommand(Method.BG_SET_POWER, getPowerStr(power), mEffect.getValue(), mDuration);
		return mLight.isPower();
	}

	/**
	 * Set device name.
	 * 
	 * @param name device name
	 * @return new Device name
	 */
	public String setName(String name) {
		executeCommand(Method.SET_NAME, name);
		return mLight.getName();
	}

	private void executeCommand(Method method, Object... params) {
		Future<Boolean> result = mMessaging.send(method, params);
		if (null != result) {
			while (!result.isDone()) {
				// Wait response
			}
		}
	}
}
