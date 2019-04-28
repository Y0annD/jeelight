package fr.yoanndiquelou.yeelight;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.yoanndiquelou.jeelight.MessageManager;
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

		MessageManager m = new MessageManager(l, socketMock);

		try {
			m.send(Method.TOGGLE, new Object[0]);
		} catch (ExecutionException e) {
			fail("Should not catch exception", e);
		}
	}
}
