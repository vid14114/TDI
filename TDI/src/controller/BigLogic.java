package controller;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import model.ConfigLoader;
import model.Server;
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
	 * The run method that is overriden
	 */
	public void run() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param command
	 */
	public void addCommand(String command) {
		throw new UnsupportedOperationException();
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
