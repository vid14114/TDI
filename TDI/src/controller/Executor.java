/**
 * 
 */
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import model.TDIDirectories;
import model.TDILogger;

/**
 * @author abideen
 * 
 */
public final class Executor {
	/*
	 * Environment checking: TODO check enviroment at startup command -v wmctrl
	 * xdg-open xfconf-query xprop
	 */
	public static final void startPlugins(String[] plugins) {
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

	// TODO finish
	public static void getStatus() throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(Runtime
				.getRuntime()
				.exec("command -v wmctrl xdg-open xfconf-query gvfs-mount")
				.getInputStream()));
	}

	/**
	 * Calls the xfconf-query method which returns the location of the
	 * background
	 * 
	 * @return The path of the background picture
	 */
	public static final String getBackground() {
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

	public static final String executeProgram(String[] exec) {
		try {
			Runtime.getRuntime().exec(exec);
		} catch (IOException e) {
			TDILogger
					.logError("Unable to start program, is xdg-open installed?");
		}
		return exec[1];
	}

	public static final BufferedReader getRunningTasks() {
		try {
			return new BufferedReader(new InputStreamReader(Runtime
					.getRuntime().exec("wmctrl -lp").getInputStream()));
		} catch (IOException e) {
			TDILogger
					.logError("Error getting list of programms, make sure wmctrl is installed");
		}
		return null;
	}

	public static final String getFocusedWindow() {
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
		}
		return wmctrlID;
	}

	public static final BufferedReader getRemovableDiskList() {
		try {
			return new BufferedReader(new InputStreamReader(Runtime
					.getRuntime().exec(new String[] { "gvfs-mount", "-li" })
					.getInputStream()));
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
		return null;
	}
}
