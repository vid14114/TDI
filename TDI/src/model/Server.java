package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import view.TDI;

/**
 * Implements the runnable interface
 */
public class Server {
	protected static String ip = "127.0.0.1";
	private Socket client;
	private static DataOutputStream send;
	private static DataInputStream read;
	public boolean forwarding = false; // if the inputs are forwarded to a
										// plugin

	public Server() {
		try {
			// Created a new socket which is bound to the port 12345
			client = new Socket(ip, 12435);
			client.setKeepAlive(true);
			send = new DataOutputStream(client.getOutputStream());
			read = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			System.out.print("Error, could not connect to port 12435 ");
			e.printStackTrace();
		}
	}

	public ArrayList<TDI> fullPose() {
		ArrayList<TDI> tdis = new ArrayList<TDI>();
		try {
			send.writeByte(ACTOConst.WI_FULL_POSE);
			read.readByte();
			byte ack = 0;
			ack = read.readByte();
			while (ack != ACTOConst.WI_ACK) {
				float x = read.readFloat();
				float y = read.readFloat();
				float z = read.readFloat();
				float q1 = read.readFloat();
				float q2 = read.readFloat();
				float q3 = read.readFloat();
				float q4 = read.readFloat();
				float[] rot = quat2Deg(q1, q2, q3, q4);
				TDI t = new TDI(ack, x, y, z, rot);
				tdis.add(t);
				ack = read.readByte();
			}
			return tdis;
		} catch (IOException e) {
			return null;
		}
	}

	public void setPose(byte id, float[] trans, float[] rot) {
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

	public void setExt(byte id) { // Not yet implemented in
									// WifiTransmitter
		try {
			send.writeByte(ACTOConst.WI_SET_EXT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setTrans(byte id, float[] trans) {
		try {
			send.writeByte(ACTOConst.WI_SET_TRANS);
			send.writeByte(id);
			send.writeFloat(trans[0]);
			send.writeFloat(trans[1]);
			send.writeFloat(trans[2]);
			byte ack = 0;
			ack = read.readByte();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setRot(byte id, float[] rot) {
		try {
			send.writeByte(ACTOConst.WI_SET_ROT);
			send.writeByte(id);
			send.writeFloat(rot[0]);
			send.writeFloat(rot[1]);
			send.writeFloat(rot[2]);
			send.writeFloat(rot[3]);
			byte ack = 0;
			ack = read.readByte();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public float[] quat2Deg(float w, float x, float y, float z) {
		double angle = 2 * Math.acos(w);
		double s = Math.sqrt(1 - w * w);
		if (s >= 0.001) {
			x /= s;
			y /= s;
			z /= s;
		}
		float[] ret = { x, y, z };
		return ret;
	}
}