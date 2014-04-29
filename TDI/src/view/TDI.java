package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TDI {

	public enum TDIState {
		desktop, inapp, sleep, taskbar, window
	}

	private ArrayList<Icon> icons;
	/**
	 * The id defines which TDI this is.
	 */
	private byte id;
	private boolean isScale;
	private boolean locked;
	private float[] position = new float[3];
	private float[] rotation = new float[3];
	private float rotationLimit;

	private TDIState state = TDIState.desktop;

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

	public void calculateRotationLimit() {
		rotationLimit = (360 / icons.size()) / 2;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TDI other = (TDI) obj;
		return id == other.id;
	}

	public ArrayList<Icon> getIcons() {
		return icons;
	}

	public byte getId() {
		return id;
	}

	public float[] getPosition() {
		return this.position;
	}

	public float[] getRotation() {
		return rotation;
	}

	/**
	 * @return the rotationLimit
	 */
	public float getRotationLimit() {
		return rotationLimit;
	}

	public TDIState getState() {
		return state;

	}

	public boolean isLocked() {
		return locked;
	}

	public boolean isScale() {
		return isScale;
	}

	/**
	 * Selects next icon
	 */
	public void rotateIconsClockwise() {
		final Icon temp = icons.get(0);
		icons.remove(0);
		icons.add(temp);
	}

	/**
	 * Selects previous icon
	 */
	public void rotateIconsCounterClockwise() {
		final Icon temp = icons.get(icons.size() - 1);
		icons.remove(icons.size() - 1);
		icons.add(0, temp);
	}

	public void setIcons(List<Icon> icons) {
		this.icons = (ArrayList<Icon>) icons;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public void setIsScale(boolean scale) {
		this.isScale = scale;
	}

	public void setPosition(float[] position) {
		this.position = position;
	}

	public void setRotation(float[] rotation) {
		this.rotation = rotation;
	}

	/**
	 * @param rotationLimit
	 *            the rotationLimit to set
	 */
	public void setRotationLimit(float rotationLimit) {
		this.rotationLimit = rotationLimit;
	}

	public void setState(TDIState state) {
		this.state = state;
	}

	/**
	 * Toggles the lock state
	 */
	public void toggleLock() {
		locked = !locked;
	}

	@Override
	public String toString() {
		String s = "TDI [id=" + id + ", icons=" + icons;
		s += ", position=" + Arrays.toString(position);
		s += ", rotation=" + Arrays.toString(rotation);
		s += ", locked=" + locked;
		s += ", state=" + state + "]";
		return s;
	}
}
