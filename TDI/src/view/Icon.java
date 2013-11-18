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
	private String execPath;
	private Point position;

	/**
	 * Constructor of icon
	 * @param name
	 * @param execPath
	 * @param position
	 */
	public Icon(String name, String execPath, Point position) {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExecPath() {
		return execPath;
	}

	public void setExecPath(String execPath) {
		this.execPath = execPath;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

}