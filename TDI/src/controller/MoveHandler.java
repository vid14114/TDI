package controller;

import view.TDI;
import view.TDI.TDIState;

public class MoveHandler implements MoveListener {
	BigLogic bigLogic;
	TDI scaleTDI;

	public MoveHandler(BigLogic bigLogic) {
		this.bigLogic = bigLogic;
	}

	private void moveDesktopMode(TDI tdi) {
		System.out.println("Desktop");
		if (!tdi.isLocked() && ProgramHandler.getRunningPrograms().size() == 0) {
			System.out.println("IN");
			bigLogic.getServer().setPose(tdi.getId(), tdi.getPosition(),
					tdi.getRotation());
			System.out.println("OUT");
		}
		if (tdi.isLocked()) {
			if (bigLogic.checkMovedToTaskbar(tdi)) {
				ProgramHandler.openProgram(tdi.getIcons().get(0));
				tdi.setRotationLimit((360 / ProgramHandler.getRunningPrograms()
						.size()) / 2);
				tdi.toggleLock();
				if (bigLogic.emptyTaskbar()) {
					tdi.setState(TDIState.taskbar);
					tdi = bigLogic.getTdis().get(
							(bigLogic.getTdis().indexOf(tdi) + 1) % 2);
				}
				tdi.setState(TDIState.inapp);
				tdi.setPosition(new float[] { 0, 0, 0 });// TODO Set the
															// position to
															// window position
			} else {
				;// TODO move icon to certain position
			}
		}
	}

	@Override
	public void movedTDI(TDI tdi) {
		System.out.println("Move");
		if (tdi.isScale()) {
			moveScaleMode(tdi);
			System.out.println("Scale");
			return;
		}
		if (startScaleMode(tdi))
			return;
		switch (tdi.getState()) {
		case desktop:
			moveDesktopMode(tdi);
			break;
		case window:
			moveWindowMode(tdi);
			break;
		case taskbar:
			// TODO move to taskbar location
			break;
		case sleep:
			moveSleepMode(tdi);
			break;
		default:
			break;
		}
	}

	private void moveScaleMode(TDI tdi) {// Exit through right tilting
		if (tdi.equals(scaleTDI))
			return;
		ProgramHandler
				.resizeProgram(
						(int) Math.abs(scaleTDI.getPosition()[0]
								- tdi.getPosition()[0]),
						(int) Math.abs(scaleTDI.getPosition()[1]
								- tdi.getPosition()[1]));
	}

	private void moveSleepMode(TDI tdi) {
		if (ProgramHandler.getNonMinimized() == 0) {
			tdi.setState(TDIState.desktop);
			bigLogic.splitIcons();
		} else {
			tdi.setState(TDIState.desktop);
		}
	}

	private void moveWindowMode(TDI tdi) {
		// TODO Map the position of table to screen;
		ProgramHandler.moveProgram((int) tdi.getPosition()[0],
				(int) tdi.getPosition()[1]);
	}

	/**
	 * Checks if two TDIs are close enough to each other to start Scale Mode
	 * 
	 * @return whether scale mode starts or not
	 */
	private boolean startScaleMode(TDI tdi) {
		// TODO vibrate? LED? some notification?
		final int range = 5; // TODO Range for closeness of TDI can and should
								// be changed
		for (final TDI tdi2 : bigLogic.getTdis()) {
			if (tdi.getId() == tdi2.getId()) {
				break;
			}
			if (Math.abs(tdi.getPosition()[0] - tdi2.getPosition()[0]) < range
					|| Math.abs(tdi.getPosition()[1] - tdi2.getPosition()[1]) < range) {
				tdi2.setIsScale(true);
				tdi.setIsScale(true);
				scaleTDI = tdi2;
				return true;
			}
		}
		return false;
	}

}
