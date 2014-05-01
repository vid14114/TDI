package controller;

import java.awt.Point;

import model.ConfigLoader;
import model.Server;
import view.TDI;
import view.TDI.TDIState;

/**
 * {@link MoveHandler} implemetier den {@link MoveListener} und deren Methode
 * {@link MoveListener#movedTDI(TDI)}. Sie ist zusaendig dafuer herauszufinden,
 * ob ein TDI sich bewegen darf und fuehrt die erforderlichen Befehle durch
 * 
 * @author TDI Team
 */
public class MoveHandler implements MoveListener {
	/**
	 * Siehe {@link BigLogic}
	 */
	BigLogic bigLogic;
	/**
	 * Falls sich die TDIs in ScaleModus befinden wird ein zweiter TDI benoetigt
	 * Siehe {@link TDI}
	 */
	TDI scaleTDI;
	/**
	 * Die Kompensationswerte fuer die Bewegung eines TDIs. Da die Werte vom
	 * Handy sehr ungenau werden koennen.
	 */
	float compensation = 20;

	/**
	 * Der Konstruktor nimmt eine Instanze der {@link BigLogic} an
	 * 
	 * @param bigLogic
	 */
	public MoveHandler(BigLogic bigLogic) {
		this.bigLogic = bigLogic;
	}

	/**
	 * Prueft, ob zwei TDI sind nah genug beieinander sind, um Scale-Modus
	 * starten
	 * 
	 * @return true, Scalemodus aktiviert, false falls nicht
	 */
	private boolean startScaleMode(TDI tdi) {
		int range = 10; // TODO Range for closeness of TDI can and should be
						// changed
		for (TDI tdi2 : bigLogic.getTdis()) {
			if (tdi.getId() == tdi2.getId())
				break;
			if (Math.abs(tdi.getPosition()[0] - tdi2.getPosition()[0]) < range
					|| Math.abs(tdi.getPosition()[1] - tdi2.getPosition()[1]) < range) {
				tdi2.setIsScale(true);
				tdi.setIsScale(true);
				scaleTDI = tdi2;
				bigLogic.getServer().toggleVibro(tdi.getId(), (byte) 12);
				bigLogic.getServer().toggleVibro(tdi2.getId(), (byte) 12);
				return true;
			}
		}
		return false;
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
	 * Die implementierte Methode von {@link MoveListener#movedTDI(TDI)}
	 * kontrolliert die notwendigen Konditionen fuer eine Bewegung und fuehrt
	 * die naechsten Schritte aus
	 */
	@Override
	public void movedTDI(TDI command) {
		TDI current = bigLogic.getTdis().get(
				bigLogic.getTdis().indexOf(command));

		if (current.getState().equals(TDIState.desktop)
				&& !current.isLocked()
				&& ProgramHandler.getRunningPrograms().size() == 0
				&& (moveChanged(current.getPosition()[0],
						command.getPosition()[0]) || moveChanged(
						current.getPosition()[1], command.getPosition()[1]))) {
			current.setMoving(true);
			bigLogic.getServer().setPose(current.getId(),
					current.getPosition(), current.getRotation());
			return;
		}

		if (current.isScale()) {
			current.setPosition(command.getPosition());
			moveScaleMode(current);
			return;
		}
		if (startScaleMode(current)) {
			current.setPosition(command.getPosition());
			return;
		}
		switch (current.getState()) {
		case desktop:
			if (current.isLocked()) {
				current.setPosition(command.getPosition());
				moveDesktopMode(current);
			}
			break;
		case window:
			current.setPosition(command.getPosition());
			moveWindowMode(current);
			break;
		case taskbar:
			bigLogic.getServer()
					.setPose(
							current.getId(),
							new float[] { bigLogic.getTaskbarLocation(),
									current.getPosition()[1],
									current.getPosition()[2] },
							current.getRotation());
			break;
		case sleep:
			moveSleepMode(current);
			break;
		default:
			break;
		}
	}

	/**
	 * Wird gerufen falls dich der {@link TDI} in {@link TDIState#sleep} Modus
	 * befindet hat und bewegt wurde
	 * 
	 * @param tdi
	 *            TDI, der bewegt wurde
	 */
	private void moveSleepMode(TDI tdi) {
		if (ProgramHandler.getNonMinimized() == 0) {
			tdi.setState(TDIState.desktop);
			bigLogic.splitIcons();
		} else
			tdi.setState(TDIState.desktop);
	}

	/**
	 * Wird gerufen falls dich der {@link TDI} in {@link TDIState#desktop} Modus
	 * befindet hat und bewegt wurde
	 * 
	 * @param tdi
	 *            TDI, der bewegt wurde
	 */
	private void moveDesktopMode(TDI tdi) {
		if (tdi.isLocked()) {
			System.out.println("Desktop MOde enter");
			if (bigLogic.checkMovedToTaskbar(tdi.getPosition()[0])) {
				ProgramHandler.openProgram(tdi.getIcons().get(0));
				tdi.setRotationLimit((360 / ProgramHandler.getRunningPrograms()
						.size()) / 2);
				tdi.toggleLock();
				bigLogic.getServer().toggleGreenLED(tdi.getId(), (byte) 13);
				if (bigLogic.emptyTaskbar()) {
					tdi.setState(TDIState.taskbar);
					tdi = bigLogic.getTdis().get(
							(bigLogic.getTdis().indexOf(tdi) + 1) % 2);
				}
				tdi.setState(TDIState.inapp);
				float[] position = Executor.getWindowPosition();
				tdi.setPosition(new float[] { position[0] / bigLogic.scaleX,
						position[1] / bigLogic.scaleY, 0 });
				tdi.setRotation(new float[] { 0, 0, 0 });
				tdi.setMoving(true);
				bigLogic.getServer().setPose(tdi.getId(), tdi.getPosition(),
						tdi.getRotation());
			} else {
				int row = (int) ((bigLogic.playFieldMaxValues[0] - (int) tdi
						.getPosition()[0] * (int) bigLogic.scaleX) / ConfigLoader.blockSize);
				int col = (int) ((bigLogic.playFieldMaxValues[1] - (int) tdi
						.getPosition()[1] * (int) bigLogic.scaleY) / ConfigLoader.blockSize);
				tdi.getIcons().get(0).setPosition(new Point(row, col));
				bigLogic.refreshIcons();
			}
		}
	}

	/**
	 * Wird gerufen falls dich der {@link TDI} in {@link TDIState#window} Modus
	 * befindet hat und bewegt wurde
	 * 
	 * @param tdi
	 *            TDI, der bewegt wurde
	 */
	private void moveWindowMode(TDI tdi) {
		ProgramHandler.moveProgram((int) tdi.getPosition()[0]
				* (int) bigLogic.scaleX, (int) tdi.getPosition()[1]
				* (int) bigLogic.scaleY);
	}

	/**
	 * Wird gerufen falls dich der {@link TDI} in {@link TDI#isScale()} Modus
	 * befindet hat und bewegt wurde
	 * 
	 * @param tdi
	 *            TDI, der bewegt wurde
	 */
	private void moveScaleMode(TDI tdi) {
		if (tdi.equals(scaleTDI))
			return;
		ProgramHandler
				.resizeProgram(
						(int) Math.abs(scaleTDI.getPosition()[0]
								- tdi.getPosition()[0]),
						(int) Math.abs(scaleTDI.getPosition()[1]
								- tdi.getPosition()[1]));
	}

}
