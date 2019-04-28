package fr.yoanndiquelou.jeelight.exception;

public class ParameterException extends Exception {

	/**
	 * Serial id.
	 */
	private static final long serialVersionUID = -339452637816309965L;

	/**
	 * Parameter exception.
	 */
	public ParameterException() {
		super();
	}

	/**
	 * Parameter exception with message.
	 * @param message message
	 */
	public ParameterException(String message) {
		super(message);
	}

	/**
	 * Command exception with message and original exeption
	 * @param message message
	 * @param e original exception
	 */
	public ParameterException(String message, Exception e) {
		super(message, e);
	}
}
