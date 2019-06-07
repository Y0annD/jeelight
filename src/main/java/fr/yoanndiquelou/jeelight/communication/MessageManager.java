package fr.yoanndiquelou.jeelight.communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.yoanndiquelou.jeelight.exception.ParameterException;
import fr.yoanndiquelou.jeelight.model.Command;
import fr.yoanndiquelou.jeelight.model.Duo;
import fr.yoanndiquelou.jeelight.model.Light;
import fr.yoanndiquelou.jeelight.model.Method;
import fr.yoanndiquelou.jeelight.model.Trio;
import fr.yoanndiquelou.jeelight.utils.FailFuture;

public class MessageManager {
	/** Message manager logger. */
	private static final Logger logger = LogManager.getLogger(MessageManager.class);
	/** response ID. */
	private static final String ID = "id";
	/** Method. */
	private static final String METHOD = "method";
	/** Properties update. */
	private static final String PROPS = "props";
	/** Command response parser. */
	private static final Pattern commandResponsePattern = Pattern.compile("\\{\"id\":(\\d*), \"result\":(.*)\\}");
	/** Notification response parser. */
	private static final Pattern notificationResponsePattern = Pattern
			.compile("\\{\"method\":\"(.*)\",\"params\":(.*)\\}");
	/** Object matcher. */
	private static final Pattern objectPattern = Pattern.compile("\"([a-z]*)\":\"?(\\w*)\"?");
	/** command executor. */
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	/** Socket to communicate with Light. */
	private final Socket mSocket;
	/** Light instance. */
	private final Light mLight;
	/** Id of message. */
	private int mId = 1;
	/** Connected to device. */
	private boolean mConnected;
	/**
	 * Receive thread.
	 */
	private Thread mReceiveThread;

	/** Socket reader. */
	private BufferedReader mReader;
	/**
	 * History of commands.
	 * <ul>
	 * <li>first parameter is ID of the command.</li>
	 * <li>second parameter is the Command.</li>
	 * <li>third parameter is the result of the command.</li>
	 * </ul>
	 */
	private Map<Integer, Duo<Command, Boolean>> mHistory = new HashMap<>();

	private static Map<Light, MessageManager> mManagers = new HashMap<>();

	/**
	 * Message manager constructor.
	 * 
	 * @param light light to manage
	 * @throws IOException        input/output exception
	 * @throws ParameterException parameter exception
	 */
	private MessageManager(Light light) throws IOException, ParameterException {
		this(light, new Socket(light.getIp(), 55443));
	}

	/**
	 * Message manager.
	 * <p>
	 * For test purposes only.
	 * </p>
	 * 
	 * @param light  light to manage
	 * @param socket socket mocket
	 * @throws ParameterException parameter exception
	 */
	private MessageManager(Light light, Socket socket) throws ParameterException {
		mLight = light;
		mSocket = socket;
		mConnected = true;
		try {
			mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			mReceiveThread = new Thread(() -> {
				while (mConnected) {
					String result;
					try {
						result = mReader.readLine();
						logger.debug("Device {}: {}", mLight.getIp(), result);
						processResponse(result);
					} catch (IOException e) {
						logger.debug("Error while listening client", e);
					}
				}
			});
			mReceiveThread.start();
		} catch (IOException e) {
			throw new ParameterException("Error with socket during instanciation", e);
		}
	}

	/**
	 * Get instance of Message manager for the specified light.
	 * 
	 * @param light light to manage
	 * @return associated message manager
	 * @throws IOException        Input/Output exception
	 * @throws ParameterException parameter exception
	 */
	public static MessageManager getInstance(Light light) throws IOException, ParameterException {
		if (mManagers.containsKey(light)) {
			return mManagers.get(light);
		} else {
			MessageManager manager = new MessageManager(light);
			mManagers.put(light, manager);
			return manager;

		}
	}

	/**
	 * Get instance of Message manager for the specified light.
	 * 
	 * @param light  light to manage
	 * @param socket socket to add to light if new message manager is created
	 * @return associated message manager
	 * @throws IOException        Input/Output exception
	 * @throws ParameterException parameter exception
	 */
	public static MessageManager getInstance(Light light, Socket socket) throws IOException, ParameterException {
		if (mManagers.containsKey(light)) {
			return mManagers.get(light);
		} else {
			MessageManager manager = new MessageManager(light, socket);
			mManagers.put(light, manager);
			return manager;

		}
	}

	/**
	 * Process the response or a notification sended by light.
	 * 
	 * @param data response
	 */
	private void processResponse(final String data) {
		if (null != data) {
			String localData = data.substring(2);
			if (localData.startsWith(ID)) {
				processCommandResponse(data);
			} else if (localData.startsWith(METHOD)) {
				processNotification(data);
			} else {
				logger.error("Unknown notification", data);
			}
		}
	}

	/**
	 * Process a response.
	 */
	private void processCommandResponse(String response) {
		logger.debug("Command response", response);
		Matcher matcher = commandResponsePattern.matcher(response);
		if (matcher.matches()) {
			Integer lId = Integer.valueOf(matcher.group(1));
			String params = matcher.group(2);
			logger.debug("Command: {}, params: {}", lId, params);
			Duo<Command, Boolean> duo = mHistory.get(lId);
			synchronized (duo) {
				duo.setV("[\"ok\"]".equals(params));
				duo.notify();
			}
		} else {
			logger.error("Command repsonse does not match response", response);
		}
	}

	/**
	 * Process a notification.
	 */
	private void processNotification(String notification) {
		logger.debug("Notification", notification);
		Matcher matcher = notificationResponsePattern.matcher(notification);

		if (matcher.matches()) {
			String method = matcher.group(1).trim();
			String params = matcher.group(2).trim();
			// Remove { and } chars at begin and end
			params = params.substring(1, params.length() - 1);
			logger.debug("Method: {}, params: {}", method, params);
			if (PROPS.equals(method)) {
				processPropertyNotification(params);
			} else {
				logger.error("Unknown notification", notification);
			}
		} else {
			logger.error("Notification response isn't correctly formated", notification);
		}
	}

	/**
	 * Process property notification.
	 * 
	 * @param params parameters
	 */
	private void processPropertyNotification(String params) {
		// Split by ,
		String[] splittedParams = params.split(",");
		for (String param : splittedParams) {
			Matcher paramMatcher = objectPattern.matcher(param);
			if (paramMatcher.matches()) {

				String property = paramMatcher.group(1);
				String value = paramMatcher.group(2).replaceAll("\"", "");
				logger.debug("Property: {}, value: {}", property, value);
				try {
					mLight.updateFromMethod(property, value);
				} catch (ParameterException e) {
					logger.error("Wrong notification", e);
				}
			} else {
				logger.debug("Not a correct parameter");
			}
		}
	}

	/**
	 * Send command.
	 * 
	 * @param method method
	 * @param params parameters of command
	 */
	public Future<Boolean> send(Method method, Object... params) {
		try {

			Command cmd = new Command(mId++, method, params);

			Duo<Command, Boolean> duo = new Duo<>(cmd, null);
			mHistory.put(mId - 1, duo);

			logger.debug("Ip: " + mLight.getIp());
			logger.debug("Data: " + cmd.toString());

			SendRunnable sendR = new SendRunnable(duo, new DataOutputStream(mSocket.getOutputStream()));
			return executor.submit(sendR);

		} catch (ParameterException e1) {
			logger.error("Wrong parameter", e1);
			return new FailFuture();
		} catch (IOException e) {
			logger.error("Error while sending command", e);
			return new FailFuture();
		}
	}

	/**
	 * Get command history.
	 * 
	 * @return command history
	 */
	public List<Trio<Integer, Command, Boolean>> getHistory() {
		List<Trio<Integer, Command, Boolean>> history = new ArrayList<>();
		for (Entry<Integer, Duo<Command, Boolean>> entry : mHistory.entrySet()) {
			history.add(new Trio<>(entry.getKey(), entry.getValue().getK(), entry.getValue().getV()));
		}
		return history;
	}
}
