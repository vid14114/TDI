package controller;

import java.util.ArrayList;

import view.TDI;
import view.TDI.TDIState;

public class Move {
	private MoveListener moveListener;
	private ArrayList<TDI> tdis;
	private boolean[] moveFlags;	
	static final int compensation = 5; 
	
	public Move(ArrayList<TDI> tdis){
		this.tdis = tdis;
		moveFlags = new boolean[tdis.size()];
	}
	
	private boolean moveChanged(float oldPos, float newPos){
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
		if(currentTDI.getState().equals(TDIState.window)){
			moved(currentTDI);
			return;
		}
		if(moveFlags[tdis.indexOf(command)]){ //Currently being moved			
			if(moveChanged(currentTDI.getPosition()[0], command.getPosition()[0]) || moveChanged(currentTDI.getPosition()[1], command.getPosition()[1]))
				currentTDI.setPosition(command.getPosition());
			else{
				moveFlags[tdis.indexOf(command)] = false;
				currentTDI.setPosition(command.getPosition());
				moved(currentTDI);
			}
		}
		else{
			if(moveChanged(currentTDI.getPosition()[0], command.getPosition()[0]) || moveChanged(currentTDI.getPosition()[1], command.getPosition()[1])){
				moveFlags[tdis.indexOf(command)] = true;
				currentTDI.setPosition(command.getPosition());
			}
		}
	}
}

interface MoveListener{
	public void movedTDI(TDI tdi);
}