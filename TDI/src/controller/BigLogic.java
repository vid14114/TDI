package controller;

import java.util.ArrayList;

import model.Server;

import view.*;

/**
 * Implements Runnable interface. Is Master, is big.
 */
public class BigLogic implements Runnable{

<<<<<<< HEAD
	private ArrayList<TDI> tdis;
=======
	private ArrayList tdis;
>>>>>>> branch 'JUnitMaria' of https://github.com/vid14114/TDI.git
	private Server server;
	/**
	 * The wallpaper
	 */
	private Wallpaper wallpaper;
	/**
	 * The commands that have to be executed.
	 */
<<<<<<< HEAD
	private ArrayList<String> commands;
=======
	private static ArrayList commands;
	
>>>>>>> branch 'JUnitMaria' of https://github.com/vid14114/TDI.git

	/**
	 * Lädt Dialog und Desktop configuration
	 * @param args
	 */
	public static void main(String[] args) {
		throw new UnsupportedOperationException();
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
	
}
