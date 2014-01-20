package controller;

import java.util.ArrayList;
import model.ConfigLoader;
import model.Server;
import view.*;

/**
 * Implements Runnable interface. Is Master, is big.
 */
public class BigLogic implements Runnable {

	private ArrayList<TDI> tdis;
	private Server server;
	private ProgramHandler programHandler;
	/**
	 * The wallpaper
	 */
	private Wallpaper wallpaper;
	/**
	 * The commands that have to be executed.
	 */
	private ArrayList<TDI> commands;

	/**
	 * Lädt Dialog und Desktop configuration
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BigLogic bl = new BigLogic();
		ConfigLoader cl = new ConfigLoader();
		cl.loadIcons();
		Server s = new Server();
		s.fullPose(bl.tdis);
	}

	/**
	 * The run method that is overridden
	 */
	public void run() {
		
		while (true) {
			if (commands.size() > 0) {
				if (tdis.contains(commands.get(0))) {
					TDI tdi = tdis.get(tdis.indexOf(commands.get(0)));
					TDI command = commands.get(0);
					if (tdi.getPosition() != command.getPosition()) {

					}
					/* if(temp.getRotation() != commands.get(0).getRotation()){
					      
						   if(programHandler.getRunningPrograms().isEmpty())
						   {
							   if(commands.get(0).getRotation()>0) // rotate clockwise
							   {
								   if(temp.getLocked()==false)
								   {
									   temp.setRotation(commands.get(0).getRotation());//new rotation
									   temp.rotateClockwise();								   
								   }
							   }
							   else //rotate counterwise
							   {
								   temp.setRotation(commands.get(0).getRotation());//new rotation
								   temp.rotateCounter();	
							   }
						   }
					   } */
					if (tdi.getTilt() != command.getTilt()) {
						if (tdi.getTilt()[0] != command.getTilt()[0]) 
							ProgramHandler.toggleMaximization();
						if (tdi.getTilt()[1] != command.getTilt()[1]) 
							tdi.toggleLock();
						if (tdi.getTilt()[2] != command.getTilt()[2]) {
							ProgramHandler.minimize(); //TODO When still focused, move TDI when not for icons
							if(ProgramHandler.isDesktopMode()); //Go to icons
							else ;//GO to location of window								
						}
						if (tdi.getTilt()[3] != command.getTilt()[3])							
							ProgramHandler.closeProgram();
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param command
	 */
	public void addCommand(TDI command) {
		commands.add(command);
	}

}
