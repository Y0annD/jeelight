package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.model.Trio;

/**
 * Test the trio object.
 * @author y0annD
 *
 */
public class TrioTest {
	
	/**
	 * TrioTest constructor.
	 */
	public TrioTest() {
		super();
	}
	
	/**
	 * Test the Trio object.
	 */
	@Test
	@DisplayName("Test object")
	public void testObject() {
		Integer k1 = Integer.valueOf(1);
		Boolean u1 = Boolean.TRUE;
		String v1 = "v1";
		Integer k2 = Integer.valueOf(2);
		Boolean u2 = Boolean.FALSE;
		String v2 = "v2";
		Trio<Integer, Boolean, String> trio = new Trio<>(k1, u1, v1);
		assertEquals(k1, trio.getK());
		assertEquals(u1, trio.getU());
		assertEquals(v1, trio.getV());
		trio.setK(k2);
		trio.setU(u2);
		trio.setV(v2);
		assertEquals(k2, trio.getK());
		assertEquals(u2, trio.getU());
		assertEquals(v2, trio.getV());
	}

}
