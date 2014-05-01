package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

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

/**
 * Die BigLogic Klasse enhaelt unsere main Methode. Sie ist dafuer zustaendig
 * alle Klassen aus den vielen packages zusammenzufuegen. Sie enhaelt viele
 * Methoden, die in andere Klassen verwendet werden, es uebergibt Attribute,
 * ruft viele Methoden und ist als Schnittpunkt fuer den Server und die Action
 * Listeners(Tilt, Move, Rotate) sehr wichtig.
 * 
 * @author TDI Team
 */
public class BigLogic implements ActionListener {
	/**
	 * Zuerst werden alle benoetigten Directories erstellt, dann wird
	 * {@link BigLogic} aufgerufen
	 * 
	 * @param args
	 *            Wird nicht beruecksichtigt
	 */
	public static void main(String[] args) {
		TDIDirectories.createDirectories();
		new BigLogic();
	}

	/**
	 * Siehe {@link ConfigLoader} Doku
	 */
	private final ConfigLoader configLoader;
	/**
	 * 
	 * Siehe {@link TDIDialog} Doku
	 */
	private final TDIDialog tdiDialog;
	/**
	 * Siehe {@link PluginTableModel} Doku
	 */
	private final PluginTableModel pluginTableModel;
	/**
	 * Siehe {@link PluginServer} Doku
	 */
	private final PluginServer plugserv = new PluginServer();
	/**
	 * Die maximalen Dimensionen(x,y) des Playgrounds werden in diesem Array
	 * gespeichert
	 */
	public float[] playFieldMaxValues = { 0, 0 };
	/**
	 * Die minimalen Dimensionen(x,y) des Playgrounds werden in diesem Array
	 * gespeichert
	 */
	public float[] playFieldMinValues = { 800, 800 };
	/**
	 * Der Skalefaktor um die X Achse zwischen dem Tisch und das Bildschirm
	 */
	public float scaleX;
	/**
	 * Der Skalefaktor um die Y Achse zwischen dem Tisch und das Bildschirm
	 */
	public float scaleY;
	/**
	 * Eine Liste von allen Icons am Desktop, wichtig fuer die
	 * {@link BigLogic#splitIcons()} Methode Siehe {@link Icon} Doku
	 */
	private ArrayList<Icon> icons;
	/**
	 * Ein ArrayListe von allen TDIs, die derzeit vom Kamera sichtbar sind Siehe
	 * {@link TDI} Doku
	 */
	private ArrayList<TDI> tdis;
	/**
	 * Siehe {@link Server} Doku
	 */
	private Server server;

	/**
	 * Siehe {@link Wallpaper} Doku
	 */
	private Wallpaper wallpaper;

	/**
	 * Siehe {@link Tilt} Doku
	 */
	Tilt tilt;

	/**
	 * Siehe {@link Rotation} Doku
	 */
	Rotation rotation;
	/**
	 * Siehe {@link Move} Doku
	 */
	Move move;

	/**
	 * Konstruktor von BigLogic. Sie laedt die benoetigten Klassen, Datein und
	 * Konfigurationen hoch
	 */
	public BigLogic() {
		configLoader = new ConfigLoader();
		pluginTableModel = new PluginTableModel(configLoader.getPlugins());
		tdiDialog = new TDIDialog(this, pluginTableModel);

		icons = configLoader.loadIcons();
		Collections.sort(icons);
		wallpaper = new Wallpaper(configLoader.loadWallpaper(),
				configLoader.getBlockSize(), configLoader.getPanelSize(),
				configLoader.getPlacementRatio(), configLoader.loadScreensize());
	}

	/**
	 * Nachdem der User sicht fuer entweder restore, start TDI oder start
	 * tutorial entschieden hat, started die Methode die notwendigen Listener
	 * Klassen {@link Tilt}, {@link Move}, {@link Rotation}. Die Verbindung zum
	 * Handy via {@link Server} wird gestartet, Die Plugins und der
	 * {@link PluginServer} werden gestartet. Eine {@link Timer} wird gestartet
	 * der alle 500ms die Positionen von den TDIs abfragt und es der Methode
	 * {@link BigLogic#newCommand(TDI)} als TDI uebergibt
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		tdiDialog.setErrorMessage("");
		if (e.getActionCommand().equals("Restore"))
			configLoader.recoverWallpaper();
		else {
			String ip;
			if ((ip = checkIp(tdiDialog.getIp1().getText(), tdiDialog.getIp2()
					.getText(), tdiDialog.getIp3().getText(), tdiDialog
					.getIp4().getText())) == null)
				return;
			// IP address is correct
			new Runnable() {
				public void run() {
					String[] plugins = new String[pluginTableModel
							.getRowCount()];
					for (int i = 0, i2 = 0; i < plugins.length; i++)
						if ((boolean) pluginTableModel.getValueAt(i, 1))
							plugins[i2++] = (String) pluginTableModel
									.getValueAt(i, 0);
					Executor.startPlugins(plugins);
					configLoader.savePlugins(plugins);
				}
			}.run();
			server = new Server("192.168.43.128");
			try {
				server.setPlSize(new int[] { 800, 800, 100 });
				tdis = server.fullPose();
			} catch (NullPointerException e1) {
				JOptionPane.showMessageDialog(null,
						"Could not connect to server");
				return;
			}
			splitIcons();
			try {
				tilt = new Tilt(tdis.get(0).getRotation());
				tilt.setTiltListener(new TiltHandler(this));

			} catch (IndexOutOfBoundsException e1) {
				tilt = new Tilt(new float[] { 0, 0, 0 });
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
						for (TDI t : tdis) {
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
	 * {@link BigLogic#scaleX} und {@link BigLogic#scaleY} werden mittels einer
	 * Formel ausgerechnet
	 * 
	 */
	private void calculateScale() {
		scaleX = configLoader.loadScreensize().x
				/ (playFieldMaxValues[0] - playFieldMinValues[0]);
		scaleY = configLoader.loadScreensize().y
				/ (playFieldMaxValues[1] - playFieldMinValues[1]);
	}

	/**
	 * Checks if the entered IP has a valid format and converts it to an integer
	 * 
	 * @param ip1
	 *            Erste octate
	 * @param ip2
	 *            Zweite octate
	 * @param ip3
	 *            Dritte octate
	 * @param ip4
	 *            Vierte octate
	 * @return Null wenn die IP Adresse nicht stimmt, oder die IP Adresse als
	 *         String
	 */
	private String checkIp(String ip1, String ip2, String ip3, String ip4) {
		int fullIP[] = new int[4];
		String ip = null;
		// Checks if appropriate length
		if ((ip1.length() <= 3) && (ip2.length() <= 3) && (ip3.length() <= 3)
				&& (ip4.length() <= 3)) {
			// not null check
			if ((ip1.length() > 0) && (ip2.length() > 0) && (ip3.length() > 0)
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
			tdiDialog
					.setErrorMessage("Input only in the following format: 000.000.000.000");
		return null;
	}

	/**
	 * Schaut ob der TDI sich in der Taskbar befindet
	 * 
	 * @param position
	 *            Die Position von der TDI
	 * @return true wenn ja, false wenn nein
	 */
	public boolean checkMovedToTaskbar(float position) {
		if (position > getTaskbarLocation())
			return true;
		return false;
	}

	/**
	 * {@link BigLogic#playFieldMaxValues} und
	 * {@link BigLogic#playFieldMinValues} werden neu gesetzt wenn die neuen
	 * Positionen, entweder kleiner oder groeßer sind
	 * 
	 * @param position
	 *            Die Postion von TDI
	 */
	public void checkPlayGround(float[] position) {
		if (position[0] > playFieldMaxValues[0])
			playFieldMaxValues[0] = position[0];
		if (position[1] > playFieldMaxValues[1])
			playFieldMaxValues[1] = position[1];
		if (position[0] < playFieldMinValues[0])
			playFieldMinValues[0] = position[0];
		if (position[1] < playFieldMinValues[1])
			playFieldMinValues[1] = position[1];
		calculateScale();
	}

	/**
	 * Kontrolliert ob ein TDI in der Taskbar ist oder nicht
	 * 
	 * @return true Falls ein TDI in der Taskbar ist, false falls nicht
	 */
	public boolean emptyTaskbar() {
		for (TDI t : tdis) {
			if (t.getState().equals(TDIState.taskbar))
				return false;
		}
		return true;
	}

	/**
	 * Schließt alle Verbindungen und setzt den alten Wallpaper wieder her
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		configLoader.recoverWallpaper();
	}

	/**
	 * Gibt alle Icons zurueck, die am Desktop sind
	 * 
	 * @return Alle Icons
	 */
	public ArrayList<Icon> getIcons() {
		return icons;
	}

	 public Move getMove() {
	 return move;
	 }

	/**
	 * Der Pluginserver wird von den Listenerklassen benoetigt
	 * 
	 * @return {@link PluginServer}
	 */
	public PluginServer getPlugserv() {
		return plugserv;
	}

	 public Rotation getRotation() {
	 return rotation;
	 }

	/**
	 * Gibt den Server zurueck
	 * 
	 * @return {@link Server}
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * Die Taskbar location vom Sicht des Kameras
	 * 
	 * @return Location von der Taskbar
	 */
	public float getTaskbarLocation() {
		return playFieldMaxValues[0] * 0.9f;
	}

	/**
	 * Alle TDIs die vom Kamera getrackt werden
	 * 
	 * @return Alle {@link TDI}
	 */
	public ArrayList<TDI> getTdis() {
		return tdis;
	}

	 public Tilt getTilt() {
	 return tilt;
	 }

	/**
	 * Die Wallpaper klasse zum editieren des Wallpapers
	 * 
	 * @return Eine Instanze der Wallpaperklasse
	 */
	public Wallpaper getWallpaper() {
		return wallpaper;
	}

	/**
	 * Wenn der Server mit {@link Server#fullPose()} neue TDI Werte bekommt,
	 * ruft er die Methode um diese weiterzugeben. Die Methode ruft dann alle
	 * TDI Listener Klassen auf: {@link Tilt}, {@link Move}, {@link Rotation}.
	 * 
	 * @param command
	 *            Im Format der Klasse {@link TDI}
	 */
	public void newCommand(TDI command) {
		checkPlayGround(command.getPosition());
		try {
			if (tdis.get(tdis.indexOf(command)).isMoving()) {
				TDI tdi = tdis.get(tdis.indexOf(command));
				if (move.moveChanged(tdi.getPosition()[0],
						command.getPosition()[0])
						|| move.moveChanged(tdi.getPosition()[1],
								command.getPosition()[1]))
					return;
				else
					tdi.setMoving(false);
			}
			tilt.tilt(command); // Checks whether the user exists inapp mode
			if (tdis.get(tdis.indexOf(command)).getState()
					.equals(TDIState.inapp))
				plugserv.sendMessage(command.getId(), command.getPosition(),
						command.getRotation());
			else {
				move.move(command);
				rotation.rotate(command);
			}
		} catch (IndexOutOfBoundsException e) {
			if (!tdis.contains(command)) {
				TDILogger.logInfo("New TDI detected by server");
				tdis.add(command);
			}
		}
	}

	/**
	 * Der Hintergrund wird neu gezeichnet und geladen
	 */
	public void refreshBackground() {
		try {
			Executor.saveBackground(getWallpaper().markArea(getTdis()));
		} catch (NullPointerException e) {
		}
	}

	/**
	 * Falls sich die Position eines Icons veraendert hatte, wird diese Position
	 * aufgerufen, damit die Konfiguration neu gespeichert und vom Xfce neu
	 * geladen werden kann. Diese Methode ruft die
	 * {@link ConfigLoader#updateConfig(ArrayList)} auf
	 */
	public void refreshIcons() {
		ArrayList<Icon> icons = new ArrayList<>();
		for (TDI tdi : tdis) {
			icons.addAll(tdi.getIcons());
		}
		configLoader.updateConfig(icons);
	}

	 public void setMove(Move move) {
	 this.move = move;
	 }
	
	 public void setRotation(Rotation rotation) {
	 this.rotation = rotation;
	 }
	
	 public void setTdis(ArrayList<TDI> tdis) {
	 this.tdis = tdis;
	 }
	
	 public void setTilt(Tilt tilt) {
	 this.tilt = tilt;
	 }

	/**
	 * Die Icons werden auf den TDIs gemappt dann wird
	 * {@link BigLogic#refreshBackground()} aufgerufen
	 */
	public void splitIcons() {
		int iconsAssigned = 0;
		for (int i = 0; i < tdis.size(); i++) {
			int f = (icons.size() - iconsAssigned) / (tdis.size() - i);
			if ((icons.size() - iconsAssigned) % (tdis.size() - i) > 0)
				f++;
			tdis.get(i).setIcons(
					new ArrayList<Icon>(icons.subList(iconsAssigned,
							iconsAssigned += f)));
			tdis.get(i).calculateRotationLimit();
		}
		refreshBackground();
	}
}
