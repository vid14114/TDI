package controller;
import view.TDI;

public class Tilt{
	private TiltListener tiltListener;
	private float[] restingTiltPos = new float[]{0,0,0};
	TiltType tiltType = null;
	static int compensation = 30;
	TDI currentTDI;
	float tiltPos;
	
	public Tilt(float[] restingRotation){
//		this.restingTiltPos = restingRotation;
	}
	
	public void setRestingRotation(float[] rotation){
		restingTiltPos = rotation;
	}
	
	private void tilt(){
		tiltListener.tiltedTDI(new TiltEvent(tiltType, currentTDI));
		tiltType = null;
	}
	
	//	public void tilted(float )
	public void setTiltListener(TiltListener listener){
		tiltListener = listener;
	}

	public void tilt(TDI command){		
		float[] rot = command.getRotation();
		if(tiltType != null){
			System.out.println("TDI: "+currentTDI.getId() );
			if(currentTDI.equals(command)){
				switch(tiltType)
				{
				case down:
					if(tiltPos < rot[2])
						tilt();
					break;
				case left:
					if(tiltPos > rot[1])
						tilt();
					break;
				case right:
					if(tiltPos < rot[1])
						tilt();
					break;
				case up:
					if(tiltPos > rot[2])
						tilt();
					break;				
				}
			}
			return;
		}
		if(rot[2] != restingTiltPos[2]){
			if(rot[2] > restingTiltPos[2]+compensation)
			{
				tiltType = TiltType.up;
				tiltPos = rot[2];
				currentTDI = command;
			}				
			else if(rot[2] < restingTiltPos[2]-compensation){
				tiltType = TiltType.down;
				tiltPos = rot[2];
				currentTDI = command;
			}
		}
		if(rot[1] != restingTiltPos[1]){
			if(rot[1] > restingTiltPos[1]+compensation){
				tiltType = TiltType.left;
				tiltPos = rot[1];
				currentTDI = command;
			}
			else if(rot[1] < restingTiltPos[1]-compensation){
				tiltType = TiltType.right;
				tiltPos = rot[1];
				currentTDI = command;
			}
		}
	}
}

interface TiltListener{
	public void tiltedTDI(TiltEvent e);
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
