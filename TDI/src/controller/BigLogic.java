package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 * <p>
 * Position(x,y,z)
 * heben => z neu > (z alt - var) //e.g. var = 200; [not used]
 * rechts bewegen: x alt > x neu
 * links bewegen: x alt < x neu
 * oben bewegen: y alt > y neu
 * unten bewegen: y alt < y neu
 */
public class BigLogic implements ActionListener {

    private final ConfigLoader configLoader;
    private final TDIDialog tdiDialog;
    private final PluginTableModel pluginTableModel;
    private final PluginServer plugserv = new PluginServer();
    private ArrayList<Icon> icons;
    private ArrayList<TDI> tdis;
    private Server server;    

	/**
     * counter for scaling
     */
    private int scaleCount = 0;

    private TDI otherFocused;
    
    private float defaultRotX=0;

    /**
     * The wallpaper
     */
    private Wallpaper wallpaper;
    public Wallpaper getWallpaper() {
		return wallpaper;
	}
    /**
     *	Compensation Value for position change
     */
    private int compHeight = 200;
    private int compPos = 1;
    
    private int thresholdRotation=10;

    public BigLogic() {
        configLoader = new ConfigLoader();
        pluginTableModel = new PluginTableModel(configLoader.getPlugins());
        tdiDialog = new TDIDialog(this, pluginTableModel);
        
        icons = configLoader.loadIcons();
        Collections.sort(icons);
        wallpaper = new Wallpaper(configLoader.loadWallpaper(), configLoader.getBlockSize(), configLoader.getPanelSize(), configLoader.getPlacementRatio(), configLoader.loadScreensize());
        
        tilt = new Tilt(new float[]{0,0,0});
    	tilt.setTiltListener(new TiltHandler(this));    	
    	
    	rotation = new Rotation(tdis);
    	rotation.setRotationListener(new RotationHandler(this));
    	
    	move = new Move(tdis);
    	move.setMoveListener(new MoveHandler(this));
    }

    Tilt tilt;
    Rotation rotation;
    Move move;
    /**
     * Lädt Dialog und Desktop configuration
     *
     * @param args
     */
    public static void main(String[] args) {
        TDIDirectories.createDirectories();
        new BigLogic();
    }
           
    public boolean checkMovedToTaskbar(TDI tdi){    	
    	if(posInTaskbar(tdi.getPosition())) 
    		return true;
    	return false;
    }
        

//    private void move(TDI tdi, TDI commands) {//TODO major rewrite
//        switch (tdi.getState().toString()) {            
//                       if (!tdi.isScale()) {
//    if (startScaleMode(commands, TDIState.window)) {
//        scaleCount += 1;
//        int waitTime = 5;
//        if (scaleCount == waitTime) {//A
//            scaleCount = 0;
//            tdi.setIsScale(true);
//            otherFocused.setIsScale(true);
//            int width = (int) (otherFocused.getPosition()[0] - tdi.getPosition()[0]);
//            int height = (int) (otherFocused.getPosition()[1] - tdi.getPosition()[1]);
//            ProgramHandler.resizeProgram(width, height);
//            tdi.setPosition(0, 0, tdi.getPosition()[2]); //TODO
//            otherFocused.setPosition(0, 0, otherFocused.getPosition()[2]);// param übernahme
//            //vibrate? LED? some notification?
//        }
//    } else {
//        if (ProgramHandler.getRunningPrograms().size() > 0) {
//            if (ProgramHandler.getNonMinimized() == 0) {//B
//                tdi.setState(TDIState.desktop);
//                tdi.setPosition(commands.getPosition()[0], commands.getPosition()[1], commands.getPosition()[2]);
//                for (TDI t : tdis) {
//                    t.setState(TDIState.desktop);
//                }
//                splitIcons();
//            }
//        }
//    }
//} else {
//    //wenn bereits im scale
//    int width = (int) (otherFocused.getPosition()[0] - tdi.getPosition()[0]);
//    int height = (int) (otherFocused.getPosition()[1] - tdi.getPosition()[1]);
//    ProgramHandler.resizeProgram(width, height);
//    tdi.setPosition(0, 0, tdi.getPosition()[2]); //TODO
//    otherFocused.setPosition(0, 0, otherFocused.getPosition()[2]);// param übernahme
//}
//            

    private void liftUp(TDI tdi, TDI commands)//heben
    {
        if(tdi.getState().equals(TDIState.inapp)){
            plugserv.sendMessage(tdi.getId(), tdi.getPosition(), tdi.getRotation());
        }
    }

    private void putDown(TDI tdi, TDI commands)//senken
    {
    	if(tdi.getState().equals(TDIState.inapp)){
            plugserv.sendMessage(tdi.getId(), tdi.getPosition(), tdi.getRotation());
        }
    }
    
    /**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

    /**
     * @return true if any (other) TDI is in taskbar state
     */
    public boolean emptyTaskbar() {
        for (TDI t : tdis) {
            if (t.getState().equals(TDIState.taskbar))
                return false;
        }
        return true;
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
   
    /**
     * checks if givenPos is in taskbar //TODO Methode
     *
     * @return
     */
    private boolean posInTaskbar(float[] pos) {
        return true;
    }

    /**
     * checks if a taskbar TDI and a window TDI are near enough to start the scale mode
     */
    private boolean startScaleMode(TDI t1, TDIState state) {
        int range = 5;
        for (TDI t2 : tdis) {
            if (t2.getState().equals(state)) {
                otherFocused = t2;
                return t1.getPosition()[0] <= t2.getPosition()[0] + range && t1.getPosition()[0] >= t2.getPosition()[0] - range || t1.getPosition()[1] <= t2.getPosition()[1] + range && t1.getPosition()[1] >= t2.getPosition()[1] - range || t2.getPosition()[0] <= t1.getPosition()[0] + range && t2.getPosition()[0] >= t1.getPosition()[0] - range || t2.getPosition()[1] <= t1.getPosition()[1] + range && t2.getPosition()[1] >= t1.getPosition()[1] - range;
            }
        }
        return false;
    }

    public void splitIcons() {
        int iconsAssigned = 0;
        for (int i = 0; i < tdis.size(); i++) {
            int f = (icons.size() - iconsAssigned) / (tdis.size() - i);
            if ((icons.size() - iconsAssigned) % (tdis.size() - i) > 0)
                f++;
            tdis.get(i).setIcons(
                    new ArrayList<Icon>(icons.subList(iconsAssigned,
                            iconsAssigned += f))
            );
            tdis.get(i).calculateRotationLimit();
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
            if ((ip = checkIp(tdiDialog.getIp1().getText(), tdiDialog.getIp2().getText(), tdiDialog.getIp3().getText(), tdiDialog.getIp4().getText())) == null)
                return;
            //IP address is correct
            new Runnable() {
                public void run() {
                    String[] plugins = new String[pluginTableModel.getRowCount()];
                    for (int i = 0, i2 = 0; i < plugins.length; i++)
                        if ((boolean) pluginTableModel.getValueAt(i, 1))
                            plugins[i2++] = (String) pluginTableModel.getValueAt(i, 0);
                    Executor.startPlugins(plugins);
                    configLoader.savePlugins(plugins);
                }
            }.run();
            server = new Server("192.168.43.32");
            tdis = server.fullPose();
            defaultRotX=tdis.get(0).getRotation()[0];
            splitIcons();
            Executor.saveBackground(wallpaper.markArea(tdis));
            //TODO Positionen für TDIs am Tisch berechnen, (besprechen!)
            Timer mo = new Timer();
            mo.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    ArrayList<TDI> tdis = server.fullPose();
                    if (tdis != null)
                        for (TDI t : tdis){
                            newCommand(t);
                        }
                }
            }, 0, 500);
            // startTDI clicked
            if (e.getActionCommand().equals("Tutorial starten"))
                new Thread(new TutorialLogic(tdis)).start();
        }
    }

    /**
     * Checks if the entered IP has a valid format and converts it to an integer
     *
     * @return int ip
     * @throws NumberFormatException
     */
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
        return null;
    }

	public PluginServer getPlugserv() {
		return plugserv;
	}
	
	public void newCommand(TDI command){
		tilt.tilt(command);
		move.move(command);
		rotation.rotate(command);
		if(tdis.get(tdis.indexOf(command)).getState().equals(TDIState.inapp))
			plugserv.sendMessage(command.getId(), command.getPosition(), command.getRotation());
	}
}