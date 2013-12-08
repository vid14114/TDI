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
                            case "51": fullPose(); break;
                            case "53": fullTrans(); break;
                            case "54": fullRot(); break;
                            case "61": getPose(); break;
                            case "63": getTrans(); break;
                            case "64": getRot(); break;
                            case "65": getPlsize(); break;
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
	

	// ask pose update of all TUIOs
	// format: CMD
	public static byte fullPose(){
		return 51;
	}
	
	// ask only translation update of all TUIOs
	// format: CMD
	public static byte fullTrans(){
		return 53;
	}
	
	// ask only rotation update of all TUIOs	
	// format: CMD
	public static byte fullRot(){
		return 54;
	}
	
	// ask pose update of specific TUIO
	// format: CMD
	public static byte getPose(){
		return 61;
	}
	
	// set/send state of specific TUIO
	// format: CMD
	public static byte getTrans()
	{
		return 63;
	}
	
	// ask only rotation update of specific TUIO
	// format: CMD
	public static byte getRot()
	{
		return 64;
	}
	
	// ask resolution of playground	
	// format: CMD
	public static byte getPlsize()
	{
		return 65;
	}
	
	// set/send state of specific TUIO
	// format: CMD, ID, float[3], float[4], ???
	public static byte setState()
	{
		return 80;
	}
	
	// set/send pose of specific TUIO	
	// format: CMD, ID, float[3], float[4]
	public static byte setPose()
	{
		return 81;
	}
	
	// set/send ext state of specific TUIO	
	// format: CMD, ID, ???
	public static byte setExt()
	{
		return 82;
	}
	
	// set/send only translation update of specific TUIO	
	// format: CMD, ID, float[3]
	public static byte setTrans()
	{
		return 83;
	}
	
	// set/send only rotation update of specific TUIO	
	// format: CMD, ID, float[4]
	public static byte setRot()
	{
		return 84;
	}

	

}
