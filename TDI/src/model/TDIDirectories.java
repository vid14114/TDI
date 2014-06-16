/**
 *
 */
package model;

import java.io.File;
import java.io.IOException;

/**
 * @author abideen
 */
public class TDIDirectories {
	/**
	 * The home directory of TDI
	 */
	private static final String TDI_HOME = System.getProperty("user.home")
			+ "/.tdi";
	/**
	 * A text file containing a list of the chosen plugins by the user
	 */
	public static final String TDI_PREFERENCE = TDI_HOME + "/preferences.txt";
	/**
	 * TDI plugins directory The program can be extended by putting installed
	 * plugins which must end with .jar and be runnable with the java .jar
	 * command
	 */
	public static final String TDI_PLUGINS = TDI_HOME + "/plugins";
	/**
	 * This folder is used by TDI for restoration purposes
	 */
	public static final String TDI_RESTORE = TDI_HOME + "/restore";
	/**
	 * All program logs are written in this directory
	 */
	public static final String TDI_LOGS = TDI_HOME + "/logs";
	/**
	 * Any temporary file needed
	 */
	public static final String TDI_TEMP = TDI_HOME + "/temp";

	/**
	 * Creates all TDI directories needed.
	 * 
	 * @throws IOException
	 */
	public static void createDirectories() {
		if (new File(TDI_PREFERENCE).exists())
			return;
		new File(TDI_HOME).mkdirs();
		try {
			new File(TDI_PREFERENCE).createNewFile();
		} catch (IOException e) {
			TDILogger.logError("Couldn't create the TDI preference text file");
		}
		new File(TDI_PLUGINS).mkdirs();
		new File(TDI_RESTORE).mkdirs();
		new File(TDI_TEMP).mkdirs();
		// new File(TDI_LOGS).mkdirs();
	}
}
