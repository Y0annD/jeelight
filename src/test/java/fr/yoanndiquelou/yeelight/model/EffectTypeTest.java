package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.model.EffectType;

@DisplayName("Test effect type")
public class EffectTypeTest {

	@DisplayName("Get value")
	@Test
	public void testEffectTypeGetValue() {
		assertEquals("sudden", EffectType.SUDDEN.getValue());
		assertEquals("smooth", EffectType.SMOOTH.getValue());
	}
}
