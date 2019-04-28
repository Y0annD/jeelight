package fr.yoanndiquelou.yeelight;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.ssdp.SSDPClient;

/**
 * Test ssdp client.
 * 
 * @author y0annD
 *
 */
public class SSDPClientTest {

	/**
	 * Test simple initialisation.
	 */
	@Test
	@DisplayName("testInitialisation")
	public void testInitialisation() {
		try {
			SSDPClient client = new SSDPClient(10000, "test", 1234);
			assertTrue(client.getDevices().isEmpty());
			assertFalse(client.isDiscovering());
		} catch (SocketException e) {
			fail("Should not catch exception", e);
		}
	}

	/**
	 * Test the ssdp request.
	 */
	@Test
	@DisplayName("testBuildRequest")
	public void testBuildRequest() {
		String service = "test";
		try {

			SSDPClient client = new SSDPClient(10000, service, 1234);
			assertEquals("M-SEARCH * HTTP/1.1\r\n" + "HOST: 239.255.255.250:1234\r\n" + "MAN: \"ssdp:discover\"\r\n"
					+ "ST: test\r\n", client.buildRequestString());
			client = new SSDPClient(10000, null, 1234);
			assertEquals("M-SEARCH * HTTP/1.1\r\n" + "HOST: 239.255.255.250:1234\r\n" + "MAN: \"ssdp:discover\"\r\n"
					+ "ST: ssdp:all\r\n", client.buildRequestString());
		} catch (SocketException e) {
			fail("Should not catch exception", e);
		}

	}

}
