package controller;
import view.TDI;

public class Tilt{
	private TiltListener tiltListener;
	private float[] restingTiltPos = new float[3];
	TiltType tiltType = null;
	static int compensation = 5;
	TDI currentTDI;
	
	public Tilt(float[] restingRotation){
		this.restingTiltPos = restingRotation;
	}
	
	public void setRestingRotation(float[] rotation){
		restingTiltPos = rotation;
	}
	
	private void tilt(){
		tiltListener.tilt(new TiltEvent(tiltType, currentTDI));
	}
	
	//	public void tilted(float )
	public void setTiltListener(TiltListener listener){
		tiltListener = listener;
	}

	public void tilted(TDI tdi){		
		float[] rot = tdi.getRotation();
		if(tiltType != null && currentTDI.equals(tdi)){
			if(rot[2] == restingTiltPos[2] || rot[1] == restingTiltPos[1])
				new Runnable() {
					public void run() {
						tilt();
						tiltType = null;
					}
				}.run();				
		}
		currentTDI = tdi;
		if(rot[2] != restingTiltPos[2]){
			if(rot[2] > restingTiltPos[2]+compensation)
				tiltType = TiltType.up;
			else if(rot[2] < restingTiltPos[2]-compensation)
				tiltType = TiltType.down;
		}
		else if(rot[1] != restingTiltPos[1]){
			if(rot[1] > restingTiltPos[1]+compensation)
				tiltType = TiltType.left;
			else if(rot[1] < restingTiltPos[1]-compensation)
				tiltType = TiltType.right;
		}
	}
}

interface TiltListener{
	public void tilt(TiltEvent e);
}
class TiltEvent{
	private TiltType tilt;
	private TDI tdi;
	
	public TiltEvent(TiltType rotation, TDI tdi){
		this.tilt = rotation;
		this.tdi = tdi;
	}	
	
	public TiltType getRotation(){
		return tilt;
	}
	
	public TDI getTDI(){
		return tdi;
	}
}
enum TiltType{
	right,left,up,down;
}
