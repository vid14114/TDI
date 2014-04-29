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
		} catch (final IOException e) {
			TDILogger.logError(e.getMessage());
		}
	}

	@Override
	protected void finalize() {
		try {
			server.close();
			for (final Socket client : clients) {
				client.close();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				final Socket socket = server.accept();
				socket.setKeepAlive(true);
				socket.setSoTimeout(0);
				clients.add(socket);
			} catch (final IOException e) {
				TDILogger.logError(e.getMessage());
			}
		}
	}

	/**
	 * Sends data to the plugin clients in the following format:
	 * {byte[4],byte[4],byte[4],byte[4],byte[4],byte[4],byte[4]} The parameters
	 * are the byte arrays it sends to the clients
	 * 
	 * @param id
	 *            the id of the TDI
	 * @param x
	 *            The x position
	 * @param y
	 *            The y position
	 * @param z
	 *            The z position
	 * @param rot
	 *            The rotation, which is a float array of 3 angles
	 */
	public void sendMessage(float id, float[] pos, float[] rot) {
		final byte[][] messages = {
				ByteBuffer.allocate(4).putFloat(id).array(),
				ByteBuffer.allocate(4).putFloat(pos[0]).array(),
				ByteBuffer.allocate(4).putFloat(pos[1]).array(),
				ByteBuffer.allocate(4).putFloat(pos[2]).array(),
				ByteBuffer.allocate(4).putFloat(rot[0]).array(),
				ByteBuffer.allocate(4).putFloat(rot[1]).array(),
				ByteBuffer.allocate(4).putFloat(rot[2]).array(), };
		new Runnable() {
			@Override
			public void run() {
				for (final Socket client : clients) {
					try {
						for (final byte[] message : messages) {
							client.getOutputStream().write(message);
						}
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.run();
	}

}
