package fr.yoanndiquelou.jeelight.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.yoanndiquelou.jeelight.exception.CommandException;
import fr.yoanndiquelou.jeelight.exception.ParameterException;

/**
 * Command class.
 * 
 * @author y0annD
 *
 */
public class Command {

	/** Command logger. */
	private static final Logger logger = LogManager.getLogger(Command.class);

	/** Id of command. */
	private int mId;
	/** Method. */
	private Method mMethod;
	/** Parameters. */
	private List<Object> mParams;
	/** Command executor. */
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	/**
	 * Command.
	 * 
	 * @param id     id
	 * @param method method
	 */
	private Command(int id, Method method) {
		mId = id;
		mMethod = method;
	}

	/**
	 * Command.
	 * 
	 * @param id     id
	 * @param method method
	 * @param params params
	 */
	public Command(int id, Method method, Object... params) throws ParameterException {
		this(id, method);
		mParams = new ArrayList<>();

		method.check(params);
		for (int i = 0; i < params.length; i++) {
				mParams.add(params[i]);
		}
	}

	/**
	 * Send the command and get the result.
	 * 
	 * @param writer write to server or file
	 * @param reader write to reader or file
	 * @return result
	 */
	public Future<Boolean> send(DataOutputStream writer, BufferedReader reader) {
		return executor.submit(() -> {
			writer.write(toString().getBytes());
			writer.flush();
			String result = reader.readLine();
			logger.debug(result);
			if (null != result) {
				return result.trim().length() > 0;
			} else {
				return false;
			}
		});
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"id\":").append(mId).append(",\"method\":\"").append(mMethod.getName())
				.append("\",\"params\":[");
		for (int i = 0; i < mParams.size(); i++) {
			if (mParams.get(i) instanceof String) {
				builder.append("\"");
			}
			builder.append(mParams.get(i).toString());
			if (mParams.get(i) instanceof String) {
				builder.append("\"");
			}
			if (i < mParams.size() - 1) {
				builder.append(",");
			}
		}
		builder.append("]}\r\n");
		return builder.toString();
	}
}
