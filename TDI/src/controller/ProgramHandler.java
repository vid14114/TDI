package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import view.Icon;

public class ProgramHandler {

	/**
	 * An arraylist of wmctrlIDs of the programs currently running
	 */
	private static ArrayList<ProgramInfo> runningPrograms = new ArrayList<>();
	/**
	 * @return the runningPrograms
	 */
	public static ArrayList<ProgramInfo> getRunningPrograms() {
		return runningPrograms;
	}

	/**
	 * If show desktop mode is on, this is set to true
	 */
	private static boolean desktopMode;

	/**
	 * Opens the given program and saves the wmctrlID in the list.
	 * 
	 * @param exec
	 *            The execution path of the program
	 */
	public static void openProgram(Icon icon) {
		try {
			if (icon.getExecPath()[1].endsWith("~"))
				return;
			Executor.executeProgram(icon.getExecPath());
			String wmctrlID = null;
			while (wmctrlID == null) {
				BufferedReader reader = Executor.getRunningTasks();
				String line;
				while ((line = reader.readLine()) != null && wmctrlID == null)
					// The exec path of trash(trash:///) is either shown as
					// rubbish bin or trash by wmctrl
					if (((icon.getExecPath()[1].equals("trash:///") && (line
							.contains("Rubbish Bin") || line.contains("Trash"))) || line
							.contains(icon.getName()))
							&& !runningPrograms.contains(new ProgramInfo(line
									.split(" ")[0], false)))
						wmctrlID = line.split(" ")[0];
					else if(line.split(" ")[1].equals("-1") && 
							line.contains(icon.getName()) && 
							runningPrograms.contains(new ProgramInfo(line.split(" ")[0], false)))
						wmctrlID = line.split(" ")[0];
			}
			runningPrograms.add(0, new ProgramInfo(wmctrlID, icon.getName(), false));
		} catch (IOException e) {
		}
	}

	/**
	 * Closes the program and removes it from the programs ArrayList
	 */
	public static void closeProgram() {
		if(runningPrograms.size() == 0 || desktopMode) return;
		try {
			String wmctrlID = getFocusedWindow();
			Runtime.getRuntime().exec(
					new String[] { "wmctrl", "-i", "-c",
							wmctrlID});
			while(runningPrograms.remove(new ProgramInfo(wmctrlID, false)));
		} catch (IOException e) {
		}
	}

	/**
	 * Closes all programs
	 */
	public static void closeAllPrograms() {
		try {
			for (ProgramInfo process : runningPrograms)
				Runtime.getRuntime().exec(
						new String[] { "wmctrl", "-i", "-c",
								process.getWmctrlID() });
			runningPrograms.clear();
		} catch (IOException e) {

		}
	}

	public static void restoreRight() {
		if(runningPrograms.size() == 0) return;
		
		desktopMode = false;
		// Put the first element to the back
		{
			ProgramInfo process = runningPrograms.get(0);
			runningPrograms.remove(0);
			runningPrograms.add(process);
		}
		try {
			Runtime.getRuntime().exec(
					new String[] { "wmctrl", "-i", "-a",
							runningPrograms.get(0).getWmctrlID() });
			runningPrograms.get(0).setMinimized(false);
		} catch (IOException e) {
		}
	}

	public static void restoreLeft() {
		if(runningPrograms.size() == 0) return;
		
		desktopMode = false;
		// Put the last element to the from
		{
			ProgramInfo process = runningPrograms
					.get(runningPrograms.size() - 1);
			runningPrograms.remove(runningPrograms.size() - 1);
			runningPrograms.add(0, process);
		}
		try {
			Runtime.getRuntime().exec(
					new String[] { "wmctrl", "-i", "-a",
							runningPrograms.get(0).getWmctrlID() });
			runningPrograms.get(0).setMinimized(false);
		} catch (IOException e) {
		}
	}

	/**
	 * The user wnts to minimize the focusedWindow
	 */
	public static void minimize() {
		if(runningPrograms.size() == 0 || desktopMode) return;
		
		try {
			if (runningPrograms.size() == 1)
				minimizeAllPrograms();
			else if (getNonMinimized() == 1)
				minimizeAllPrograms();
			else {
				String wmctrlID = getFocusedWindow();
				Runtime.getRuntime()
						.exec(new String[] { "wmctrl", "-i", "-r",
								wmctrlID, "-b", "add,below" });
				runningPrograms.get(
						runningPrograms.indexOf(new ProgramInfo(wmctrlID, false)))
						.setMinimized(true);
			}
		} catch (IOException e) {
			System.err
					.println("An error happened while trying to minimize a program");
		} catch (IndexOutOfBoundsException e) {

		}
	}

	public static void moveProgram(int x, int y, int widht, int heigth) {
		if(runningPrograms.size() == 0) return;
		try {
			Runtime.getRuntime().exec(
					new String[] { "wmctrl", "-i", "-r",
							getFocusedWindow(), "-e",
							"0," + x + "," + y + "," + widht + "," + heigth });
		} catch (IOException e) {
			System.err
					.println("An error happened while trying to move or resize a program");
		}
	}
	
	/**
	 * Minimizes all programs
	 */
	public static void minimizeAllPrograms() {
		try {
			Runtime.getRuntime().exec(new String[] { "wmctrl", "-k", "on" });
			desktopMode = true;
		} catch (IOException e) {
		}
	}

	/**
	 * Restores all programs
	 */
	public static void restoreAllPrograms() {
		try {
			if (desktopMode) {
				Runtime.getRuntime().exec(
						new String[] { "wmctrl", "-k", "off" });
				desktopMode = false;
			} else
				for (int i = 0; i < runningPrograms.size(); i++) {
					Runtime.getRuntime().exec(
							new String[] { "wmctrl", "-i", "-a",
									runningPrograms.get(i).getWmctrlID() });
					runningPrograms.get(i).setMinimized(false);
				}
		} catch (IOException e) {
		}
	}

	/**
	 * Toggles between maximize and non-maximized
	 */
	public static void toggleMaximization() {
		if(runningPrograms.size() == 0 || desktopMode) return;
		try {
			Runtime.getRuntime().exec(
					new String[] { "wmctrl", "-i", "-r",
							getFocusedWindow(), "-b", "toggle",
							"maximized_vert", "maximized_horz" });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the number of non-minimized programs
	 * 
	 * @return The amount of non-minimizzed programs
	 */
	public static int getNonMinimized() {
		int nonMinimized = 0;
		for (ProgramInfo process : runningPrograms)
			if (!process.isMinimized())
				nonMinimized++;
		return nonMinimized;
	}
	
	private static String getFocusedWindow(){
		String wmctrlID = "0000";
		int i = 0;
		while(!runningPrograms.contains(new ProgramInfo(wmctrlID, false))){			
			wmctrlID = Executor.getFocusedWindow();
			if(++i == 300){
				verifyWindows();
				i = 0;
			}
		}
		return wmctrlID;
	}
	
	private static void verifyWindows(){
		try {
			for(int i = 0; i < runningPrograms.size(); i++){
				BufferedReader br = Executor.getRunningTasks();
				String line;
				//Here we check whether the process has being registered under the right name 
				while((line = br.readLine()) != null)
					if(line.contains(runningPrograms.get(i).getWmctrlID())) break;
				br = Executor.getRunningTasks();
				//If not, we reassign the wmctrlID to the window
				while((line = br.readLine()) != null)
					if(line.contains(runningPrograms.get(i).getWindowName())){
						runningPrograms.get(i).setWmctrlID(line.split(" ")[0]);
						break;
					}
			}
		}
		catch (IOException e) {
			//TODO error message
		}
	}

	/**
	 * @return the desktopMode
	 */
	public static boolean isDesktopMode() {
		return desktopMode;
	}
}

class ProgramInfo {
	String wmctrlID;
	/**
	 * @param wmctrlID the wmctrlID to set
	 */
	public void setWmctrlID(String wmctrlID) {
		this.wmctrlID = wmctrlID;
	}

	String windowName;
	/**
	 * @return the windowName
	 */
	public String getWindowName() {
		return windowName;
	}

	/**
	 * @param windowName the windowName to set
	 */
	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	boolean minimized;

	/**
	 * @param exec
	 * @param minimized
	 */
	public ProgramInfo(String exec, boolean minimized) {
		this.wmctrlID = exec;
		this.minimized = minimized;
	}

	public ProgramInfo(String exec, String windowName, boolean minimized) {
		this.wmctrlID = exec;
		this.minimized = minimized;
		this.windowName = windowName;
	}
	/**
	 * @return the exec
	 */
	public String getWmctrlID() {
		return wmctrlID;
	}

	/**
	 * @return the minimized
	 */
	public boolean isMinimized() {
		return minimized;
	}

	/**
	 * @param minimized
	 *            the minimized to set
	 */
	public void setMinimized(boolean minimized) {
		this.minimized = minimized;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;		
		if (wmctrlID.equals(((ProgramInfo)obj).getWmctrlID()))
			return true;
		return false;
	}

}