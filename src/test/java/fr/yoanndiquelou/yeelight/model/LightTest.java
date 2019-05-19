package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.model.Light;

/**
 * Test light class.
 * 
 * @author y0annD
 *
 */
@DisplayName("LightTest")
public class LightTest {
	/** Light. */
	private Light mLight;
	/** Light UUID. */
	private long uuid = new Random(10).nextLong();
	/** Model. */
	private String model = "model";
	/** Name. */
	private String name = "name";

	/**
	 * Constructor.
	 */
	public LightTest() {
		mLight = new Light();
		mLight.setIp("127.0.0.1");
		mLight.setId(uuid);
		mLight.setModel(model);
		mLight.setFirmware(1);
		mLight.setPower(true);
		mLight.setBrightness(1);
		mLight.setColorMode(1);
		mLight.setCt(1);
		mLight.setRGB(1);
		mLight.setHue(1);
		mLight.setSaturation(1);
		mLight.setName(name);
	}

	/**
	 * Test getters.
	 */
	@Test
	@DisplayName("testGetters")
	public void testGetters() {
		assertEquals("127.0.0.1", mLight.getIp());
		assertEquals(uuid, mLight.getId());
		assertEquals(model, mLight.getModel());
		assertEquals(1, mLight.getFirmware());
		assertTrue(mLight.isPower());
		assertEquals(1, mLight.getBrightness());
		assertEquals(1, mLight.getColorMode());
		assertEquals(1, mLight.getCt());
		assertEquals(1, mLight.getRGB());
		assertEquals(1, mLight.getHue());
		assertEquals(1, mLight.getSaturation());
		assertEquals(name, mLight.getName());
		assertTrue( mLight.getTasks().isEmpty());
	}
	
	/**
	 * Test task management.
	 */
	@Test
	@DisplayName("testTasks")
	public void testTasks() {
		String taskName = "name";
		mLight.addTask(taskName);
		assertEquals(1, mLight.getTasks().size());
		assertTrue(mLight.getTasks().contains(taskName));
		mLight.addTask(taskName);
		assertEquals(1, mLight.getTasks().size());
		mLight.removeTask(taskName);
		assertTrue(mLight.getTasks().isEmpty());
	}
	
	@Test
	@DisplayName("Test static construction")
	public void testStaticConstruction() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			File f = new File(getClass().getClassLoader().getResource("deviceSSDP.txt").getFile());
			File fToString = new File(getClass().getClassLoader().getResource("deviceTXT.txt").getFile());

			byte[] content = Files.readAllBytes(f.toPath());
			Light light = Light.fromDatagramPacket(addr, content);
			light.setName("0x42");
			assertEquals("yeelight://10.42.42.10:55443", light.getLocation());
			System.out.println(light.toString());
			assertEquals(new String(Files.readAllBytes(fToString.toPath())).trim(), light.toString().trim());
		} catch (IOException  e) {
			fail("Unable to test static construction", e);
		}
	}

}
