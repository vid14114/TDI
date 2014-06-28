package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.TDILogger;
import view.Icon;

/**
 * ProgramHandler manipuliert Programme. Starten, schließen, minimieren,
 * maximieren und bewegen sind die Hauptkomponente der Klasse.
 * 
 * @author TDI Team
 */
public class ProgramHandler {

	/**
	 * Eine Arraylist von wmctrlIDs der laufenden Programme
	 */
	private static final ArrayList<ProgramInfo> runningPrograms = new ArrayList<>();
	/**
	 * Wenn Show Desktop-Modus an ist, wird dies auf true gesetzt
	 */
	private static boolean desktopMode;

	/**
	 * Schließt alle geoefnetten Programme
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

	/**
	 * Schließt das Programm und entfernt es aus der {@link #runningPrograms}
	 * Liste
	 */
	public static void closeProgram() {
		if (runningPrograms.size() == 0 || desktopMode)
			return;
		try {
			String wmctrlID = getFocusedWindow();
			Runtime.getRuntime().exec(
					new String[] { "wmctrl", "-i", "-c", wmctrlID });
			while (runningPrograms.remove(new ProgramInfo(wmctrlID, false)))
				;
		} catch (IOException e) {
		}
	}

	/**
	 * Gibt die aktuell fokussierten Program zurueck
	 * 
	 * @return Die wmctrlID vom fokussierten Fenster
	 */
	private static String getFocusedWindow() {
		String wmctrlID = "0000";
		int i = 0;
		while (!runningPrograms.contains(new ProgramInfo(wmctrlID, false))) {
			wmctrlID = Executor.getFocusedWindow();
			if (++i == 300) {
				verifyWindows();
				i = 0;
			}
		}
		return wmctrlID;
	}

	/**
	 * Geht durch alle Programme durch und gibt zurueck wie viele minimiert sind
	 * 
	 * @return Die Menge an nicht-minimierten Programme
	 */
	public static int getNonMinimized() {
		int nonMinimized = 0;
		for (ProgramInfo process : runningPrograms)
			if (!process.isMinimized())
				nonMinimized++;
		return nonMinimized;
	}

	/**
	 * Gibt alle geoefnetten Programme zurueck
	 * 
	 * @return Alle Programme
	 */
	public static ArrayList<ProgramInfo> getRunningPrograms() {
		return runningPrograms;
	}

	/**
	 * @return the desktopMode
	 */
	public static boolean isDesktopMode() {
		return desktopMode;
	}

	/**
	 * Minimiert das zum Zeitpunkt fokusierten Fenster
	 */
	public static void minimize() {
		if (runningPrograms.size() == 0 || desktopMode)
			return;

		try {
			if (runningPrograms.size() == 1)
				minimizeAllPrograms();
			else if (getNonMinimized() == 1)
				minimizeAllPrograms();
			else {
				String wmctrlID = getFocusedWindow();
				Runtime.getRuntime().exec(
						new String[] { "wmctrl", "-i", "-r", wmctrlID, "-b",
								"add,below" });
				runningPrograms.get(
						runningPrograms
								.indexOf(new ProgramInfo(wmctrlID, false)))
						.setMinimized(true);
			}
		} catch (IOException e) {
			TDILogger
					.logError("An error happened while trying to minimize a program");
		} catch (IndexOutOfBoundsException e) {
			TDILogger.logError("Impossible argument. ERROR");
		}
	}

	/**
	 * Minimiert alle Programme
	 */
	public static void minimizeAllPrograms() {
		try {
			Runtime.getRuntime().exec(new String[] { "wmctrl", "-k", "on" });
			desktopMode = true;
		} catch (IOException e) {
		}
	}

	/**
	 * Bewegt ein Program anhand der Koordinaten die vom {@link MoveHandler}
	 * gegeben werden
	 * 
	 * @param x
	 *            Die X Koordinate
	 * @param y
	 *            Die X Koordinate
	 */
	public static void moveProgram(int x, int y) {
		if (runningPrograms.size() == 0)
			return;
		try {
			Runtime.getRuntime().exec(
					new String[] { "wmctrl", "-i", "-r", getFocusedWindow(),
							"-e", "0," + x + "," + y + "," + -1 + "," + -1 });
		} catch (IOException e) {
			System.err
					.println("An error happened while trying to move or resize a program");
		}
	}

	/**
	 * oeffnet den gegebenen Programm und speichert die wmctrlID in der
	 * {@link #runningPrograms} Liste.
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
							.contains("Rubbish Bin") || line.contains("Trash")))
							|| line.contains(icon.getName()) || line.split(" ")[(line
							.split(" ").length - 1)]
							.equals(icon.getExecPath()[1]))
							&& !runningPrograms.contains(new ProgramInfo(line
									.split(" ")[0], false)))
						wmctrlID = line.split(" ")[0];
					else if (icon.getExecPath()[1].contains("mpc") && line.contains("Tangible Musicplayer"))
						wmctrlID = line.split(" ")[0];
					else if (line.split(" ")[1].equals("-1")
							&& line.contains(icon.getName())
							&& runningPrograms.contains(new ProgramInfo(line
									.split(" ")[0], false)))
						wmctrlID = line.split(" ")[0];
			}
			runningPrograms.add(0, new ProgramInfo(wmctrlID, icon.getName(),
					false));
		}catch (NullPointerException e){
			TDILogger.logError("Could not start program " +icon.getName());
		}
		catch (IOException e) {
		}
	}

	/**
	 * Vergoeßert/Verkleinert ein Program anhand der Koordinaten die vom
	 * {@link MoveHandler} gegeben werden
	 * 
	 * @param widht
	 *            Die Breite
	 * @param height
	 *            Die Hoehe
	 */
	public static void resizeProgram(int widht, int height) {
		if (runningPrograms.size() == 0)
			return;
		try {
			Runtime.getRuntime()
					.exec(new String[] { "wmctrl", "-i", "-r",
							getFocusedWindow(), "-e",
							"0," + -1 + "," + -1 + "," + widht + "," + height });
		} catch (IOException e) {
			System.err
					.println("An error happened while trying to move or resize a program");
		}
	}

	/**
	 * Stellt alle minimierten Programme her
	 */
	public static void restoreAllPrograms() {
		try {
			if (desktopMode) {
				Runtime.getRuntime().exec(
						new String[] { "wmctrl", "-k", "off" });
				desktopMode = false;
			} else
				for (ProgramInfo runningProgram : runningPrograms) {
					Runtime.getRuntime().exec(
							new String[] { "wmctrl", "-i", "-a",
									runningProgram.getWmctrlID() });
					runningProgram.setMinimized(false);
				}
		} catch (IOException e) {
		}
	}

	/**
	 * Stellt das vorige Program in der Liste her
	 */
	public static void restoreLeft() {
		if (runningPrograms.size() == 0)
			return;

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
	 * Stellt das naechste Program in der Liste her
	 */
	public static void restoreRight() {
		if (runningPrograms.size() == 0)
			return;

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

	/**
	 * Schaltet zwischen maximieren und nicht maximierten Zustand
	 */
	public static void toggleMaximization() {
		if (runningPrograms.size() == 0 || desktopMode)
			return;
		try {
			InputStreamReader bf = new InputStreamReader(Runtime
					.getRuntime()
					.exec(new String[] { "wmctrl", "-i", "-r",
							getFocusedWindow(), "-b",
							"toggle,maximized_vert,maximized_horz" })
					.getErrorStream());
			BufferedReader bf2 = new BufferedReader(bf);
			while (bf2.ready())
				System.out.println(bf2.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ist dafuer zustaendig zu kontrollieren ob die Werte vom
	 * {@link #getFocusedWindow()} stimmen
	 */
	private static void verifyWindows() {
		try {
			for (ProgramInfo runningProgram : runningPrograms) {
				BufferedReader br = Executor.getRunningTasks();
				String line;
				// Here we check whether the process has being registered under
				// the right name
				while ((line = br.readLine()) != null)
					if (line.contains(runningProgram.getWmctrlID()))
						break;
				br = Executor.getRunningTasks();
				// If not, we reassign the wmctrlID to the window
				while ((line = br.readLine()) != null)
					if (line.contains(runningProgram.getWindowName())) {
						runningProgram.setWmctrlID(line.split(" ")[0]);
						break;
					}
			}
		} catch (IOException e) {
			// TODO error message
		}
	}
}

/**
 * Eine Klasse die ein geoeffnetes Fenster repraesentiert
 * 
 * @author TDI Team
 * 
 */
class ProgramInfo {
	/**
	 * Die wmctrlID
	 */
	private String wmctrlID;
	/**
	 * Der Name, der vom wmctrl zurueckgeliefert wird. Unterscheidet sich oft
	 * vom Icon namen
	 */
	private String windowName;
	/**
	 * Zustand, ob das Programm minimiert ist oder nicht
	 */
	private boolean minimized;

	/**
	 * Der Konstuktor nimmt den wmctrlID und den Zustand des Programms.
	 * 
	 * @param wmctrlID
	 * @param minimized
	 */
	public ProgramInfo(String wmctrlID, boolean minimized) {
		this.wmctrlID = wmctrlID;
		this.minimized = minimized;
	}

	/**
	 * Falls sich der Name des Programms vom Iconnamen unterscheidet, wird
	 * dieser Konstuktor aufgerufen
	 * 
	 * @param wmctrlID
	 * @param windowName
	 * @param minimized
	 */
	public ProgramInfo(String wmctrlID, String windowName, boolean minimized) {
		this.wmctrlID = wmctrlID;
		this.minimized = minimized;
		this.windowName = windowName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return obj != null
				&& wmctrlID.equals(((ProgramInfo) obj).getWmctrlID());
	}

	/**
	 * @return the windowName
	 */
	public String getWindowName() {
		return windowName;
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

	/**
	 * @param windowName
	 *            the windowName to set
	 */
	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	/**
	 * @param wmctrlID
	 *            the wmctrlID to set
	 */
	public void setWmctrlID(String wmctrlID) {
		this.wmctrlID = wmctrlID;
	}

}
