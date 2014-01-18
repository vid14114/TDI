package model;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.Test;

public class ServerTest implements Runnable {

	private ServerSocket server;

	@Test
	public void test() {
		try {
			server = new ServerSocket(2345);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Server s = new Server();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		assertTrue(true);
	}

}
