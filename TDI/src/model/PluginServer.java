package model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PluginServer implements Runnable {

	private ServerSocket server;
	private ArrayList<Socket> clients = new ArrayList<>();

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
		while(true){
			try {
				Socket socket = server.accept();
				socket.setKeepAlive(true);
				socket.setSoTimeout(0);
				clients.add(socket);
			} catch (IOException e) {
				//TODO Error message
			}
		}
	}

	/**
	 * TODO Figure out message format....
	 * @param message The message in bytes
	 */
	public void sendMessage(byte[] message) {
		for (Socket client : clients)
			try {
				client.getOutputStream().write(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	protected void finalize() {
		try {
			server.close();
			for(Socket client: clients)
				client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
