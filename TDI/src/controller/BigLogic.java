package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import model.ConfigLoader;
import model.Server;
import view.Icon;
import view.TDI;
import view.TDIDialog;
import view.Wallpaper;

/**
 * Implements Runnable interface. Is Master, is big.
 */
public class BigLogic implements Runnable {

	private ArrayList<Icon> icons;
	private ArrayList<TDI> tdis;
	private Server server;
	private Wallpaper wallpaper = new Wallpaper();
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

			}
		}
	}

	/**
	 * 
	 * @param command
	 */
	public BigLogic() {
		ConfigLoader cl = new ConfigLoader();
		TDIDialog td = new TDIDialog(cl.getPlugins());
		icons = cl.loadIcons();
		Collections.sort(icons);
		wallpaper.setBackground(cl.loadWallpaper());
		wallpaper.setResolution(cl.loadScreensize());
		server = new Server();
		tdis = server.fullPose();
		splitIcons();
		Timer mo = new Timer();
		mo.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				ArrayList<TDI> tdis = server.fullPose();
				for (TDI t : tdis) {
					commands.add(t);
				}
			}
		}, 0, 500);
	}

	public void splitIcons() {
		float f = icons.size() / tdis.size();
		if (icons.size() % tdis.size() == 0) {
			for (TDI t : tdis) {
				int f1 = (int) f;
				int fromIndex = 0;
				t.setIcons(icons.subList(fromIndex, fromIndex += f1));
			}
		}
	}
}
