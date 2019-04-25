package fr.yoanndiquelou.jeelight;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import fr.yoanndiquelou.jeelight.model.Light;
import fr.yoanndiquelou.jeelight.model.Method;
import fr.yoanndiquelou.jeelight.ssdp.Device;
import fr.yoanndiquelou.jeelight.ssdp.SSDPClient;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		System.out.println("Hello World!");
		Map<String, Light> devMap = new HashMap();
		MessageManager manager = new MessageManager();
//        List<Device> devices = SSDPClient.discover(1000, "urn:schemas-upnp-org:device:ZonePlayer:1");
		List<Device> devices = SSDPClient.discover(5000, 1982, "wifi_bulb");
		System.out.println(devices.size() + " ssdp devices found");
		for (Device dev : devices) {
			System.out.println(dev.toString());
		}
		List<Light> lights = devices.stream().distinct().map(new DeviceToLight()).collect(Collectors.<Light>toList());
		for (Light l : lights) {
			System.out.println(l.toString());
			try {

				if (l.isPower()) {
					manager.send(l, Method.SET_POWER, new Object[] { "off", "smooth", 500 });
				} else {
					manager.send(l, Method.SET_POWER, new Object[] { "on", "smooth", 500 });
				}
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
}
