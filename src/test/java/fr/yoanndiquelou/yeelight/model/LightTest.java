package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import fr.yoanndiquelou.jeelight.model.Method;

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
		mLight.setBgPower(false);
		mLight.setBrightness(1);
		mLight.setBgBrightness(51);
		mLight.setColorMode(1);
		mLight.setBgColorMode(2);
		mLight.setCt(1);
		mLight.setBgCt(1800);
		mLight.setRGB(1);
		mLight.setBgRGB(1900);
		mLight.setHue(1);
		mLight.setBgHue(10);
		mLight.setSaturation(1);
		mLight.setBgSaturation(78);
		mLight.setActiveMode(1);
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
		assertFalse(mLight.isBgPower());
		assertEquals(1, mLight.getBrightness());
		assertEquals(51, mLight.getBgBrightness());
		assertEquals(1, mLight.getColorMode());
		assertEquals(2, mLight.getBgColorMode());
		assertEquals(1, mLight.getCt());
		assertEquals(1800, mLight.getBgCt());
		assertEquals(1, mLight.getRGB());
		assertEquals(1900, mLight.getBgRGB());
		assertEquals(1, mLight.getHue());
		assertEquals(10, mLight.getBgHue());
		assertEquals(1, mLight.getSaturation());
		assertEquals(78, mLight.getBgSaturation());
		assertEquals(name, mLight.getName());
		assertEquals(1, mLight.getActiveMode());
		assertTrue( mLight.getAvailableMethods().isEmpty());
	}
	
	/**
	 * Test task management.
	 */
	@Test
	@DisplayName("testTasks")
	public void testTasks() {
		Method method = Method.ADJUST_BRIGHT;
		mLight.addMethod(method);
		assertEquals(1, mLight.getAvailableMethods().size());
		assertTrue(mLight.getAvailableMethods().contains(method));
		mLight.addMethod(method);
		assertEquals(1, mLight.getAvailableMethods().size());
		mLight.removeMethod(method);
		assertTrue(mLight.getAvailableMethods().isEmpty());
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
			light.setIp("0.0.0.66");
			assertEquals("yeelight://10.42.42.10:55443", light.getLocation());
			assertEquals(new String(Files.readAllBytes(fToString.toPath())).trim(), light.toString().trim());
			assertEquals(20, light.getAvailableMethods().size());
		} catch (IOException  e) {
			fail("Unable to test static construction", e);
		}
	}

}
