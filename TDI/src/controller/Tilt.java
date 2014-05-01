package controller;

import view.TDI;

/**
 * Die {@link Tilt} Klasse kontrolliert ob ein TDI geneigt wurde oder nicht. Falls
 * ein TDI geneigt wurde, ruft er den {@link TiltListener} auf, der anschließend vom {@link TiltHandler} gehandelt wird
 * 
 * @author TDI Team
 *
 */
public class Tilt {
	/**
	 * Siehe {@link TiltListener}
	 */
	private TiltListener tiltListener;
	/**
	 * Die RestingTiltPos der TDIs am Anfang.
	 * Wichtig falls die Fläch auf der die TDIs liegen nicht flach ist
	 */
	private float[] restingTiltPos = new float[] { 0, 0, 0 };
	/**
	 * Siehe {@link TiltType}
	 */
	TiltType tiltType = null;
	/**
	 * Die Kompensationswerte fuer das Beigen eines TDIs. Da die Werte vom
	 * Handy sehr ungenau werden koennen.
	 */
	static int compensation = 30;
	/**
	 * Das TDI das gerade geneigt wird
	 */
	TDI currentTDI;
	/**
	 * Die momentane tiltPos des TDIs
	 */
	float tiltPos;

	public Tilt(float[] restingRotation) {
		// this.restingTiltPos = restingRotation;
	}

	/**
	 * Setzt die Rotation der {@link #restingTiltPos}
	 * @param rotation Die übergebene Tilt position
	 */
	public void setRestingRotation(float[] rotation) {
		restingTiltPos = rotation;
	}

	/**
	 * Ruft den Tiltlistener auf
	 */
	private void tilt() {
		tiltListener.tiltedTDI(new TiltEvent(tiltType, currentTDI));
		tiltType = null;
	}

	// public void tilted(float )
	/**
	 * Setzt den Tiltlistener
	 * @param listener Eine Instanze des Tiltlisteners
	 */
	public void setTiltListener(TiltListener listener) {
		tiltListener = listener;
	}

	/**
	 * Die {@link #tilt()} Methode wird von der {@link BigLogic#newCommand(TDI)} Methode aufgerufen.
	 * Diese Methode ist dafuer zustaendig zu kontrollieren ob ein
	 * {@link TDI} geneigt wurde oder nicht.
	 * 
	 * @param command
	 *            Die neuen Werte des {@link TDI}, vom
	 *            {@link BigLogic#newCommand(TDI)} uebergeben
	 */
	public void tilt(TDI command) {
		float[] rot = command.getRotation();
		if (tiltType != null) {
			if (currentTDI.equals(command)) {
				switch (tiltType) {
				case down:
					if (tiltPos < rot[2])
						tilt();
					break;
				case left:
					if (tiltPos > rot[1])
						tilt();
					break;
				case right:
					if (tiltPos < rot[1])
						tilt();
					break;
				case up:
					if (tiltPos > rot[2])
						tilt();
					break;
				}
			}
			return;
		}
		if (rot[2] != restingTiltPos[2]) {
			if (rot[2] > restingTiltPos[2] + compensation) {
				tiltType = TiltType.up;
				tiltPos = rot[2];
				currentTDI = command;
			} else if (rot[2] < restingTiltPos[2] - compensation) {
				tiltType = TiltType.down;
				tiltPos = rot[2];
				currentTDI = command;
			}
		}
		if (rot[1] != restingTiltPos[1]) {
			if (rot[1] > restingTiltPos[1] + compensation) {
				tiltType = TiltType.left;
				tiltPos = rot[1];
				currentTDI = command;
			} else if (rot[1] < restingTiltPos[1] - compensation) {
				tiltType = TiltType.right;
				tiltPos = rot[1];
				currentTDI = command;
			}
		}
	}
}
/**
 * Die Listener Klasse fuer die {@link Tilt} Klasse. Alle Klassen die move
 * handlen wollen muessen {@link TiltListener#tiltedTDI(TiltEvent)} implementieren
 * 
 * @author TDI Team
 * 
 */
interface TiltListener {
	public void tiltedTDI(TiltEvent e);
}

/**
 * Die {@link TiltEvent} Klasse ist wichtig für die Übergabe von einer {@link Tilt} event an den Listener.
 * Sie gibt die Information weiter um welchen Art des {@link Tilt} es sich handelt und welcher TDI betroffen ist 
 * @author TDI Team
 *
 */
class TiltEvent {
	private TiltType tilt;
	private TDI tdi;

	public TiltEvent(TiltType rotation, TDI tdi) {
		this.tilt = rotation;
		this.tdi = tdi;
	}

	public TiltType getRotation() {
		return tilt;
	}

	public TDI getTDI() {
		return tdi;
	}
}

/**
 * Es gibt vier tilt arten: {@link TiltType#right}, {@link TiltType#left}, {@link TiltType#up}, {@link TiltType#down}
 * @author TDI Team
 *
 */
enum TiltType {
	right, left, up, down;
}
