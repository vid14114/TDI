package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Die TDI Klasse repr�sentiert ein TDI
 * 
 * @author TDI Team
 */

public class TDI {
    
    /**
     * ü ä ö
     * Identifiert das TDI
	 */
	private byte id;
	
	/**
	 * Eine Liste der Icons fuer die das TDI zustaendig ist
	 */
    private ArrayList<Icon> icons;
    
    /**
     * Wo sich das TDI befindet
     */
    private float[] position = new float[3];
    
    /**
     * Ob das TDI gedreht ist und wie
     */
    private float[] rotation = new float[3];
    
    /**
     * Ob das TDI bewegt wird oder nicht
     */
    private boolean moving;
    
    /**
	 * @return gibt zurueck ob sich das TDI bewegt
	 */
	public boolean isMoving() {
		return moving;
	}

	/**
	 * @param setzt die moving Variable auf die gegebene
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	/**
	 * Das Limit der Rotation
	 */
	private float rotationLimit;
	
	/**
	 * Ob das TDI im Desktop oder in der Taskbar ist
	 */
    private TDIState state = TDIState.desktop;
    
    /**
     * Ob es gelocked ist
     */
	private boolean locked;
	
	/**
	 * Ob es derzeit ein Fenster skaliert
	 */
	private boolean isScale;

    /**
     * Konstruktor von TDI
     * 
     * @param id ID des TDIs
	 * @param posX X Position auf dem Tisch
	 * @param posY Y Position auf dem Tisch
	 * @param posZ Z Position auf dem Tisch
	 * @param rot Wie es gedreht ist
     */
    public TDI(byte id, float posX, float posY, float posZ, float[] rot) {
        this.id = id;
        position[0] = posX;
        position[1] = posY;
        position[2] = posZ;
        rotation = rot;
    }

    /**
	 * @return Die Position
	 */
	public float[] getPosition() {
		return this.position;
	}

	/**
	 * @return der State
	 */
	public TDIState getState()
	{
		return state;
		
	}
	
	/**
	 * @param state das ist der State auf den der derzeitige gesetzt wird
	 */
	public void setState(TDIState state)
	{
		this.state = state;
	}
	
	/**
	 * @return gibt zurueck ob es gelocked ist
	 */
	public boolean isLocked()
	{
		return locked;
	}
	
	/**
	 * @return gibt zurueck ob es am skalieren ist
	 */
	public boolean isScale()
	{
		return isScale;
	}
	
	/**
	 * @return gibt zurueck ob es skaliert ist
	 */
	public void setIsScale(boolean scale)
	{
		this.isScale=scale;
	}
	
	/**
	 * @param position das ist die Position auf den der derzeitige gesetzt wird
	 */
	public void setPosition(float[] position) {
		this.position = position;
	}

	/**
	 * @return gibt die Rotation zurueck
	 */
	public float[] getRotation() {
		return rotation;
	}

	/**
	 * @param rotation das ist die Rotation auf den der derzeitige gesetzt wird
	 */
	public void setRotation(float[] rotation) {
		this.rotation = rotation;
	}

    /**
     * Waehlt das naechste Icon aus
     */
    public void rotateIconsClockwise() {
        Icon temp = icons.get(0);
        icons.remove(0);
        icons.add(temp);
    }

    /**
     * Waehlt das vorherige Icon aus
     */
    public void rotateIconsCounterClockwise() {
        Icon temp = icons.get(icons.size() - 1);
        icons.remove(icons.size() - 1);
        icons.add(0, temp);
    }

    /**
     * Wechselt zwischen den locked states
     */
    public void toggleLock() {
        locked = !locked;
    }

    /**
	 * @return gibt die ID zurueck
	 */
    public byte getId() {
        return id;
    }

    /**
	 * @param id das ist die ID auf die die derzeitige gesetzt wird
	 */
    public void setId(byte id) {
        this.id = id;
    }

    /**
	 * @return gibt die Icons zurueck
	 */
    public ArrayList<Icon> getIcons() {
        return icons;
    }

    /**
	 * @param icons das sind die Icons auf die die derzeitigen gesetzt werden
	 */
    public void setIcons(List<Icon> icons) {
        this.icons = (ArrayList<Icon>) icons;
    }

    /**
	 * @return gibt die Beschreibung der Klasse in einer leserlichen Form zurueck
	 */
    @Override
    public String toString() {
        String s = "TDI [id=" + id + ", icons=" + icons;
        s += ", position=" + Arrays.toString(position);
        s += ", rotation=" + Arrays.toString(rotation);
        s += ", locked=" + locked;
        s += ", state=" + state + "]";
        return s;
    }

    /**
     * @param obj das Objekt mit dem verglichen wird
	 * @return gibt zurueck ob die Objekte gleich sind
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
        return id == other.id;
    }

    /**
     *Definiert das enum fuer die states
     */
    public enum TDIState {
        inapp, desktop, taskbar, window, sleep
    }

    /**
	 * @return gibt das Limit der Rotation zurueck
	 */
	public float getRotationLimit() {
		return rotationLimit;
	}

	/**
	 * Berechnet das Limit der Rotation
	 */
	public void calculateRotationLimit(){
		rotationLimit = (360/icons.size());
	}
	
	/**
	 * @param rotationLimit das Limit der Rotation auf das das derzeitige Limit gesetzt wird
	 */
	public void setRotationLimit(float rotationLimit) {
		this.rotationLimit = rotationLimit;
	}
}
