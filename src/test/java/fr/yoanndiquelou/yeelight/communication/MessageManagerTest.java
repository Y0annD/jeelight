package fr.yoanndiquelou.yeelight.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.communication.MessageManager;
import fr.yoanndiquelou.jeelight.exception.ParameterException;
import fr.yoanndiquelou.jeelight.model.Light;
import fr.yoanndiquelou.jeelight.model.Method;

@DisplayName("Message manager test")
public class MessageManagerTest {

	/**
	 * Constructor.
	 */
	public MessageManagerTest() {
		super();
	}

	/**
	 * Test the senc dommand.
	 * 
	 * @throws IOException
	 */
	@Test
	@DisplayName("testSendCommand")
	public void testSendCommand() throws IOException {
		Light l = new Light();
		l.setIp("127.0.0.1");
		Socket socketMock = mock(Socket.class);
		byte[] buf = new byte[1024];
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
		when(socketMock.getOutputStream()).thenReturn(byteArrayOutputStream);
		when(socketMock.getInputStream()).thenReturn(byteArrayInputStream);
		try {
			MessageManager m = new MessageManager(l, socketMock);

			m.send(Method.TOGGLE, new Object[0]);
			assertEquals(1, m.getHistory().size());
			assertNull(m.getHistory().get(0).getV());
		} catch (ParameterException e) {
			fail("Should not catch exception", e);
		}
	}

	/**
	 * Test receive response command.
	 * 
	 * @throws IOException IOException
	 */
	@Test
	@DisplayName("testReceiveCommandResponse")
	public void testReceiveResponseCommand() throws IOException {
		Light l = new Light();
		l.setIp("127.0.0.1");
		Socket socketMock = mock(Socket.class);
		socketMock.setSoTimeout(10000);
		byte[] buf = new byte[1024];
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
		when(socketMock.getOutputStream()).thenReturn(byteArrayOutputStream);
		when(socketMock.getInputStream()).thenReturn(byteArrayInputStream);
		try {
			MessageManager m = new MessageManager(l, socketMock);

			Future<Boolean> result = m.send(Method.TOGGLE, new Object[0]);
			assertEquals(1, m.getHistory().size());
			assertNull(m.getHistory().get(0).getV());
			socketMock.getOutputStream().write("{\"id\":1, \"result\":[\"ok\"]}\r\n".getBytes());
			socketMock.getOutputStream().flush();
			java.lang.reflect.Method presponse = MessageManager.class.getDeclaredMethod("processResponse", String.class);
			presponse.setAccessible(true);
			presponse.invoke(m, "{\"id\":1, \"result\":[\"ok\"]}");
			while(!result.isDone()) {}
			assertTrue(result.get());
		} catch (ParameterException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InterruptedException | ExecutionException e) {
			fail("Should not catch exception", e);
		}
	}
	
	/**
	 * Test receive response command.
	 * 
	 * @throws IOException IOException
	 */
	@Test
	@DisplayName("testReceiveNotificationResponse")
	public void testReceiveResponseNotification() throws IOException {
		Light l = new Light();
		l.setIp("127.0.0.1");
		Socket socketMock = mock(Socket.class);
		socketMock.setSoTimeout(10000);
		byte[] buf = new byte[1024];
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
		when(socketMock.getOutputStream()).thenReturn(byteArrayOutputStream);
		when(socketMock.getInputStream()).thenReturn(byteArrayInputStream);
		l.addListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(null != evt) {
					assertEquals("power", evt.getPropertyName());
					assertEquals(false, evt.getOldValue());
					assertEquals(true, evt.getNewValue());
					l.removeListener(this);
				}
				
			}
		});
		try {
			MessageManager m = new MessageManager(l, socketMock);

			java.lang.reflect.Method presponse = MessageManager.class.getDeclaredMethod("processResponse", String.class);
			presponse.setAccessible(true);
			presponse.invoke(m, "{\"method\":\"props\",\"params\":{\"power\":\"on\"}}");
			assertTrue(l.isPower());
		} catch (ParameterException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			fail("Should not catch exception", e);
		}
	}
	
	/**
	 * Test receive response command.
	 * 
	 * @throws IOException IOException
	 */
	@Test
	@DisplayName("testReceiveNotificationBadResponse")
	public void testReceiveResponseBadNotification() throws IOException {
		Light l = new Light();
		l.setIp("127.0.0.1");
		Socket socketMock = mock(Socket.class);
		socketMock.setSoTimeout(10000);
		byte[] buf = new byte[1024];
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
		when(socketMock.getOutputStream()).thenReturn(byteArrayOutputStream);
		when(socketMock.getInputStream()).thenReturn(byteArrayInputStream);
		try {
			MessageManager m = new MessageManager(l, socketMock);

			java.lang.reflect.Method presponse = MessageManager.class.getDeclaredMethod("processResponse", String.class);
			presponse.setAccessible(true);
			presponse.invoke(m, "{\"method\":\"props\",\"params\":{\"power\":\"no\"}}");
			assertFalse(l.isPower());

		} catch (ParameterException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			fail("Should not catch exception", e);
		}
	}

}
