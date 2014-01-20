/**
 * 
 */
package model;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author abideen
 * 
 */
public final class TDILogger {

	public static final String TAG = "TDI";
	public static Logger logger = Logger.getLogger(TDILogger.class.getName());

	public synchronized static void logError(String message) {
		logger.log(Level.WARNING, message);
	}
}
