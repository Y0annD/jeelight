package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.model.ColorFlowEnd;
import fr.yoanndiquelou.jeelight.model.ColorMode;
import fr.yoanndiquelou.jeelight.model.EffectType;

@DisplayName("Test color mode type")
public class ColorModeTest {

	@DisplayName("Get value")
	@Test
	public void testColorModeTypeGetValue() {
		assertEquals(1, ColorMode.COLOR.getValue());
		assertEquals(2, ColorMode.TEMPERATURE.getValue());
		assertEquals(7, ColorMode.SLEEP.getValue());
	}
}
