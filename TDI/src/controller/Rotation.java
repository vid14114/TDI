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
				float upperBorder = currentTDI.getRotation()[0] + (currentTDI.getRotationLimit()/2);
				float lowerBorder = currentTDI.getRotation()[0] - (currentTDI.getRotationLimit()/2);
				if(upperBorder > 180)
					upperBorder = -360 + upperBorder;
				if(lowerBorder < -180)
					lowerBorder = 360 - upperBorder;
				if(Math.abs(tdi.getRotation()[0]) < lowerBorder){
					int rot = (int) (Math.abs(tdi.getRotation()[0])/currentTDI.getRotationLimit());
					currentTDI.getRotation()[0] = tdi.getRotation()[0];
					for(;rot >= 0; rot--) rotation(RotationType.left, currentTDI);			
				}
				
				if((upperBorder < 0 && Math.abs(tdi.getRotation()[0]) < Math.abs(upperBorder)) || (Math.abs(tdi.getRotation()[0]) > upperBorder && upperBorder > 0)){
					int rot = (int) (Math.abs(tdi.getRotation()[0])/currentTDI.getRotationLimit());			
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
