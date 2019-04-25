package fr.yoanndiquelou.jeelight;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import fr.yoanndiquelou.jeelight.exception.CommandException;
import fr.yoanndiquelou.jeelight.exception.ParameterException;
import fr.yoanndiquelou.jeelight.model.Command;
import fr.yoanndiquelou.jeelight.model.Light;
import fr.yoanndiquelou.jeelight.model.Method;

public class MessageManager {
	/** Id of message. */
	int id = 150;

	public void send(Light light, Method method, Object[] params) throws InterruptedException, ExecutionException {
		Command cmd;
		try {
			cmd = new Command(id++, method, params);
			System.out.println("Ip: " + light.getIp());
			System.out.println("Data: " + cmd.toString());
			try (Socket clientSocket = new Socket(light.getIp(), 55443)) {
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				Future<Boolean> sendFuture = cmd.send(outToServer, inFromServer);
				while (!sendFuture.isDone()) {
					System.out.println("Calculating...");
					Thread.sleep(300);
				}

				Boolean result = sendFuture.get();
				System.out.println("Result: " + result);
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (CommandException | ParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
