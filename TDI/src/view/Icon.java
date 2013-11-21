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
	public Icon(String name, Point position) {
		throw new UnsupportedOperationException();
	}

	public String getExecPath() {
		return execPath;
	}

	public void setExecPath(String execPath) {
		this.execPath = execPath;
	}

}