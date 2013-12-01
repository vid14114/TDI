package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProgramHandler {//TODO how will this class be called, by whom

	/**
	 * An arraylist of wmctrlIDs of the programs currently running
	 */
	private static ArrayList<String> runningPrograms = new ArrayList<>();

	/**
	 * Opens the given program and saves the wmctrlID in the list.
	 * @param exec The execution path of the program
	 */
	public static void openProgram(String[] exec) {
		try{
			Executor.executeProgram(exec);	
			String wmctrlID = null;			
			while(wmctrlID == null){
				BufferedReader reader = Executor.getRunningTasks();
				String line;
				while((line = reader.readLine()) != null){
					if(exec[1].equals("trash:///"))//The exec path of trash (trash:///) is either shown as rubbish bin or trash by wmctrl
						if((line.contains("Rubbish Bin") || line.contains("Trash")) && !runningPrograms.contains(line.split(" ")[0].replaceFirst("0x", "")))
							wmctrlID = line.split(" ")[0].replaceFirst("0x", "");
					else if(line.contains(exec[1]) && !runningPrograms.contains(line.split(" ")[0]))
						wmctrlID = line.split(" ")[0];
				}
			}
			runningPrograms.add(0,wmctrlID);
		}catch(IOException e){
		}
	}

	/**
	 * Closes the program and removes it from the programs ArrayList
	 */
	public static void closeProgram() {
		try {
			Runtime.getRuntime().exec(new String[]{"wmctrl", "-i", "-c" , Executor.getFocusedWindow()});
			runningPrograms.remove(Executor.getFocusedWindow());
		} catch (IOException e) {
		}
	}

	/**
	 * Closes all programs
	 */
	public static void closeAllPrograms() {
		try {
			for(String process: runningPrograms)
				Runtime.getRuntime().exec(new String[]{"wmctrl", "-i", "-c" ,process});
			runningPrograms.clear();
		} catch (IOException e) {

		}
	}

	/**
	 * Toggles between minimised and restored (gets the program from the programs array)
	 * 
	 *///TODO rewrite
	public static void toggleMinimization() {
        try {//TDOD wmctrl -i -a  0x01400005 <-- use for restoring
    		if(runningPrograms.size() == 1)
    			minimizeAllPrograms();
    		else
                Runtime.getRuntime().exec(new String[]{"wmctrl", "-i", "-r", Executor.getFocusedWindow(), "-b", "add,below"});
        } catch (IOException e) {
                System.err.println("An error happened while trying to minimize a program");
        }
	}

	/**
	 * Minimizes all programs
	 */
	public static void minimizeAllPrograms() {
		try {	
			Runtime.getRuntime().exec(new String[]{"wmctrl", "-k", "on"});
		} catch (IOException e) {
		}
	}

	/**
	 * Restores all programs
	 */
	public static void restoreAllPrograms() {
		try {	
			Runtime.getRuntime().exec(new String[]{"wmctrl", "-k", "off"});
		} catch (IOException e) {
		}
	}

	/**
	 * Toggles between maximize and non-maximized
	 */
	public static void toggleMaximization() {
		try {
			Runtime.getRuntime().exec(new String[]{"wmctrl", "-i", "-r", Executor.getFocusedWindow(), "-b", "toggle","maximized_vert","maximized_horz"});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}