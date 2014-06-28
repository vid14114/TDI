/**
 *
 */
package model;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

/**
 * @author abideen
 */
public class PluginServerTest {
	private final PluginServer ps = new PluginServer();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		new Thread(ps).start();
	}

	/**
	 * Test method for {@link model.PluginServer#sendMessage(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSendMessage() throws IOException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Socket s = new Socket("127.0.0.1", 34000);
					Socket s1 = new Socket("127.0.0.1", 34000);
					Socket s2 = new Socket("127.0.0.1", 34000);
					byte[] b = new byte[4];
					while (s.getInputStream().available() > 1) {
						s.getInputStream().read(b);
						System.out.println(ByteBuffer.wrap(b).getFloat());
					}
					while (s1.getInputStream().available() > 1) {
						s1.getInputStream().read(b);
						System.out.println(ByteBuffer.wrap(b).getFloat());
					}
					while (s2.getInputStream().available() > 1) {
						s2.getInputStream().read(b);
						System.out.println(ByteBuffer.wrap(b).getFloat());
					}
					s.close();
					s1.close();
					s2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		ps.sendMessage(123, new float[] { 1231, 213131, 312313 });
		ps.sendMessage(123, new float[] { 234f, 243f, 242f });
		System.in.read();

	}

}
