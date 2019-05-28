package fr.yoanndiquelou.jeelight.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Future that represent a failing state.
 * <p>
 * If this future is returned, it means that something goes wrong
 * </p>
 * 
 * @author Y0annD
 *
 */
public class FailFuture implements Future<Boolean> {

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return true;
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public Boolean get() throws InterruptedException, ExecutionException {
		return false;
	}

	@Override
	public Boolean get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return false;
	}

}
