package fr.yoanndiquelou.jeelight.ssdp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SSDPClient {

	/**
	 * Discover any UPNP device using SSDP (Simple Service Discovery Protocol).
	 * 
	 * @param timeout     in milliseconds
	 * @param port        port to use
	 * @param serviceType if null it use "ssdp:all"
	 * @return List of devices discovered
	 * @throws IOException
	 * @see <a href=
	 *      "https://en.wikipedia.org/wiki/Simple_Service_Discovery_Protocol">SSDP
	 *      Wikipedia Page</a>
	 */
	public static List<Device> discover(int timeout, int port, String serviceType) throws IOException {
		List<Device> devices = new ArrayList<Device>();
		byte[] sendData;
		byte[] receiveData = new byte[1024];

		/* Create the search request */
		StringBuilder msearch = new StringBuilder("M-SEARCH * HTTP/1.1\r\nHOST: 239.255.255.250:").append(port)
				.append("\r\nMAN: \"ssdp:discover\"\r\n");
		if (serviceType == null) {
			msearch.append("ST: ssdp:all\r\n");
		} else {
			msearch.append("ST: ").append(serviceType).append("\r\n");
		}

		/* Send the request */
		sendData = msearch.toString().getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
				InetAddress.getByName("239.255.255.250"), port);
		System.out.println(msearch.toString());
		DatagramSocket clientSocket = new DatagramSocket();
		clientSocket.setSoTimeout(timeout);
		clientSocket.send(sendPacket);

		/* Receive all responses */
		while (true) {
			try {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				devices.add(Device.parse(receivePacket));
			} catch (SocketTimeoutException e) {
				System.err.println("Timeout");
				break;
			}
		}

		clientSocket.close();
		return Collections.unmodifiableList(devices);
	}

	/**
	 * Discover one device.
	 * 
	 * @param timeout     request timeout
	 * @param port        port to listen
	 * @param serviceType service type
	 * @return the device object
	 * @throws IOException Input/Output exception
	 */
	public static Device discoverOne(int timeout, int port, Optional<String> serviceType) throws IOException {
		Device device = null;
		byte[] sendData;
		byte[] receiveData = new byte[1024];

		/* Create the search request */
		StringBuilder msearch = new StringBuilder("M-SEARCH * HTTP/1.1\r\nHOST: 239.255.255.250:").append(port)
				.append("\r\nMAN: \"ssdp:discover\"\r\n");
		if (!serviceType.isPresent()) {
			msearch.append("ST: ssdp:all\r\n");
		} else {
			msearch.append("ST: ").append(serviceType.get()).append("\r\n");
		}

		/* Send the request */
		sendData = msearch.toString().getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
				InetAddress.getByName("239.255.255.250"), port);
		DatagramSocket clientSocket = new DatagramSocket();
		clientSocket.setSoTimeout(timeout);
		clientSocket.send(sendPacket);

		/* Receive one response */
		try {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			device = Device.parse(receivePacket);
		} catch (SocketTimeoutException e) {
		}

		clientSocket.close();
		return device;
	}
}
