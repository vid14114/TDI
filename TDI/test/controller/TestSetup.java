package controller;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import view.TDI;
import view.TDI.TDIState;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSetup {
	private static BigLogic b;

	@BeforeClass
	public static void aSetup() throws InterruptedException {
		System.out.println("before");
		ArrayList<TDI> tdis = new ArrayList<>();
		TDI t1 = new TDI((byte) 49, 100, 100, 100, new float[] { 0, 0, 0 });
		TDI t2 = new TDI((byte) 50, 150, 150, 100, new float[] { 0, 0, 0 });
		b = new BigLogic();
		tdis.add(t1);
		tdis.add(t2);
		b.setTdis(tdis);
		b.splitIcons();
		Tilt tilt = new Tilt(new float[] { 0, 0, 0 });
		tilt.setTiltListener(new TiltHandler(b));

		Rotation rotation = new Rotation(tdis);
		rotation.setRotationListener(new RotationHandler(b));

		Move move = new Move(tdis);
		move.setMoveListener(new MoveHandler(b));

		b.setTilt(tilt);
		b.setRotation(rotation);
		b.setMove(move);
		// t=new Thread(b);
		// t.start();
	}

	@Test
	public void bRotateRightDesktop() throws InterruptedException {
		Thread.sleep(2000);
		TDI t2 = new TDI((byte) 49, 100, 100, 100, new float[] { 30, 0, 0 });
		b.newCommand(t2);
		Thread.sleep(1000);
	}

	// @Test
	// public void cRotateLeftDesktop() throws InterruptedException {
	// System.out.println("left");
	// TDI t2=new TDI((byte) 49,100,100,100, new float[]{-40,0,0});
	// b.newCommand(t2);
	// Thread.sleep(1000);
	// }
	//
	// @Test
	// public void dLock() throws InterruptedException{
	// System.out.println("lock");
	// b.newCommand(new TDI((byte)49, 100, 100, 100, new float[]{0, 0, -50}));
	// b.newCommand(new TDI((byte)49, 100, 100, 100, new float[]{0, 0, 0}));
	// Assert.assertEquals(true, b.getTdis().get(0).isLocked());
	// Thread.sleep(1000);
	// }
	//
	// @Test
	// public void dUnlock() throws InterruptedException
	// {
	// System.out.println("unlock");
	// b.newCommand(new TDI((byte)49, 100, 100, 100, new float[]{0, 0, -50}));
	// b.newCommand(new TDI((byte)49, 100, 100, 100, new float[]{0, 0, 0}));
	// Assert.assertEquals(false, b.getTdis().get(0).isLocked());
	// Thread.sleep(1000);
	// }
	//
	// @Ignore
	// @Test
	// public void eCloseProgram() throws InterruptedException{
	// System.out.println("Open a program and close it");
	//
	// //Start with test
	// int openedPrograms = ProgramHandler.getRunningPrograms().size();
	// //1. Lock TDI
	// b.newCommand(new TDI((byte)49, 100, 100, 100, new float[]{0, 0,-60}));
	// b.newCommand(new TDI((byte)49, 100, 100, 100, new float[]{0,0,0}));
	//
	// //2. Move up
	// b.newCommand(new TDI((byte)49, 501, 100, 100, new float[]{0,0,0}));
	// b.newCommand(new TDI((byte)49, 501, 100, 100, new float[]{0,0,0}));
	// //3. A program should have opened, TDI should be in inapp mode
	// Assert.assertEquals((openedPrograms+1),
	// ProgramHandler.getRunningPrograms().size());
	// Assert.assertEquals(TDIState.inapp, b.getTdis().get(1).getState());
	// //4. Get the TDI out of in app mode, by tilting right
	// b.newCommand(new TDI((byte)50, 200, 200, 200, new float[]{0,0,-70}));
	// b.newCommand(new TDI((byte)50, 200, 200, 200, new float[]{0,0,0}));
	// Assert.assertEquals(TDIState.window, b.getTdis().get(1).getState());
	// //5. Tilt the TDI to the left, thereby closing it.
	// b.newCommand(new TDI((byte)50, 200, 200, 200, new float[]{0,0,70}));
	// b.newCommand(new TDI((byte)50, 200, 200, 200, new float[]{0,0,0}));
	// Assert.assertEquals(openedPrograms,
	// ProgramHandler.getRunningPrograms().size());
	// Thread.sleep(1000);
	// }
	//
	// @Ignore
	// @Test
	// public void fMaximizeAndRestore() throws InterruptedException{
	// System.out.println("Maximize and restore program and move it");
	// //1. Lock TDI
	// b.newCommand(new TDI((byte)49, 100, 100, 100, new float[]{0, 0,-60}));
	// b.newCommand(new TDI((byte)49, 100, 100, 100, new float[]{0,0,0}));
	//
	// //2. Move up
	// b.newCommand(new TDI((byte)49, 501, 100, 100, new float[]{0,0,0}));
	// b.newCommand(new TDI((byte)49, 501, 100, 100, new float[]{0,0,0}));
	//
	// //3. Get the TDI out of in app mode, by tilting right
	// b.newCommand(new TDI((byte)50, 200, 200, 200, new float[]{0, 0,-60}));
	// b.newCommand(new TDI((byte)50, 200, 200, 200, new float[]{0, 0,0}));
	// Thread.sleep(1000);
	// //4. Tilt the TDI up, thereby toggling maximize and restore
	// b.newCommand(new TDI((byte)50, 200, 200, 200, new float[]{0,60, 0}));
	// b.newCommand(new TDI((byte)50, 200, 200, 200, new float[]{0,0, 0}));
	// Thread.sleep(1000);
	// //5. Moving window around
	// b.newCommand(new TDI((byte)50, 200, 400, 200, new float[]{0,0, 0}));
	// Thread.sleep(200);
	// b.newCommand(new TDI((byte)50, 400, 200, 200, new float[]{0,0, 0}));
	// Thread.sleep(200);
	// b.newCommand(new TDI((byte)50, 240, 200, 200, new float[]{0,0, 0}));
	// Thread.sleep(200);
	// b.newCommand(new TDI((byte)50, 200, 320, 200, new float[]{0,0, 0}));
	// Thread.sleep(200);
	//
	//
	// //6. Scaling the window
	// b.newCommand(new TDI((byte)50, 200, 104, 200, new float[]{0,0, 0}));
	// Thread.sleep(200);
	// b.newCommand(new TDI((byte)50, 200, 320, 200, new float[]{0,0, 0}));
	// Thread.sleep(200);
	// b.newCommand(new TDI((byte)50, 300, 320, 200, new float[]{0,0, 0}));
	// Thread.sleep(200);
	// b.newCommand(new TDI((byte)50, 200, 138, 200, new float[]{0,0, 0}));
	// Thread.sleep(200);
	// //Exiting scale mode
	// b.newCommand(new TDI((byte)50, 200, 200, 200, new float[]{0, 0,-60}));
	// b.newCommand(new TDI((byte)50, 200, 200, 200, new float[]{0, 0,0}));
	// Thread.sleep(1000);
	//
	//
	// //5. Moving window around again
	// b.newCommand(new TDI((byte)50, 200, 400, 200, new float[]{0,0, 0}));
	// Thread.sleep(200);
	// b.newCommand(new TDI((byte)50, 400, 200, 200, new float[]{0,0, 0}));
	// Thread.sleep(200);
	// b.newCommand(new TDI((byte)50, 240, 200, 200, new float[]{0,0, 0}));
	// Thread.sleep(200);
	// b.newCommand(new TDI((byte)50, 200, 320, 200, new float[]{0,0, 0}));
	// Thread.sleep(200);
	//
	// //7. Tilt the TDI to the left, thereby closing it.
	// b.newCommand(new TDI((byte)50, 200, 200, 200, new float[]{0,0,70}));
	// b.newCommand(new TDI((byte)50, 200, 200, 200, new float[]{0,0,0}));
	// }
	//
	// //@Test
	// public void zTutorial1() throws InterruptedException {
	// aSetup(); //begin the tutorial from scratch, nix gut two threads running
	// System.out.println("doubleRight");
	// TDI t1=new TDI((byte) 49, 100, 100, 100, new float[]{0, 0, 50});
	// TDI t2=new TDI((byte) 49, 100, 100, 100, new float[]{0, 0, 100});
	// b.newCommand(t1);
	// b.newCommand(t2);
	// }
	//
	// @Ignore //Need other tests before this one, pls do :)
	// @Test
	// public void zTutorial8() {
	// System.out.println("minimize");
	// b.getTdis().get(0).setState(TDIState.window);
	// TDI t1=new TDI((byte) 49, 100, 100, 100, new float[]{-45, 0, 0});
	// TDI t2=new TDI((byte) 49, 100, 100, 100, new float[]{0, 0, 0});
	// TDI t3=new TDI((byte) 49, 100, 100, 100, new float[]{-45, 0, 0});
	// b.newCommand(t1);
	// b.newCommand(t2);
	// b.newCommand(t3);
	// }
}
