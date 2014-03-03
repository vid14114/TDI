package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import model.ConfigLoader;
import model.PluginTableModel;
import model.Server;
import view.Icon;
import view.TDI;
import view.TDI.TDIState;
import view.TDIDialog;
import view.Wallpaper;

/**
 * Implements Runnable interface. Is Master, is big.
 * rotation(z,x,y) [annahme]
 * Info:
 * rechts neigen => x alt > x neu
 * links neigen => x alt < x neu
 * oben neigen => y alt > y neu
 * runter neigen => y alt < y neu
 * rechts drehen => z alt > z neu 
 * links drehen => z alt < z neu
 * 
 * Position(x,y,z)
 * heben => z neu > (z alt - var) //e.g. var = 200; [not used]
 * rechts bewegen: x alt > x neu
 * links bewegen: x alt < x neu
 * oben bewegen: y alt > y neu
 * unten bewegen: y alt < y neu
 * 
 */
public class BigLogic implements Runnable, ActionListener {

	private TDIDialog tdiDialog;
	private PluginTableModel pluginTableModel;
	ConfigLoader configLoader;
	private ArrayList<Icon> icons;
	private ArrayList<TDI> tdis;
	private Server server;
	/**
	 * counter for scaling
	 */
	private int scaleCount=0;
	private int scaleCount2=0;
	/**
	 * Compensation Value for position change
	 */
	int compPos = 5;
	float compPos2[]={5,5,5};
	/**
	 * compensation value for Rotation changes
	 */
	private int compRot = 200;
	/**
	 * times(1s = 100ms) to wait for scaling
	 */
	private int waitTime=5; 

	
	/**
	 * The wallpaper
	 */
	private Wallpaper wallpaper;
	/**
	 * The commands that have to be executed.
	 */
	private ArrayList<TDI> commands=new ArrayList<TDI>();

	/**
	 * Lädt Dialog und Desktop configuration
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new BigLogic()).start();
	}

	/**
	 * The run method that is overridden
	 */
	public void run() {

		while (true) {
			if (commands.size() > 0) {
				if (tdis.contains(commands.get(0))) {
					TDI tdi = tdis.get(tdis.indexOf(commands.get(0)));
					System.out.println(tdi.toString());
					TDI command = commands.get(0);
					float[] movParam = new float[3];
					TDI windFocused=null;
					TDI taskFocused=null;
					if (tdi.getPosition() != command.getPosition()) { //CASE POSITION
						if ((tdi.getPosition()[0] > command.getPosition()[0]+compPos || tdi.getPosition()[0] > command.getPosition()[0]-compPos ||
								tdi.getPosition()[1] < command.getPosition()[1]+compPos || tdi.getPosition()[1] < command.getPosition()[1]-compPos)) // if x or y axis changed
						{
							//SZENARIO A DM
							if(tdi.getState().equals("desktop")) // is in Desktop Mode == kein TDI in Taskbar == kein Fenster offen
							{
								if(tdi.getLocked() == false)
								{
									if(PosInTaskbar(command.getPosition()[0],command.getPosition()[1]) == false)//ist die neue Position in Taskbar?
									{	
										splitIcons();
									}
									else // neue pos in taskbar => setzen des Tdi in taskbar mode öffen von program
									{
										tdi.setState(TDIState.taskbar);
										ProgramHandler.openProgram(tdi.getIcons().get(0)); //open active icon
										//Vibrate(); //500ms TODO
										//setLedRed(); TODO
										tdi.setPosition(0, 0, 0); // linke Ecke der Taskbar
										for(int x=2,y=10; x < tdis.size() ;x++) // set evry other tdi in the middle of the desk
										{
											// TODO set position
											tdis.get(tdis.indexOf(commands.get(x))).setPosition(0, 0, 0+y); // irgendwo am Rand des Tisches (+y damit sie nicht aufeinander fahren)
											tdis.get(tdis.indexOf(commands.get(x))).setState(TDIState.sleep); //
											//setLedGreen(); TODO

											//set one tdi in window mode - position = middle of the desk 
											tdis.get(tdis.indexOf(commands.get(1))).setState(TDIState.window);
											tdis.get(tdis.indexOf(commands.get(1))).setPosition(0, 0, 0); // TODO set position
										}
									}
								}
							}
							if(tdi.getState().equals("taskbar"))
							{
								if(ProgramHandler.getRunningPrograms().size()>0) //if programs are open (at least 1)
								{
									if(PosInTaskbar(command.getPosition()[0],command.getPosition()[1])) // neue pos immer noch in taskbar
									{
										tdi.setPosition(command.getPosition()[0], command.getPosition()[1], tdi.getPosition()[2]);
									}
									else // neue pos außerhalb der taskbar
									{
										for(TDI t: tdis) // find out if any other TDI has focused a window
										{
											if(t.getState().equals("window"))
												windFocused=t;
										}
										if(windFocused != null)
										{
											if(StartScaleMode(tdi,windFocused))
											{
												scaleCount2=scaleCount2+1;
												if(scaleCount2==waitTime)
												{
													scaleCount2=0;
													// skaliermodus TDI repräsentiert untere linke Ecke des Fensters
													taskFocused.setPosition(0, 0, 0);// TODO set position
													tdi.setIsScale(true);
													windFocused.setIsScale(true);
												    int width=(int) (windFocused.getPosition()[0]-tdi.getPosition()[0]);
												    int height=(int) (windFocused.getPosition()[1]-tdi.getPosition()[1]);
													ProgramHandler.resizeProgram(width, height);
												}
												else 
												{
													tdi.setPosition(tdis.indexOf(commands.get(0).getPosition()[0]), tdis.indexOf(commands.get(0).getPosition()[1]), tdis.indexOf(commands.get(0).getPosition()[2]));
												}
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
										tdis.get(tdis.indexOf(commands.get(x))).setState(TDIState.desktop); //?
										tdis.get(tdis.indexOf(commands.get(x))).setPosition(1, 1, 1);// TODO set position
									}
								}
							}
							if(tdi.getState().equals("window"))
							{
								for(TDI t: tdis) // find out taskbar tdi
								{
									if(t.getState().equals("taskbar"))
										taskFocused=t;
									
								}
								// taskbar TDI in nähe des aktuellen
								if(StartScaleMode(taskFocused, tdi))
								{
									scaleCount=scaleCount+1;
										if(scaleCount==waitTime)
										{
											scaleCount=0;
											// skaliermodus TDI repräsentiert obere linke Ecke des Fensters
											tdi.setPosition(0, 0, 0);// TODO set position
											// TODO window TDI repräsentiert obere rechte Ecke des Fensters
											taskFocused.setPosition(0, 0, 0);// TODO set position
											tdi.setIsScale(true);
											taskFocused.setIsScale(true);
										    int width=(int) (taskFocused.getPosition()[0]-tdi.getPosition()[0]);
										    int height=(int) (taskFocused.getPosition()[1]-tdi.getPosition()[1]);
											ProgramHandler.resizeProgram(width, height);
										}
										else 
										{
											tdi.setPosition(tdis.indexOf(commands.get(0).getPosition()[0]), tdis.indexOf(commands.get(0).getPosition()[1]), tdis.indexOf(commands.get(0).getPosition()[2]));
										}
								}
								else
								{
									tdi.setPosition(command.getPosition()[0], command.getPosition()[1], tdi.getPosition()[2]);
									//tdi.getIcons().get(0).setPosition(); new position of program window
								}
							}
							if(tdi.getState().equals("inapp"))
							{
								//TODO in app 
							}
						}
					}	
					//neigen 
					if (tdi.getRotation()[1] != command.getRotation()[1] || tdi.getRotation()[2] != command.getRotation()[2]) {
						
						if(tdi.getState().equals("desktop"))
						{
							//nach rechts neigen (x)
							if (tdi.getRotation()[1] > command.getRotation()[1]+compRot || tdi.getRotation()[1] > command.getRotation()[1]-compRot)
								tdi.toggleLock();
							//tdi.toggleGreenLED();//TODO
						}
						if(tdi.getState().equals("window"))
						{
							//nach oben
							if (tdi.getRotation()[2] > command.getRotation()[2]+compRot || tdi.getRotation()[2] > command.getRotation()[2]-compRot)
							{
								ProgramHandler.toggleMaximization();
								if(ProgramHandler.getNonMinimized()==0)
								{
									tdi.setState(TDIState.desktop);
									tdi.setPosition(1, 1, 1);
									splitIcons();
								}
							}
							//nach links neigen
							if(tdi.getRotation()[1] < command.getRotation()[1]+compRot || tdi.getRotation()[1] < command.getRotation()[1]-compRot)
							{
								ProgramHandler.closeProgram();
								tdi.getIcons().remove(0);
							}
							// TDI nach unten neigen
							if (tdi.getRotation()[2] < command.getRotation()[2]+compRot || tdi.getRotation()[2] < command.getRotation()[2]-compRot) {
								ProgramHandler.minimize(); //TODO When still focused, move TDI when not for icons
								if(ProgramHandler.isDesktopMode())
								{
									tdi.setState(TDIState.desktop);
									tdi.setPosition(1, 1, 1); // TODO set pos
									splitIcons();
								}
								else
								{
									tdi.setPosition(1, 1, 1);// TODO set pos
									//TODO GO to location of window
								};	
							}
						}
						if(tdi.getState().equals("taskbar"))
						{

							/* TDI nach oben neigen
							Alle Fenster werden wiederhergestellt */
							//nach oben
							if (tdi.getRotation()[2] > command.getRotation()[2]+compRot || tdi.getRotation()[2] > command.getRotation()[2]-compRot)
							{
								ProgramHandler.restoreAllPrograms();
								if(ProgramHandler.isDesktopMode())
								{
									if(!(tdis.get(1).getState().equals("taskbar")))
									{
										tdis.get(1).setState(TDIState.window);
										tdis.get(1).setPosition(1, 1, 1); //TODO to maximised window
									}
									else
									{
										tdis.get(2).setState(TDIState.window);
										tdis.get(1).setPosition(1, 1, 1); //TODO to maximised window
									}
								}
								else
								{
									for(TDI t:tdis)
									{
										if(t.getState().equals("window"))
										{
											t.setPosition(1, 1, 1); //TODO to maximised window
										}
									}
								}
							}
							//nach links/rechts neigen
							if(tdi.getRotation()[1] < command.getRotation()[1]+compRot || tdi.getRotation()[1] > command.getRotation()[1]-compRot || 
								tdi.getRotation()[2] < command.getRotation()[2]+compRot || tdi.getRotation()[2] > command.getRotation()[2]-compRot)
							{
								ProgramHandler.closeAllPrograms();
								for(TDI t:tdis)
								{
									t.setState(TDIState.desktop);
								}
							}
							// TDI nach unten neigen
							if (tdi.getRotation()[2] < command.getRotation()[2]+compRot || tdi.getRotation()[2] < command.getRotation()[2]-compRot) {
								ProgramHandler.minimizeAllPrograms();
								for(TDI t:tdis)
								{
									if(t.getState().equals("window"))
									{
										t.setState(TDIState.desktop);
										tdi.setPosition(1, 1, 1);// TODO set pos
										splitIcons();
									}

								}
							}
						}
						if(tdi.getState().equals("inapp"))
						{
							//nach rechts neigen
							if(tdi.getRotation()[1] > command.getRotation()[1]+compRot || tdi.getRotation()[1] > command.getRotation()[1]-compRot)
							{
								tdi.setState(TDIState.window);
								// TODO server var
							}
						}

					}

					// drehen
					if(tdi.getRotation()[0] != command.getRotation()[0])
					{
						if(tdi.getState().equals("taskbar"))
						{
							//nicht im Skaliermodus
							if(tdi.isScale()==false)
							{
								//nach links Das vorherige Fenster in der Taskleiste wird fokussiert
								if(tdi.getRotation()[0] < command.getRotation()[0]+compRot || tdi.getRotation()[0] < command.getRotation()[0]-compRot)
								{
									tdi.setRotation(command.getRotation());
									tdi.getIcons().set(0, tdi.getIcons().get(tdi.getIcons().size()));
								}
								//nach rechts Das nächste Fenster in der Taskleiste wird fokussiert
								if(tdi.getRotation()[0] > command.getRotation()[0]+compRot || tdi.getRotation()[0] > command.getRotation()[0]-compRot)
								{
									tdi.setRotation(command.getRotation());
									tdi.getIcons().set(0, tdi.getIcons().get(1));
								}
							}
						}
						if(tdi.getState().equals("desktop"))
						{
							//nach links Das vorherige Icon, wofür das TDI zuständig ist wird ausgewählt
							if(tdi.getRotation()[0] < command.getRotation()[0]+compRot || tdi.getRotation()[0] < command.getRotation()[0]-compRot)
							{
								tdi.setRotation(command.getRotation());
								tdi.getIcons().set(0, tdi.getIcons().get(tdi.getIcons().size()));
							}
							//nach rechts Das nächste Icon, wofür das TDI zuständig ist wird ausgewählt
							if(tdi.getRotation()[0] > command.getRotation()[0]+compRot || tdi.getRotation()[0] > command.getRotation()[0]-compRot)
							{
								tdi.setRotation(command.getRotation());
								tdi.getIcons().set(0, tdi.getIcons().get(1));
							}
						}
					}
				}
				Executor.saveBackground(wallpaper.markArea(tdis));
			}
			
		}
		
	}

	/**
	 * checks if givenPos is in taskbar //TODO Methode
	 * @return
	 */
	private boolean PosInTaskbar(float x, float y)
	{
		return true;
	}
	/**
	 * checks if a taskbar TDI and a window TDI are near enough to start the scale mode
	 */
	private boolean StartScaleMode(TDI t1, TDI t2)
	{
		int range = 5;
		if(t1.getPosition()[0] <= t2.getPosition()[0]+range && t1.getPosition()[0] >= t2.getPosition()[0]-range ||
				t1.getPosition()[1] <= t2.getPosition()[1]+range && t1.getPosition()[1] >= t2.getPosition()[1]-range)
			return true;
		if(t2.getPosition()[0] <= t1.getPosition()[0]+range && t2.getPosition()[0] >= t1.getPosition()[0]-range ||
				t2.getPosition()[1] <= t1.getPosition()[1]+range && t2.getPosition()[1] >= t1.getPosition()[1]-range)
			return true;
		else
			return false;
	}

	public BigLogic() {
		configLoader = new ConfigLoader();
		pluginTableModel=new PluginTableModel(configLoader.getPlugins());
		tdiDialog=new TDIDialog(this, pluginTableModel);
		
		icons = configLoader.loadIcons();
		Collections.sort(icons);
		wallpaper=new Wallpaper(configLoader.loadWallpaper(), configLoader.getBlockSize());
	}
	
	public void splitIcons() {
		int iconsAssigned = 0;
		for (int i = 0; i < tdis.size(); i++) {
			int f = (icons.size() - iconsAssigned) / (tdis.size() - i);
			if ((icons.size() - iconsAssigned) % (tdis.size() - i) > 0)
				f++;
			tdis.get(i).setIcons(
					new ArrayList<Icon>(icons.subList(iconsAssigned,
							iconsAssigned += f)));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		tdiDialog.setErrorMessage("");
		if(e.getActionCommand().equals("Restore"));
			//TODO Restore;
		else
		{		
			String ip;
			if( (ip = checkIp(tdiDialog.getIp1().getText(),tdiDialog.getIp2().getText(),tdiDialog.getIp3().getText(),tdiDialog.getIp4().getText())) == null) return;			
			//IP address is correct
			new Runnable() {
				public void run() {
					String[] plugins = new String[pluginTableModel.getRowCount()];
					for(int i = 0, i2 = 0; i < plugins.length; i++)
						if((boolean)pluginTableModel.getValueAt(i, 1) == true) 
							plugins[i2++] = (String) pluginTableModel.getValueAt(i, 0);
					Executor.startPlugins(plugins);
					configLoader.savePlugins(plugins);						
				}
			}.run();
			server = new Server("192.168.43.12");
			tdis = server.fullPose();
			splitIcons();
			//TODO Positionen für TDIs am Tisch berechnen, (besprechen!)
			Timer mo = new Timer();
			mo.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					ArrayList<TDI> tdis = server.fullPose();
					if(tdis!=null)
						for (TDI t : tdis)
							commands.add(t);
				}
			}, 0, 500);
			// startTDI clicked
			if (e.getActionCommand().equals("Start/Connect"));//TODO Choose name			
			if(e.getActionCommand().equals("Tutorial starten"))
				new Thread(new TutorialLogic(tdis)).start();
		}
	}
	
	/**
	 * Checks if the entered IP has a valid format and converts it to an integer
	 * 
	 * @return int ip
	 * @throws NumberFormatException
	 * */
	private String checkIp(String ip1, String ip2, String ip3, String ip4) {
		int fullIP[] = new int[4];
		String ip = null;
		// Checks if appropriate length
		if ((ip1.length() <= 3) && (ip2.length() <= 3)
				&& (ip3.length() <= 3)
				&& (ip4.length() <= 3)) {
			// not null check
			if ((ip1.length() > 0) && (ip2.length() > 0)
					&& (ip3.length() > 0)
					&& (ip4.length() > 0)) {
				try {
					// checks format
					fullIP[0] = Integer.parseInt(ip1);
					fullIP[1] = Integer.parseInt(ip2);
					fullIP[2] = Integer.parseInt(ip3);
					fullIP[3] = Integer.parseInt(ip4);

					// checks ip<255
					if ((fullIP[0] < 255) && (fullIP[1] < 255)
							&& (fullIP[2] < 255) && (fullIP[3] < 255)) {
						ip = fullIP[0] + "." + fullIP[1] + "." + fullIP[2]
								+ "." + fullIP[3];
						return ip;
					} else
						tdiDialog.setErrorMessage("Number must be <255");
				} catch (NumberFormatException e) {
					tdiDialog.setErrorMessage("Please enter only numbers");
				}
			} else
				tdiDialog.setErrorMessage("Input must not be empty");
		} else
			tdiDialog.setErrorMessage("Input only in the following format: 000.000.000.000");
		return ip;
	}
}

