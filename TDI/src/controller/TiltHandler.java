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
	public void tilt(TiltEvent e) {
		switch (e.getRotation()) {
		case up:			
			break;
		case right: 
			tiltRight(big.getTdis().get(big.getTdis().indexOf(e.getTDI())));
		default:
			break;
		}
	}
	
	private void tiltRight(TDI tdi) {
        switch (tdi.getState().toString()) {
            case "desktop":
                tdi.toggleLock();
                //TODO toggle green LED
                break;
            case "taskbar":
                ProgramHandler.closeAllPrograms();                
                for (TDI t : big.getTdis()) {
                    t.setState(TDIState.desktop);
                }
                big.splitIcons();
                break;
            case "inapp":
                tdi.setState(TDIState.window);
                //end of plugin
                break;
            case "sleep":
            	tdi.setState(TDIState.desktop);
                if (ProgramHandler.getNonMinimized() == 0)                    
                    big.splitIcons();
                break;
        }
        Executor.saveBackground(big.getWallpaper().markArea(big.getTdis()));
    }
	
	private void tiltLeft(TDI tdi, TDI commands) {
        switch (tdi.getState().toString()) {
            case "taskbar":
                ProgramHandler.closeAllPrograms();
                tdi.setRotation(commands.getRotation());
                for (TDI t : tdis) {
                    t.setState(TDIState.desktop);
                }
                splitIcons();
                break;
            case "window":
                ProgramHandler.closeProgram();
                if (ProgramHandler.getRunningPrograms().size() == 0){                
                	for(int i = 0; i < tdis.size(); tdis.get(i).setState(TDIState.desktop));
                	splitIcons();
                }
                else if(ProgramHandler.getNonMinimized() == 0)
                	tdi.setState(TDIState.desktop);                	
                else
                	tdi.setRotation(commands.getRotation()); //TODO Check logic                
                	
                break;
            case "inapp":
                tdi.setRotation(commands.getRotation());
                plugserv.sendMessage(tdi.getId(), tdi.getPosition()[0], tdi.getPosition()[1], tdi.getPosition()[2], tdi.getRotation());
                break;
            case "sleep":
                if (ProgramHandler.getNonMinimized() == 0) {
                    tdi.setState(TDIState.desktop);
                    splitIcons();
                } else
                    tdi.setState(TDIState.desktop);
                break;
        }
        Executor.saveBackground(wallpaper.markArea(tdis));
    }

    private void tiltUp(TDI tdi, TDI commands) {
        switch (tdi.getState().toString()) {
            case "taskbar":
                ProgramHandler.restoreAllPrograms();
                int count = 0;
                for (TDI t : tdis) {
                    if (t.getState().equals(TDIState.window))
                        count++;
                }
                if (count == 0) {
                    if (tdis.get(0).getState().equals(TDIState.window))
                        tdis.get(1).setState(TDIState.window);
                    else
                        tdis.get(0).setState(TDIState.window);
                }
                break;
            case "window":
                ProgramHandler.toggleMaximization();//
                tdi.setRotation(commands.getRotation());
                if (ProgramHandler.getNonMinimized() == 0) {
                    tdi.setState(TDIState.desktop);
                    splitIcons();
                }
                break;
            case "inapp":
                tdi.setRotation(commands.getRotation());
                plugserv.sendMessage(tdi.getId(), tdi.getPosition()[0], tdi.getPosition()[1], tdi.getPosition()[2], tdi.getRotation());
                break;
            case "sleep":
                if (ProgramHandler.getNonMinimized() == 0) {
                    tdi.setState(TDIState.desktop);
                    splitIcons();
                } else
                    tdi.setState(TDIState.desktop);
                break;
        }
        Executor.saveBackground(wallpaper.markArea(tdis));
    }

    private void tiltDown(TDI tdi, TDI commands) {
        switch (tdi.getState().toString()) {
            case "taskbar":
                ProgramHandler.minimizeAllPrograms();
                tdi.setRotation(commands.getRotation());
                for (TDI t : tdis) {
                    t.setState(TDIState.desktop);
                }
                splitIcons();
                break;
            case "window":
                ProgramHandler.minimize();
                tdi.setRotation(commands.getRotation());
                if (ProgramHandler.getNonMinimized() == 0) {
                    tdi.setState(TDIState.desktop);
                    splitIcons();
                }
                break;
            case "inapp":
                tdi.setRotation(commands.getRotation());
                plugserv.sendMessage(tdi.getId(), tdi.getPosition()[0], tdi.getPosition()[1], tdi.getPosition()[2], tdi.getRotation());
                break;
            case "sleep":
                if (ProgramHandler.getNonMinimized() == 0) {
                    tdi.setState(TDIState.desktop);
                    splitIcons();
                } else
                    tdi.setState(TDIState.desktop);
                break;
        }
        Executor.saveBackground(wallpaper.markArea(tdis));
    }
}
