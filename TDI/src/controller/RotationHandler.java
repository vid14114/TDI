package controller;

import view.TDI;

/**
 * {@link RotationHandler} implemetier den {@link RotationListener} und deren Methode
 * {@link RotationListener#rotatedTDI(RotateEvent)}. Sie führt alle notwendigen Befehle durch, die durch die Rotation in Frage gerufen wurden
 * @author TDI Team
 */
public class RotationHandler implements RotationListener {
	/**
	 * Siehe {@link BigLogic}
	 */
	BigLogic bigLogic;

	/**
	 * Der Konstruktor nimmt eine Instanze der {@link BigLogic} an
	 * @param bigLogic
	 */
	public RotationHandler(BigLogic bigLogic) {
		this.bigLogic = bigLogic;
	}

	/**
	 * Die implementierte Methode von {@link RotationListener#rotatedTDI(RotateEvent)}
	 * kriegt einen RotateEvent, und macht mit der Information die sie bekommt, die notwendigen Änderungen
	 */
	@Override
	public void rotatedTDI(RotateEvent e) {
		switch (e.getRotation()) {
		case left:
			rotateLeft(e.getTDI());
			break;
		case right:
			rotateRight(e.getTDI());
			break;
		}
	}

	/**
	 * Wird aufgerufen wenn das übergebene TDI nach rechts gedreht wurde
	 * @param tdi Aktiver TDI
	 */
	private void rotateRight(TDI tdi) {
		System.out.println("rotate right");
		switch (tdi.getState()) {
		case desktop:
			tdi.rotateIconsClockwise();
			bigLogic.refreshBackground();
			break;
		case taskbar:
			ProgramHandler.restoreRight();
			break;
		default:
			break;
		}
	}

	/**
	 * Wird aufgerufen wenn das übergebene TDI nach links gedreht wurde
	 * @param tdi Aktives TDI
	 */
	private void rotateLeft(TDI tdi) {
		System.out.println("rotate left");
		switch (tdi.getState()) {
		case desktop:
			tdi.rotateIconsCounterClockwise();
			bigLogic.refreshBackground();
			break;
		case taskbar:
			ProgramHandler.restoreLeft();
			break;
		default:
			break;
		}
	}
}
