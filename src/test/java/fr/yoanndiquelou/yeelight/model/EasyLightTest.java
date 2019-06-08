package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.communication.MessageManager;
import fr.yoanndiquelou.jeelight.exception.CommandException;
import fr.yoanndiquelou.jeelight.model.AdjustType;
import fr.yoanndiquelou.jeelight.model.ColorFlowEnd;
import fr.yoanndiquelou.jeelight.model.ColorMode;
import fr.yoanndiquelou.jeelight.model.EasyLight;
import fr.yoanndiquelou.jeelight.model.EffectType;
import fr.yoanndiquelou.jeelight.model.FlowColor;
import fr.yoanndiquelou.jeelight.model.Light;
import fr.yoanndiquelou.jeelight.model.Method;
import fr.yoanndiquelou.jeelight.model.SceneClass;

@DisplayName("Test the easylight object")
public class EasyLightTest {
	/** Result of command execution. */
	Future<Boolean> mFut;
	/** Test light. */
	private Light mLight;

	/**
	 * Constructor.
	 */
	public EasyLightTest() {
		mFut = new Future<Boolean>() {

			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				return false;
			}

			@Override
			public boolean isCancelled() {
				return false;
			}

			@Override
			public boolean isDone() {
				return true;
			}

			@Override
			public Boolean get() throws InterruptedException, ExecutionException {
				return null;
			}

			@Override
			public Boolean get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				return true;
			}
		};
	}

	@BeforeEach
	public void setUp() {
		mLight = new Light();
		for (Method m : Method.values()) {
			mLight.addMethod(m);
		}
	}

	@DisplayName("Test constructor")
	@Test
	public void testConstructor() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertEquals(100, el.getDuration());
		assertEquals(EffectType.SMOOTH, el.getEffectType());
	}

	@DisplayName("Test set duration")
	@Test
	public void testSetDuration() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.setDuration(29);
		});
		el.setDuration(30);
		assertEquals(30, el.getDuration());
	}

	@DisplayName("Test set effect type")
	@Test
	public void testSetEffectType() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		el.setEffectType(EffectType.SUDDEN);
		assertEquals(EffectType.SUDDEN, el.getEffectType());
	}

	@DisplayName("Test Toggle method")
	@Test
	public void testEasyLightToggle() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		when(mockManager.send(Method.TOGGLE)).thenReturn(mFut);
		try {
			el.toggle();
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.TOGGLE);
	}

	@DisplayName("Test start flow")
	@Test
	public void testStartFlow() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);
		List<FlowColor> flow = new ArrayList<>();
		flow.add(new FlowColor(ColorMode.COLOR, 12345, 75, 100));
		flow.add(new FlowColor(ColorMode.TEMPERATURE, 12345, 75, 100));
		when(mockManager.send(Method.START_CF, 1, ColorFlowEnd.OFF.getValue(), "100,1,12345,75,100,2,12345,75"))
				.thenReturn(mFut);
		try {
			el.startCf(ColorFlowEnd.OFF, flow);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.START_CF, 2, ColorFlowEnd.OFF.getValue(), "100,1,12345,75,100,2,12345,75");
	}

	@DisplayName("Test stop flow")
	@Test
	public void testStopFlow() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);
		when(mockManager.send(Method.STOP_CF)).thenReturn(mFut);
		try {
			el.stopCf();
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.STOP_CF);
	}

	@DisplayName("Test add cron")
	@Test
	public void testAddCron() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);
		when(mockManager.send(Method.CRON_ADD, 0, 1)).thenReturn(mFut);
		try {
			el.setCron(1);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.CRON_ADD, 0, 1);
	}

	@DisplayName("Test get cron")
	@Test
	public void testGetCron() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);
		when(mockManager.send(Method.CRON_GET, 0)).thenReturn(mFut);
		try {
			el.getCron();
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.CRON_GET, 0);
	}

	@DisplayName("Test del cron")
	@Test
	public void testDelCron() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);
		when(mockManager.send(Method.CRON_DEL, 0)).thenReturn(mFut);
		try {
			el.delCron();
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.CRON_DEL, 0);
	}

	@DisplayName("Test set scene")
	@Test
	public void testSetScene() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);
		when(mockManager.send(Method.SET_SCENE, SceneClass.AUTO_DELAY_OFF.getValue(), 50, 5)).thenReturn(mFut);
		when(mockManager.send(Method.SET_SCENE, SceneClass.COLOR.getValue(), 65280, 70)).thenReturn(mFut);
		when(mockManager.send(Method.SET_SCENE, SceneClass.CT.getValue(), 5400, 100)).thenReturn(mFut);
		when(mockManager.send(Method.SET_SCENE, SceneClass.HSV.getValue(), 300, 70, 100)).thenReturn(mFut);
		when(mockManager.send(Method.SET_SCENE, SceneClass.CF.getValue(), 0, 0, "500,1,255,100,1000,1,16776960,70"))
				.thenReturn(mFut);

		try {
			el.setScene(SceneClass.AUTO_DELAY_OFF, 50, 5);
			el.setScene(SceneClass.COLOR, 65280, 70);
			el.setScene(SceneClass.CT, 5400, 100);
			el.setScene(SceneClass.HSV, 300, 70, 100);
			el.setScene(SceneClass.CF, 0, 0, "500,1,255,100,1000,1,16776960,70");
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		// test parameters
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.COLOR, -1, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.COLOR, 0xffffff1, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.COLOR, 10, -1);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.COLOR, 10, 101);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.CT, 1499, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.CT, 5901, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.AUTO_DELAY_OFF, 0, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.HSV, 0, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.HSV, -1, 1, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.HSV, 360, 1, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.HSV, 1, -1, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.HSV, 1, 101, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.HSV, 0, 1, "abc");
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.HSV, 0, 1, -1);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.HSV, 0, 1, 101);
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.CF, -1, 1, "");
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.CF, 0, -1, "");
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.CF, 0, 3, "");
		});
		assertThrows(CommandException.class, () -> {
			el.setScene(SceneClass.COLOR, 10, 101, 0);
		});
	}

	@DisplayName("Test set scene")
	@Test
	public void testSetBgScene() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);
		when(mockManager.send(Method.BG_SET_SCENE, SceneClass.AUTO_DELAY_OFF.getValue(), 50, 5)).thenReturn(mFut);
		when(mockManager.send(Method.BG_SET_SCENE, SceneClass.COLOR.getValue(), 65280, 70)).thenReturn(mFut);
		when(mockManager.send(Method.BG_SET_SCENE, SceneClass.CT.getValue(), 5400, 100)).thenReturn(mFut);
		when(mockManager.send(Method.BG_SET_SCENE, SceneClass.HSV.getValue(), 300, 70, 100)).thenReturn(mFut);
		when(mockManager.send(Method.BG_SET_SCENE, SceneClass.CF.getValue(), 0, 0, "500,1,255,100,1000,1,16776960,70"))
				.thenReturn(mFut);

		try {
			el.setBgScene(SceneClass.AUTO_DELAY_OFF, 50, 5);
			el.setBgScene(SceneClass.COLOR, 65280, 70);
			el.setBgScene(SceneClass.CT, 5400, 100);
			el.setBgScene(SceneClass.HSV, 300, 70, 100);
			el.setBgScene(SceneClass.CF, 0, 0, "500,1,255,100,1000,1,16776960,70");
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		// test parameters
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.COLOR, -1, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.COLOR, 0xffffff1, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.COLOR, 10, -1);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.COLOR, 10, 101);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.CT, 1499, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.CT, 5901, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.AUTO_DELAY_OFF, 0, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.HSV, 0, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.HSV, -1, 1, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.HSV, 360, 1, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.HSV, 1, -1, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.HSV, 1, 101, 1);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.HSV, 0, 1, "abc");
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.HSV, 0, 1, -1);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.HSV, 0, 1, 101);
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.CF, -1, 1, "");
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.CF, 0, -1, "");
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.CF, 0, 3, "");
		});
		assertThrows(CommandException.class, () -> {
			el.setBgScene(SceneClass.COLOR, 10, 101, 0);
		});
	}

	@DisplayName("Test set color temperature")
	@Test
	public void testSetCtAbx() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.setCtAbx(65001);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.setCtAbx(1699);
		});
		when(mockManager.send(Method.SET_CT_ABX, 1701, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		try {
			el.setCtAbx(1701);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.SET_CT_ABX, 1701, EffectType.SMOOTH.getValue(), 100);
	}

	@DisplayName("Test set color")
	@Test
	public void testSetRgbWithValues() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		when(mockManager.send(Method.SET_RGB, 16777215, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		try {
			el.setRgb(255, 255, 255);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.SET_RGB, 16777215, EffectType.SMOOTH.getValue(), 100);

		when(mockManager.send(Method.SET_RGB, 16777215, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		try {
			el.setRgb(0xFFFFFE);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.SET_RGB, 16777214, EffectType.SMOOTH.getValue(), 100);
	}

	@DisplayName("Test set hsv")
	@Test
	public void testSetHsv() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.setHsv(-1, 1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.setHsv(360, 1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.setHsv(1, -1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.setHsv(1, 101);
		});
		when(mockManager.send(Method.SET_HSV, 1, 1, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		try {
			el.setHsv(1, 1);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.SET_HSV, 1, 1, EffectType.SMOOTH.getValue(), 100);
	}

	@DisplayName("Test set bright")
	@Test
	public void testSetBright() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.setBright(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.setBright(101);
		});

		when(mockManager.send(Method.SET_BRIGHT, 1, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		try {
			el.setBright(1);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.SET_BRIGHT, 1, EffectType.SMOOTH.getValue(), 100);
	}

	@DisplayName("Test set power")
	@Test
	public void testSetPower() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		when(mockManager.send(Method.SET_POWER, "on", EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		try {
			el.setPower(true);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.SET_POWER, "on", EffectType.SMOOTH.getValue(), 100);
	}

	@DisplayName("Test set name")
	@Test
	public void testSetName() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		when(mockManager.send(Method.SET_NAME, "Name")).thenReturn(mFut);
		try {
			el.setName("Name");
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.SET_NAME, "Name");
	}

	@DisplayName("Test bg toggle method")
	@Test
	public void testEasyBgLightToggle() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		when(mockManager.send(Method.BG_TOGGLE)).thenReturn(mFut);
		try {
			el.bgToggle();
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.BG_TOGGLE);
	}

	@DisplayName("Test bg set color temperature")
	@Test
	public void testbgSetCtAbx() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.setBgCtAbx(65001);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.setBgCtAbx(1699);
		});
		when(mockManager.send(Method.BG_SET_CT_ABX, 1701, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		try {
			el.setBgCtAbx(1701);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.BG_SET_CT_ABX, 1701, EffectType.SMOOTH.getValue(), 100);
	}

	@DisplayName("Test set background color")
	@Test
	public void testSetBgRgbWithValues() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		when(mockManager.send(Method.BG_SET_RGB, 16777215, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		try {
			el.setBgRgb(255, 255, 255);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.BG_SET_RGB, 16777215, EffectType.SMOOTH.getValue(), 100);

		when(mockManager.send(Method.BG_SET_RGB, 16777214, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		try {
			el.setBgRgb(0xFFFFFE);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.BG_SET_RGB, 16777214, EffectType.SMOOTH.getValue(), 100);
	}

	@DisplayName("Test set background hsv")
	@Test
	public void testSetBgHsv() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.setBgHsv(-1, 1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.setBgHsv(360, 1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.setBgHsv(1, -1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.setBgHsv(1, 101);
		});
		when(mockManager.send(Method.BG_SET_HSV, 1, 1, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		try {
			el.setBgHsv(1, 1);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.BG_SET_HSV, 1, 1, EffectType.SMOOTH.getValue(), 100);
	}

	@DisplayName("Test set background bright")
	@Test
	public void testSetBgBright() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.setBgBright(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.setBgBright(101);
		});

		when(mockManager.send(Method.BG_SET_BRIGHT, 1, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		try {
			el.setBgBright(1);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.BG_SET_BRIGHT, 1, EffectType.SMOOTH.getValue(), 100);
	}

	@DisplayName("Test adjust brightness")
	@Test
	public void testAdjustBright() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.adjustBright(-101, 30);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.adjustBright(101, 30);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.adjustBright(100, 29);
		});

		when(mockManager.send(Method.ADJUST_BRIGHT, 100, 30)).thenReturn(mFut);
		try {
			el.adjustBright(100, 30);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.ADJUST_BRIGHT, 100, 30);
	}

	@DisplayName("Test adjust color temperature")
	@Test
	public void testAdjustCt() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.adjustColorTemperature(-101, 30);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.adjustColorTemperature(101, 30);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.adjustColorTemperature(100, 29);
		});

		when(mockManager.send(Method.ADJUST_CT, 100, 30)).thenReturn(mFut);
		try {
			el.adjustColorTemperature(100, 30);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.ADJUST_CT, 100, 30);
	}

	@DisplayName("Test adjust color")
	@Test
	public void testAdjustColor() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.adjustColor(-101, 30);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.adjustColor(101, 30);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.adjustColor(100, 29);
		});

		when(mockManager.send(Method.ADJUST_COLOR, 100, 30)).thenReturn(mFut);
		try {
			el.adjustColor(100, 30);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.ADJUST_COLOR, 100, 30);
	}

	@DisplayName("Test adjust background brightness")
	@Test
	public void testBgAdjustBright() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.bgAdjustBright(-101, 30);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.bgAdjustBright(101, 30);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.bgAdjustBright(100, 29);
		});

		when(mockManager.send(Method.BG_ADJUST_BRIGHT, 100, 30)).thenReturn(mFut);
		try {
			el.bgAdjustBright(100, 30);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.BG_ADJUST_BRIGHT, 100, 30);
	}

	@DisplayName("Test adjust background color temperature")
	@Test
	public void testBgAdjustCt() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.bgAdjustColorTemperature(-101, 30);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.bgAdjustColorTemperature(101, 30);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.bgAdjustColorTemperature(100, 29);
		});

		when(mockManager.send(Method.BG_ADJUST_CT, 100, 30)).thenReturn(mFut);
		try {
			el.bgAdjustColorTemperature(100, 30);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.BG_ADJUST_CT, 100, 30);
	}

	@DisplayName("Test adjust background color")
	@Test
	public void testBgAdjustColor() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.bgAdjustColor(-101, 30);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.bgAdjustColor(101, 30);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.bgAdjustColor(100, 29);
		});

		when(mockManager.send(Method.BG_ADJUST_COLOR, 100, 30)).thenReturn(mFut);
		try {
			el.bgAdjustColor(100, 30);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.BG_ADJUST_COLOR, 100, 30);
	}

	@DisplayName("Test set background power")
	@Test
	public void testSetBgPower() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		when(mockManager.send(Method.BG_SET_POWER, "off", EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		try {
			el.setBgPower(false);
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.BG_SET_POWER, "off", EffectType.SMOOTH.getValue(), 100);
	}

	@DisplayName("Test set adjust")
	@Test
	public void testSetAdjust() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.setAdjust(AdjustType.INCREASE, "color");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.setAdjust(AdjustType.INCREASE, "fake");
		});

		when(mockManager.send(Method.SET_ADJUST, AdjustType.INCREASE.getValue(), "bright")).thenReturn(mFut);
		try {
			el.setAdjust(AdjustType.INCREASE, "bright");
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.SET_ADJUST, AdjustType.INCREASE.getValue(), "bright");

		try {
			el.setAdjust(AdjustType.INCREASE, "ct");
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.SET_ADJUST, AdjustType.INCREASE.getValue(), "ct");

		try {
			el.setAdjust(AdjustType.CIRCLE, "color");
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.SET_ADJUST, AdjustType.CIRCLE.getValue(), "color");
	}

	@DisplayName("Test set background adjust")
	@Test
	public void testSetBgAdjust() {

		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(IllegalArgumentException.class, () -> {
			el.setBgAdjust(AdjustType.INCREASE, "color");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			el.setBgAdjust(AdjustType.INCREASE, "fake");
		});

		when(mockManager.send(Method.BG_SET_ADJUST, AdjustType.CIRCLE.getValue(), "color")).thenReturn(mFut);
		try {
			el.setBgAdjust(AdjustType.CIRCLE, "color");
		} catch (CommandException e) {
			fail("Should not throw exception");
		}

		verify(mockManager).send(Method.BG_SET_ADJUST, AdjustType.CIRCLE.getValue(), "color");
	}

	@DisplayName("Test unavailable command")
	@Test
	public void testUnavailableCommand() {
		mLight.removeMethod(Method.BG_SET_POWER);
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);

		assertThrows(CommandException.class, () -> {
			el.setBgPower(false);
		});
	}

	@DisplayName("Test get light")
	@Test
	public void testGetLight() {
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(mLight, mockManager);
		assertEquals(mLight, el.getLight());
	}
}
