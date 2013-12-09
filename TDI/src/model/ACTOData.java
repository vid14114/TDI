package model;

/**
 * CommData
 * 		data class for communication data packages and definition of protocol
 * 
 * @author Emanuel Vonach
 * 
 * based on code by Kaltenriner Christoph, Leichtfried Michael
 *
 */

import java.io.IOException;

public class ACTOData {
	
	private final String TAG = this.getClass().getSimpleName();
	
	//Byte Range is from -128 to 127
	public byte type;				//specifies the type resp. content
	
	public byte id;					//the id of the TUIO the data is valid for 
	
	public int state;				// single int values
//	public float fValue;			// single float values
//	public boolean[] bArray;		//boolean array (extensions)
	public int[] resolution;// = new int[3];		//three ints
	public float[] translation;// = new float[3];	//three floats
	public float[] rotation;// = new float[4];	//four floats (quaternion)
		
//	//CommData data types
	public static final byte EMPTY = 0;		//type
	public static final byte STATE = 1;		//type, state
	public static final byte ID = 2;		//type, id
	public static final byte RES = 3;		//type, int[3]
	public static final byte POSE = 4;		//type, id, translation[3], rotation[4]
	public static final byte TRANS = 5;		//type, id, translation[3]
	public static final byte ROT = 6;		//type, id, rotation[4]
//	public static final byte EXT = 7;


	//for Bundle representation
	public static final String B_TYP = "TYP";
	public static final String B_ID = "ID";
	public static final String B_STATE = "STA";
	
	public static final String B_FLOAT = "FLO";
	
	public static final String B_RES = "RES";

	public static final String B_TRANS = "TRA";
	public static final String B_ROT = "ROT";


	


	
	/**
	 * CommData
	 * 		empty constructor for communication data packages
	 * 
	 * @value byte type:	according to protocol, defines type and format of data package
	 * @value byte id:	target TUIO
	 * @value iValue/fValue/iArray/translation/rotation:	data depending on type
	 * 
	 */
	public ACTOData()
	{
		this.type = EMPTY;
	}
	
	/**
	 * CommData constructor: types without additional data
	 * @param byte type: defines type and format of data package
	 */
	// e.g. for ACK
	//TODO: check for right type, give type for confirmation & differentiation, OR remove type and set automatically if never necessary to differentiate
	// if type wrong, type = EMPTY for safety
	public ACTOData(byte type)
	{
		this.type = type;
	}
	
	/**
	 * CommData constructor: byte type, int iValue
	 * @param byte type: defines type and format of data package
	 * @param int iValue
	 */
	// e.g. WI_SET_ST...
	public ACTOData(byte type, int state)
	{
		if (type == STATE) {
			this.type = type;
			this.state = state;
		}
		else this.type = EMPTY;
	}
	
	/**
	 * CommData constructor: byte type, int[3] iArray
	 * @param byte type: defines type and format of data package
	 * @param int[] iArray
	 */
	// e.g. WI_SET_PLSIZE
	public ACTOData(byte type, int[] resolution)
	{
		if (type == RES && resolution.length == 3) {
			this.type = type;
			this.resolution = resolution;
		}
		else this.type = EMPTY;
	}
	
	/**
	 * CommData constructor: byte type, byte id
	 * @param byte type: defines type and format of data package
	 * @param byte id
	 */
	// e.g. WI_GET...
	public ACTOData(byte type, byte id)
	{
		if (type == ID) {
			this.type = type;
			this.id = id;
		}
		else this.type = EMPTY;
	}
	
//	/**
//	 * CommData constructor: byte type, byte id, int iValue
//	 * @param byte type: defines type and format of data package
//	 * @param byte id
//	 * @param int iValue
//	 */
//	// e.g. WI_SET_EXT...
//	public ACTOData(byte type, byte id, int iValue)
//	{
//		if (type == EXT) {
//			this.type = type;
//			this.id = id;
//			this.iValue = iValue;
//		}
//		else this.type = EMPTY;
//	}
	
	/**
	 * CommData constructor: byte type, byte id, float[] fqArray
	 * @param byte type: defines type and format of data package
	 * @param byte id
	 * @param float[] fqArray: either float[3] translation (translation) or float[4] rotation (quaternion)
	 */
	// e.g. WI_SET_TRANS
	public ACTOData(byte type, byte id, float[] fqArray)
	{
		if (type == TRANS && fqArray.length == 3) {
			this.type = type;
			this.id = id;
			this.translation = fqArray;
		}
		else if (type == ROT && fqArray.length == 4) {
			this.type = type;
			this.id = id;
			this.rotation = fqArray;
		}
		else this.type = EMPTY;
	}
	
	/**
	 * CommData constructor: byte type, byte id, float[3] translation, float[4] rotation
	 * @param byte type: defines type and format of data package
	 * @param byte id
	 * @param float[3] translation: xyz
	 * @param float[4] rotation: quaternion
	 */
	// e.g. WI_SET_POSE
	public ACTOData(byte type, byte id, float[] translation, float[] rotation)
	{
		if (type == POSE) {
			this.type = type;
			this.id = id;
			this.translation = translation;
			this.rotation = rotation;
		}
		else this.type = EMPTY;
	}
	//TODO: is check necessary? possible?
	public static boolean checkADataIntegrity(ACTOData ad) {
		return true;
//		public static final byte EMPTY = 0;		//type
//		public static final byte STATE = 1;		//type, state
//		public static final byte ID = 2;		//type, id
//		public static final byte RES = 3;		//type, int[3]
//		public static final byte POSE = 4;		//type, id, translation[3], rotation[4]
//		public static final byte TRANS = 5;		//type, id, translation[3]
//		public static final byte ROT = 6;		//type, id, rotation[4]
////		public static final byte EXT = 7;
	}
	


	//TODO: do we want to throw exceptions?
//	public class ACTODataFormatException extends Exception {
//	
//		public ACTODataFormatException(String message){
//			super(message);
//		}
//	}
	
}