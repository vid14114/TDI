package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import model.ConfigLoader;
import model.PluginServer;
import model.PluginTableModel;
import model.Server;
import model.TDIDirectories;
import model.TDILogger;
import view.Icon;
import view.TDI;
import view.TDI.TDIState;
import view.TDIDialog;
import view.Wallpaper;
public class BigLogic implements ActionListener {

    private final ConfigLoader configLoader;
    private final TDIDialog tdiDialog;
    private final PluginTableModel pluginTableModel;
    private final PluginServer plugserv = new PluginServer();
    public float[] playFieldMaxValues= {0,0};
    public float[] playFieldMinValues= {800,800};
    public float scaleX;
    public float scaleY;
    private ArrayList<Icon> icons;
    private ArrayList<TDI> tdis;
    private Server server; 
    private Wallpaper wallpaper;
    public Wallpaper getWallpaper() {
		return wallpaper;
	}

    public BigLogic() {
        configLoader = new ConfigLoader();
        pluginTableModel = new PluginTableModel(configLoader.getPlugins());
        tdiDialog = new TDIDialog(this, pluginTableModel);
        
        icons = configLoader.loadIcons();
        Collections.sort(icons);
        wallpaper = new Wallpaper(configLoader.loadWallpaper(), configLoader.getBlockSize(), configLoader.getPanelSize(), configLoader.getPlacementRatio(), configLoader.loadScreensize());
    }

    Tilt tilt;
    public Tilt getTilt() {
		return tilt;
	}

	public void setTilt(Tilt tilt) {
		this.tilt = tilt;
	}

	public Rotation getRotation() {
		return rotation;
	}

	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

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
    
    public float getTaskbarLocation(){
    	return playFieldMaxValues[0]/0.9f;
    }
           
    public boolean checkMovedToTaskbar(float position){    	
    	if(position > getTaskbarLocation()) 
    		return true;
    	return false;
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
        refreshBackground();
    }
    
    public void refreshIcons(){
    	ArrayList<Icon> icons = new ArrayList<>();
    	for(TDI tdi: tdis){
    		icons.addAll(tdi.getIcons());
    	}
    	configLoader.updateConfig(icons);
    }
    
    public void refreshBackground(){
    	Executor.saveBackground(getWallpaper().markArea(
				getTdis()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tdiDialog.setErrorMessage("");
        if(e.getActionCommand().equals("Restore"))
        	configLoader.recoverWallpaper();
       	else
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
            server = new Server(ip);
            int[] plsize={800,800,100};
            server.setPlSize(plsize);           
            tdis = server.fullPose();
            splitIcons();
            try{
            	tilt = new Tilt(tdis.get(0).getRotation());
            	tilt.setTiltListener(new TiltHandler(this));
            }catch(IndexOutOfBoundsException e1){
            	tilt = new Tilt(new float[]{0,0,0});
            	tilt.setTiltListener(new TiltHandler(this));
            }

        	rotation = new Rotation(tdis);
        	rotation.setRotationListener(new RotationHandler(this));
        	
        	move = new Move(tdis);
        	move.setMoveListener(new MoveHandler(this));
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
	
	private void calculateScale(){
		scaleX = configLoader.loadScreensize().x/(playFieldMaxValues[0]-playFieldMinValues[0]);
		scaleY = configLoader.loadScreensize().y/(playFieldMaxValues[1]-playFieldMinValues[1]);
	}
	
	public void checkPlayGround(float[] position){
		if(position[0] > playFieldMaxValues[0])
			playFieldMaxValues[0] = position[0]; 
		if(position[1] > playFieldMaxValues[1])
			playFieldMaxValues[1] = position[1];
		if(position[0] < playFieldMinValues[0])
			playFieldMinValues[0] = position[0]; 
		if(position[1] < playFieldMinValues[1])
			playFieldMinValues[1] = position[1];	
		calculateScale();
	}
	
	public void newCommand(TDI command){
		checkPlayGround(command.getPosition());
		try{
			tilt.tilt(command); //Checks whether the user exists inapp mode 
			if(tdis.get(tdis.indexOf(command)).getState().equals(TDIState.inapp))
				plugserv.sendMessage(command.getId(), command.getPosition(), command.getRotation());
			else
			{
				move.move(command);
				rotation.rotate(command);
			}
		}catch(IndexOutOfBoundsException e){			
			if(!tdis.contains(command)){
				TDILogger.logInfo("New TDI detected by server");
				tdis.add(command);
			}
		}
	}
}
