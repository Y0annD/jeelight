package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.model.Duo;
import fr.yoanndiquelou.jeelight.model.Trio;

/**
 * Test the trio object.
 * @author y0annD
 *
 */
public class DuoTest {
	
	/**
	 * TrioTest constructor.
	 */
	public DuoTest() {
		super();
	}
	
	/**
	 * Test the Trio object.
	 */
	@Test
	@DisplayName("Test object")
	public void testObject() {
		Integer k1 = Integer.valueOf(1);
		String v1 = "v1";
		Integer k2 = Integer.valueOf(2);
		String v2 = "v2";
		Duo<Integer, String> trio = new Duo<>(k1, v1);
		assertEquals(k1, trio.getK());
		assertEquals(v1, trio.getV());
		trio.setK(k2);
		trio.setV(v2);
		assertEquals(k2, trio.getK());
		assertEquals(v2, trio.getV());
	}

}
