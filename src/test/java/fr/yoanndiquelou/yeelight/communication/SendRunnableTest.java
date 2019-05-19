package fr.yoanndiquelou.yeelight.communication;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doThrow;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.communication.SendRunnable;
import fr.yoanndiquelou.jeelight.exception.ParameterException;
import fr.yoanndiquelou.jeelight.model.Command;
import fr.yoanndiquelou.jeelight.model.Duo;
import fr.yoanndiquelou.jeelight.model.Method;

/**
 * SendRunnable test.
 * 
 * @author Y0annD
 *
 */
public class SendRunnableTest {

	/**
	 * Send runnable test.
	 */
	@Test
	@DisplayName("SendRunnable")
	public void testSendRunnable() {
		
		try {
			Command c = new Command(1, Method.TOGGLE, new Object[0]);
			Duo<Command, Boolean> duo = new Duo<>(c, null);
			DataOutputStream dos = mock(DataOutputStream.class);
			Future<Boolean> fut = Executors.newSingleThreadExecutor().submit(new SendRunnable(duo, dos));
			synchronized(duo) {
				duo.setV(true);
				duo.notify();
			}
			while (!fut.isDone()) {
			}
			assertTrue(fut.get());
		} catch (ParameterException | InterruptedException | ExecutionException e) {
			fail("Should not throw exception", e);
		}
	}
	
	/**
	 * Send runnable test with wait.
	 */
	@Test
	@DisplayName("SendRunnableWithWait")
	public void testSendRunnableWithWait() {
		
		try {
			Command c = new Command(1, Method.TOGGLE, new Object[0]);
			Duo<Command, Boolean> duo = new Duo<>(c, null);
			DataOutputStream dos = mock(DataOutputStream.class);
			Future<Boolean> fut = Executors.newSingleThreadExecutor().submit(new SendRunnable(duo, dos));
			Thread.sleep(300);
			synchronized(duo) {
				duo.setV(true);
				duo.notify();
			}
			while (!fut.isDone()) {
			}
			assertTrue(fut.get());
		} catch (ParameterException | InterruptedException | ExecutionException e) {
			fail("Should not throw exception", e);
		}
	}
	
	/**
	 * Send runnable test with exception.
	 */
	@Test
	@DisplayName("SendRunnableWithException")
	public void testSendRunnableWithException() {
		
		try {
			Command c = new Command(1, Method.TOGGLE, new Object[0]);
			Duo<Command, Boolean> duo = new Duo<>(c, null);
			DataOutputStream dos = mock(DataOutputStream.class);
			doThrow(new IOException()).when(dos).flush();
			Future<Boolean> fut = Executors.newSingleThreadExecutor().submit(new SendRunnable(duo, dos));
			assertFalse(fut.get());
		} catch (ParameterException | InterruptedException | ExecutionException | IOException e) {
			fail("Should not throw exception", e);
		}
	}
}
