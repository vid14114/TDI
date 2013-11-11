package view;

import java.util.ArrayList;

public class TDI {

	/**
	 * The id defines which TDI this is.
	 */
	private int id;
	private ArrayList<Icon> icons;
	private float[] position;
	/**
	 * Rotation in int
	 */
	private int rotation; 
	private int state; //MUST BE ENUM DO NOT FORGET CHANGE THIS YO MATE!!!!! TODOTODOTODOTODO
	private boolean locked;

	public float[] getPosition() {
		return this.position;
	}

	public void setPosition(float[] position) {
		this.position = position;
	}

	public int getRotation() {
		return this.rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	/**
	 * Constructor for TDI.
	 */
	public TDI() {
		
	}

	/**
	 * Selects next icon
	 */
	public void rotateClockwise() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Selects previous icon
	 */
	public void rotateCounter() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Toggles the lock state
	 */
	public void toggleLock() {
		throw new UnsupportedOperationException();
	}

}