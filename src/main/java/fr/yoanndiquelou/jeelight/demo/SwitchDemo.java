package fr.yoanndiquelou.jeelight.demo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.yoanndiquelou.jeelight.communication.MessageManager;
import fr.yoanndiquelou.jeelight.exception.ParameterException;
import fr.yoanndiquelou.jeelight.model.Light;
import fr.yoanndiquelou.jeelight.model.Method;
import fr.yoanndiquelou.jeelight.ssdp.SSDPClient;

/**
 * Hello world!
 *
 */
public class SwitchDemo {
	/** App logger. */
	private static final Logger logger = LogManager.getLogger(SwitchDemo.class);

	public static void main(String[] args) throws IOException {
		logger.info("Start!");

		SSDPClient client = new SSDPClient(5000, "wifi_bulb", 1982);
		List<Light> devices = client.getDevices();
		logger.info(devices.size() + " ssdp devices found");
		for (Light dev : devices) {
			logger.info(dev.toString());
		}
		client.addListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (SSDPClient.ADD.equals(evt.getPropertyName())) {
					Light l = (Light) evt.getNewValue();
					MessageManager manager = null;
					logger.info("New device");
					try {
						manager = new MessageManager(l);
					} catch (ParameterException | IOException e) {
						logger.error("Network exception", e);
						System.exit(1);
					}
					Future<Boolean> future;

					future = manager.send(Method.TOGGLE, new Object[0]);
					devices.add(l);
					while (!future.isDone()) {}
					try {
						logger.info("Command result: {}", future.get());
					} catch (ExecutionException | InterruptedException e) {
						logger.error("Error during command execution", e);
					}
				}

			}
		});
		client.startDiscovering();

	}
}
