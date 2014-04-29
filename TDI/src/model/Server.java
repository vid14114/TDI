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

import view.TDI;

/**
 * Implements the runnable interface
 */
public class Server {
	private static DataInputStream read;
	private static DataOutputStream send;
	Socket client;

	public Server(String ip) {
		try {
			// Created a new socket which is bound to the port 12345
			client = new Socket(ip, 12435);
			client.setKeepAlive(true);
			send = new DataOutputStream(client.getOutputStream());
			read = new DataInputStream(new BufferedInputStream(
					client.getInputStream()));
		} catch (final IOException e) {
			TDILogger.logError("Error, could not connect to port 12435 "
					+ e.getMessage());
		}
	}

	public ArrayList<TDI> fullPose() {
		final ArrayList<TDI> tdis = new ArrayList<TDI>();
		try {
			// send.writeByte(ACTOConst.WI_FULL_POSE);
			send.writeByte(ACTOConst.WI_GET_POSE);
			send.writeByte(49); // TUIO 1
			read.readByte();
			read.mark(9);
			byte wi_msg = read.readByte();
			read.reset();
			while (read.available() > 0 && read.readByte() == wi_msg) {
				final byte id = read.readByte();
				final float x = read.readFloat();
				final float y = read.readFloat();
				final float z = read.readFloat();
				final float q1 = read.readFloat();
				final float q2 = read.readFloat();
				final float q3 = read.readFloat();
				final float q4 = read.readFloat();
				final Rotation r = new Rotation(q4, q1, q2, q3, true);
				final double angles[] = r.getAngles(RotationOrder.XYZ);
				final float[] rot = new float[3];
				rot[0] = (float) ((float) angles[0] * 180 / Math.PI);
				rot[1] = (float) ((float) angles[1] * 180 / Math.PI);
				rot[2] = (float) ((float) angles[2] * 180 / Math.PI);
				if (x != 0) {
					final TDI t = new TDI(id, x, y, z, rot);
					System.out.println(t.toString());
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
				final byte id = read.readByte();
				final float x = read.readFloat();
				final float y = read.readFloat();
				final float z = read.readFloat();
				final float q1 = read.readFloat();
				final float q2 = read.readFloat();
				final float q3 = read.readFloat();
				final float q4 = read.readFloat();
				final Rotation r = new Rotation(q4, q1, q2, q3, true);
				final double angles[] = r.getAngles(RotationOrder.XYZ);
				final float[] rot = new float[3];
				rot[0] = (float) ((float) angles[0] * 180 / Math.PI);
				rot[1] = (float) ((float) angles[1] * 180 / Math.PI);
				rot[2] = (float) ((float) angles[2] * 180 / Math.PI);
				if (x != 0) {
					final TDI t = new TDI(id, x, y, z, rot);
					System.out.println(t.toString());
					tdis.add(t);
				}
				read.mark(9);
			}
			read.reset();
			return tdis;
		} catch (final IOException e) {
			return null;
		}
	}

	public void setExt(byte id) { // TODO Set ext needs to be implemented
		// WifiTransmitter
		try {
			send.writeByte(ACTOConst.WI_SET_EXT);
		} catch (final IOException e) {
			TDILogger.logError(e.getMessage());
		}
	}

	public void setPlSize(int size[]) {
		try {
			send.writeByte(ACTOConst.WI_SET_PLSIZE);
			send.writeInt(size[0]);
			send.writeInt(size[1]);
			send.writeInt(size[2]);
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	public void setPose(byte id, float[] pos, float[] rot) {
		try {
			send.writeByte(ACTOConst.WI_SET_POSE);
			send.writeByte(id);
			send.writeFloat(pos[0]);
			send.writeFloat(pos[1]);
			send.writeFloat(pos[2]);
			final Rotation r = new Rotation(RotationOrder.XYZ, rot[0], rot[1],
					rot[2]);
			send.writeFloat((float) r.getQ3());
			send.writeFloat((float) r.getQ0());
			send.writeFloat((float) r.getQ1());
			send.writeFloat((float) r.getQ2());
			read.readByte();
		} catch (final IOException e) {
			TDILogger.logError(e.getMessage());
		}
	}

	public void setRot(byte id, float[] rot) {
		final Vector3D v = new Vector3D(rot[0], rot[1], rot[2]);
		final Quaternion q = new Quaternion(v.toArray());
		try {
			send.writeByte(ACTOConst.WI_SET_ROT);
			send.writeByte(id);
			send.writeFloat((float) q.getQ0());
			send.writeFloat((float) q.getQ1());
			send.writeFloat((float) q.getQ2());
			send.writeFloat((float) q.getQ3());
			read.readByte();
		} catch (final IOException e) {
			TDILogger.logError(e.getMessage());
		}
	}

	public void setTrans(byte id, float[] trans) {
		try {
			send.writeByte(ACTOConst.WI_SET_TRANS);
			send.writeByte(id);
			send.writeFloat(trans[0]);
			send.writeFloat(trans[1]);
			send.writeFloat(trans[2]);
			read.readByte();
		} catch (final IOException e) {
			TDILogger.logError(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		client.close();
	}
}