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
					while(s.getInputStream().available() > 1){
						s.getInputStream().read(b);
						System.out.println(ByteBuffer.wrap(b).getFloat());
					}	
					while(s1.getInputStream().available() > 1){
						s1.getInputStream().read(b);
						System.out.println(ByteBuffer.wrap(b).getFloat());
					}	
					while(s2.getInputStream().available() > 1){
						s2.getInputStream().read(b);
						System.out.println(ByteBuffer.wrap(b).getFloat());
					}	
					s.close();
					s1.close();
					s2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		System.out.println("waiting");
		ps.sendMessage(123,1231,213131,312313,new float[]{132123,12342,123412});
		ps.sendMessage(223f,234f,243f,242f,new float[]{13234.78f,123.25f,234.3f});
		System.in.read();
		
	}

}
