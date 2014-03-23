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

public class PluginServer implements Runnable{

	
	private Socket client;
	private static DataOutputStream send;
	private static DataInputStream read;
	String ip ="127.0.0.1";
	MusicDialog m;

	public PluginServer(MusicDialog m){
		this.m = m;
		try {
			// Created a new socket which is bound to the port 12345
			client = new Socket(ip, 34000);
			client.setKeepAlive(true);
			send = new DataOutputStream(client.getOutputStream());
			read = new DataInputStream(new BufferedInputStream(client.getInputStream()));
		} catch (IOException e) {
			System.out.print("Error, could not connect to port 12435 ");
			e.printStackTrace();
		}

	}


	@Override
	public void run() {
		try{
			while (read.available()>0)
			{
				float id=read.readByte();
				float x = read.readFloat();
				float y = read.readFloat();
				float z = read.readFloat();
				m.TDIMoved(id,x,y,z);
				read.mark(4);
			}
			read.reset();
		}catch(Exception e){
			
		}
	}
}
