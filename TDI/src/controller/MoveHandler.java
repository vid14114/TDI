package controller;
import java.awt.Point;

import model.ConfigLoader;
import model.Server;
import view.TDI;
import view.TDI.TDIState;

public class MoveHandler implements MoveListener{
	BigLogic bigLogic;
	TDI scaleTDI;
	float compensation = 20;
	
	public MoveHandler(BigLogic bigLogic){
		this.bigLogic = bigLogic;
	}
	
    /**
     * Checks if two TDIs are close enough to each other to start Scale Mode
     * @return whether scale mode starts or not
     */
    private boolean startScaleMode(TDI tdi) {
        int range = 10; //TODO Range for closeness of TDI can and should be changed        
        for (TDI tdi2: bigLogic.getTdis()) {
        	if(tdi.getId() == tdi2.getId()) break;
            if(Math.abs(tdi.getPosition()[0] - tdi2.getPosition()[0]) < range || Math.abs(tdi.getPosition()[1] - tdi2.getPosition()[1]) < range)
            {
            	tdi2.setIsScale(true);
            	tdi.setIsScale(true);
            	scaleTDI = tdi2;
            	bigLogic.getServer().toggleVibro(tdi.getId(), (byte)12);
            	bigLogic.getServer().toggleVibro(tdi2.getId(), (byte)12);
            	return true;
            }
        }
        return false;
    }

	
    public boolean moveChanged(float oldPos, float newPos){		
		if(oldPos + compensation < newPos || oldPos-compensation > newPos)
			return true;
		return false;
	}
    
	@Override
	public void movedTDI(TDI command) {
		TDI current = bigLogic.getTdis().get(bigLogic.getTdis().indexOf(command));		
		
		if(current.getState().equals(TDIState.desktop) && !current.isLocked() && ProgramHandler.getRunningPrograms().size() == 0 
				&& (moveChanged(current.getPosition()[0], command.getPosition()[0]) || moveChanged(current.getPosition()[1], command.getPosition()[1]))){
			current.setMoving(true);
			bigLogic.getServer().setPose(current.getId(), current.getPosition(), current.getRotation());
			return;
		}			
		
		if(current.isScale()){
			current.setPosition(command.getPosition());
			moveScaleMode(current);
			return;
		}
		if(startScaleMode(current)){
			current.setPosition(command.getPosition());
			return;
		}
		switch (current.getState()) {
		case desktop: 
			if(current.isLocked()){
				current.setPosition(command.getPosition());
				moveDesktopMode(current);				
			}break;
		case window: 
			current.setPosition(command.getPosition());
			moveWindowMode(current); break;
		case taskbar:
			bigLogic.getServer().setPose(current.getId(), new float[]{bigLogic.getTaskbarLocation(), current.getPosition()[1], current.getPosition()[2]},  current.getRotation());			
            break;		
		case sleep: moveSleepMode(current); break;
		default:
			break;          
		}
	}
	
	private void moveSleepMode(TDI tdi){
		if (ProgramHandler.getNonMinimized() == 0) {
            tdi.setState(TDIState.desktop);
            bigLogic.splitIcons();
        } else
            tdi.setState(TDIState.desktop);
	}
	
	private void moveDesktopMode(TDI tdi){			
		if(tdi.isLocked()){			
			System.out.println("Desktop MOde enter");
			if(bigLogic.checkMovedToTaskbar(tdi.getPosition()[0])){
				ProgramHandler.openProgram(tdi.getIcons().get(0));
				tdi.setRotationLimit((360/ProgramHandler.getRunningPrograms().size())/2);
				tdi.toggleLock();
				bigLogic.getServer().toggleGreenLED(tdi.getId(), (byte)13);
				if(bigLogic.emptyTaskbar()){	
					tdi.setState(TDIState.taskbar);					
					tdi = bigLogic.getTdis().get((bigLogic.getTdis().indexOf(tdi)+1)%2);
				}
				tdi.setState(TDIState.inapp);
				float[] position = Executor.getWindowPosition();
				tdi.setPosition(new float[]{position[0]/bigLogic.scaleX,position[1]/bigLogic.scaleY,0});
				tdi.setRotation(new float[]{0,0,0});
				tdi.setMoving(true);
				bigLogic.getServer().setPose(tdi.getId(), tdi.getPosition(), tdi.getRotation());
			}else{
				int row = (int) ((bigLogic.playFieldMaxValues[0] - (int)tdi.getPosition()[0]*(int)bigLogic.scaleX)/ConfigLoader.blockSize);
				int col = (int) (( bigLogic.playFieldMaxValues[1] - (int)tdi.getPosition()[1]*(int)bigLogic.scaleY)/ConfigLoader.blockSize);
				tdi.getIcons().get(0).setPosition(new Point(row, col));
				bigLogic.refreshIcons();
			}
		}
	}
	
	private void moveWindowMode(TDI tdi){		
		ProgramHandler.moveProgram((int)tdi.getPosition()[0]*(int)bigLogic.scaleX, (int)tdi.getPosition()[1]*(int)bigLogic.scaleY);
	}
	
  private void moveScaleMode(TDI tdi) {
	  if(tdi.equals(scaleTDI)) return;
	  ProgramHandler.resizeProgram((int)Math.abs(scaleTDI.getPosition()[0] - tdi.getPosition()[0]),(int) Math.abs(scaleTDI.getPosition()[1] - tdi.getPosition()[1]));	  
  }

}
