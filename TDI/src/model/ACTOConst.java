package model;

/**
 * ACTO_Const pure referencing class for constants and definition of protocols.
 * constants should be called using <tt>ACTO_Const.CAPITAL_LETTERS</tt> no
 * instancing of this class possible
 * 
 * @author Emanuel Vonach
 */

public final class ACTOConst {

	/**
	 * DEFINITION OF STATES e.g. Wifi, USB, etc
	 */
	public static final int STATE_CLOSED = 1;

	public static final int STATE_CONNECTED = 3;
	public static final int STATE_PENDING = 2;
	public static final int STATE_UNDEFINED = 0;
	// of all TUIOs // format:
	// CMD
	public static final int SVC_FULL_EXT = 152; // reserved // ask ext update of

	// of all TUIOs // format:
	// CMD
	public static final int SVC_FULL_POSE = 151; // reserved // ask pose update
	// translation update of all
	// TUIOs // format: CMD
	public static final int SVC_FULL_ROT = 154; // reserved // ask only rotation
												// update of all TUIOs //
												// format: CMD
	public static final int SVC_FULL_STATE = 150; // reserved // ask full update

	// all TUIOs // format: CMD
	public static final int SVC_FULL_TRANS = 153; // reserved // ask only
	// of specific TUIO // format:
	// CMD, ID
	public static final int SVC_GET_EXT = 162; // reserved // ask ext update of

	public static final int SVC_GET_PLSIZE = 165; // reserved // ask resolution
													// of playground // format:
													// CMD

	// specific TUIO // format:
	// CMD, ID
	public static final int SVC_GET_POSE = 161; // reserved // ask pose update
												// translation update of
												// specific TUIO // format:
												// CMD, ID
	public static final int SVC_GET_ROT = 164; // reserved // ask only rotation
												// update of specific TUIO //
												// format: CMD, ID
												// TODO: could be divided into
												// individual msg, like
												// WI_GET_##?
	public static final int SVC_GET_SETTING = 166; // format arg1 = which
													// setting
	public static final int SVC_GET_STATE = 160; // reserved // ask update of
	// specific TUIO // format: CMD,
	// ID
	public static final int SVC_GET_TRANS = 163; // reserved // ask only

	public static final int SVC_INFO_USB = 116;
	public static final int SVC_INFO_WIFI = 117;
	public static final int SVC_KILL_SERVICE = 103; // reserved
	/**
	 * MESSAGES FOR INTER-SERVICE COMMUNICATION
	 */
	// TODO: messages > 100 to discern from WI, unnecessary?
	public static final int SVC_REG_CLIENT = 101; // replyTo has to be set
													// specific TUIO // format:
													// CMD,
	// ID, float[3], float[4]
	public static final int SVC_SET_EXT = 182; // reserved // set/send ext state

	public static final int SVC_SET_PLSIZE = 185; // reserved // set/send
													// resolution of playground,
													// equivalent String "U" //
													// format: CMD, int[3]

	// of specific TUIO //
	// format: CMD, ID,
	// float[3], float[4], ???
	public static final int SVC_SET_POSE = 181; // reserved // set/send pose of

	// translation update of
	// specific TUIO // format:
	// CMD, ID, float[3]
	public static final int SVC_SET_ROT = 184; // reserved // set/send only
												// rotation update of specific
												// TUIO // format: CMD, ID,
												// float[4]
												// TODO: could be divided into
												// individual msg, like
												// WI_GET_##?
	public static final int SVC_SET_SETTING = 186; // format arg1 = which
													// setting, arg2 = int =
													// state or port, undefined
													// = toggle
	// also answer to GET messages
	public static final int SVC_SET_STATE = 180; // reserved // set/send state
													// of specific TUIO //
													// format:
													// CMD, ID, ???
	public static final int SVC_SET_TRANS = 183; // reserved // set/send only
	public static final int SVC_TRK_UPDATE = 120;

	public static final int SVC_UNREG_CLIENT = 102; // replyTo has to be set

	public static final String TAG = "ACTO_Const";

	public static final byte USB_ACK = 40; // ask/sending acknowledge // format:
	public static final byte USB_DATA_BYTE = 60; // send byte data to TUIO

	// //format: CMD, ID,
	// dataID(byte), data(byte),
	// SEP
	public static final byte USB_DATA_CHAR = 61; // send char data to TUIO

	// //format: CMD, ID,
	// dataID(byte), data(int), SEP
	public static final byte USB_DATA_FLOAT = 63; // send float data to TUIO
													// //format: CMD, ID,
													// dataID(byte),
													// data(float), SEP
													// //format: CMD, ID,
													// dataID(byte), data(char),
													// SEP
	public static final byte USB_DATA_INT = 62; // send integer data to TUIO
	public static final byte USB_MOTOR_1 = 50; // analog unit on extension
	// //format: CMD, ID,
	// speed(int), SEP
	public static final byte USB_MOTOR_2 = 51; // analog unit on extension
	public static final byte USB_MOTOR_3 = 52; // analog unit on extension

	public static final byte USB_MOTOR_4 = 53; // analog unit on extension
												// CMD, SEP
	public static final byte USB_NAK = 41; // negative acknowledge // format:
											// CMD, SEP
	public static final byte USB_PING = 45; // are you online? // format: CMD,
	/**
	 * MESSAGES FOR USB SERIAL COMMUNICATION single byte which specifies the
	 * command and thereby the package content
	 */
	public static final byte USB_SEP = -111; // use to separate messages
												// ID, SEP
	public static final byte USB_SYNC = 46; // give me full sync of TUIO //
											// format: CMD, ID, SEP

	/**
	 * MESSAGES FOR WIFI PROTOCOL single byte which specifies the command and
	 * thereby the package content
	 */
	public static final byte WI_ACK = 40; // ask/sending acknowledge, = String

	public static final byte WI_EVT_TRK_READY = 42; // reserved // sending
													// onTrackerReady
													// notification // format:
													// CMD
													// of all TUIOs // format:
													// CMD
	public static final byte WI_FULL_EXT = 52; // reserved // ask ext update of
												// of all TUIOs // format:
												// CMD
	public static final byte WI_FULL_POSE = 51; // reserved // ask pose update
												// translation update of all
												// TUIOs // format: CMD
	public static final byte WI_FULL_ROT = 54; // reserved // ask only rotation
												// update of all TUIOs //
												// format: CMD
	public static final byte WI_FULL_STATE = 50; // reserved // ask full update

	// all TUIOs // format: CMD
	public static final byte WI_FULL_TRANS = 53; // reserved // ask only
													// specific TUIO // format:
													// CMD,
													// ID
	public static final byte WI_GET_EXT = 62; // reserved // ask ext update of
	public static final byte WI_GET_PLSIZE = 65; // ask resolution of playground
	// // format: CMD
	// specific TUIO // format: CMD,
	// ID
	public static final byte WI_GET_POSE = 61; // reserved // ask pose update of
												// translation update of
												// specific TUIO // format: CMD,
												// ID
	public static final byte WI_GET_ROT = 64; // reserved // ask only rotation
												// update of specific TUIO //
												// format: CMD, ID

	// String "B" // format: CMD
	public static final byte WI_GET_ST_APBRC = 67; // ask app broadcast state //

	// // format: CMD
	public static final byte WI_GET_ST_CAL = 70; // ask calibration state //
													// format: CMD
	public static final byte WI_GET_ST_USB = 66; // ask USB state, equivalent
	// CMD
	public static final byte WI_GET_ST_WIBRC = 69; // ask wifi broadcast state
													// format: CMD
	public static final byte WI_GET_ST_WIFI = 68; // ask WIFI state // format:
	public static final byte WI_GET_STATE = 60; // reserved // ask update of

	// specific TUIO // format: CMD,
	// ID
	public static final byte WI_GET_TRANS = 63; // reserved // ask only

	// "(" // format: CMD
	public static final byte WI_NAK = 41; // negative acknowledge, = String ")"
											// // format: CMD
											// specific TUIO // format: CMD,
											// ID, float[3], float[4]
	public static final byte WI_SET_EXT = 82; // reserved // set/send ext state

	public static final byte WI_SET_PLSIZE = 85; // reserved // set/send
													// resolution of playground,
													// equivalent String "U" //
													// format: CMD, int[3]
	// specific TUIO // format: CMD,
	// ID, float[3], float[4], ???
	public static final byte WI_SET_POSE = 81; // reserved // set/send pose of

	// translation update of
	// specific TUIO // format: CMD,
	// ID, float[3]
	public static final byte WI_SET_ROT = 84; // reserved // set/send only
												// rotation update of specific
												// TUIO // format: CMD, ID,
												// float[4]
												// state or port, undefined
												// = toggle) // format: CMD,
												// int
	public static final byte WI_SET_ST_APBRC = 87; // set/send broadcast state
	// (int = state, undefined =
	// toggle) // format: CMD,
	// int
	public static final byte WI_SET_ST_CAL = 90; // set/send calibration state
													// (int = state or port,
													// undefined = toggle) //
													// format: CMD, int
	// TODO: convert from byte to int and back for bandwidth efficiency if
	// necessary?
	public static final byte WI_SET_ST_USB = 86; // set/send USB state (int =

	// state or port, undefined
	// = toggle) // format: CMD,
	// int
	public static final byte WI_SET_ST_WIBRC = 89; // set/send broadcast state
													// (int = state, undefined =
													// toggle) // format: CMD,
													// int
	public static final byte WI_SET_ST_WIFI = 88; // set/send WIFI state (int =
													// also answer to GET
													// messages
	public static final byte WI_SET_STATE = 80; // reserved // set/send state of
												// of specific TUIO // format:
												// CMD, ID, ???
	public static final byte WI_SET_TRANS = 83; // reserved // set/send only

	// TODO: USB_DATA also as getter, and setter
	// TODO: use only one digUnit / anaUnit aber daf�r zus�tzliche IDbyte wie
	// bei data? verst�ndlicher?

	// public static final byte USB_DI_UNIT_0 = 0; // digital unit on extension
	// //format: set: CMD, ID, data(byte), SEP , get:CMD, ID, SEP
	// public static final byte USB_DI_UNIT_1 = 1; // digital unit on extension
	// public static final byte USB_DI_UNIT_2 = 2; // digital unit on extension
	// public static final byte USB_DI_UNIT_3 = 3; // digital unit on extension
	// public static final byte USB_DI_UNIT_4 = 4; // digital unit on extension
	// public static final byte USB_DI_UNIT_5 = 5; // digital unit on extension
	// public static final byte USB_DI_UNIT_6 = 6; // digital unit on extension
	// public static final byte USB_DI_UNIT_7 = 7; // digital unit on extension
	// public static final byte USB_DI_UNIT_8 = 8; // digital unit on extension
	// public static final byte USB_DI_UNIT_9 = 9; // digital unit on extension
	// public static final byte USB_DI_UNIT_10 = 10; // digital unit on
	// extension
	// public static final byte USB_DI_UNIT_11 = 11; // digital unit on
	// extension
	// public static final byte USB_DI_UNIT_12 = 12; // digital unit on
	// extension
	// public static final byte USB_DI_UNIT_13 = 13; // digital unit on
	// extension
	// public static final byte USB_DI_UNIT_14 = 14; // digital unit on
	// extension
	// public static final byte USB_DI_UNIT_15 = 15; // digital unit on
	// extension
	// public static final byte USB_DI_UNIT_16 = 16; // digital unit on
	// extension
	// public static final byte USB_DI_UNIT_17 = 17; // digital unit on
	// extension
	// public static final byte USB_DI_UNIT_18 = 18; // digital unit on
	// extension
	// public static final byte USB_DI_UNIT_19 = 19; // digital unit on
	// extension
	//
	// public static final byte USB_AN_UNIT_0 = 20; // analog unit on extension
	// //format: set: CMD, ID, data(int), SEP , get:CMD, ID, SEP
	// public static final byte USB_AN_UNIT_1 = 21; // analog unit on extension
	// public static final byte USB_AN_UNIT_2 = 22; // analog unit on extension
	// public static final byte USB_AN_UNIT_3 = 23; // analog unit on extension
	// public static final byte USB_AN_UNIT_4 = 24; // analog unit on extension
	// public static final byte USB_AN_UNIT_5 = 25; // analog unit on extension
	// public static final byte USB_AN_UNIT_6 = 26; // analog unit on extension
	// public static final byte USB_AN_UNIT_7 = 27; // analog unit on extension
	// public static final byte USB_AN_UNIT_8 = 28; // analog unit on extension
	// public static final byte USB_AN_UNIT_9 = 29; // analog unit on extension
	// public static final byte USB_AN_UNIT_10 = 30; // analog unit on extension
	// public static final byte USB_AN_UNIT_11 = 31; // analog unit on extension
	// public static final byte USB_AN_UNIT_12 = 32; // analog unit on extension
	// public static final byte USB_AN_UNIT_13 = 33; // analog unit on extension
	// public static final byte USB_AN_UNIT_14 = 34; // analog unit on extension
	// public static final byte USB_AN_UNIT_15 = 35; // analog unit on extension
	// public static final byte USB_AN_UNIT_16 = 36; // analog unit on extension
	// public static final byte USB_AN_UNIT_17 = 37; // analog unit on extension
	// public static final byte USB_AN_UNIT_18 = 38; // analog unit on extension
	// public static final byte USB_AN_UNIT_19 = 39; // analog unit on extension

	/**
	 * ACTO_Const private constructor prevents instancing objects of this class
	 */

	private ACTOConst() {
		throw new AssertionError();
	}

}