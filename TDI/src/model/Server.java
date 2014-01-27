package model;

import java.io.BufferedInputStream;
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
			read = new DataInputStream(new BufferedInputStream(client.getInputStream()));
		} catch (IOException e) {
			System.out.print("Error, could not connect to port 12435 ");
			e.printStackTrace();
		}
	}

	public ArrayList<TDI> fullPose() {
		ArrayList<TDI> tdis = new ArrayList<TDI>();
		try {
			send.writeByte(ACTOConst.WI_FULL_POSE);
			byte ack=read.readByte();
			read.mark(9);
			byte wi_msg = read.readByte();
			read.reset();
			while (read.available()>0 && read.readByte() == wi_msg) {
				byte id = read.readByte();
				float x = read.readFloat();
				float y = read.readFloat();
				float z = read.readFloat();
				float q1 = read.readFloat();
				float q2 = read.readFloat();
				float q3 = read.readFloat();
				float q4 = read.readFloat();
				Quaternion q=new Quaternion(q1, q2, q3, q4);
				float[] rot=quat2Euler(q.w, q.x, q.y, q.z);
				TDI t = new TDI(id, x, y, z, rot);
				tdis.add(t);
				read.mark(9);
			}
			read.reset();
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
	
	public float[] quat2Euler(float w, float x, float y, float z)
	{
		double heading, attitude, bank;
		double sqw = w*w;
	    double sqx = x*x;
	    double sqy = y*y;
	    double sqz = z*z;
		double unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise is correction factor
		double test = x*y + z*w;
		if (test > 0.499*unit) { // singularity at north pole
			heading = 2 * Math.atan2(x,w);
			attitude = Math.PI/2;
			bank = 0;
			float[] ret={(float) heading, (float) attitude, (float) bank};
			return ret;
		}
		if (test < -0.499*unit) { // singularity at south pole
			heading = -2 * Math.atan2(x,w);
			attitude = -Math.PI/2;
			bank = 0;
			float[] ret={(float) heading, (float) attitude, (float) bank};
			return ret;
		}
	    heading = Math.atan2(2*y*w-2*x*z , sqx - sqy - sqz + sqw);
		attitude = Math.asin(2*test/unit);
		bank = Math.atan2(2*x*w-2*y*z , -sqx + sqy - sqz + sqw);
		float[] ret={(float) heading, (float) attitude, (float) bank};
		return ret;
	}
}