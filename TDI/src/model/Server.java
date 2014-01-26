package model;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.Socket;
import java.util.ArrayList;
import toxi.geom.Quaternion;
import view.TDI;

/**
 * Implements the runnable interface
 */
public class Server {
	protected static String ip = "192.168.1.36";
	private Socket client;
	private static DataOutputStream send;
	private static DataInputStream read;
	private static PushbackInputStream push;
	public boolean forwarding = false; // if the inputs are forwarded to a
										// plugin

	public Server() {
		try {
			// Created a new socket which is bound to the port 12345
			client = new Socket(ip, 12435);
			client.setKeepAlive(true);
			send = new DataOutputStream(client.getOutputStream());
			read = new DataInputStream(client.getInputStream());
			push = new PushbackInputStream(read);
		} catch (IOException e) {
			System.out.print("Error, could not connect to port 12435 ");
			e.printStackTrace();
		}
	}

	public ArrayList<TDI> fullPose() {
		ArrayList<TDI> tdis = new ArrayList<TDI>();
		try {
			byte tmp;
			send.writeByte(ACTOConst.WI_FULL_POSE);
			byte wi_msg = read.readByte();
			push.unread(wi_msg);
			while ((tmp = read.readByte()) == wi_msg) {
				byte id = read.readByte();
				float x = read.readFloat();
				float y = read.readFloat();
				float z = read.readFloat();
				float q1 = read.readFloat();
				float q2 = read.readFloat();
				float q3 = read.readFloat();
				float q4 = read.readFloat();
				Quaternion q = new Quaternion(q1, q2, q3, q4);
				TDI t = new TDI(id, x, y, z, q.toAxisAngle());
				tdis.add(t);
			}
			push.unread(tmp);
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
			byte ack = read.readByte();
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
		Quaternion q = Quaternion.createFromEuler(rot[0], rot[1], rot[2]);
		try {
			send.writeByte(ACTOConst.WI_SET_ROT);
			send.writeByte(id);
			send.writeFloat(q.w);
			send.writeFloat(q.x);
			send.writeFloat(q.y);
			send.writeFloat(q.z);
			byte ack = 0;
			ack = read.readByte();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}