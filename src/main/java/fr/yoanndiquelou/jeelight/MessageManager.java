package fr.yoanndiquelou.jeelight;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.yoanndiquelou.jeelight.exception.ParameterException;
import fr.yoanndiquelou.jeelight.model.Command;
import fr.yoanndiquelou.jeelight.model.Light;
import fr.yoanndiquelou.jeelight.model.Method;

public class MessageManager {
	/** Message manager logger. */
	private static final Logger logger = LogManager.getLogger(MessageManager.class);
	/** Socket to communicate with Light. */
	private final Socket mSocket;
	/** Light instance. */
	private final Light mLight;
	/** Id of message. */
	private int id = 1;

	/**
	 * Message manager constructor.
	 * 
	 * @param light light to manage
	 * @throws IOException input/output exception
	 */
	public MessageManager(Light light) throws IOException {
		mLight = light;
		mSocket = new Socket(mLight.getIp(), 55443);
	}

	/**
	 * Message manager.
	 * <p>
	 * For test purposes only.
	 * </p>
	 * 
	 * @param light  light to manage
	 * @param socket socket mocket
	 */
	public MessageManager(Light light, Socket socket) {
		mLight = light;
		mSocket = socket;
	}

	public boolean send(Method method, Object[] params) throws ExecutionException {
		Command cmd;
		boolean result;
		try {
			cmd = new Command(id++, method, params);
			logger.debug("Ip: " + mLight.getIp());
			logger.debug("Data: " + cmd.toString());
			DataOutputStream outToServer = new DataOutputStream(mSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			Future<Boolean> sendFuture = cmd.send(outToServer, inFromServer);
			try {

				while (!sendFuture.isDone()) {
					logger.debug("Waiting for command result ...");
					Thread.sleep(300);

				}

				result = sendFuture.get();
			} catch (InterruptedException e) {
				result = false;
				throw new ExecutionException("Error while sending command", e);
			}
			logger.debug("Result: " + result);

		} catch (ParameterException e1) {
			logger.error("Wrong parameter", e1);
			result = false;
		} catch (IOException e) {
			logger.error("Error while sending command", e);
			result = false;
		}
		return result;
	}

}
