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
	private static ObjectOutputStream send;
	
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
                            case "LULZ": System.out.println("Lulz was sent"); break;
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
	public static void sendCommand(String cmd) {
		try {
            send.writeObject(cmd);
	    } catch (IOException e) {
	            e.printStackTrace();
	    }
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
	// 51
	public static void fullPose()
	{
		sendCommand("51");
	}
	
	// ask only translation update of all TUIOs
	// format: CMD
	// 53
	public static void fullTrans()
	{
		sendCommand("53");
	}
	
	// ask only rotation update of all TUIOs	
	// format: CMD
	// 54
	public static void fullRot()
	{
		sendCommand("54");
	}
	
	// ask pose update of specific TUIO
	// format: CMD, ID
	// 61
	public static void getPose(String id)
	{
		sendCommand("61, "+id);
	}
	
	// set/send state of specific TUIO
	// format: CMD, ID
	// 63
	public static void getTrans(String id)
	{
		sendCommand("63, "+id);
	}
	
	// ask only rotation update of specific TUIO
	// format: CMD, ID
	// 64
	public static void getRot(String id)
	{
		sendCommand("64, "+id);
	}
	
	// ask resolution of playground	
	// format: CMD
	// 65
	public static void getPlsize()
	{
		sendCommand("65");
	}
	
	// set/send state of specific TUIO
	// format: CMD, ID, float[3], float[4], ???
	// 80
	public static void setState(String id, float[] trans, float[] rot)
	{
		sendCommand("80, "+id+", "+trans+", "+rot);
	}
	
	// set/send pose of specific TUIO	
	// format: CMD, ID, float[3], float[4]
	// 81
	public static void setPose(String id, float[] trans, float[] rot)
	{
		sendCommand("80, "+id+", "+trans+", "+rot);
	}
	
	// set/send ext state of specific TUIO	
	// format: CMD, ID, ???
	// 82
	public static void setExt(String id)
	{
		sendCommand("82, "+id);
	}
	
	// set/send only translation update of specific TUIO	
	// format: CMD, ID, float[3]
	// 83
	public static void setTrans(String id, float[] rot)
	{
		sendCommand("80, "+id+", "+rot);
	}
	
	// set/send only rotation update of specific TUIO	
	// format: CMD, ID, float[4]
	// 84
	public static void setRot(String id, float[] trans)
	{
		sendCommand("80, "+id+", "+trans);
	}

	

}
