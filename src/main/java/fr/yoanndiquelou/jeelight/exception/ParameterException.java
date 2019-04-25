package fr.yoanndiquelou.jeelight.exception;

public class ParameterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -339452637816309965L;

	/**
	 * Command exception.
	 */
	public ParameterException() {
		super();
	}

	public ParameterException(String message) {
		super(message);
	}

	public ParameterException(String message, Exception e) {
		super(message, e);
	}
}
