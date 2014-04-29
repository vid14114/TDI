package view;

import java.awt.Point;

public class Icon implements Comparable<Icon> {
	/**
	 * The execution path to the program.
	 */
	private String[] execPath;
	/**
	 * Command to mount removable drives
	 */
	private String[] mountCmd;
	/**
	 * Name of the icon
	 */
	private String name;

	/**
	 * The position of the icon on the desktop
	 */
	private Point position;

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

	@Override
	public int compareTo(Icon i) {
		return ((position.x - i.position.x) * 1000) + position.y - i.position.y;
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
		final Icon other = (Icon) obj;
		return name.equals(other.name);
	}

	public String[] getExecPath() {
		return execPath;
	}

	public String[] getMountPath() {
		return mountCmd;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	public void setExecPath(String[] execPath) {
		this.execPath = execPath;
	}

	public void setMountCmd(String[] mountCmd) {
		this.mountCmd = mountCmd;
	}

	void setName(String name) {
		if (name == null)
			return;
		this.name = name;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	void setPosition(Point position) {
		if (position == null)
			return;
		this.position = position;
	}
}