package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Implements the runnable interface
 * 
 * RUN METHOD CALLS ADD METHOD FROM BIGLOGIC;IS MASTER, IS BIG. IS SLIGHTLY
 * SMALLER, IS NOT SO BIG.
 */
public class Server {

	/**
	 * 
	 * ip
	 */
	protected static String ip;
	private Socket client; // we are a client, smartphone is the server
	private static ObjectOutputStream send;
	private static ObjectInputStream read;
	public boolean forwarding=false; //if the inputs are forwarded to a plugin

	public Server() {
		try {
			// Created a new socket which is bound to the port 2345
			client = new Socket(ip, 2345);
			client.setKeepAlive(true);
			send = new ObjectOutputStream(client.getOutputStream());
			read = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			System.out.print("Error, could not connect to port 2345");
		}
	}

	public static void fullPose() {
		try {
			send.writeByte(ACTOConst.WI_FULL_POSE);
			byte ack = 0;
			while (ack != ACTOConst.WI_ACK) {
				ack = read.readByte();
				// TODO handle input
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void fullTrans() {
		try {
			send.writeByte(ACTOConst.WI_FULL_TRANS);
			byte ack = 0;
			while (ack != ACTOConst.WI_ACK) {
				ack = read.readByte();
				// TODO handle input
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void fullRot() {
		try {
			send.writeByte(ACTOConst.WI_FULL_ROT);
			byte ack = 0;
			while (ack != ACTOConst.WI_ACK) {
				ack = read.readByte();
				// TODO handle input
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getPose(byte id) {
		try {
			send.writeByte(ACTOConst.WI_GET_POSE);
			send.writeByte(id);
			byte ack = 0;
			while (ack != ACTOConst.WI_ACK) {
				ack = read.readByte();
				// TODO handle input
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getTrans(byte id) {
		try {
			send.writeByte(ACTOConst.WI_GET_TRANS);
			send.writeByte(id);
			byte ack = 0;
			while (ack != ACTOConst.WI_ACK) {
				ack = read.readByte();
				// TODO handle input
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getRot(byte id) {
		try {
			send.writeByte(ACTOConst.WI_GET_ROT);
			send.writeByte(id);
			byte ack = 0;
			while (ack != ACTOConst.WI_ACK) {
				ack = read.readByte();
				// TODO handle input
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getPlsize() {
		try {
			send.writeByte(ACTOConst.WI_GET_PLSIZE);
			byte ack = 0;
			while (ack != ACTOConst.WI_ACK) {
				ack = read.readByte();
				// TODO handle input
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void setPose(byte id, float[] trans, float[] rot) {
		try {
			send.writeByte(ACTOConst.WI_SET_POSE);
			send.writeByte(id);
			send.writeFloat(trans[0]);
			send.writeFloat(trans[1]);
			send.writeFloat(trans[2]);
			send.writeFloat(rot[0]);
			send.writeFloat(rot[1]);
			send.writeFloat(rot[2]);
			send.writeFloat(rot[3]);
			byte ack = 0;
			while (ack != ACTOConst.WI_ACK) {
				ack = read.readByte();
				// TODO handle input
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setExt(byte id) { // Not yet implemented in
											// WifiTransmitter
		try {
			send.writeByte(ACTOConst.WI_SET_EXT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// set/send only translation update of specific TUIO
	// format: CMD, ID, float[3]
	// 83
	public static void setTrans(byte id, float[] trans) {
		try {
			send.writeByte(ACTOConst.WI_SET_TRANS);
			send.writeByte(id);
			send.writeFloat(trans[0]);
			send.writeFloat(trans[1]);
			send.writeFloat(trans[2]);
			byte ack = 0;
			while (ack != ACTOConst.WI_ACK) {
				ack = read.readByte();
				// TODO handle input
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setRot(byte id, float[] rot) {
		try {
			send.writeByte(ACTOConst.WI_SET_ROT);
			send.writeByte(id);
			send.writeFloat(rot[0]);
			send.writeFloat(rot[1]);
			send.writeFloat(rot[2]);
			send.writeFloat(rot[3]);
			byte ack = 0;
			while (ack != ACTOConst.WI_ACK) {
				ack = read.readByte();
				// TODO handle input
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}