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

	/**
	 * Exception with message.
	 * 
	 * @param message message
	 */
	public CommandException(String message) {
		super(message);
	}

	/**
	 * Exception with message and original exception.
	 * 
	 * @param message message
	 * @param e original exception
	 */
	public CommandException(String message, Exception e) {
		super(message, e);
	}

}
