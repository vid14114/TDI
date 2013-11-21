/**
 * 
 */
package controller;

import java.io.IOException;
import java.util.Date;
import model.ConfigLoader;
import model.Logger;

/**
 * @author abideen
 *
 */
public final class Executor {
	public static final void startPlugins(String [] plugins){
		try{
			for(String plugin:plugins){
				String []cmd = {"java", "-jar", ConfigLoader.PLUGINS_LOC+"/"+plugin+".jar"};
				Runtime.getRuntime().exec(cmd);
			}
		}catch(IOException e){
			Logger.logError(new Date().toString()+": Executor - Could not execute plugins");
		}
	}
}
