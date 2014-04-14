package controller;

import java.util.LinkedList;

import view.TDI;
import view.TDI.TDIState;

public class Tilt extends Thread{
	private LinkedList<TiltListener> tiltListener = new LinkedList<>();
	private float[] restingRotation = new float[3];
	Rotation rotationType = null;
	static int compensation = 5;
	float[] currentRot = new float[3];
	TDI currentTDI;
	
	public Tilt(float[] restingRotation){
		this.restingRotation = restingRotation;
	}
	
	private void rotation(){
		for(TiltListener t:tiltListener)
			t.tilt(new TiltEvent(rotationType, currentTDI));
	}
	
	//	public void tilted(float )
	public void addTiltListener(TiltListener listener){
		tiltListener.add(listener);
	}

	public boolean removeTiltListener(TiltListener listener){
		return tiltListener.remove(listener);
	}  	
	
	public void rotated(TDI tdi, float[] rot){		
		if(rotationType != null){
			currentRot = rot;
			return;
		}
		currentTDI = tdi;
		if(rot[0] != restingRotation[0]){
			if(rot[0] > restingRotation[0]+compensation)
				rotationType = Rotation.up;
			else if(rot[0] < restingRotation[0]-compensation)
				rotationType = Rotation.down;
		}
		else if(rot[1] != restingRotation[1]){
			if(rot[1] > restingRotation[1]+compensation)
				rotationType = Rotation.left;
			else if(rot[1] > restingRotation[1]-compensation)
				rotationType = Rotation.right;
		}
	}

	public void run(){
		while(true){
			if(currentRot[0] == restingRotation[0] || currentRot[1] == restingRotation[1]){
				rotation();
				rotationType = null;
			}
		}
	}
}

interface TiltListener{
	public void tilt(TiltEvent e);
}
class TiltEvent{
	private Rotation rotation;
	private TDI tdi;
	
	public TiltEvent(Rotation rotation, TDI tdi){
		this.rotation = rotation;
		this.tdi = tdi;
	}	
	
	public Rotation getRotation(){
		return rotation;
	}
	
	public TDI getTDI(){
		return tdi;
	}
}
enum Rotation{
	right,left,up,down;
}
