package controller;

import java.io.IOException;
import java.util.ArrayList;

import model.ConfigLoader;
import model.Server;
import view.*;

/**
 * Implements Runnable interface. Is Master, is big.
 */
public class BigLogic implements Runnable{

	private ArrayList<TDI> tdis;
	private Server server;
	
	/**
	 * The wallpaper
	 */
	private Wallpaper wallpaper;
	/**
	 * The commands that have to be executed.
	 */
	private ArrayList<String> commands;
	/**
	 * Lädt Dialog und Desktop configuration
	 * @param args
	 */
	public static void main(String[] args) {
		BigLogic bl=new BigLogic();
		ConfigLoader cl=new ConfigLoader();
//		TDIDialog t=new TDIDialog(cl.getPlugins());
		cl.loadIcons();		
		Server s=new Server();
		s.fullPose(bl.tdis);
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
