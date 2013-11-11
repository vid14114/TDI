package model;

/**
 * Implements the runnable interface
 * 
 * RUN METHOD CALLS ADD METHOD FROM BIGLOGIC;IS MASTER, IS BIG.
 * IS SLIGHTLY SMALLER, IS NOT SO BIG.
 */
public class Server implements Runnable{

	/**
	 * ip
	 */
	protected static String ip;

	/**
	 * Overrides the run method of the runnable interface
	 */
	public void run() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sends the specified command
	 */
	public static void sendCommand() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sends command to external program
	 * @param externalCommand
	 */
	public static void sendExternalCommand(String externalCommand) {
		throw new UnsupportedOperationException();
	}

}