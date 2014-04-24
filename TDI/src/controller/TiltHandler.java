/**
 * 
 */
package controller;

import view.TDI;
import view.TDI.TDIState;

/**
 * @author abideen
 *
 */
public class TiltHandler implements TiltListener {
	BigLogic big;
	
	public TiltHandler(BigLogic big){
		this.big = big;
	}
	/* (non-Javadoc)
	 * @see controller.TiltListener#tilt(controller.TiltEvent)
	 */
	@Override
	public void tiltedTDI(TiltEvent e) {
		TDI tdi = big.getTdis().get(big.getTdis().indexOf(e.getTDI()));
		switch (e.getRotation()) {
		case up:			
			tiltUp(tdi); break;
		case down:
			tiltDown(tdi); break;
		case right: 
			tiltRight(tdi); break;
		case left: 
			tiltLeft(tdi); break;
		}
	}
	
	private void tiltRight(TDI tdi) {
        switch (tdi.getState()) {
            case desktop:
                tdi.toggleLock();
                //TODO toggle green LED
                break;
            case taskbar:
                ProgramHandler.closeAllPrograms();                
                for (TDI t : big.getTdis()) {
                    t.setState(TDIState.desktop);
                }
                big.splitIcons();
                break;
            case inapp:
                tdi.setState(TDIState.window);
                //end of plugin
                break;
		default:
			break;
        }
        Executor.saveBackground(big.getWallpaper().markArea(big.getTdis()));
    }
	
	private void tiltLeft(TDI tdi) {
        switch (tdi.getState()) {
            case taskbar:
                ProgramHandler.closeAllPrograms();
                for (TDI t : big.getTdis()) {
                    t.setState(TDIState.desktop);
                }
                big.splitIcons();
                break;
            case window:
                ProgramHandler.closeProgram();
                tdi.setRotationLimit((360/ProgramHandler.getRunningPrograms().size())/2);
                if (ProgramHandler.getRunningPrograms().size() == 0){                
                	for(int i = 0; i < big.getTdis().size(); big.getTdis().get(i).setState(TDIState.desktop));
                	big.splitIcons();
                }
                else if(ProgramHandler.getNonMinimized() == 0)
                	tdi.setState(TDIState.desktop);                	
                else
                	 //TODO Check logic                	
                break;
            case inapp:
                big.getPlugserv().sendMessage(tdi.getId(), tdi.getPosition(), tdi.getRotation());
                break;
		default:
			break;
        }
        Executor.saveBackground(big.getWallpaper().markArea(big.getTdis()));
    }

    private void tiltUp(TDI tdi) {
        switch (tdi.getState()) {
            case taskbar:
                ProgramHandler.restoreAllPrograms();
                int count = 0;
                for (TDI t : big.getTdis()) {
                    if (t.getState().equals(TDIState.window))
                        count++;
                }
                if (count == 0) {
                    if (big.getTdis().get(0).getState().equals(TDIState.window))
                    	big.getTdis().get(1).setState(TDIState.window);
                    else
                    	big.getTdis().get(0).setState(TDIState.window);
                }
                break;
            case window:
                ProgramHandler.toggleMaximization();//
                if (ProgramHandler.getNonMinimized() == 0) {
                    tdi.setState(TDIState.desktop);
                    big.splitIcons();
                }
                break;
            case inapp:
                big.getPlugserv().sendMessage(tdi.getId(), tdi.getPosition(), tdi.getRotation());
                break;
		default:
			break;
        }
        Executor.saveBackground(big.getWallpaper().markArea(big.getTdis()));
    }

    private void tiltDown(TDI tdi) {
        switch (tdi.getState()) {
            case taskbar:
                ProgramHandler.minimizeAllPrograms();
                for (TDI t : big.getTdis()) {
                    t.setState(TDIState.desktop);
                }
                big.splitIcons();
                break;
            case window:
                ProgramHandler.minimize();
                if (ProgramHandler.getNonMinimized() == 0) {
                    tdi.setState(TDIState.desktop);
                    big.splitIcons();
                }
                break;
            case inapp:
                big.getPlugserv().sendMessage(tdi.getId(), tdi.getPosition(), tdi.getRotation());
                break;
		default:
			break;
        }
        Executor.saveBackground(big.getWallpaper().markArea(big.getTdis()));
    }
}
