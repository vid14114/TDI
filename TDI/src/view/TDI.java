package view;

import java.util.ArrayList;

public class TDI {

	/**
	 * The id defines which TDI this is.
	 */
	private byte id;
	private ArrayList<Icon> icons;
	private float[] position=new float[3];
	//TODO neigung float? int? array? tilting in 4 directions
	private float[] tilt = new float[4];
	/**
	 * Rotation in int
	 */
	private float rotation; 
	private int state; //MUST BE ENUM DO NOT FORGET CHANGE THIS YO MATE!!!!! TODOTODOTODOTODO
	private boolean locked;

	public float[] getPosition() {
		return this.position;
	}

	public void setPosition(float x, float y, float z) {
		position[0]=x;
		position[1]=y;
		position[2]=z;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	/**
	 * 
	 * @param id
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param rot
	 */
	public TDI(byte id, float posX, float posY, float posZ, float rot) {
        this.id = id;
        position[0] = posX;
        position[1] = posY;
        position[2] = posZ;
        rotation = rot;
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

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TDI other = (TDI) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * @return the tilt
	 */
	public float[] getTilt() {
		return tilt;
	}

	/**
	 * @param tilt the tilt to set
	 */
	public void setTilt(float[] tilt) {
		this.tilt = tilt;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}