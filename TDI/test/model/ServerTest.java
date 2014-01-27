package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import toxi.geom.Quaternion;

import org.junit.Assert;
import org.junit.Test;

import view.TDI;

public class ServerTest implements Runnable {

	Server s;
	private ServerSocket wifiTrans;
	private Socket client;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	boolean listening = true;
	byte id = 0;
	float[] rot = { 0, 0, 0 };
	TDI t1 = new TDI(id, 10, 100, 0, rot);

	@Test
	public void test() throws InterruptedException {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					wifiTrans = new ServerSocket(12435);
					client = wifiTrans.accept();
					dos = new DataOutputStream(client.getOutputStream());
					dis = new DataInputStream(client.getInputStream());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while (listening) {
					byte input;
					try {
						input = dis.readByte();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					switch (input) {
					case ACTOConst.WI_FULL_POSE:
						sendFullPose();
						break;
					}
					break;
				}
			}
		}).start();
		Server s = new Server();
		ArrayList<TDI> t = s.fullPose();
		System.out.println(t.get(0).toString());
		Assert.assertEquals(t1.getId(), t.get(0).getId());
		Assert.assertEquals(t1.getPosition(), t.get(0).getPosition());
		Assert.assertEquals(t1.getRotation(), t.get(0).getRotation());
	}

	@Override
	public void run() {
		
	}

	public void sendFullPose() {
		try {
			dos.writeByte(ACTOConst.WI_ACK);
			dos.writeByte(ACTOConst.WI_FULL_POSE);
			dos.writeByte(t1.getId());
			dos.writeFloat(t1.getPosition()[0]);
			dos.writeFloat(t1.getPosition()[1]);
			dos.writeFloat(t1.getPosition()[2]);
			Quaternion q1 = Quaternion.createFromEuler(t1.getRotation()[0],
					t1.getRotation()[1], t1.getRotation()[2]);
			dos.writeFloat(q1.w);
			dos.writeFloat(q1.x);
			dos.writeFloat(q1.y);
			dos.writeFloat(q1.z);
			listening = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
