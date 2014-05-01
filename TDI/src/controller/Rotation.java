package controller;

import java.util.ArrayList;

import view.TDI;
/**
 * Die {@link Rotation} Klasse kontrolliert ob ein TDI sich bewegt hat oder nicht. Falls
 * sich ein TDI bewegt hat, ruft er {@link RotationListener} auf, der anschließend von
 * {@link RotationHandler} gehandelt werden.
 * @author TDI Team
 */
public class Rotation extends Thread {
	/**
	 * Siehe {@link RotationListener}
	 */
	private RotationListener rotationListener;
	/**
	 * Eine Liste aller TDIs
	 * Siehe {@link TDI}
	 */
	ArrayList<TDI> tdis;

	/**
	 * Im Konstruktor bekommt Rotation von {@link BigLogic} die Liste aller TDIs
	 * @param tdis The TDIs am Feld
	 */
	public Rotation(ArrayList<TDI> tdis) {
		this.tdis = tdis;
	}

	/**
	 * Setzt den {@link RotationListener}
	 * 
	 * @param rotationListener
	 *            {@link RotationListener}
	 */
	public void setRotationListener(RotationListener rotationListener) {
		this.rotationListener = rotationListener;
	}

	/**
	 * Benachritigt den Listener, dass ein TDI rotiert wurde
	 * @param rotationType Siehe {@link RotationType}
	 * @param currentTDI Der active TDI
	 */
	private void rotation(RotationType rotationType, TDI currentTDI) {
		rotationListener.rotatedTDI(new RotateEvent(rotationType, currentTDI));
	}

	/**
	 * Die rotate Methode wird von der {@link BigLogic#newCommand(TDI)} Methode aufgerufen.
	 * Diese Methode schaut ob eine TDI rotiert wurde und wie oft. Wenn die Rotation erfolgreich war wird {@link #rotation(RotationType, TDI)} aufgerufen
	 * @param command
	 * 				Die neuen Werte des {@link TDI}, vom
	 *            {@link BigLogic#newCommand(TDI)} uebergeben
	 */
	public void rotate(TDI command) {
		final TDI tdi = command;
		new Runnable() {

			@Override
			public void run() {
				TDI currentTDI = tdis.get(tdis.indexOf(tdi));
				float upperBorder = currentTDI.getRotation()[0]
						+ (currentTDI.getRotationLimit() / 2);
				float lowerBorder = currentTDI.getRotation()[0]
						- (currentTDI.getRotationLimit() / 2);
				if (upperBorder > 180)
					upperBorder = -360 + upperBorder;
				if (lowerBorder < -180)
					lowerBorder = 360 - upperBorder;
				if (Math.abs(tdi.getRotation()[0]) < lowerBorder) {
					int rot = (int) (Math.abs(tdi.getRotation()[0]) / currentTDI
							.getRotationLimit());
					currentTDI.getRotation()[0] = tdi.getRotation()[0];
					for (; rot >= 0; rot--)
						rotation(RotationType.left, currentTDI);
				}

				if ((upperBorder < 0 && Math.abs(tdi.getRotation()[0]) < Math
						.abs(upperBorder))
						|| (Math.abs(tdi.getRotation()[0]) > upperBorder && upperBorder > 0)) {
					int rot = (int) (Math.abs(tdi.getRotation()[0]) / currentTDI
							.getRotationLimit());
					currentTDI.getRotation()[0] = tdi.getRotation()[0];
					for (; rot >= 0; rot--)
						rotation(RotationType.right, currentTDI);
				}
			}
		}.run();
	}
}

/**
 * Die Listener Klasse fuer die {@link Rotation} Klasse. Alle Klassen die {@link Rotation}
 * handlen wollen muessen {@link RotationListener#rotatedTDI(RotateEvent)} implementieren
 * 
 * @author TDI Team
 * 
 */
interface RotationListener {
	public void rotatedTDI(RotateEvent e);
}
/**
 * Die RotateEvent Klasse ist wichtig für die Übergabe von einer Rotation event an den Listener.
 * Die gibt die Information weiter um welchen Art der Rotation es sich handelt und welcher TDI betroffen ist 
 * @author TDI Team
 *
 */
class RotateEvent {
	/**
	 * Siehe {@link RotationType}
	 */
	private RotationType rotation;
	/**
	 * Siehe {@link TDI}
	 */
	private TDI tdi;

	/**
	 * Der Konstruktor der von {@link Rotation} aufgerufen wird um die Rotation anzugeben
	 * @param rotation Die Rotationsart
	 * @param tdi Der aktive TDI
	 */
	public RotateEvent(RotationType rotation, TDI tdi) {
		this.rotation = rotation;
		this.tdi = tdi;
	}

	/**
	 * Gibt die Rotationsart zurück
	 * @return Rotationsart
	 */
	public RotationType getRotation() {
		return rotation;
	}

	/**
	 * Gibt den {@link TDI} zurück
	 * @return TDI
	 */
	public TDI getTDI() {
		return tdi;
	}
}
/**
 * Es gibt zwei rotations arten {@link RotationType#right} und {@link RotationType#left}.
 * @author TDI Team
 *
 */
enum RotationType {
	right, left;
}
