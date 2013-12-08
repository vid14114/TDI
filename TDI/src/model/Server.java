package model;

/**
 * Implements the runnable interface
 * 
 * RUN METHOD CALLS ADD METHOD FROM BIGLOGIC;IS MASTER, IS BIG.
 * IS SLIGHTLY SMALLER, IS NOT SO BIG.
 */
public class Server implements Runnable{

	/**
	 * 
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
	

	// ask only translation update of specific TUIO	
	public static byte sendWI_GET_TRANS() 
	{
		return 63;
	}
	// ask only rotation update of specific TUIO
	public static byte sendWI_GET_ROT()
	{
		return 64;
	}
	// ask resolution of playground //also answer to GET messages
	public static byte sendWI_GET_PLSIZE()
	{
		return 65;
	}
	// set/send state of specific TUIO
	public static byte sendWI_SET_STATE()
	{
		return 80;
	}
	

}