package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TDI {
    
    /**
     * The id defines which TDI this is.
	 */
	private byte id;
    private ArrayList<Icon> icons;
    private float[] position = new float[3];
    private float[] rotation = new float[3];
    private boolean moving;
    /**
	 * @return the moving
	 */
	public boolean isMoving() {
		return moving;
	}

	/**
	 * @param moving the moving to set
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	private float rotationLimit;
    private TDIState state = TDIState.desktop;
	private boolean locked;
	private boolean isScale;

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

	public float[] getPosition() {
		return this.position;
	}

	public TDIState getState()
	{
		return state;
		
	}
	
	public void setState(TDIState state)
	{
		this.state = state;
	}
	
	public boolean isLocked()
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
	public void setPosition(float[] position) {
		this.position = position;
	}

	public float[] getRotation() {
		return rotation;
	}

	public void setRotation(float[] rotation) {
		this.rotation = rotation;
	}

    /**
     * Selects next icon
     */
    public void rotateIconsClockwise() {
        Icon temp = icons.get(0);
        icons.remove(0);
        icons.add(temp);
    }

    /**
     * Selects previous icon
     */
    public void rotateIconsCounterClockwise() {
        Icon temp = icons.get(icons.size() - 1);
        icons.remove(icons.size() - 1);
        icons.add(0, temp);
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

    public ArrayList<Icon> getIcons() {
        return icons;
    }

    public void setIcons(List<Icon> icons) {
        this.icons = (ArrayList<Icon>) icons;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TDI other = (TDI) obj;
        return id == other.id;
    }

    public enum TDIState {
        inapp, desktop, taskbar, window, sleep
    }

	/**
	 * @return the rotationLimit
	 */
	public float getRotationLimit() {
		return rotationLimit;
	}

	public void calculateRotationLimit(){
		rotationLimit = (360/icons.size());
	}
	
	/**
	 * @param rotationLimit the rotationLimit to set
	 */
	public void setRotationLimit(float rotationLimit) {
		this.rotationLimit = rotationLimit;
	}
}
