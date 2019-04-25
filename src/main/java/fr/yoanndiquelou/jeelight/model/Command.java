package fr.yoanndiquelou.jeelight.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fr.yoanndiquelou.jeelight.exception.CommandException;
import fr.yoanndiquelou.jeelight.exception.ParameterException;

public class Command {
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
	 * @param params parameters
	 */
//	public Command(int id, String method, List<Object> params) {
//		this(id, method);
//		mParams = params;
//	}

	/**
	 * Command.
	 * 
	 * @param id     id
	 * @param method method
	 * @param params params Array
	 */
//	public Command(int id, String method, Object[] params) {
//		this(id, method);
//		mParams = Arrays.asList(params);
//	}

	/**
	 * Command.
	 * 
	 * @param id     id
	 * @param method method
	 * @param params params
	 */
	public Command(int id, Method method, Object... params) throws CommandException, ParameterException {
		this(id, method);
		mParams = new ArrayList<Object>();
		
		for (Object param : params) {
			mParams.add(param);
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
			System.out.println(result);
			return result.length() > 0;
		});
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"id\":").append(mId).append(",\"method\":\"").append(mMethod.getName()).append("\",\"params\":[");
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
