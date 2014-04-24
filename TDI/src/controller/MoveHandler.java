package controller;
import view.TDI;
import view.TDI.TDIState;

public class MoveHandler implements MoveListener{
	BigLogic bigLogic;
	
	public MoveHandler(BigLogic bigLogic){
		this.bigLogic = bigLogic;
	}
	
	@Override
	public void movedTDI(TDI tdi) {	
		switch (tdi.getState()) {
		case desktop: moveDesktopMode(tdi); break;
		case window: moveWindowMode(tdi); break;
		case taskbar:
			//TODO move to taskbar location
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
		if(!tdi.isLocked() && ProgramHandler.getRunningPrograms().size() == 0)
			bigLogic.getServer().setPose(tdi.getId(), tdi.getPosition(), tdi.getRotation());
		if(tdi.isLocked()){
			if(bigLogic.checkMovedToTaskbar(tdi)){
				ProgramHandler.openProgram(tdi.getIcons().get(0));
				tdi.setRotationLimit((360/ProgramHandler.getRunningPrograms().size())/2);
				tdi.toggleLock();
				if(bigLogic.emptyTaskbar()){	
					tdi.setState(TDIState.taskbar);											
					tdi = bigLogic.getTdis().get((bigLogic.getTdis().indexOf(tdi)+1)%2);
				}
				tdi.setState(TDIState.inapp); // should be in app
				tdi.setPosition(new float[]{0,0,0});//TODO Set the position to window position
			}else
				;//TODO move icon to certain position
		}
	}
	
	private void moveWindowMode(TDI tdi){
		//TODO Map the position of table to screen;
		ProgramHandler.moveProgram((int)tdi.getPosition()[0], (int)tdi.getPosition()[1]);
	}

}
