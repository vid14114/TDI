package controller;

import view.TDI;

public class Tilt {
	static int compensation = 5;
	TDI currentTDI;
	private float[] restingTiltPos = new float[3];
	private TiltListener tiltListener;
	TiltType tiltType = null;

	public Tilt(float[] restingRotation) {
		this.restingTiltPos = restingRotation;
	}

	public void setRestingRotation(float[] rotation) {
		restingTiltPos = rotation;
	}

	// public void tilted(float )
	public void setTiltListener(TiltListener listener) {
		tiltListener = listener;
	}

	private void tilt() {
		tiltListener.tiltedTDI(new TiltEvent(tiltType, currentTDI));
	}

	public void tilt(TDI command) {
		final float[] rot = command.getRotation();
		if (tiltType != null) {
			if (currentTDI.equals(command))
				if (rot[2] == restingTiltPos[2] || rot[1] == restingTiltPos[1]) {
					new Runnable() {
						@Override
						public void run() {
							tilt();
							tiltType = null;
						}
					}.run();
				}
			return;
		}
		currentTDI = command;
		if (rot[1] != restingTiltPos[1]) {
			if (rot[1] > restingTiltPos[1] + compensation) {
				tiltType = TiltType.up;
			} else if (rot[1] < restingTiltPos[1] - compensation) {
				tiltType = TiltType.down;
			}
		} else if (rot[2] != restingTiltPos[2]) {
			if (rot[2] > restingTiltPos[2] + compensation) {
				tiltType = TiltType.left;
			} else if (rot[2] < restingTiltPos[2] - compensation) {
				tiltType = TiltType.right;
			}
		}
	}
}

class TiltEvent {
	private final TDI tdi;
	private final TiltType tilt;

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

interface TiltListener {
	public void tiltedTDI(TiltEvent e);
}

enum TiltType {
	down, left, right, up;
}
