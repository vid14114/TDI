package controller;

import java.util.ArrayList;

import view.TDI;
import view.TDI.TDIState;

public class Move {
	private MoveListener moveListener;
	private ArrayList<TDI> tdis;
	private boolean[] moveFlags;
	private float[][] oldPos = new float[2][3];
	static final int compensation = 20; 
	
	public Move(ArrayList<TDI> tdis){
		this.tdis = tdis;
		moveFlags = new boolean[2];
	}
	
	public boolean moveChanged(float oldPos, float newPos){
		if(oldPos + compensation < newPos || oldPos-compensation > newPos)
			return true;
		return false;
	}
	
	private void moved(TDI tdi){
		moveListener.movedTDI(tdi);
	}
	
	public void setMoveListener(MoveListener moveListener){
		this.moveListener = moveListener;
	}
	
	public void move(TDI command){		
		TDI currentTDI = tdis.get(tdis.indexOf(command));		
		if(currentTDI.getState().equals(TDIState.window) || currentTDI.isScale()){
			currentTDI.setPosition(command.getPosition());
			moved(command);
			return;
		}
		if(moveFlags[tdis.indexOf(command)]){ //Currently being moved			
			if(moveChanged(oldPos[tdis.indexOf(command)][0], command.getPosition()[0]) || moveChanged(oldPos[tdis.indexOf(command)][1], command.getPosition()[1]))
				oldPos[tdis.indexOf(command)] = command.getPosition();				
			else{
				moveFlags[tdis.indexOf(command)] = false;
				moved(command);
			}
		}
		else{
			if(moveChanged(oldPos[tdis.indexOf(command)][0], command.getPosition()[0]) || moveChanged(oldPos[tdis.indexOf(command)][1], command.getPosition()[1]))
				moveFlags[tdis.indexOf(command)] = true;
		}
	}
}

interface MoveListener{
	public void movedTDI(TDI tdi);
}