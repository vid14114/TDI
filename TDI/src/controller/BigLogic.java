package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import model.ConfigLoader;
import model.PluginServer;
import model.PluginTableModel;
import model.Server;
import model.TDIDirectories;
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
	private PluginServer plugserv = new PluginServer();
	/**
	 * var used to prevent overwriting of tdis
	 * 1 = forward
	 * 0 = don't forward
	 */
	int forward = 1;
	/**
	 * counter for scaling
	 */
	private int scaleCount=0;
	/**
	 * Compensation Value for position change
	 */
	int compPos = 1;
	float compPos2[]={5,5,5};
	/**
	 * times(1s = 100ms) to wait for scaling
	 */
	private int waitTime=5; 

	private int compHeight=200;
	
	private TDI otherFocused;
	
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
		TDIDirectories.createDirectories();
		new Thread(new BigLogic()).start();
	}

	/**
	 * The run method that is overridden
	 */
	public void run() {

		while(true)
		{
			if (commands.size() > 0) {
				if (tdis.contains(commands.get(0))) {
					TDI tdi = tdis.get(tdis.indexOf(commands.get(0)));
				//	System.out.println(tdi.toString());
					TDI command = commands.get(0);
					System.out.println(tdi.getPosition()[0]);
					forward=1;
					//Position
					if(Arrays.equals(tdi.getPosition(), command.getPosition())==false)
					{
						if(tdi.getPosition()[0] != command.getPosition()[0])
						{
							if(tdi.getPosition()[0] >= command.getPosition()[0]+compPos || tdi.getPosition()[0] >= command.getPosition()[0]-compPos)
							{
								Move(tdi, commands.get(0));
							}
							else
								if(tdi.getPosition()[0] <= command.getPosition()[0]+compPos || tdi.getPosition()[0] <= command.getPosition()[0]-compPos)
								{
									Move(tdi, commands.get(0));
								}
						}
						else
							if(tdi.getPosition()[1] != command.getPosition()[1])
							{
								if(tdi.getPosition()[1] <= command.getPosition()[1]+compPos || tdi.getPosition()[1] <= command.getPosition()[1]-compPos)
								{
									Move(tdi, commands.get(0));
								}
								else
									if(tdi.getPosition()[1]+compPos > command.getPosition()[1] || tdi.getPosition()[1] >= command.getPosition()[1]-compPos)
									{
										Move(tdi, commands.get(0));
									}
							}
							else
								if(tdi.getPosition()[2] != command.getPosition()[2])
								{
									if((tdi.getPosition()[2]-compHeight) <= command.getPosition()[2]+compPos || (tdi.getPosition()[2]-compHeight) <= command.getPosition()[2]-compPos)
									{
										LiftUp(tdi, commands.get(0));
									}
									else
										if((tdi.getPosition()[2]-compHeight) >= command.getPosition()[2]+compPos || (tdi.getPosition()[2]-compHeight) >= command.getPosition()[2]-compPos)
										{
											PutDown(tdi, commands.get(0));
										}
								}
						forward=0;
					}						
					//Rotation
					if(Arrays.equals(tdi.getRotation(), command.getRotation())==false && forward==1)
					{
						if(tdi.getRotation()[0] != command.getRotation()[0])
						{
							if(tdi.getRotation()[0] >= command.getRotation()[0]+compPos || tdi.getRotation()[0] >= command.getRotation()[0]-compPos)
							{
								RotateRight(tdi, commands.get(0));
							}
							else
								if(tdi.getPosition()[0] <= command.getPosition()[0]+compPos || tdi.getRotation()[0] <= command.getRotation()[0]-compPos)
								{
									RotateLeft(tdi, commands.get(0));
								}
						}
						else
							if(tdi.getRotation()[1] != command.getRotation()[1])
							{
								if(tdi.getRotation()[1] <= command.getRotation()[1]+compPos || tdi.getRotation()[1] <= command.getRotation()[1]-compPos)
								{
									TiltLeft(tdi, commands.get(0));
								}
								else
									if(tdi.getRotation()[1]+compPos > command.getRotation()[1] || tdi.getRotation()[1] >= command.getRotation()[1]-compPos)
									{
										TiltRight(tdi, commands.get(0));
									}
							}
							else
								if(tdi.getRotation()[2] != command.getRotation()[2])
								{
									if((tdi.getRotation()[2]-compHeight) <= command.getRotation()[2]+compPos || (tdi.getRotation()[2]-compHeight) <= command.getRotation()[2]-compPos)
									{
										TiltDown(tdi, commands.get(0));
									}
									else
										if((tdi.getRotation()[2]-compHeight) >= command.getRotation()[2]+compPos || (tdi.getRotation()[2]-compHeight) >= command.getRotation()[2]-compPos)
										{
											TiltUp(tdi, commands.get(0));
										}
								}
						}
					}
				}
			}
	}
	private void Move(TDI tdi, TDI commands)
	{
		switch(tdi.getState().toString())
		{
		case "desktop":
			//scale
			if(tdi.getLocked())
			{
				if(ProgramHandler.getRunningPrograms().size()==0)
				{
					if(PosInTaskbar(commands.getPosition()))
					{
						if(EmptyTaskbar()==false)
						{//B1
							ProgramHandler.openProgram(tdi.getIcons().get(0));
							tdi.setState(TDIState.window);
							tdi.toggleLock();
							tdi.setPosition(commands.getPosition()[0], commands.getPosition()[1], commands.getPosition()[2]);
						}
						else
						{//B
							//TDI.vibrate()
							ProgramHandler.openProgram(tdi.getIcons().get(0));
							tdi.setState(TDIState.taskbar);
							tdi.toggleLock();
							tdi.setPosition(commands.getPosition()[0], commands.getPosition()[1], commands.getPosition()[2]);
						}
					}
				}
				else
				{
					tdi.setPosition(commands.getPosition()[0], commands.getPosition()[1], commands.getPosition()[2]);
					ProgramHandler.moveProgram((int)tdi.getPosition()[0], (int)tdi.getPosition()[1]);
				}
			}
			else
			{
				if(ProgramHandler.getRunningPrograms().size()==0)
				{
					if(PosInTaskbar(commands.getPosition()))
					{
						if(EmptyTaskbar())
						{//B2
							tdi.setState(TDIState.taskbar);
							SetOtherTDI(TDIState.window,tdi);
							tdi.toggleLock();
							tdi.setPosition(commands.getPosition()[0], commands.getPosition()[1], commands.getPosition()[2]);
						}
					}
					else
					{//A
						tdi.setPosition(commands.getPosition()[0], commands.getPosition()[1], commands.getPosition()[2]);
						splitIcons();
					}
				}
				else
				{//D
					tdi.setState(TDIState.inapp);
					//server var.
				}
			}
			;break;
		case "taskbar":
			//scale
			if(tdi.isScale()==false)
			{
				if(StartScaleMode(commands,TDIState.window))
				{
					scaleCount+=1;
					if(scaleCount==waitTime)
					{//A
						scaleCount=0;
						tdi.setIsScale(true);
						otherFocused.setIsScale(true);
					    int width=(int) (otherFocused.getPosition()[0]-tdi.getPosition()[0]);
					    int height=(int) (otherFocused.getPosition()[1]-tdi.getPosition()[1]);
						ProgramHandler.resizeProgram(width, height);
						tdi.setPosition(0, 0, tdi.getPosition()[2]); //TODO
						otherFocused.setPosition(0, 0, otherFocused.getPosition()[2]);// param übernahme
						//vibrate? LED? some notification?
					}
				}
				else
				{
					if(ProgramHandler.getRunningPrograms().size()>0)
					{
						if(ProgramHandler.getNonMinimized()==0)
						{//B
							tdi.setState(TDIState.desktop);
							tdi.setPosition(commands.getPosition()[0], commands.getPosition()[1], commands.getPosition()[2]);
							for(TDI t:tdis)
							{
								t.setState(TDIState.desktop);
							}
							splitIcons();
						}
					}
				}
			}
			else
			{
				//wenn bereits im scale
			    int width=(int) (otherFocused.getPosition()[0]-tdi.getPosition()[0]);
			    int height=(int) (otherFocused.getPosition()[1]-tdi.getPosition()[1]);
				ProgramHandler.resizeProgram(width, height);
				tdi.setPosition(0, 0, tdi.getPosition()[2]); //TODO
				otherFocused.setPosition(0, 0, otherFocused.getPosition()[2]);// param übernahme
			}
			break;
		case "window":
			if(tdi.getLocked()==false)
			{
				if(tdi.isScale()==false)
				{
					if(StartScaleMode(commands,TDIState.taskbar))
					{
						scaleCount+=1;
						if(scaleCount==waitTime)
						{
							scaleCount=0;
							tdi.setIsScale(true);
							otherFocused.setIsScale(true);
						    int width=(int) (otherFocused.getPosition()[0]-tdi.getPosition()[0]);
						    int height=(int) (otherFocused.getPosition()[1]-tdi.getPosition()[1]);
							ProgramHandler.resizeProgram(width, height);
							tdi.setPosition(0, 0, tdi.getPosition()[2]); //TODO
							otherFocused.setPosition(0, 0, otherFocused.getPosition()[2]);// param übernahme
							//vibrate? LED? some notification?
						}
					}
				}
				else
				{
					//wenn bereits im scale
				    int width=(int) (otherFocused.getPosition()[0]-tdi.getPosition()[0]);
				    int height=(int) (otherFocused.getPosition()[1]-tdi.getPosition()[1]);
					ProgramHandler.resizeProgram(width, height);
					tdi.setPosition(0, 0, tdi.getPosition()[2]); //TODO
					otherFocused.setPosition(0, 0, otherFocused.getPosition()[2]);// param übernahme
				}
			}
			else
			{
				tdi.setPosition(commands.getPosition()[0], commands.getPosition()[1], commands.getPosition()[2]);
				ProgramHandler.moveProgram((int)tdi.getPosition()[0], (int)tdi.getPosition()[1]);
			}
			;break;
		case "inapp":
			tdi.setPosition(commands.getPosition()[0], commands.getPosition()[1], commands.getPosition()[2]);
			plugserv.sendMessage(tdi.getId(), tdi.getPosition()[0], tdi.getPosition()[1], tdi.getPosition()[2], tdi.getRotation());
			;break;
		case "sleep":
			if(ProgramHandler.getNonMinimized()==0)
			{
				tdi.setState(TDIState.desktop);
				splitIcons();
			}
			else
				tdi.setState(TDIState.desktop);
			;break;
		}
	}
	private void LiftUp(TDI tdi, TDI commands)//heben
	{
		switch(tdi.getState().toString())
		{
		case "desktop": break;
		case "taskbar": break;
		case "window": break;
		case "inapp": 
			tdi.setPosition(commands.getPosition()[0], commands.getPosition()[1], commands.getPosition()[2]);
			plugserv.sendMessage(tdi.getId(), tdi.getPosition()[0], tdi.getPosition()[1], tdi.getPosition()[2], tdi.getRotation());
			break;
		case "sleep":
			if(ProgramHandler.getNonMinimized()==0)
			{
				tdi.setState(TDIState.desktop);
				splitIcons();
			}
			else
				tdi.setState(TDIState.desktop);
			break;
		}
	}
	private void PutDown(TDI tdi, TDI commands)//senken
	{
		switch(tdi.getState().toString())
		{
		case "desktop": break;
		case "taskbar": break;
		case "window": break;
		case "inapp": 
			tdi.setPosition(commands.getPosition()[0], commands.getPosition()[1], commands.getPosition()[2]);
			plugserv.sendMessage(tdi.getId(), tdi.getPosition()[0], tdi.getPosition()[1], tdi.getPosition()[2], tdi.getRotation());
			break;
		case "sleep":
			if(ProgramHandler.getNonMinimized()==0)
			{
				tdi.setState(TDIState.desktop);
				splitIcons();
			}
			else
				tdi.setState(TDIState.desktop);
			break;
		}
	}
	private void RotateRight(TDI tdi, TDI commands)
	{
		switch(tdi.getState().toString())
		{
		case "desktop":
			if(ProgramHandler.getNonMinimized()==0)
			{
				tdi.getIcons().set(0, tdi.getIcons().get(1));
				tdi.setRotation(commands.getRotation());
			}
			break;
		case "taskbar":
			if(ProgramHandler.getRunningPrograms().size()>1)
			{
				ProgramHandler.restoreRight();
			}
			break;
		case "inapp": 
			tdi.setRotation(commands.getRotation());
			plugserv.sendMessage(tdi.getId(), tdi.getPosition()[0], tdi.getPosition()[1], tdi.getPosition()[2], tdi.getRotation());
			break;
		case "sleep":
			if(ProgramHandler.getNonMinimized()==0)
			{
				tdi.setState(TDIState.desktop);
				splitIcons();
			}
			else
				tdi.setState(TDIState.desktop);
			break;
		}
	}
	private void RotateLeft(TDI tdi, TDI commands)
	{
		switch(tdi.getState().toString())
		{
		case "desktop":
				if(ProgramHandler.getNonMinimized()==0)
				{
					tdi.getIcons().set(0, tdi.getIcons().get(tdi.getIcons().size()-1));
					tdi.setRotation(commands.getRotation());
				}
			break;
		case "taskbar":
				if(ProgramHandler.getRunningPrograms().size()>1)
				{
					ProgramHandler.restoreLeft();
				}
			break;
		case "inapp": 
			tdi.setRotation(commands.getRotation());
			plugserv.sendMessage(tdi.getId(), tdi.getPosition()[0], tdi.getPosition()[1], tdi.getPosition()[2], tdi.getRotation());
			break;
		case "sleep":
			if(ProgramHandler.getNonMinimized()==0)
			{
				tdi.setState(TDIState.desktop);
				splitIcons();
			}
			else
				tdi.setState(TDIState.desktop);
			break;
		}
	}
	private void TiltRight(TDI tdi, TDI commands)
	{
		switch(tdi.getState().toString())
		{
		case "desktop": 
			tdi.toggleLock();
			//toggle green LED
			break;
		case "taskbar":
			ProgramHandler.closeAllPrograms();
			tdi.setRotation(commands.getRotation());
			for(TDI t: tdis)
			{
				t.setState(TDIState.desktop);
			}
			splitIcons();
			break;
		case "inapp":
			tdi.setRotation(commands.getRotation());
			tdi.setState(TDIState.window);
			break;
		case "sleep": 
			if(ProgramHandler.getNonMinimized()==0)
			{
				tdi.setState(TDIState.desktop);
				splitIcons();
			}
			else
				tdi.setState(TDIState.desktop);
			break;
		}
	}
	private void TiltLeft(TDI tdi, TDI commands)
	{
		switch(tdi.getState().toString())
		{
		case "taskbar": 
			ProgramHandler.closeAllPrograms();
			tdi.setRotation(commands.getRotation());
			for(TDI t: tdis)
			{
				t.setState(TDIState.desktop);
			}
			splitIcons();
			break;
		case "window":
			ProgramHandler.closeProgram();
			break;
		case "inapp":
			tdi.setRotation(commands.getRotation());
			plugserv.sendMessage(tdi.getId(), tdi.getPosition()[0], tdi.getPosition()[1], tdi.getPosition()[2], tdi.getRotation());
			break;
		case "sleep":
			if(ProgramHandler.getNonMinimized()==0)
			{
				tdi.setState(TDIState.desktop);
				splitIcons();
			}
			else
				tdi.setState(TDIState.desktop);
			break;
		}
	}
	private void TiltUp(TDI tdi, TDI commands)
	{
		switch(tdi.getState().toString())
		{
		case "taskbar":
			ProgramHandler.restoreAllPrograms();
			int count=0;
			for(TDI t:tdis)
			{
				if(t.getState().equals(TDIState.window))
				count++;
			}
			if(count==0)
			{
				if(tdis.get(0).getState().equals(TDIState.window))
				tdis.get(1).setState(TDIState.window);
				else
				tdis.get(0).setState(TDIState.window);
			}
				break;
		case "window": 
			ProgramHandler.toggleMaximization();//
			tdi.setRotation(commands.getRotation());
			if(ProgramHandler.getNonMinimized()==0)
			{
				tdi.setState(TDIState.desktop);
				splitIcons();
			}
			break;
		case "inapp": 
			tdi.setRotation(commands.getRotation());
			plugserv.sendMessage(tdi.getId(), tdi.getPosition()[0], tdi.getPosition()[1], tdi.getPosition()[2], tdi.getRotation());
			break;
		case "sleep":
			if(ProgramHandler.getNonMinimized()==0)
			{
				tdi.setState(TDIState.desktop);
				splitIcons();
			}
			else
				tdi.setState(TDIState.desktop);
			break;
		}
	}
	private void TiltDown(TDI tdi, TDI commands)
	{
		switch(tdi.getState().toString())
		{
		case "taskbar": 
			ProgramHandler.minimizeAllPrograms();
			tdi.setRotation(commands.getRotation());
			for(TDI t: tdis)
			{
				t.setState(TDIState.desktop);
			}
			splitIcons();
			break;
		case "window": 
			ProgramHandler.minimize();
			tdi.setRotation(commands.getRotation());
			if(ProgramHandler.getNonMinimized()==0)
			{
				tdi.setState(TDIState.desktop);
				splitIcons();
			}
			break;
		case "inapp":
			tdi.setRotation(commands.getRotation());
			plugserv.sendMessage(tdi.getId(), tdi.getPosition()[0], tdi.getPosition()[1], tdi.getPosition()[2], tdi.getRotation());
			break;
		case "sleep":
			if(ProgramHandler.getNonMinimized()==0)
			{
				tdi.setState(TDIState.desktop);
				splitIcons();
			}
			else
				tdi.setState(TDIState.desktop);
			break;
		}
	}

	private boolean EmptyTaskbar()
	{
		for(TDI t:tdis)
		{
			if(t.getState().equals(TDIState.taskbar))
				return false;
			else
				return true;
		}
		return true;
	}
	public void SetOtherTDI(TDIState state, TDI tdi)
	{
		if(!(tdis.get(0).equals(tdi)))
		{
			tdis.get(0).setState(state);
			tdis.get(0).getIcons().add(0,tdi.getIcons().get(0));
			tdi.getIcons().remove(0);
		}
		else
		{
			tdis.get(1).setState(state);
			tdis.get(1).getIcons().add(0,tdi.getIcons().get(0));
			tdi.getIcons().remove(0);
		}
	}
	public ArrayList<Icon> getIcons() {
		return icons;
	}

	public void setIcons(ArrayList<Icon> icons) {
		this.icons = icons;
	}

	public ArrayList<TDI> getTdis() {
		return tdis;
	}

	public void setTdis(ArrayList<TDI> tdis) {
		this.tdis = tdis;
	}

	public ArrayList<TDI> getCommands() {
		return commands;
	}

	public void setCommands(ArrayList<TDI> commands) {
		this.commands = commands;
	}

	/**
	 * checks if givenPos is in taskbar //TODO Methode
	 * @return
	 */
	private boolean PosInTaskbar(float[] pos)
	{
		return true;
	}
	/**
	 * checks if a taskbar TDI and a window TDI are near enough to start the scale mode
	 */
	private boolean StartScaleMode(TDI t1, TDIState state)
	{
		int range = 5;
		for(TDI t2: tdis)
		{
			if(t2.getState().equals(state))
			{
				otherFocused=t2;
				if(t1.getPosition()[0] <= t2.getPosition()[0]+range && t1.getPosition()[0] >= t2.getPosition()[0]-range ||
						t1.getPosition()[1] <= t2.getPosition()[1]+range && t1.getPosition()[1] >= t2.getPosition()[1]-range)
					return true;
				if(t2.getPosition()[0] <= t1.getPosition()[0]+range && t2.getPosition()[0] >= t1.getPosition()[0]-range ||
						t2.getPosition()[1] <= t1.getPosition()[1]+range && t2.getPosition()[1] >= t1.getPosition()[1]-range)
					return true;
				else
					return false;
			}
		}
		return false;
	}

	public BigLogic() {
		configLoader = new ConfigLoader();
		pluginTableModel=new PluginTableModel(configLoader.getPlugins());
		tdiDialog=new TDIDialog(this, pluginTableModel);
		
		icons = configLoader.loadIcons();
		Collections.sort(icons);
//		wallpaper=new Wallpaper(configLoader.loadWallpaper(), configLoader.getBlockSize(), configLoader.getPanelSize(), configLoader.getPlacementRatio());
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
	//	if(e.getActionCommand().equals(TDIState.restore));
			//TODO Restore;
	//	else
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
			server = new Server("192.168.1.36");
			tdis = server.fullPose();
			splitIcons();
			Executor.saveBackground(wallpaper.markArea(tdis));
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

