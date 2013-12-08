package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Implements the runnable interface
 * 
 * RUN METHOD CALLS ADD METHOD FROM BIGLOGIC;IS MASTER, IS BIG.
 * IS SLIGHTLY SMALLER, IS NOT SO BIG.
 */
public class Server implements Runnable{

	/**
	 * 
	 * ip
	 */
	protected static String ip;
	private ServerSocket server;
	private ObjectOutputStream send;
	
	public Server(){
		try {
            //Created a new serversocket instance, which is bound to the port 1234
            server = new ServerSocket(2345);
    } catch (IOException e) {
    	System.out.print("Error, could not connect to port 2345");
    }
	}

	/**
	 * Overrides the run method of the runnable interface
	 */
	public void run() {
		while(true){
            try{
                    Socket client = null;
                    while(client == null || client.isClosed()){
                            client = server.accept();
                    }
                    client.setKeepAlive(true);
                    ObjectInputStream read = new ObjectInputStream(client.getInputStream());
                    send = new ObjectOutputStream(client.getOutputStream());                                
                    while(client.isConnected()){ //Here the whole message handling is done
                            String[] message = ((String)read.readObject()).split(";"); //For some reasons, only when the objectoutputstream sends messages, can they be read by the server
                            switch(message[0].toLowerCase()){
                            case "51": break;
                            case "53": break;
                            case "54": break;
                            case "61": break;
                            case "63": sendWI_GET_TRANS(); break;
                            case "64": sendWI_GET_PLSIZE(); break;
                            case "65": sendWI_GET_PLSIZE(); break;
                            case "80": sendWI_SET_STATE(); break;
                            case "81": break;
                            case "82": break;
                            case "83": break;
                            case "84": break;
                            default: send.writeObject("Unknown command"); break;
                            }                                
                    }
            }
            catch(IOException e){
                    e.printStackTrace();
                    System.out.println(e.getCause().getMessage());
            } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println(e.getCause().getMessage());
            }
    }
	}

	/**
	 * Sends the specified command
	 */
	public static void sendCommand() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sends command to external program
	 * @param externalCommand
	 */
	public static void sendExternalCommand(String externalCommand) {
		throw new UnsupportedOperationException();
	}
	

	// ask only translation update of specific TUIO	
	public static byte sendWI_GET_TRANS() 
	{
		return 63;
	}
	// ask only rotation update of specific TUIO
	public static byte sendWI_GET_ROT()
	{
		return 64;
	}
	// ask resolution of playground //also answer to GET messages
	public static byte sendWI_GET_PLSIZE()
	{
		return 65;
	}
	// set/send state of specific TUIO
	public static byte sendWI_SET_STATE()
	{
		return 80;
	}
	

}
