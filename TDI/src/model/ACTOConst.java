package model;

/**
 * ACTO_Const
 * 		pure referencing class for constants and definition of protocols.
 * 		constants should be called using <tt>ACTO_Const.CAPITAL_LETTERS</tt>
 * 		no instancing of this class possible
 * 
 * @author Emanuel Vonach
 */

public final class ACTOConst {
	
	public static final String TAG = "ACTO_Const";

	/**
	 * DEFINITION OF STATES
	 *  e.g. Wifi, USB, etc
	 */
	public static final int STATE_CLOSED = 1;
	public static final int STATE_PENDING = 2;
	public static final int STATE_CONNECTED = 3;
	public static final int STATE_UNDEFINED = 0;
	
	/**
	 * MESSAGES FOR INTER-SERVICE COMMUNICATION
	 */
	//TODO: messages > 100 to discern from WI, unnecessary?
	public static final int SVC_REG_CLIENT = 101;	// replyTo has to be set
	public static final int SVC_UNREG_CLIENT = 102;	// replyTo has to be set
	public static final int SVC_KILL_SERVICE = 103;	//reserved
	public static final int SVC_TEST_STH = 104;	//reserved
	
	public static final int SVC_INIT_DONE = 105;
	
	public static final int SVC_INFO_USB = 116;
	public static final int SVC_INFO_WIFI = 117;

	public static final int SVC_TRK_UPDATE = 120;
	
//	public static final int SVC_FULL_STATE = 150;	//reserved // ask full update of all TUIOs					// format: CMD
	public static final int SVC_ALL_POSE = 151;	//reserved // ask pose update of all TUIOs					// format: CMD
//	public static final int SVC_FULL_EXT = 152;		//reserved // ask ext update of all TUIOs					// format: CMD
//	public static final int SVC_FULL_TRANS = 153;	//reserved // ask only translation update of all TUIOs		// format: CMD
//	public static final int SVC_FULL_ROT = 154;		//reserved // ask only rotation update of all TUIOs			// format: CMD
	
	public static final int SVC_GET_STATE = 160;	//reserved // ask update of specific TUIO					// format: CMD, ID
	public static final int SVC_GET_POSE = 161;		//reserved // ask pose update of specific TUIO 				// format: CMD, ID
	public static final int SVC_GET_EXT = 162;		//reserved // ask ext update of specific TUIO				// format: CMD, ID	// CMD, full USB_GET_MSG without SEP
	public static final int SVC_GET_TRANS = 163;	//reserved // ask only translation update of specific TUIO	// format: CMD, ID
	public static final int SVC_GET_ROT = 164;		//reserved // ask only rotation update of specific TUIO		// format: CMD, ID
	
	public static final int SVC_GET_PLSIZE = 165;	// ask resolution of playground								// format: CMD
	
	//TODO: could be divided into individual msg, like WI_GET_##?
	public static final int SVC_GET_SETTING = 166;	// format: arg1 = which setting
	
	//also answer to GET messages
	public static final int SVC_SET_STATE = 180; 	//reserved // set/send state of specific TUIO						// format: CMD, ID, float[3], float[4], ???
	public static final int SVC_SET_POSE = 181; 	//reserved // set/send pose of specific TUIO						// format: CMD, ID, float[3], float[4]
	public static final int SVC_SET_EXT = 182; 		//reserved // set/send ext state of specific TUIO					// format: CMD, ID	// CMD, full USB_GET_MSG without SEP
	public static final int SVC_SET_TRANS = 183;	//reserved // set/send only translation update of specific TUIO		// format: CMD, ID, float[3]
	public static final int SVC_SET_ROT = 184; 		//reserved // set/send only rotation update of specific TUIO		// format: CMD, ID, float[4]
	
	public static final int SVC_SET_PLSIZE = 185; 	//reserved // set/send resolution of playground, equivalent String "U"	// format: CMD, int[3]
	
	//TODO: could be divided into individual msg, like WI_GET_##?
	public static final int SVC_SET_SETTING = 186;	// format: arg1 = which setting, arg2 = int = state or port, undefined = toggle


	/**
	 * MESSAGES FOR WIFI PROTOCOL
	 * single byte which specifies the command and thereby the package content
	 */
	public static final byte WI_ACK = 40;			// ask/sending acknowledge, = String "("						// format: CMD
	public static final byte WI_NAK = 41;			// negative acknowledge, = String ")"							// format: CMD
	
	public static final byte WI_EVT_TRK_READY = 42;	//reserved // sending onTrackerReady notification			// format: CMD
	
//	public static final byte WI_FULL_STATE = 50;	//TODO: remove? // ask full update of all TUIOs						// format: CMD
	public static final byte WI_ALL_POSE = 51;		//TODO: remove? // ask pose update of all TUIOs						// format: CMD
//	public static final byte WI_FULL_EXT = 52; 		//TODO: remove? // ask ext update of all TUIOs						// format: CMD
//	public static final byte WI_FULL_TRANS = 53;	//TODO: remove? // ask only translation update of all TUIOs			// format: CMD
//	public static final byte WI_FULL_ROT = 54; 		//TODO: remove? // ask only rotation update of all TUIOs				// format: CMD
	
	//public static final byte WI_GET_STATE = 60;		//TODO: remove? // ask update of specific TUIO						// format: CMD, ID
	public static final byte WI_GET_POSE = 61;		//reserved // ask pose update of specific TUIO 					// format: CMD, ID
	public static final byte WI_GET_EXT = 62;		//reserved // ask ext update of specific TUIO					// format: CMD, ID	// CMD, full USB_GET_MSG without SEP
	public static final byte WI_GET_TRANS = 63; 	//reserved // ask only translation update of specific TUIO		// format: CMD, ID
	public static final byte WI_GET_ROT = 64;		//reserved // ask only rotation update of specific TUIO			// format: CMD, ID
	
	public static final byte WI_GET_PLSIZE = 65; 	// ask resolution of playground								// format: CMD
	
	public static final byte WI_GET_ST_USB = 66; 	// ask USB state, equivalent String "B"						// format: CMD
	public static final byte WI_GET_ST_APBRC = 67; 	// ask app broadcast state									// format: CMD
	public static final byte WI_GET_ST_WIFI = 68; 	// ask WIFI state											// format: CMD
	public static final byte WI_GET_ST_WIBRC = 69; 	// ask wifi broadcast state									// format: CMD
	public static final byte WI_GET_ST_CAL = 70;	// ask calibration state									// format: CMD
	
	//also answer to GET messages
	//public static final byte WI_SET_STATE = 80;		//TODO: remove? // set/send state of specific TUIO					// format: CMD, ID, float[3], float[4], ???
	public static final byte WI_SET_POSE = 81;		//reserved // set/send pose of specific TUIO					// format: CMD, ID, float[3], float[4]
	public static final byte WI_SET_EXT = 82;		//reserved // set/send ext state of specific TUIO				// format: CMD, ID	// CMD, full USB_SET_MSG without SEP
	public static final byte WI_SET_TRANS = 83; 	//reserved // set/send only translation update of specific TUIO	// format: CMD, ID, float[3]
	public static final byte WI_SET_ROT = 84;		//reserved // set/send only rotation update of specific TUIO	// format: CMD, ID, float[4]
	
	public static final byte WI_SET_PLSIZE = 85; 	// set/send resolution of playground, equivalent String "U"		// format: CMD, int[3]
	
	//TODO: convert states from byte to int and back for bandwidth efficiency if necessary?
	public static final byte WI_SET_ST_USB = 86;	// set/send USB state (int = state or port, undefined = toggle)			// format: CMD, int
	public static final byte WI_SET_ST_APBRC = 87;	// set/send broadcast state (int = state, undefined = toggle)			// format: CMD, int
	public static final byte WI_SET_ST_WIFI = 88;	// set/send WIFI state (int = state or port, undefined = toggle)		// format: CMD, int
	public static final byte WI_SET_ST_WIBRC = 89;	// set/send broadcast state (int = state, undefined = toggle)			// format: CMD, int
	public static final byte WI_SET_ST_CAL = 90;	// set/send calibration state (int = state or port, undefined = toggle)	// format: CMD, int

	
	
	/**
	 * MESSAGES FOR USB SERIAL COMMUNICATION
	 * single byte which specifies the command and thereby the package content
	 */
	public static final byte USB_SEP = -111;		//use to separate messages
	
	public static final byte USB_ACK = 40;			// ask/sending acknowledge			// format: CMD, SEP
	public static final byte USB_NAK = 41; 			// negative acknowledge				// format: CMD, SEP
	
	public static final byte USB_PING = 45; 		// are you online?					// format: CMD, ID, SEP
	public static final byte USB_SYNC = 46;			// give me full sync of TUIO		// format: CMD, ID, SEP
	
	public static final byte USB_MOTOR = 50;		// motor unit on extension			//format: CMD, ID, direction(byte), speed(byte), SEP
	public static final byte USB_MOTOR_MS = 51;		// motor unit on extension			//format: CMD, ID, direction(byte), speed(byte), time_ms(int), SEP
	
	public static final byte USB_GET_DI_UNIT = 60;	// get state of digital unit on ext		//format: CMD, ID, dataID(byte), SEP
	public static final byte USB_SET_DI_UNIT = 80;	// set state of digital unit on ext		//format: CMD, ID, dataID(byte), data(byte), SEP
	public static final byte USB_SET_PWM_UNIT = 82;	// answer of analog unit state on ext	//format: CMD, ID, dataID(byte), data(int), SEP
	
	public static final byte USB_GET_AN_UNIT = 61;	// get state of analog unit on ext		//format: CMD, ID, dataID(byte), SEP
	public static final byte USB_SET_AN_UNIT = 81;	// set state of analog unit on ext		//format: CMD, ID, dataID(byte), data(int), SEP
	
	public static final byte USB_GET_BYTE = 66;		// get byte data				//format: CMD, ID, dataID(byte), SEP
	public static final byte USB_GET_CHAR = 67;		// get char data				//format: CMD, ID, dataID(byte), SEP
	public static final byte USB_GET_INT = 68;		// get int data					//format: CMD, ID, dataID(byte), SEP
	public static final byte USB_GET_FLOAT = 69;	// get float data				//format: CMD, ID, dataID(byte), SEP
	
	public static final byte USB_SET_BYTE = 86;		// send byte data				//format: CMD, ID, dataID(byte), data(byte), SEP
	public static final byte USB_SET_CHAR = 87;		// send char data				//format: CMD, ID, dataID(byte), data(char16), SEP
	public static final byte USB_SET_INT = 88;		// send integer data			//format: CMD, ID, dataID(byte), data(int32), SEP
	public static final byte USB_SET_FLOAT = 89;	// send float data				//format: CMD, ID, dataID(byte), data(float32), SEP
	
	
	/**
	 * ACTO_Const
	 * 		private constructor prevents instancing objects of this class
	 */

	private ACTOConst(){
	    throw new AssertionError();
	  }

}