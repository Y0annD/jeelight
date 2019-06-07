package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.model.EffectType;
import fr.yoanndiquelou.jeelight.model.SceneClass;

@DisplayName("Test Scene class")
public class SceneClassTest {

	@DisplayName("Get value")
	@Test
	public void testEffectTypeGetValue() {
		assertEquals("cf", SceneClass.CF.getValue());
		assertEquals("ct", SceneClass.CT.getValue());
		assertEquals("auto_delay_off", SceneClass.AUTO_DELAY_OFF.getValue());
		assertEquals("hsv", SceneClass.HSV.getValue());
		assertEquals("color", SceneClass.COLOR.getValue());
	}
}
