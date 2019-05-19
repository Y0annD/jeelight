package fr.yoanndiquelou.jeelight.model;

import java.util.ArrayList;
import java.util.List;

import fr.yoanndiquelou.jeelight.exception.ParameterException;

/**
 * Command class.
 * 
 * @author y0annD
 *
 */
public class Command {

	/** Id of command. */
	private int mId;
	/** Method. */
	private Method mMethod;
	/** Parameters. */
	private List<Object> mParams;
	

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
