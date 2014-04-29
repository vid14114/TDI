package controller;

import view.TDI;

public class RotationHandler implements RotationListener {
	BigLogic bigLogic;

	public RotationHandler(BigLogic bigLogic) {
		this.bigLogic = bigLogic;
	}

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
