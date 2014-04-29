package view;

import java.awt.Point;

public class Icon implements Comparable<Icon>{
	/**
	 * Name of the icon
	 */
	private String name;
	/**
	 * The execution path to the program.
	 */
	private String[] execPath;
	/**
	 * The position of the icon on the desktop
	 */
	private Point position;

	/**
	 * Command to mount removable drives
	 */
	private String[] mountCmd;

	/**
	 * Constructor of icon
	 * 
	 * @param name
	 * @param position
	 */
	public Icon(String name, Point position) {
		setName(name);
		setPosition(position);
	}

	public String getName() {
		return name;
	}

    void setName(String name) {
        if (name == null)
            return;
		this.name = name;
	}

	public String[] getExecPath() {
		return execPath;
	}

	public void setExecPath(String[] execPath) {
		this.execPath = execPath;
	}

	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
    public void setPosition(Point position) {
        if (position == null)
            return;
		this.position = position;
	}

	public String[] getMountPath() {
		return mountCmd;
	}

	public void setMountCmd(String[] mountCmd) {
		this.mountCmd = mountCmd;
	}

	/**
	 * The equals method only checks whether the name is equal all other
	 * attributes are ignored
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (name == null || obj == null || getClass() != obj.getClass())
			return false;
		Icon other = (Icon) obj;
        return name.equals(other.name);
    }

	@Override
	public int compareTo(Icon i) {
        return ((position.x - i.position.x) * 1000) + position.y - i.position.y;
    }
}