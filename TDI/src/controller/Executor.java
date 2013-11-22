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
	public static final void startPlugins(String [] plugins){
		try{
			for(String plugin:plugins){
				String []cmd = {"java", "-jar", TDIDirectories.TDI_PLUGINS+"/"+plugin+".jar"};
				Runtime.getRuntime().exec(cmd);
			}
		}catch(IOException e){
			TDILogger.logError(e.getMessage());
		}
	}
	/**
	 * Calls the xfconf-query method which returns the location of the background
	 * @return The path of the background picture
	 */
	public static final String getBackground(){	
		String background = null;
		try {
			String [] cmd = {"xfconf-query", "-c", "xfce4-desktop", "-p", "/backdrop/screen0/monitor0/image-path"};			
			BufferedReader b = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream()));
			background = b.readLine();
			b.close();
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
		return background;
	}
}
