package fr.yoanndiquelou.jeelight.exception;

public class CommandException extends Exception {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -2840904224517527661L;

	/**
	 * Command exception.
	 */
	public CommandException() {
		super();
	}

	public CommandException(String message) {
		super(message);
	}

	public CommandException(String message, Exception e) {
		super(message, e);
	}

}
