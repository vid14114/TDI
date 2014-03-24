package model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class PluginServer implements Runnable {

    private final ArrayList<Socket> clients = new ArrayList<>();
    private ServerSocket server;

	public PluginServer() {
		try {
			server = new ServerSocket(34000, 50,
					InetAddress.getByName("127.0.0.1"));
			server.setSoTimeout(0);
		} catch (IOException e) {
			// TODO Error message
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket socket = server.accept();
				socket.setKeepAlive(true);
				socket.setSoTimeout(0);
				clients.add(socket);
			} catch (IOException e) {
				// TODO Error message
			}
		}
	}


	/**
	 * Sends data to the plugin clients in the following format:
	 * {byte[4],byte[4],byte[4],byte[4],byte[4],byte[4],byte[4]}
	 * The parameters are the byte arrays it sends to the clients
	 * @param id the id of the TDI
	 * @param x The x position
	 * @param y The y position
	 * @param z The z position
	 * @param rot The rotation, which is a float array of 3 angles
	 */
	public void sendMessage(float id, float x, float y, float z, float[] rot) {
		final byte[][] messages = {ByteBuffer.allocate(4).putFloat(id).array(), 
				ByteBuffer.allocate(4).putFloat(x).array(),
				ByteBuffer.allocate(4).putFloat(y).array(),
				ByteBuffer.allocate(4).putFloat(z).array(),
				ByteBuffer.allocate(4).putFloat(rot[0]).array(),
				ByteBuffer.allocate(4).putFloat(rot[1]).array(),
				ByteBuffer.allocate(4).putFloat(rot[2]).array(),};
		new Runnable() {			
			@Override
			public void run() {
				for (Socket client : clients)
					try {
						for(byte[] message : messages)
							client.getOutputStream().write(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}.run();
	}

	@Override
	protected void finalize() {
		try {
			server.close();
			for (Socket client : clients)
				client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
