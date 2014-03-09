package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingWorker.StateValue;

public class TDI {

	public enum TDIState{
		inapp,desktop,taskbar,window,sleep,restore;
	}
	/**
	 * The id defines which TDI this is.
	 */
	private byte id;
	private ArrayList<Icon> icons;	
	private float[] position = new float[3];
	private float[] rotation = new float[3];
	private TDIState state=TDIState.desktop;
	private boolean locked;
	private boolean isScale;

	public float[] getPosition() {
		return this.position;
	}

	public Enum<TDIState> getState()
	{
		return state;
		
	}
	
	public void setState(TDIState state)
	{
		this.state = state;
	}
	
	public boolean getLocked()
	{
		return locked;
	}
	public boolean isScale()
	{
		return isScale;
	}
	public void setIsScale(boolean scale)
	{
		this.isScale=scale;
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
	public ArrayList<Icon> getIcons()
	{
		return icons;
	}

	public void setIcons(List<Icon> icons) {
		this.icons = (ArrayList<Icon>) icons;
	}
	
	@Override
	public String toString() {
		String s="TDI [id=" + id + ", icons=" + icons;
		s+=", position="+ Arrays.toString(position);
		s+=", rotation="+ Arrays.toString(rotation);
		s+=", locked="+ locked;
		s+=", state="+ state + "]";
		return s;
	}

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
}
