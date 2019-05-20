package fr.yoanndiquelou.yeelight.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.communication.MessageManager;
import fr.yoanndiquelou.jeelight.model.AdjustType;
import fr.yoanndiquelou.jeelight.model.ColorFlowEnd;
import fr.yoanndiquelou.jeelight.model.ColorMode;
import fr.yoanndiquelou.jeelight.model.EasyLight;
import fr.yoanndiquelou.jeelight.model.EffectType;
import fr.yoanndiquelou.jeelight.model.FlowColor;
import fr.yoanndiquelou.jeelight.model.Light;
import fr.yoanndiquelou.jeelight.model.Method;

@DisplayName("Test the easylight object")
public class EasyLightTest {

	Future<Boolean> mFut;

	/**
	 * COnstructor.
	 */
	public EasyLightTest() {
		mFut = new Future<Boolean>() {

			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isCancelled() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isDone() {
				return true;
			}

			@Override
			public Boolean get() throws InterruptedException, ExecutionException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Boolean get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				return true;
			}
		};
	}

	@DisplayName("Test constructor")
	@Test
	public void testConstructor() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertEquals(100, el.getDuration());
		assertEquals(EffectType.SMOOTH, el.getEffectType());
	}
	
	@DisplayName("Test set duration")
	@Test
	public void testSetDuration() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, () ->{
			el.setDuration(29);
		});
		el.setDuration(30);
		assertEquals(30, el.getDuration());
	}

	@DisplayName("Test set effect type")
	@Test
	public void testSetEffectType() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		
		el.setEffectType(EffectType.SUDDEN);
		assertEquals(EffectType.SUDDEN, el.getEffectType());
	}
	
	@DisplayName("Test Toggle method")
	@Test
	public void testEasyLightToggle() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		when(mockManager.send(Method.TOGGLE)).thenReturn(mFut);
		el.toggle();

		verify(mockManager).send(Method.TOGGLE);
	}
	
	@DisplayName("Test start flow")
	@Test
	public void testStartFlow() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);
		List<FlowColor> flow = new ArrayList<>();
		flow.add(new FlowColor(ColorMode.COLOR, 12345, 75, 100));
		flow.add(new FlowColor(ColorMode.TEMPERATURE, 12345, 75, 100));
		when(mockManager.send(Method.START_CF, 1,ColorFlowEnd.OFF.getValue(),"100,1,12345,75,100,2,12345,75")).thenReturn(mFut);
		el.startCf(ColorFlowEnd.OFF, flow);

		verify(mockManager).send(Method.START_CF,2, ColorFlowEnd.OFF.getValue(),"100,1,12345,75,100,2,12345,75");
	}
	
	@DisplayName("Test stop flow")
	@Test
	public void testStopFlow() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);
		when(mockManager.send(Method.STOP_CF)).thenReturn(mFut);
		el.stopCf();

		verify(mockManager).send(Method.STOP_CF);
	}
	
	@DisplayName("Test add cron")
	@Test
	public void testAddCron() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);
		when(mockManager.send(Method.CRON_ADD, 0,1)).thenReturn(mFut);
		el.setCron(1);

		verify(mockManager).send(Method.CRON_ADD,0,1);
	}
	
	@DisplayName("Test get cron")
	@Test
	public void testGetCron() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);
		when(mockManager.send(Method.CRON_GET, 0)).thenReturn(mFut);
		el.getCron();

		verify(mockManager).send(Method.CRON_GET,0);
	}
	
	@DisplayName("Test del cron")
	@Test
	public void testDelCron() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);
		when(mockManager.send(Method.CRON_DEL, 0)).thenReturn(mFut);
		el.delCron();

		verify(mockManager).send(Method.CRON_DEL,0);
	}
	
	
	
	@DisplayName("Test set color temperature")
	@Test
	public void testSetCtAbx() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.setCtAbx(65001);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.setCtAbx(1699);
		});
		when(mockManager.send(Method.SET_CT_ABX, 1701, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		el.setCtAbx(1701);

		verify(mockManager).send(Method.SET_CT_ABX, 1701, EffectType.SMOOTH.getValue(), 100);
	}

	@DisplayName("Test set color")
	@Test
	public void testSetRgbWithValues() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		when(mockManager.send(Method.SET_RGB,16777215 , EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		el.setRgb(255, 255, 255);

		verify(mockManager).send(Method.SET_RGB, 16777215 , EffectType.SMOOTH.getValue(), 100);
	}
	
	@DisplayName("Test set hsv")
	@Test
	public void testSetHsv() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.setHsv(-1,1);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.setHsv(360,1);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.setHsv(1,-1);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.setHsv(1,101);
		});
		when(mockManager.send(Method.SET_HSV, 1,1, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		el.setHsv(1,1);

		verify(mockManager).send(Method.SET_HSV, 1,1, EffectType.SMOOTH.getValue(), 100);
	}

	@DisplayName("Test set bright")
	@Test
	public void testSetBright() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.setBright(-1);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.setBright(101);
		});
		
		when(mockManager.send(Method.SET_BRIGHT, 1, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		el.setBright(1);

		verify(mockManager).send(Method.SET_BRIGHT, 1, EffectType.SMOOTH.getValue(), 100);
	}
	
	@DisplayName("Test set power")
	@Test
	public void testSetPower() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);
		
		when(mockManager.send(Method.SET_POWER, "On", EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		el.setPower(true);

		verify(mockManager).send(Method.SET_POWER, "On", EffectType.SMOOTH.getValue(), 100);
	}
	
	@DisplayName("Test set name")
	@Test
	public void testSetName() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);
		
		when(mockManager.send(Method.SET_NAME, "Name")).thenReturn(mFut);
		el.setName("Name");

		verify(mockManager).send(Method.SET_NAME, "Name");
	}
	
	@DisplayName("Test bg toggle method")
	@Test
	public void testEasyBgLightToggle() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		when(mockManager.send(Method.BG_TOGGLE)).thenReturn(mFut);
		el.bgToggle();

		verify(mockManager).send(Method.BG_TOGGLE);
	}
	
	@DisplayName("Test bg set color temperature")
	@Test
	public void testbgSetCtAbx() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.setBgCtAbx(65001);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.setBgCtAbx(1699);
		});
		when(mockManager.send(Method.BG_SET_CT_ABX, 1701, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		el.setBgCtAbx(1701);

		verify(mockManager).send(Method.BG_SET_CT_ABX, 1701, EffectType.SMOOTH.getValue(), 100);
	}
	
	@DisplayName("Test set background color")
	@Test
	public void testSetBgRgbWithValues() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		when(mockManager.send(Method.BG_SET_RGB, 16777215 , EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		el.setBgRgb(255, 255, 255);

		verify(mockManager).send(Method.BG_SET_RGB, 16777215 , EffectType.SMOOTH.getValue(), 100);
	}
	
	@DisplayName("Test set background hsv")
	@Test
	public void testSetBgHsv() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.setBgHsv(-1,1);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.setBgHsv(360,1);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.setBgHsv(1,-1);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.setBgHsv(1,101);
		});
		when(mockManager.send(Method.BG_SET_HSV, 1,1, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		el.setBgHsv(1,1);

		verify(mockManager).send(Method.BG_SET_HSV, 1,1, EffectType.SMOOTH.getValue(), 100);
	}
	
	@DisplayName("Test set background bright")
	@Test
	public void testSetBgBright() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.setBgBright(-1);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.setBgBright(101);
		});
		
		when(mockManager.send(Method.BG_SET_BRIGHT, 1, EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		el.setBgBright(1);

		verify(mockManager).send(Method.BG_SET_BRIGHT, 1, EffectType.SMOOTH.getValue(), 100);
	}
	
	@DisplayName("Test adjust brightness")
	@Test
	public void testAdjustBright() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.adjustBright(-101, 30);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.adjustBright(101, 30);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.adjustBright(100, 29);
		});
		
		when(mockManager.send(Method.ADJUST_BRIGHT, 100, 30)).thenReturn(mFut);
		el.adjustBright(100,30);

		verify(mockManager).send(Method.ADJUST_BRIGHT, 100, 30);
	}
	
	@DisplayName("Test adjust color temperature")
	@Test
	public void testAdjustCt() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.adjustColorTemperature(-101, 30);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.adjustColorTemperature(101, 30);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.adjustColorTemperature(100, 29);
		});
		
		when(mockManager.send(Method.ADJUST_CT, 100, 30)).thenReturn(mFut);
		el.adjustColorTemperature(100,30);

		verify(mockManager).send(Method.ADJUST_CT, 100, 30);
	}
	
	@DisplayName("Test adjust color")
	@Test
	public void testAdjustColor() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.adjustColor(-101, 30);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.adjustColor(101, 30);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.adjustColor(100, 29);
		});
		
		when(mockManager.send(Method.ADJUST_COLOR, 100, 30)).thenReturn(mFut);
		el.adjustColor(100,30);

		verify(mockManager).send(Method.ADJUST_COLOR, 100, 30);
	}
	
	@DisplayName("Test adjust background brightness")
	@Test
	public void testBgAdjustBright() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.bgAdjustBright(-101, 30);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.bgAdjustBright(101, 30);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.bgAdjustBright(100, 29);
		});
		
		when(mockManager.send(Method.BG_ADJUST_BRIGHT, 100, 30)).thenReturn(mFut);
		el.bgAdjustBright(100,30);

		verify(mockManager).send(Method.BG_ADJUST_BRIGHT, 100, 30);
	}
	
	@DisplayName("Test adjust background color temperature")
	@Test
	public void testBgAdjustCt() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.bgAdjustColorTemperature(-101, 30);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.bgAdjustColorTemperature(101, 30);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.bgAdjustColorTemperature(100, 29);
		});
		
		when(mockManager.send(Method.BG_ADJUST_CT, 100, 30)).thenReturn(mFut);
		el.bgAdjustColorTemperature(100,30);

		verify(mockManager).send(Method.BG_ADJUST_CT, 100, 30);
	}
	
	@DisplayName("Test adjust background color")
	@Test
	public void testBgAdjustColor() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.bgAdjustColor(-101, 30);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.bgAdjustColor(101, 30);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.bgAdjustColor(100, 29);
		});
		
		when(mockManager.send(Method.BG_ADJUST_COLOR, 100, 30)).thenReturn(mFut);
		el.bgAdjustColor(100,30);

		verify(mockManager).send(Method.BG_ADJUST_COLOR, 100, 30);
	}
	
	@DisplayName("Test set background power")
	@Test
	public void testSetBgPower() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);
		
		when(mockManager.send(Method.BG_SET_POWER, "Off", EffectType.SMOOTH.getValue(), 100)).thenReturn(mFut);
		el.setBgPower(false);

		verify(mockManager).send(Method.BG_SET_POWER, "Off", EffectType.SMOOTH.getValue(), 100);
	}
	
	@DisplayName("Test set adjust")
	@Test
	public void testSetAdjust() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.setAdjust(AdjustType.INCREASE,"color");
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.setAdjust(AdjustType.INCREASE,"fake");
		});
		
		when(mockManager.send(Method.SET_ADJUST, AdjustType.INCREASE.getValue(), "bright")).thenReturn(mFut);
		el.setAdjust(AdjustType.INCREASE, "bright");

		verify(mockManager).send(Method.SET_ADJUST, AdjustType.INCREASE.getValue(), "bright");
	}
	
	@DisplayName("Test set background adjust")
	@Test
	public void testSetBgAdjust() {
		Light l = new Light();
		MessageManager mockManager = mock(MessageManager.class);
		EasyLight el = new EasyLight(l, mockManager);

		assertThrows(IllegalArgumentException.class, ()->{
			el.setBgAdjust(AdjustType.INCREASE,"color");
		});
		assertThrows(IllegalArgumentException.class, ()->{
			el.setBgAdjust(AdjustType.INCREASE,"fake");
		});
		
		when(mockManager.send(Method.BG_SET_ADJUST, AdjustType.CIRCLE.getValue(), "color")).thenReturn(mFut);
		el.setBgAdjust(AdjustType.CIRCLE, "color");

		verify(mockManager).send(Method.BG_SET_ADJUST, AdjustType.CIRCLE.getValue(), "color");
	}
}
