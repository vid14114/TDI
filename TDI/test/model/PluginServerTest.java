/**
 * 
 */
package model;

import java.io.IOException;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

/**
 * @author abideen
 * 
 */
public class PluginServerTest {
	PluginServer ps = new PluginServer();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		new Thread(ps).start();
	}

	/**
	 * Test method for {@link model.PluginServer#sendMessage(java.lang.String)}.
	 */
	@Test
	public void testSendMessage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Socket s = new Socket("127.0.0.1", 34000);
					Socket s1 = new Socket("127.0.0.1", 34000);
					Socket s2 = new Socket("127.0.0.1", 34000);
					System.out.println(s.getInputStream().read());
					System.out.println(s.getInputStream().read());
					System.out.println(s.getInputStream().read());
					System.out.println(s.getInputStream().read());
					System.out.println(s.getInputStream().read());
					System.out.println(s.getInputStream().read());
					System.out.println(s1.getInputStream().read());
					System.out.println(s1.getInputStream().read());
					System.out.println(s1.getInputStream().read());
					System.out.println(s1.getInputStream().read());
					System.out.println(s1.getInputStream().read());
					System.out.println(s1.getInputStream().read());
					System.out.println(s2.getInputStream().read());
					System.out.println(s1.getInputStream().read());
					System.out.println(s2.getInputStream().read());
					s.close();
					s1.close();
					s2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		System.out.print("waiting");
		ps.sendMessage(new byte[] { 2, 3, 100, 23 });
		ps.sendMessage(new byte[] { 4, 10, 23 });
	}

}
