package main;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Der PluginServer kommuniziert mit dem TDI Programm
 * 
 * @author TDI Team
 */

public class PluginServer implements Runnable{

	/**
	 * Der Client
	 */
	private Socket client;
	
	/**
	 * Das was empfangen wird
	 */
	private static DataInputStream read;
	
	/**
	 * Die ip
	 */
	String ip ="127.0.0.1";
	
	/**
	 * Der Dialog
	 */
	MusicDialog m;
	byte[] by = new byte[4];

	/**
	 * Konstruktor des PluginServers
	 * @param m der MusicDialog
	 */
	public PluginServer(MusicDialog m){
		this.m = m;
		try {
			// Created a new socket which is bound to the port 12345
			client = new Socket(ip, 34000);
			client.setKeepAlive(true);
			client.setSoTimeout(0);
			read = new DataInputStream(new BufferedInputStream(client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Empfaengt permanent Informationen und gibt sie dem Dialog weiter
	 */
	@Override
	public void run() {
		try{
			while (true)
			{
				//Receiving Data
				while(read.available() > 0){
					read.read(by);
					float id = ByteBuffer.wrap(by).getFloat();
					
					read.read(by);
					float x = ByteBuffer.wrap(by).getFloat();
					
					read.read(by);
					float y = ByteBuffer.wrap(by).getFloat();
					
					read.read(by);
					float z = ByteBuffer.wrap(by).getFloat();								
					
					//Finished receiving
					if(m.TDIMoved(id,x,y,z))
						sendMessage(m.getId(), new float[]{m.getXPos(), m.getYPos(), m.getZPos()});
				}
				
			}
		}catch(Exception e){
			return;
		}
	}
	
	public void sendMessage(float id, float[] pos) {
		final byte[][] messages = {
				ByteBuffer.allocate(4).putFloat(id).array(), 
				ByteBuffer.allocate(4).putFloat(pos[0]).array(),
				ByteBuffer.allocate(4).putFloat(pos[1]).array(),
				ByteBuffer.allocate(4).putFloat(pos[2]).array()};
		new Runnable() {			
			@Override
			public void run() {
					try {
						for(byte[] message : messages)
							client.getOutputStream().write(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}.run();
	}
}
