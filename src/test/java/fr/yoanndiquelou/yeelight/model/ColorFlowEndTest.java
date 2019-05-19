package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.model.ColorFlowEnd;
import fr.yoanndiquelou.jeelight.model.EffectType;

@DisplayName("Test color flow end type")
public class ColorFlowEndTest {

	@DisplayName("Get value")
	@Test
	public void testColorFlowEndTypeGetValue() {
		assertEquals(0, ColorFlowEnd.RECOVER.getValue());
		assertEquals(1, ColorFlowEnd.KEEP.getValue());
		assertEquals(2, ColorFlowEnd.OFF.getValue());
	}
}
