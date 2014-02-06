package controller;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import model.ConfigLoader;
import model.Server;
import view.Icon;
import view.TDI;
import view.Wallpaper;

/**
 * Implements Runnable interface. Is Master, is big.
 */
public class BigLogic implements Runnable {

	private ArrayList<TDI> tdis;
	private Server server;

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
					float[] movParam = new float[3];
					if (tdi.getPosition() != command.getPosition()) { //CASE POSITION
						if (tdi.getPosition()[0] != command.getPosition()[0]) // x Axis changed
						{
							//SZENARIO A DM
							if(tdi.getState()=="desktop") // is in Desktop Mode == kein TDI in Taskbar == kein Fenster offen
							{
								if(tdi.getLocked() == false)
								{
									if(PosInTaskbar(command.getPosition()[0],command.getPosition()[1]) == false)//ist die neue Position in Taskbar?
									{	
										// TDI für Icons zuständig 
										// neu berechenen der zugehörigen Icons	
									}
									else // neue pos in taskbar => setzen des Tdi in taskbar mode öffen von program
									{
										tdi.setState("taskbar");
										ProgramHandler.openProgram(tdi.getIcons().get(0)); //open active icon
										//Vibrate(); //500ms
										//setLedRed();
										tdi.setPosition(0, 0, 0); // linke Ecke der Taskbar
										for(int x=2,y=10; x < tdis.size() ;x++) // set evry other tdi in the middle of the desk
										{
											tdis.get(tdis.indexOf(commands.get(x))).setPosition(0, 0, 0+y);  // irgendwo am Rand des Tisches (+y damit sie nicht aufeinander fahren)
											tdis.get(tdis.indexOf(commands.get(x))).setState("default"); //
											//setLedGreen();
											
											//set one tdi in window mode - position = middle of the desk
											tdis.get(tdis.indexOf(commands.get(1))).setState("window");
											tdis.get(tdis.indexOf(commands.get(1))).setPosition(0, 0, 0);
										}
									}
								}
							}
							if(tdi.getState()=="taskbar")
							{
								if(ProgramHandler.getRunningPrograms().size()>0) //if programs are open (at least 1)
								{
									if(PosInTaskbar(command.getPosition()[0],command.getPosition()[1])) // neue pos immer noch in taskbar
									{
										tdi.setPosition(command.getPosition()[0], command.getPosition()[1], tdi.getPosition()[2]);
									}
									else // neue pos außerhalb der taskbar
									{
										TDI windFocused=null;
										for(TDI t: tdis) // find out if any other TDI has focused a window
										{
											if(t.getState()=="window")
												windFocused=t;
										}
										if(windFocused != null) 
										{
											if(true)
											{
												// fokusiertes TDI in nähe des aktuellen
												// start counter 
												// skaliermodus TDI repräsentiert obere linke Ecke des Fensters
												// task TDI repräsentiert untere linke Ecke des Fensters
											}
										}
										else
										{
											tdi.setPosition(command.getPosition()[0], command.getPosition()[1], tdi.getPosition()[2]);
										}
									}
								}
								else
								{
									for(int x=0; x < tdis.size() ;x++) // set evry tdi in desktop mode cause no programs are open
									{
										tdis.get(tdis.indexOf(commands.get(x))).setState("desktop"); //?
										tdis.get(tdis.indexOf(commands.get(x))).setPosition(1, 1, 1);// wohin??
									}
								}
							}
							if(tdi.getState()=="window")
							{
								if(true)
								{
									// taskbar TDI in nähe des aktuellen
									// start counter 
									// skaliermodus TDI repräsentiert obere linke Ecke des Fensters
									// window TDI repräsentiert obere rechte Ecke des Fensters
								}
								else
								{
									tdi.setPosition(command.getPosition()[0], command.getPosition()[1], tdi.getPosition()[2]);
									//tdi.getIcons().get(0).setPosition(); new position of program window
								}
							}
							if(tdi.getState()=="inapp")
							{
								
							}
						}
					}					
					//neigen
					if (tdi.getRotation() != command.getRotation()) {
						//TDI nach oben neigen TM
						if (tdi.getRotation()[0] != command.getRotation()[0]) 
							ProgramHandler.toggleMaximization();
						//TDI nach rechts neigen DM
						if (tdi.getRotation()[1] != command.getRotation()[1]) 
							tdi.toggleLock();
						// ?
						if (tdi.getRotation()[2] != command.getRotation()[2]) {
							// TDI nach unten neigen TM
							ProgramHandler.minimize(); //TODO When still focused, move TDI when not for icons 
							//TDI nach rechts neigen DM ??
							if(ProgramHandler.isDesktopMode()); //Go to icons
							else ;//GO to location of window								
						}
						//TDI nach links/rechts neigen TM
						if (tdi.getRotation()[3] != command.getRotation()[3])							
							ProgramHandler.closeProgram();
						// missing: TDI nach links neigen DM; ..
						// => sollte es nicht eher getPosition statt getRotation sein?
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
	/**
	 * checks if givenPos is in taskbar
	 * @return 
	 */
	public boolean PosInTaskbar(float x, float y)
	{
		return true;
	}

	public BigLogic() {
		ConfigLoader cl = new ConfigLoader();
		cl.loadIcons();
		final Server s = new Server();
		Timer mo = new Timer();
		mo.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				ArrayList<TDI> tdis = s.fullPose();
				for (TDI t : tdis) {
					commands.add(t);
				}
			}
		}, 0, 500);
	}

}
