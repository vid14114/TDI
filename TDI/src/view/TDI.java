package view;

import java.util.ArrayList;

public class TDI {

	/**
	 * The id defines which TDI this is.
	 */
	private byte id;
	private ArrayList<Icon> icons;
	private float[] position = new float[3];
	private float[] rotation = new float[3];
	private int state;
	private boolean locked;

	public float[] getPosition() {
		return this.position;
	}

	public void setPosition(float x, float y, float z) {
		position[0] = x;
		position[1] = y;
		position[2] = z;
	}

	public float[] getRotation() {
		return rotation;
	}

	public void setRotation(float[] rotation) {
		this.rotation = rotation;
	}

	/**
	 * Constructor for TDI.
	 */

	public TDI(byte id, float posX, float posY, float posZ, float[] rot) {
		this.id = id;
		position[0] = posX;
		position[1] = posY;
		position[2] = posZ;
		rotation = rot;
	}

	/**
	 * Selects next icon
	 */
	public void rotateClockwise() { // TODO after startup routine
		throw new UnsupportedOperationException();
	}

	/**
	 * Selects previous icon
	 */
	public void rotateCounter() { // TODO after startup routine
		throw new UnsupportedOperationException();
	}

	/**
	 * Toggles the lock state
	 */
	public void toggleLock() {
		locked = !locked;
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}
}