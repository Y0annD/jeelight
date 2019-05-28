package fr.yoanndiquelou.jeelight.utils;

/**
 * Convert a string attribute to the appropriate object.
 * 
 * @author Y0annD
 *
 */
public class AttributeParser {

	/**
	 * Private constructor.
	 */
	private AttributeParser() {
		super();
	}

	/**
	 * Parse object in convenient type.
	 * 
	 * @param param parameter
	 * @return parameter parse in the correct type
	 */
	public static Object parse(final String param) {
		Object result;
		if ("on".equals(param)) {
			result = true;
		} else if ("off".equals(param)) {
			result = false;
		} else {
			try {
				result = Integer.valueOf(param);
			} catch (NumberFormatException e) {
				try {
					result = Long.valueOf(param);
				} catch (NumberFormatException e1) {
					result = param;
				}
			}
		}
		return result;
	}

}
