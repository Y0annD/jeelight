package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
			DataOutputStream dos = new DataOutputStream(new ByteArrayOutputStream());
			BufferedReader reader = new BufferedReader(BufferedReader.nullReader());
			Future<Boolean> fut = c.send(dos, reader);
			try {
				while (!fut.isDone()) {
					Thread.sleep(300);
				}

				assertFalse(fut.get());
			} catch (InterruptedException | ExecutionException e) {
				fail("Error while sending command", e);

			}
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new ByteArrayInputStream("data".getBytes())));
			Future<Boolean> fut = c.send(dos, reader);
			try {
				while (!fut.isDone()) {
					Thread.sleep(300);
				}
				assertEquals(waited, baos.toString());
				assertTrue(fut.get());
			} catch (InterruptedException | ExecutionException e) {
				fail("Error while sending command", e);

			}
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new ByteArrayInputStream(" ".getBytes())));
			Future<Boolean> fut = c.send(dos, reader);
			try {
				while (!fut.isDone()) {
					Thread.sleep(300);
				}
				assertEquals(waited, baos.toString());
				assertFalse(fut.get());
			} catch (InterruptedException | ExecutionException e) {
				fail("Error while sending command", e);

			}
		} catch (ParameterException e) {
			fail("Exception should not be thrown");
		}
	}
	
	@Test
	public void testParameterException() {
		assertThrows(ParameterException.class, ()->{
			Command c = new Command(2, Method.SET_POWER, new Object[] { "off", 500, "smooth" });
		});
	}
}
