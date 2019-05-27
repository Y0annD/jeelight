package fr.yoanndiquelou.yeelight.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.utils.FailFuture;

/**
 * Test failFuture.
 * 
 * @author yoann
 *
 */
@DisplayName("Test fail future")
public class FailFutureTest {

	@Test
	@DisplayName("Test object")
	public void testObject() {
		Future<Boolean> ff = new FailFuture();
		assertFalse(ff.cancel(true));
		assertFalse(ff.cancel(false));
		assertTrue(ff.isCancelled());
		assertTrue(ff.isDone());
		try {
			assertFalse(ff.get());
			assertFalse(ff.get(10, TimeUnit.MILLISECONDS));
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			fail("Should not thrown exception");
		}

	}
}
