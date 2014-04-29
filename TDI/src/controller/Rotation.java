package controller;

import java.util.ArrayList;

import view.TDI;

class RotateEvent {
	private final RotationType rotation;
	private final TDI tdi;

	public RotateEvent(RotationType rotation, TDI tdi) {
		this.rotation = rotation;
		this.tdi = tdi;
	}

	public RotationType getRotation() {
		return rotation;
	}

	public TDI getTDI() {
		return tdi;
	}
}

public class Rotation extends Thread {
	private RotationListener rotationListener;
	ArrayList<TDI> tdis;

	public Rotation(ArrayList<TDI> tdis) {
		this.tdis = tdis;
	}

	public void rotate(TDI command) {
		final TDI tdi = command;
		new Runnable() {

			@Override
			public void run() {
				final TDI currentTDI = tdis.get(tdis.indexOf(tdi));
				final float upperBorder = currentTDI.getRotation()[0]
						+ (currentTDI.getRotationLimit() / 2);
				final float lowerBorder = currentTDI.getRotation()[0]
						- (currentTDI.getRotationLimit() / 2);
				if (tdi.getRotation()[0] < lowerBorder) {
					int rot = Math
							.abs((int) ((lowerBorder - tdi.getRotation()[0]) / currentTDI
									.getRotationLimit()));
					currentTDI.getRotation()[0] = tdi.getRotation()[0];
					for (; rot >= 0; rot--) {
						rotation(RotationType.left, currentTDI);
					}
				}

				if (tdi.getRotation()[0] > upperBorder) {
					int rot = Math
							.abs((int) ((upperBorder - tdi.getRotation()[0]) / currentTDI
									.getRotationLimit()));
					currentTDI.getRotation()[0] = tdi.getRotation()[0];
					for (; rot >= 0; rot--) {
						rotation(RotationType.right, currentTDI);
					}
				}
			}
		}.run();
	}

	private void rotation(RotationType rotationType, TDI currentTDI) {
		rotationListener.rotatedTDI(new RotateEvent(rotationType, currentTDI));
	}

	public void setRotationListener(RotationListener rotationListener) {
		this.rotationListener = rotationListener;
	}
}

interface RotationListener {
	public void rotatedTDI(RotateEvent e);
}

enum RotationType {
	left, right;
}
