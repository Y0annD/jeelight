package fr.yoanndiquelou.yeelight.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.exception.CommandException;

@DisplayName("CommandExceptionTest")
public class CommandExceptionTest {

	private static final String MESSAGE = "dummy exception";

	/**
	 * Constructor test without parameters.
	 */
	@Test
	@DisplayName("Constructor without parameters")
	public void testConstructorWithoutParameters() {
		Exception exception = assertThrows(CommandException.class, () -> {
			throw new CommandException();
		});
		assertEquals(null, exception.getMessage());
	}

	
	/**
	 * Constructor test with message.
	 */
	@Test
	@DisplayName("Constructor with message")
	public void testConstructorWithMessage() {
		Exception exception = assertThrows(CommandException.class, () -> {
			throw new CommandException(MESSAGE);
		});
		assertEquals(MESSAGE, exception.getMessage());
	}
	
	/**
	 * Constructor test with exception.
	 */
	@Test
	@DisplayName("Constructor with exception")
	public void testConstructorWithException() {
		Exception e = new Exception();
		Exception exception = assertThrows(CommandException.class, () -> {
			throw new CommandException(MESSAGE, e);
		});
		assertEquals(MESSAGE, exception.getMessage());
		assertEquals(e, exception.getCause());
	}

}
