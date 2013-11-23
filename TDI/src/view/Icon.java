package view;

import java.awt.Point;

public class Icon {
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
	 * Constructor of icon
	 * @param name
	 * @param execPath
	 * @param position
	 */
	public Icon(String name, Point position){
		setName(name);
		setPosition(position);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name == null)
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
	 * @param position the position to set
	 */
	public void setPosition(Point position) {
		if(position == null) return;
		this.position = position;
	}

	/**
	 * The equals method only checks whether the name is equal all other attributes are ignored
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (name == null || obj == null || getClass() != obj.getClass())
			return false;
		Icon other = (Icon) obj;
		if (!name.equals(other.name))
			return false;
		return true;
	}
	
}