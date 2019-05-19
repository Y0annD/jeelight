package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.exception.ParameterException;
import fr.yoanndiquelou.jeelight.model.EffectType;
import fr.yoanndiquelou.jeelight.model.Method;

/**
 * 
 * @author yoann
 *
 */
@DisplayName("MethodTest")
public class MethodTest {

	/**
	 * Constructor.
	 */
	public MethodTest() {
		super();
	}

	/**
	 * Test check with success.
	 */
	@Test
	@DisplayName("TestCheckSuccess")
	public void testCheckSuccess() {
		Method m = Method.SET_POWER;
		try {
			m.check("off", EffectType.SMOOTH.getValue(), 500);
			assertTrue(true);

		} catch (ParameterException e) {
			fail("Exception  should not be thrown", e);
		}
	}

	/**
	 * Test check with difference in parameter number.
	 */
	@Test
	@DisplayName("TestCheckFailParameterLength")
	public void testCheckFailParameterLength() {
		Method m = Method.SET_POWER;
		Exception e = assertThrows(ParameterException.class, () -> {
			m.check("off", EffectType.SMOOTH.getValue());
		});
		assertEquals("Found 2 parameters but expect 3", e.getMessage());
	}
	
	/**
	 * Test check with difference in parameter Type.
	 */
	@Test
	@DisplayName("TestCheckFailParameterType")
	public void testCheckFailParameterType() {
		Method m = Method.SET_POWER;
		Exception e = assertThrows(ParameterException.class, () -> {
			m.check("off", EffectType.SMOOTH.getValue(), "type");
		});
		assertEquals("Parameter at index 2 is incorrect. Integer waited, but found String", e.getMessage());
	}
	
	/**
	 * Test get parameter method.
	 */
	@Test
	@DisplayName("TestGetParameter")
	public void testGetParameter() {
		Method m = Method.SET_POWER;
		assertEquals(String.class, m.getParameter(0));
	}
}
