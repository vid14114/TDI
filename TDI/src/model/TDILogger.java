/**
 *
 */
package model;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author abideen
 */
public final class TDILogger {

	private static final Logger logger = Logger.getLogger(TDILogger.class
			.getName());
	public static final String TAG = "TDI";

	public synchronized static void logError(String message) {
		logger.log(Level.WARNING, message);
	}

	public synchronized static void logInfo(String message) {
		logger.log(Level.INFO, message);
	}

	public static void startLog() {
		FileHandler fh;
		try {
			fh = new FileHandler(TDIDirectories.TDI_LOGS + "/"
					+ new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
			logger.addHandler(fh);
			final SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (SecurityException | IOException e) {
			logger.log(Level.WARNING, "Error trying to initialize the Logger");
		}
	}
}
