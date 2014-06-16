package view;

import java.awt.Point;

/**
 * Die Icon Klasse repr�sentiert ein Icon
 * 
 * @author TDI Team
 */

public class Icon implements Comparable<Icon>{
	/**
	 * Name des Icons auf dem Desktop
	 */
	private String name;

	/**
	 * Der Pfad der benoetigt wird um das Programm auszufuehren
	 */
	private String[] execPath;

	/**
	 * Die Position des Icons auf dem Desktop
	 */
	private Point position;

	/**
	 * Der Befehl um entfernbare Platten zu mounten
	 */
	private String[] mountCmd;

	/**
	 * Konstruktor von Icon
	 * 
	 * @param name
	 *            Name des Icons
	 * @param position
	 *            die Position auf dem Desktop
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
	 * Diese Methode kontrolliert ob die Namen gleich sind, alles andere wird
	 * ignoriert
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

	/**
	 * @return der Pfad zum ausfuehren des Programms
	 */
	public String[] getExecPath() {
		return execPath;
	}

	/**
	 * @return der Pfad zum mounten
	 */
	public String[] getMountPath() {
		return mountCmd;
	}

	/**
	 * @return der Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return die Position des Icons
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @param execPath
	 *            das ist der Pfad zum ausfuehren des Programms auf den der
	 *            derzeitige gesetzt wird
	 */
	public void setExecPath(String[] execPath) {
		this.execPath = execPath;
	}

	/**
	 * @param mountCmd
	 *            das ist der Pfad zum Mounten auf den der derzeitige gesetzt
	 *            wird
	 */
	public void setMountCmd(String[] mountCmd) {
		this.mountCmd = mountCmd;
	}

	/**
	 * @param name
	 *            das ist der Name des Icons auf den der derzeitige gesetzt wird
	 */
	void setName(String name) {
		if (name == null)
			return;
		this.name = name;
	}

	/**
	 * @param position
	 *            das ist die Position auf die der derzeitige gesetzt wird
	 */
	public void setPosition(Point position) {
		if (position == null)
			return;
		this.position = position;
	}
}
