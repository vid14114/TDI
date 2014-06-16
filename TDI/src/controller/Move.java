/**
 * 
 */
package controller;

import java.util.ArrayList;
import view.TDI;
import view.TDI.TDIState;

/**
 * Die Move Klasse kontrolliert ob ein TDI sich bewegt hat oder nicht. Falls
 * sich ein TDI bewegt hat, ruft er die Listeners auf, die anschließend von
 * Handlers gehandelt.
 * 
 * @author TDI Team
 */
public class Move {
	/**
	 * Siehe {@link MoveListener}
	 */
	private MoveListener moveListener;
	/**
	 * Eine {@link ArrayList} aller TDIs
	 */
	private ArrayList<TDI> tdis;
	/**
	 * Ein Array der dokumetiert ob ein TDI gerade bewegt wird oder nicht
	 */
	private boolean[] moveFlags;
	/**
	 * Die alten Positionen der TDIs werden kurz in dieser ArrayListe
	 * gespeichert
	 */
	private float[][] oldPos = new float[2][3];
	/**
	 * Die Kompensationswerte fuer die Bewegung eines TDIs. Da die Werte vom
	 * Handy sehr ungenau werden koennen.
	 */
	static final int compensation = 50;

	/**
	 * Der Konstuktor nimmt die TDIs an, und initialisert die moveflags
	 * 
	 * @param tdis
	 */
	public Move(ArrayList<TDI> tdis) {
		this.tdis = tdis;
		moveFlags = new boolean[2];
	}

	/**
	 * Die move Methode wird von der {@link BigLogic#newCommand(TDI)} Methode
	 * aufgerufen. Diese Methode ist dafuer zustaendig zu kontrollieren ob sich
	 * ein {@link TDI} bewegt hat oder nicht.
	 * 
	 * @param command
	 *            Die neuen Werte des {@link TDI}, vom
	 *            {@link BigLogic#newCommand(TDI)} uebergeben
	 */
	public void move(TDI command) {
		TDI currentTDI = tdis.get(tdis.indexOf(command));
		if (currentTDI.getState().equals(TDIState.window)
				|| currentTDI.isScale()) {
			currentTDI.setPosition(command.getPosition());
			moved(command);
			return;
		}
		if (moveFlags[tdis.indexOf(command)]) { // Currently being moved
			if (moveChanged(oldPos[tdis.indexOf(command)][0],
					command.getPosition()[0])
					|| moveChanged(oldPos[tdis.indexOf(command)][1],
							command.getPosition()[1]))
				oldPos[tdis.indexOf(command)] = command.getPosition();
			else {
				moveFlags[tdis.indexOf(command)] = false;
				moved(command);
			}
		} else {
			if (moveChanged(oldPos[tdis.indexOf(command)][0],
					command.getPosition()[0])
					|| moveChanged(oldPos[tdis.indexOf(command)][1],
							command.getPosition()[1]))
				moveFlags[tdis.indexOf(command)] = true;
		}
	}

	/**
	 * Kontrolliert ob sich die TDIs bewegt haben oder nicht. Indem er die
	 * Kompensationswerte beruecksichtigt
	 * 
	 * @param oldPos
	 *            Die alte TDI Position
	 * @param newPos
	 *            Die neue TDI Position
	 * @return true, falls dich das TDI tatsaechlich bewegt hat, sonst false
	 */
	public boolean moveChanged(float oldPos, float newPos) {
		if (oldPos + compensation < newPos || oldPos - compensation > newPos)
			return true;
		return false;
	}

	/**
	 * Benachrichtigt alle den Listener, das sich eine TDI bewegt hat
	 * 
	 * @param tdi
	 *            Das bewegte TDI
	 */
	private void moved(TDI tdi) {
		moveListener.movedTDI(tdi);
	}

	/**
	 * Setzt den {@link MoveListener}
	 * 
	 * @param moveListener
	 *            {@link MoveListener}
	 */
	public void setMoveListener(MoveListener moveListener) {
		this.moveListener = moveListener;
	}
}

/**
 * Die Listener Klasse fuer die {@link Move} Klasse. Alle Klassen die move
 * handlen wollen muessen {@link MoveListener#movedTDI(TDI)} implementieren
 * 
 * @author TDI Team
 * 
 */
interface MoveListener {
	/**
	 * Wird von {@link Move} aufgerufen sobals sich ein TDI bewegt hat
	 * 
	 * @param tdi
	 *            Das TDI das sich bewegt hat
	 */
	public void movedTDI(TDI tdi);
}