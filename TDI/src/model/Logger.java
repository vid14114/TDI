/**
 * 
 */
package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author abideen
 *
 */
public final class Logger {
	
	public static final String LOG_LOCATION = "/.tdi/logs/errors.log";
	public static final String TAG = "TDI";
	
	public synchronized static void logError(String message){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_LOCATION));
			bw.append(message+"\n");
			bw.close();
		} catch (IOException e) {
			System.err.println(TAG+": Unable to log errors");
		}
	}
}