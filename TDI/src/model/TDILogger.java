/**
 * 
 */
package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author abideen
 *
 */
public final class TDILogger {
	
	public static final String LOG_LOCATION = "/.tdi/logs/errors.log";
	public static final String TAG = "TDI";
	public final Logger logger =  Logger.getLogger("TDI");
	
	public synchronized static void logError(String message){
		try {			
			BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_LOCATION));
			bw.append(new Date().toString()+": "+message+"\n");
			bw.close();
		} catch (IOException e) {
			System.err.println(TAG+": Unable to log errors");
		}
	}
}
