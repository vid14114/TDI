package controller;
import java.awt.Point;

import model.ConfigLoader;
import view.TDI;
import view.TDI.TDIState;

public class MoveHandler implements MoveListener{
	BigLogic bigLogic;
	TDI scaleTDI;
	
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

	
	@Override
	public void movedTDI(TDI tdi) {		
		if(tdi.isScale()){
			moveScaleMode(tdi);
			return;
		}
		if(startScaleMode(tdi)) return;
		switch (tdi.getState()) {
		case desktop: moveDesktopMode(tdi); break;
		case window: moveWindowMode(tdi); break;
		case taskbar:
			bigLogic.getServer().setPose(tdi.getId(), new float[]{bigLogic.getTaskbarLocation(), tdi.getPosition()[1], tdi.getPosition()[2]},  tdi.getRotation());			
            break;		
		case sleep: moveSleepMode(tdi); break;
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
		System.out.println("Move desktop");
		if(!tdi.isLocked() && ProgramHandler.getRunningPrograms().size() == 0)
			bigLogic.getServer().setPose(tdi.getId(), tdi.getPosition(), tdi.getRotation());
		if(tdi.isLocked()){
			if(bigLogic.checkMovedToTaskbar(tdi.getPosition()[0])){
				ProgramHandler.openProgram(tdi.getIcons().get(0));
				tdi.setRotationLimit((360/ProgramHandler.getRunningPrograms().size())/2);
				tdi.toggleLock();
				if(bigLogic.emptyTaskbar()){	
					tdi.setState(TDIState.taskbar);					
					tdi = bigLogic.getTdis().get((bigLogic.getTdis().indexOf(tdi)+1)%2);
				}
				tdi.setState(TDIState.inapp);
				float[] position = Executor.getWindowPosition();
				tdi.setPosition(new float[]{position[0]/bigLogic.scaleX,position[1]/bigLogic.scaleX,0});
			}else{
				int row = ((int)tdi.getPosition()[0]*(int)bigLogic.scaleX)/ConfigLoader.blockSize;
				int col = ((int)tdi.getPosition()[1]*(int)bigLogic.scaleY)/ConfigLoader.blockSize;
				tdi.getIcons().get(0).setPosition(new Point(row, col));
				bigLogic.refreshIcons();
			}
		}
	}
	
	private void moveWindowMode(TDI tdi){		
		ProgramHandler.moveProgram((int)tdi.getPosition()[0]*(int)bigLogic.scaleX, (int)tdi.getPosition()[1]*(int)bigLogic.scaleY);
	}
	
  private void moveScaleMode(TDI tdi) {//Exit through right tilting
	  if(tdi.equals(scaleTDI)) return;
	  ProgramHandler.resizeProgram((int)Math.abs(scaleTDI.getPosition()[0] - tdi.getPosition()[0]),(int) Math.abs(scaleTDI.getPosition()[1] - tdi.getPosition()[1]));	  
  }

}
