package controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import view.Wallpaper;
import model.ConfigLoader;
import model.TDIDirectories;
import model.TDILogger;

/**
 * Die Executor Klasse ist die Zentrale Schnittstelle zum Betriebssystem. Die
 * Klassen enthaelt nur Methoden die Direkt auf die Konsole des Betriebssystems
 * zugreifen.
 * 
 * @author TDI Team
 */
public final class Executor {

	/**
	 * Alle plugins die der User auswaehlt, werden weitergegeben und gestartet
	 * 
	 * @param plugins
	 *            Eine Liste von Plugins
	 */
	public static void startPlugins(String[] plugins) {
		try {
			for (String plugin : plugins)
				Runtime.getRuntime().exec(
						new String[] {
								"java",
								"-jar",
								TDIDirectories.TDI_PLUGINS + "/" + plugin
										+ ".jar" });
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
	}

	/*
	 * command -v wmctrl xdg-open xfconf-query xprop
	 */
	// //TODO finish
	// public static void getStatus() throws IOException{
	// BufferedReader bf = new BufferedReader(new
	// InputStreamReader(Runtime.getRuntime().exec("command -v wmctrl xdg-open xfconf-query gvfs-mount").getInputStream()));
	// }

	/**
	 * Speichert das neue Hintergrundbild ins {@link TDIDirectories#TDI_TEMP}
	 * directory, greift auf die Konsole und setzt das Bild als Hintergrundbild
	 * 
	 * @param image
	 *            Ein BufferedImage
	 */
	public static void saveBackground(BufferedImage image) {
		try {
			File restore = new File(TDIDirectories.TDI_TEMP + "/" + "temp."
					+ ConfigLoader.imageType);
			ImageIO.write(image, ConfigLoader.imageType, restore);
			Runtime.getRuntime().exec(
					new String[] { "xfconf-query", "-c", "xfce4-desktop", "-p",
							"/backdrop/screen0/monitor0/image-path", "-s",
							restore.getAbsolutePath() });
			Runtime.getRuntime().exec(new String[] { "xfdesktop", "--reload" });
			restore.deleteOnExit();
		} catch (IOException e) {
			TDILogger.logError("Error saving wallpaper " + e.getMessage());
		}
	}

	/**
	 * Liefert die Position vom fokusierten Fenster zurueck
	 * 
	 * @return Ein Array mit der Positoin (x,y)
	 */
	public static float[] getWindowPosition() {
		float[] position = new float[] { 0, 0 };
		BufferedReader bf;
		try {
			bf = new BufferedReader(new InputStreamReader(
					Runtime.getRuntime()
							.exec(new String[] { "xwininfo", "-id",
									getFocusedWindow() }).getInputStream()));
			String line;
			while ((line = bf.readLine()) != null) {
				if (line.contains("Absolute upper-left X:"))
					position[0] = Integer.parseInt(line.split(":")[1].trim());
				if (line.contains("Absolute upper-left Y:"))
					position[1] = Integer.parseInt(line.split(":")[1].trim());
			}
			bf.close();
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
		return position;
	}

	/**
	 * Ruft die xfconf-Abfragemethode, die den Pfad des Hintergrundbilds
	 * zurueckliefert
	 * 
	 * @return Der Pfad des Hintergrundbildes
	 */
	public static String getBackground() {
		String background = null;
		try {
			BufferedReader b = new BufferedReader(new InputStreamReader(Runtime
					.getRuntime()
					.exec(new String[] { "xfconf-query", "-c", "xfce4-desktop",
							"-p", "/backdrop/screen0/monitor0/image-path" })
					.getInputStream()));
			background = b.readLine();
			b.close();
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
		return background;
	}

	/**
	 * Fuehrt den angegebenen exec eines Programms aus
	 * 
	 * @param exec
	 *            Der Pfad des Programms
	 */
	public static void executeProgram(String[] exec) {
		try {
			Runtime.getRuntime().exec(exec);
		} catch (IOException e) {
			TDILogger
					.logError("Unable to start program, is xdg-open installed?");
		}
	}

	/**
	 * Gibt einen Screenshot in Form eines BufferedReader aller geoeffneten
	 * Fenster aus
	 * 
	 * @return Einen {@link BufferedReader} mit allen geoefnetten Fenster
	 */
	public static BufferedReader getRunningTasks() {
		try {
			return new BufferedReader(new InputStreamReader(Runtime
					.getRuntime().exec("wmctrl -lp").getInputStream()));
		} catch (IOException e) {
			TDILogger
					.logError("Error getting list of programms, make sure wmctrl is installed");
		}
		return null;
	}

	/**
	 * Holen Sie sich den aktuellen fokussierten Fenster
	 * 
	 * @return die wmctrlID des aktiven Fenster
	 */
	public static String getFocusedWindow() {
		String wmctrlID = null;
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					Runtime.getRuntime().exec("xprop -root").getInputStream()));
			String line;
			while ((line = bf.readLine()) != null)
				if (line.contains("_NET_ACTIVE_WINDOW(WINDOW)")
						&& wmctrlID == null)
					wmctrlID = line.split("#")[1].trim().split(",")[0]
							.replaceFirst("0x", "0x0");
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
		return wmctrlID;
	}

	/**
	 * Gibt alle aktiven Wechseldatentraeger
	 * 
	 * @return Als {@link BufferedReader}
	 */
	public static BufferedReader getRemovableDiskList() {
		try {
			return new BufferedReader(new InputStreamReader(Runtime
					.getRuntime().exec(new String[] { "gvfs-mount", "-li" })
					.getInputStream()));
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
		return null;
	}

	/**
	 * Holt sich die Panelsize aus der Konsole
	 * 
	 * @return Die Laenge als String {@link BufferedReader#readLine()}
	 */
	public static String getPanelSize() {
		String panelSize = null;
		try {
			BufferedReader bf = new BufferedReader(
					new InputStreamReader(
							Runtime.getRuntime()
									.exec(new String[] { "xfconf-query", "-c",
											"xfce4-panel", "-p",
											"/panels/panel-1/size" })
									.getInputStream()));
			panelSize = bf.readLine();
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
		return panelSize;
	}

	/**
	 * Holt sich den Placementration aus der Konsole, wichtig fuer den
	 * {@link Wallpaper}
	 * 
	 * @return Die Groeße als String {@link BufferedReader#readLine()}
	 */
	public static String getPlacementRatio() {
		String ratio = null;
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					Runtime.getRuntime()
							.exec(new String[] { "xfconf-query", "-c", "xfwm4",
									"-p", "/general/placement_ratio" })
							.getInputStream()));
			ratio = bf.readLine();
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
		return ratio;
	}
}
