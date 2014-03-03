package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PluginServer implements Runnable{
	String hostName = "127.0.0.1";
	int portNumber = 34000;
	Socket kkSocket;
	PrintWriter out;
	BufferedReader in;

	public PluginServer(){
		try {
			kkSocket = new Socket(hostName, portNumber);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		String fromServer;
		try {
			fromServer = in.readLine();
			if(fromServer != null){
			    System.out.println("Server: " + fromServer);
			    if (fromServer.equals("x")){
			    	
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
