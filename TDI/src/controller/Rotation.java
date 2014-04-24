package controller;
import java.util.ArrayList;

import view.TDI;

public class Rotation extends Thread{
	private RotationListener rotationListener;
	ArrayList<TDI> tdis;
	
	public Rotation(ArrayList<TDI> tdis){
		this.tdis = tdis;
	}	
	
	public void setRotationListener(RotationListener rotationListener) {
		this.rotationListener = rotationListener;
	}
	
	private void rotation(RotationType rotationType, TDI currentTDI){
		rotationListener.rotatedTDI(new RotateEvent(rotationType, currentTDI));
	}
	
	public void rotate(TDI command){
		final TDI tdi = command; 
		new Runnable() {
			
			@Override
			public void run() {
				TDI currentTDI = tdis.get(tdis.indexOf(tdi));
				float upperBorder = currentTDI.getRotation()[0] + currentTDI.getRotationLimit();
				float lowerBorder = currentTDI.getRotation()[0] - currentTDI.getRotationLimit();
				if(lowerBorder < 0)
					lowerBorder = 360 + lowerBorder;
				if(upperBorder > 360)
					upperBorder = 0 + (upperBorder-360);
				if(tdi.getRotation()[0] < lowerBorder && tdi.getRotation()[0] > upperBorder){
					int rot = (int) ((lowerBorder - currentTDI.getPosition()[0])/currentTDI.getRotationLimit());
					currentTDI.getRotation()[0] = tdi.getRotation()[0];
					for(;rot >= 0; rot--) rotation(RotationType.left, currentTDI);			
				}
				
				if(tdi.getRotation()[0] > upperBorder){
					int rot = (int) ((upperBorder - currentTDI.getPosition()[0])/currentTDI.getRotationLimit());			
					currentTDI.getRotation()[0] = tdi.getRotation()[0];
					for(;rot >= 0; rot--) rotation(RotationType.right, currentTDI);			
				}
			}
		}.run();	
	}
}

interface RotationListener{
	public void rotatedTDI(RotateEvent e);
}


class RotateEvent{
	private RotationType rotation;
	private TDI tdi;
	
	public RotateEvent(RotationType rotation, TDI tdi){
		this.rotation = rotation;
		this.tdi = tdi;
	}	
	
	public RotationType getRotation(){
		return rotation;
	}
	
	public TDI getTDI(){
		return tdi;
	}
}

enum RotationType{
	right,left;
}
