package fr.yoanndiquelou.jeelight.model;

import java.util.ArrayList;
import java.util.List;

import fr.yoanndiquelou.jeelight.exception.ParameterException;

public enum Method {
	SET_CT_ABX("set_ct_abx", Integer.class, String.class, Integer.class),
	SET_RGB("set_rgb", Integer.class, String.class, Integer.class),
	SET_HSV("set_hsv", Integer.class, Integer.class, String.class, Integer.class),
	SET_BRIGHT("set_bright", Integer.class, String.class, Integer.class),
	SET_POWER("set_power", String.class, String.class, Integer.class),
	TOGGLE("toggle"),
	SET_DEFAULT("set_default"),
	START_CF("start_cf", Integer.class, Integer.class, String.class),
	STOP_CF("stop_cf"),
	SET_SCENE("set_scene", String.class, Integer.class, Integer.class),
	CRON_ADD("cron_add", Integer.class, Integer.class),
	CRON_GET("cron_get", Integer.class),
	CRON_DEL("cron_del", Integer.class),
	SET_ADJUST("set_adjust", String.class, String.class),
	SET_MUSIC("set_music", Integer.class, String.class, Integer.class),
	SET_NAME("set_name", String.class),
	BG_SET_RGB("bg_set_rgb", Integer.class, String.class, Integer.class),
	BG_SET_HSV("bg_set_hsv", Integer.class, Integer.class, String.class, Integer.class),
	BG_SET_CT_ABX("bg_set_ct_abx", Integer.class, String.class, Integer.class),
	BG_STOP_CF("bg_stop_cf"),
	BG_SET_SCENE("bg_set_scene", String.class, Integer.class, Integer.class),
	BG_SET_DEFAULT("bg_set_default"),
	BG_SET_POWER("bg_set_power", String.class, String.class, Integer.class, Integer.class),
	BG_SET_BRIGHT("bg_set_bright", Integer.class, String.class, Integer.class),
	BG_SET_ADJUST("bg_set_adjust", String.class, String.class),
	BG_TOGGLE("bg_toggle"),
	DEV_TOGGLE("dev_toggle"),
	ADJUST_BRIGHT("adjust_bright", Integer.class, Integer.class),
	ADJUST_CT("adjust_ct", Integer.class, Integer.class),
	ADJUST_COLOR("adjust_color", Integer.class, Integer.class),
	BG_ADJUST_BRIGHT("bg_adjust_bright", Integer.class, Integer.class),
	BG_ADJUST_CT("bg_adjust_ct", Integer.class, Integer.class),
	BG_ADJUST_COLOR("bg_adjust_color", Integer.class, Integer.class);

	private String mName;
	private List<Class<?>> mParamTypes;

	private Method(String name, Class<?>... paramTypes) {
		mName = name;
		mParamTypes = new ArrayList<Class<?>>();
		for (Class<?> paramType : paramTypes) {
			mParamTypes.add(paramType);
		}
	}

	public void check(Object... parameters) throws ParameterException {
		if(mParamTypes.size() != parameters.length) {
			throw new ParameterException("Found "+parameters.length+" parameters but expect " + mParamTypes.size());
		}
		for(int i = 0; i< parameters.length; i++) {
			if(!parameters[i].getClass().equals(mParamTypes.get(i))) {
				throw new ParameterException("Parameter at index "+i+" is incorrect. " +mParamTypes.get(i).getSimpleName()+" waited, but found "+parameters[i].getClass().getSimpleName());
			}
		}
		
	}

	/**
	 * Name of the method.
	 * 
	 * @return name of the method
	 */
	public String getName() {
		return mName;
	}
	
	/**
	 * Get the parameter at specified index.
	 * 
	 * @param index index
	 * @return parameter class
	 */
	public Class<?> getParameter(final int index) {
		return mParamTypes.get(index);
	}
}
