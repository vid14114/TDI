package model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import controller.Executor;

/**
 * Der {@link PluginServer} schickt Daten an Plugins die vom {@link Executor} gestartet wurden.
 * Der Server laeuft auf den localhost auf den Port 34000, und Plugins koennen sich darauf anmelden, um Positionsdate zu erhalten 
 * @author TDI Team
 *
 */
public class PluginServer implements Runnable {
	/**
	 * Eine {@link ArrayList} aller clients die verbunden
	 */
	private final ArrayList<Socket> clients = new ArrayList<>();
	/**
	 * Siehe {@link ServerSocket}
	 */
	private ServerSocket serverSocket;
	private Server server;
	
	/**
	 * Startet den server
	 */
	public PluginServer() {
		try {
			serverSocket = new ServerSocket(34000, 50,
					InetAddress.getByName("127.0.0.1"));
			serverSocket.setSoTimeout(0);
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
	}
	
	public void setServer(Server server){
		this.server = server;
	}

	@Override
	protected void finalize() {
		try {
			serverSocket.close();
			for (Socket client : clients)
				client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Eine endlos-Schleife der darauf wartet dass sich Clients verbinden. Diese Clients werden dann in die {@link #clients} Liste hinzugefuegt 
	 */
	@Override
	public void run() {
		while (true) {
			try {
				final Socket socket = serverSocket.accept();
				socket.setKeepAlive(true);
				socket.setSoTimeout(0);
				clients.add(socket);
				new Runnable() {
					
					@Override
					public void run() {
						while(socket.isConnected())
						{
							byte b[] = new byte[4];
							try {								
								byte id = (byte) socket.getInputStream().read();																
								
								socket.getInputStream().read(b);
								float x = ByteBuffer.wrap(b).getFloat();								
								socket.getInputStream().read(b);
								float y = ByteBuffer.wrap(b).getFloat();								
								socket.getInputStream().read(b);
								float z = ByteBuffer.wrap(b).getFloat();
								float[] pos = {x,y,z};
								
								socket.getInputStream().read(b);
								x = ByteBuffer.wrap(b).getFloat();								
								socket.getInputStream().read(b);
								y = ByteBuffer.wrap(b).getFloat();								
								socket.getInputStream().read(b);
								z = ByteBuffer.wrap(b).getFloat();
								float[] rot = {x,y,z};
								server.setPose(id, pos, rot);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						try {
							socket.close();
						} catch (IOException e) {}
					}
				}.run();
			} catch (IOException e) {
				TDILogger.logError(e.getMessage());
			}
		}
	}

	/**
	 * Sendet Daten an den Plugin Klienten im folgenden Format: 
	 * {byte[4],byte[4],byte[4],byte[4],byte[4],byte[4],byte[4]} 
	 * Parameter die geschickt werden sind:
	 * @param id
	 *            Die ID des TDIS
	 * @param pos
	 *            Die x Position
	 *            Die y Position
	 *            Die z Position
	 * @param rot
	 *            Die Rotation als Array der laenge 3
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
				for (Socket client : clients)
					try {
						for (byte[] message : messages)
							client.getOutputStream().write(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}.run();
	}

}
