package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.exception.ParameterException;
import fr.yoanndiquelou.jeelight.model.Command;
import fr.yoanndiquelou.jeelight.model.Method;

/**
 * Test command class.
 * 
 * @author y0annD
 *
 */
@DisplayName("CommandTest")
public class CommandTest {

	/**
	 * Constructor.
	 */
	public CommandTest() {
		super();
	}

	/**
	 * Test simple command.
	 */
	@Test
	@DisplayName("testSimpleCommandWithoutResult")
	public void testSimpleCommandWithoutResult() {
		final String waited = "{\"id\":1,\"method\":\"bg_toggle\",\"params\":[]}\r\n";
		try {
			Command c = new Command(1, Method.BG_TOGGLE);
			assertEquals(waited, c.toString());

		} catch (ParameterException e) {
			fail("Exception should not be thrown");
		}
	}

	/**
	 * Test simple command.
	 */
	@Test
	@DisplayName("testComplexCommandWithtResult")
	public void testComplexCommandWithResult() {
		final String waited = "{\"id\":2,\"method\":\"set_power\",\"params\":[\"off\",\"smooth\",500]}\r\n";
		try {
			Command c = new Command(2, Method.SET_POWER, new Object[] { "off", "smooth", 500 });
			assertEquals(waited, c.toString());

		} catch (ParameterException e) {
			fail("Exception should not be thrown", e);
		}
	}

	/**
	 * Test simple command.
	 */
	@Test
	@DisplayName("testComplexCommandWithtResult")
	public void testComplexCommandWithoutResult() {
		final String waited = "{\"id\":2,\"method\":\"set_power\",\"params\":[\"off\",\"smooth\",500]}\r\n";
		try {
			Command c = new Command(2, Method.SET_POWER, new Object[] { "off", "smooth", 500 });
			assertEquals(waited, c.toString());

		} catch (ParameterException e) {
			fail("Exception should not be thrown");
		}
	}

	@Test
	public void testParameterException() {
		assertThrows(ParameterException.class, () -> {
			new Command(2, Method.SET_POWER, new Object[] { "off", 500, "smooth" });
		});
	}
}
