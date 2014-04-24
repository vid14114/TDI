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
		switch (tdi.getState()) {
		case desktop:
			tdi.rotateIconsClockwise();
			Executor.saveBackground(bigLogic.getWallpaper().markArea(
					bigLogic.getTdis()));
			break;
		case taskbar:
			ProgramHandler.restoreRight();
			break;
		case inapp:
			bigLogic.getPlugserv().sendMessage(tdi.getId(), tdi.getPosition(),
					tdi.getRotation());
			break;
		default:
			break;
		}
	}

	private void rotateLeft(TDI tdi) {
		switch (tdi.getState()) {
		case desktop:
			tdi.rotateIconsCounterClockwise();
			Executor.saveBackground(bigLogic.getWallpaper().markArea(
					bigLogic.getTdis()));
			break;
		case taskbar:
			ProgramHandler.restoreLeft();
			break;
		case inapp:
			bigLogic.getPlugserv().sendMessage(tdi.getId(), tdi.getPosition(),
					tdi.getRotation());
			break;
		default:
			break;
		}
	}
}
