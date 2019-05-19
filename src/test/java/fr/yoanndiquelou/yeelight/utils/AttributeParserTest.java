package fr.yoanndiquelou.yeelight.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.utils.AttributeParser;

/**
 * Test attribute parser class.
 * @author Y0annD
 *
 */
@DisplayName("Test attribute parsing")
public class AttributeParserTest {
	
	@DisplayName("test parsing On value")
	@Test
	public void testParseOn() {
		assertTrue((Boolean)AttributeParser.parse("on"));
	}
	
	@DisplayName("test parsing Off value")
	@Test
	public void testParseOff() {
		assertFalse((Boolean)AttributeParser.parse("off"));
	}
	
	@DisplayName("test parsing Integer value")
	@Test
	public void testParseInt() {
		assertEquals(Integer.valueOf(10), AttributeParser.parse("10"));
	}
	
	@DisplayName("test parsing Long value")
	@Test
	public void testParseLong() {
		assertEquals(Long.valueOf(Integer.MAX_VALUE)+10, AttributeParser.parse(String.valueOf(Long.valueOf(Integer.MAX_VALUE)+10)));
	}

	@DisplayName("test String")
	@Test
	public void testParseString() {
		assertEquals("John Doe", AttributeParser.parse("John Doe"));
	}
}
