package model;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import controller.BigLogic;
import view.TDI;

/**
 * {@link Server} bietet die Schnittstelle zwischen unserem Programm und das Trackingsystem.
 * Der Server schickt Anfragen und auch Befehle an das Tackingsystem und bekommt je nachdem Informationen zurueck
 */
public class Server {
	private static DataOutputStream send;
	private static DataInputStream read;
	Socket client;

	/**
	 * Startet den Server und verbindet sich zum Trackingsystem
	 * @param ip Die IP adresse des Trackingsystems
	 */
	public Server(String ip) {
		try {
			// Created a new socket which is bound to the port 12345
			client = new Socket(ip, 12435);
			client.setKeepAlive(true);
			send = new DataOutputStream(client.getOutputStream());
			read = new DataInputStream(new BufferedInputStream(
					client.getInputStream()));
		} catch (IOException e) {
			TDILogger.logError("Error, could not connect to port 12435 "
					+ e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		client.close();
	}

	/**
	 * Fordert die Information aller TDIs an, die das Trackingsystem moment sieht
	 * @return Die erhaltene aenderungen als eine {@link ArrayList} von {@link TDI}
	 */
	public ArrayList<TDI> fullPose() {
		ArrayList<TDI> tdis = new ArrayList<TDI>();
		try {
			// send.writeByte(ACTOConst.WI_FULL_POSE);
			send.writeByte(ACTOConst.WI_GET_POSE);
			send.writeByte(49); // TUIO 1
			read.readByte();
			read.mark(9);
			byte wi_msg = read.readByte();
			read.reset();
			while (read.available() > 0 && read.readByte() == wi_msg) {
				byte id = read.readByte();
				float x = read.readFloat();
				float y = read.readFloat();
				float z = read.readFloat();
				float q1 = read.readFloat();
				float q2 = read.readFloat();
				float q3 = read.readFloat();
				float q4 = read.readFloat();
				Rotation r = new Rotation(q4, q1, q2, q3, true);
				double angles[] = r.getAngles(RotationOrder.XYZ);
				float[] rot = new float[3];
				rot[0] = (float) ((float) angles[0] * 180 / Math.PI);
				rot[1] = (float) ((float) angles[1] * 180 / Math.PI);
				rot[2] = (float) ((float) angles[2] * 180 / Math.PI);
				if (x != 0) {
					TDI t = new TDI(id, x, y, z, rot);
					tdis.add(t);
				}
				read.mark(9);
			}
			read.reset();

			send.writeByte(ACTOConst.WI_GET_POSE);
			send.writeByte(50); // TUIO 2
			read.readByte();
			read.mark(9);
			wi_msg = read.readByte();
			read.reset();
			while (read.available() > 0 && read.readByte() == wi_msg) {
				byte id = read.readByte();
				float x = read.readFloat();
				float y = read.readFloat();
				float z = read.readFloat();
				float q1 = read.readFloat();
				float q2 = read.readFloat();
				float q3 = read.readFloat();
				float q4 = read.readFloat();
				Rotation r = new Rotation(q4, q1, q2, q3, true);
				double angles[] = r.getAngles(RotationOrder.XYZ);
				float[] rot = new float[3];
				rot[0] = (float) ((float) angles[0] * 180 / Math.PI);
				rot[1] = (float) ((float) angles[1] * 180 / Math.PI);
				rot[2] = (float) ((float) angles[2] * 180 / Math.PI);
				if (x != 0) {
					TDI t = new TDI(id, x, y, z, rot);
					tdis.add(t);
				}
				read.mark(9);
			}
			read.reset();
			return tdis;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Setzt das Playground auf size 
	 * @param size
	 */
	public void setPlSize(int size[]) {
		try {
			send.writeByte(ACTOConst.WI_SET_PLSIZE);
			send.writeInt(size[0]);
			send.writeInt(size[1]);
			send.writeInt(size[2]);
			read.readByte();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Setzt alle Daten des ausgewoehlten TDIs
	 * @param id Die ID des TDIs
	 * @param pos Die neue Position
	 * @param rot Die neue Rotation
	 */
	public void setPose(byte id, float[] pos, float[] rot) {
		try {
			send.writeByte(ACTOConst.WI_SET_POSE);
			send.writeByte(id);
			send.writeFloat(pos[0]);
			send.writeFloat(pos[1]);
			send.writeFloat(pos[2]);
			Rotation r = new Rotation(RotationOrder.XYZ, rot[0], rot[1], rot[2]);
			send.writeFloat((float) r.getQ3());
			send.writeFloat((float) r.getQ0());
			send.writeFloat((float) r.getQ1());
			send.writeFloat((float) r.getQ2());
			read.readByte();
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
	}

	/** 
	 * Setzt nur die Rotation des TDIs
	 * @param id Die ID des TDIs
	 * @param rot Die neue Rotation
	 */
	public void setRot(byte id, float[] rot) {
		Vector3D v = new Vector3D(rot[0], rot[1], rot[2]);
		Quaternion q = new Quaternion(v.toArray());
		try {
			send.writeByte(ACTOConst.WI_SET_ROT);
			send.writeByte(id);
			send.writeFloat((float) q.getQ0());
			send.writeFloat((float) q.getQ1());
			send.writeFloat((float) q.getQ2());
			send.writeFloat((float) q.getQ3());
			read.readByte();
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
	}

	/**
	 * Setzt nur die Position des ausgewoehlten TDIs
	 * @param id Die ID des TDIs
	 * @param trans Die neue Position
	 */
	public void setTrans(byte id, float[] trans) {
		try {
			send.writeByte(ACTOConst.WI_SET_TRANS);
			send.writeByte(id);
			send.writeFloat(trans[0]);
			send.writeFloat(trans[1]);
			send.writeFloat(trans[2]);
			read.readByte();
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
	}

	/**
	 * Schaltet das Gruene LED des TDIs an
	 * @param id ID 
	 * @param state > 0 --> an, sonst aus
	 */
	public void toggleGreenLED(byte id, byte state) {
		try {
			send.writeByte(ACTOConst.WI_SET_EXT);
			send.writeByte(ACTOConst.USB_SET_DI_UNIT);
			send.writeByte(id);
			send.writeByte((byte) 0);
			send.writeByte(state);
			read.readByte();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Schaltet das Rote LED des TDIs an
	 * @param id ID 
	 * @param state > 0 --> an, sonst aus
	 */
	public void toggleRedLED(byte id, byte state) {
		try {
			send.writeByte(ACTOConst.WI_SET_EXT);
			send.writeByte(ACTOConst.USB_SET_DI_UNIT);
			send.writeByte(id);
			send.writeByte((byte) 1);
			send.writeByte(state);
			read.readByte();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sagt dem TDI er soll vibrieren
	 * @param id ID 
	 * @param state > 0 --> an, sonst aus
	 */
	public void toggleVibro(byte id, byte state) {
		try {
			send.writeByte(ACTOConst.WI_SET_EXT);
			send.writeByte(ACTOConst.USB_SET_DI_UNIT);
			send.writeByte(id);
			send.writeByte((byte) 2);
			send.writeByte(state);
			read.readByte();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
