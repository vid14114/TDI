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
	private Socket client; //we are a client, smartphone is the server
	private static ObjectOutputStream send;
	
	public Server(){
		try {
            //Created a new socket which is bound to the port 2345
            client = new Socket(ip, 2345);
            Thread t=new Thread(this);
            t.start();
    } catch (IOException e) {
    	System.out.print("Error, could not connect to port 2345");
    }
	}

	@Override
	public void run() {
		while(true){
            try{
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
	public static void sendCommand(ACTOData cmd) {
		try {
            send.writeObject(cmd);
	    } catch (IOException e) {
	            e.printStackTrace();
	    }
	}

	// ask pose update of all TUIOs
	// format: CMD
	// 51
	public static void fullPose()
	{
		ACTOData a=new ACTOData(ACTOConst.WI_FULL_POSE);
		sendCommand(a);
	}
	
	// ask only translation update of all TUIOs
	// format: CMD
	// 53
	public static void fullTrans()
	{
		ACTOData a=new ACTOData(ACTOConst.WI_FULL_TRANS);
		sendCommand(a);
	}
	
	// ask only rotation update of all TUIOs	
	// format: CMD
	// 54
	public static void fullRot()
	{
		ACTOData a=new ACTOData(ACTOConst.WI_FULL_ROT);
		sendCommand(a);
	}
	
	// ask pose update of specific TUIO
	// format: CMD, ID
	// 61
	public static void getPose(byte id)
	{
		ACTOData a=new ACTOData(ACTOConst.WI_GET_POSE, id);
		sendCommand(a);
	}
	
	// set/send state of specific TUIO
	// format: CMD, ID
	// 63
	public static void getTrans(byte id)
	{
		ACTOData a=new ACTOData(ACTOConst.WI_GET_TRANS, id);
		sendCommand(a);
	}
	
	// ask only rotation update of specific TUIO
	// format: CMD, ID
	// 64
	public static void getRot(byte id)
	{
		ACTOData a=new ACTOData(ACTOConst.WI_GET_ROT, id);
		sendCommand(a);
	}
	
	// ask resolution of playground	
	// format: CMD
	// 65
	public static void getPlsize()
	{
		ACTOData a=new ACTOData(ACTOConst.WI_GET_PLSIZE);
		sendCommand(a);
	}
	
	// set/send state of specific TUIO
	// format: CMD, ID, float[3], float[4], ???
	// 80
	public static void setState(byte id, float[] trans, float[] rot)
	{
		ACTOData a=new ACTOData(ACTOConst.WI_SET_STATE, id, trans, rot);
		sendCommand(a);
	}
	
	// set/send pose of specific TUIO	
	// format: CMD, ID, float[3], float[4]
	// 81
	public static void setPose(byte id, float[] trans, float[] rot)
	{
		ACTOData a=new ACTOData(ACTOConst.WI_SET_POSE, id, trans, rot);
		sendCommand(a);
	}
	
	// set/send ext state of specific TUIO	
	// format: CMD, ID, ???
	// 82
	public static void setExt(byte id)
	{
		ACTOData a=new ACTOData(ACTOConst.WI_SET_EXT, id);
		sendCommand(a);
	}
	
	// set/send only translation update of specific TUIO	
	// format: CMD, ID, float[3]
	// 83
	public static void setTrans(byte id, float[] rot)
	{
		ACTOData a=new ACTOData(ACTOConst.WI_SET_TRANS, id, rot);
		sendCommand(a);
	}
	
	// set/send only rotation update of specific TUIO	
	// format: CMD, ID, float[4]
	// 84
	public static void setRot(byte id, float[] trans)
	{
		ACTOData a=new ACTOData(ACTOConst.WI_SET_ROT, id, trans);
		sendCommand(a);
	}

	

}