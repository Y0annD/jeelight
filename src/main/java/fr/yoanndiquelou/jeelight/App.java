package fr.yoanndiquelou.jeelight;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.yoanndiquelou.jeelight.model.Light;
import fr.yoanndiquelou.jeelight.model.Method;
import fr.yoanndiquelou.jeelight.ssdp.SSDPClient;

/**
 * Hello world!
 *
 */
public class App {
	/** App logger. */
	private static final Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args) throws IOException {
		logger.info("Start!");

		SSDPClient client = new SSDPClient(5000, "wifi_bulb", 1982);
		Set<Light> devices = client.getDevices();
		logger.info(devices.size() + " ssdp devices found");
		for (Light dev : devices) {
			logger.info(dev.toString());
		}
		client.addListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				try {
					if (SSDPClient.ADD.equals(evt.getPropertyName())) {
						Light l = (Light) evt.getNewValue();
						MessageManager manager = null;
						try {
							manager = new MessageManager(l);
						} catch (IOException e) {
							logger.error("Network exception", e);
							System.exit(1);
						}
						if (l.isPower()) {
							manager.send(Method.SET_POWER, new Object[] { "off", "smooth", 500 });
						} else {
							manager.send(Method.SET_POWER, new Object[] { "on", "smooth", 500 });
						}
						devices.add(l);
					}
				} catch (ExecutionException e) {
					logger.error("Error while sending command", e);
				}
			}
		});
		client.startDiscovering();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.error("Unable to pause application");
		}
		client.stopDiscovering();

		System.exit(0);
	}
}
