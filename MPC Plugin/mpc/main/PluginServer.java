package main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class PluginServer implements Runnable{

	
	private Socket client;
	private static DataOutputStream send;
	private static DataInputStream read;
	String ip ="127.0.0.1";
	MusicDialog m;
	byte[] by = new byte[4];

	public PluginServer(MusicDialog m){
		this.m = m;
		try {
			// Created a new socket which is bound to the port 12345
			client = new Socket(ip, 34000);
			client.setKeepAlive(true);
			client.setSoTimeout(0);
			send = new DataOutputStream(client.getOutputStream());
			read = new DataInputStream(new BufferedInputStream(client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


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
					
					read.skip(by.length*3);
					
					//Finished receiving
					m.TDIMoved(id,x,y,z);
				}
				
			}
		}catch(Exception e){
			return;
		}
	}
}
