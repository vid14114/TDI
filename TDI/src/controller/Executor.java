/**
 * 
 */
package controller;

import java.io.IOException;

/**
 * @author abideen
 *
 */
public final class Executor {
	public static final void startPlugins(String [] plugins){
		try{
			for(String plugin:plugins){
				String []cmd = {"java", "-jar", plugin+".jar"};
				Runtime.getRuntime().exec(cmd);
			}
		}catch(IOException e){
			
		}
	}
}
