package main;

/**
 * Main Klasse enthaelt unsere Mainmethode diese Methode wird verwendet um den Rest aufzurufen.
 * 
 * @author TDI Team
 */

public class Main {

	public static void main(String[] args) {
		MusicDialog m = new MusicDialog();
		new Thread(new PluginServer(m)).start();
	}
}
