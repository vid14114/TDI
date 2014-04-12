package controller;

import java.util.ArrayList;

import org.junit.Before;
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
	private static Thread t;

	@BeforeClass
	public static void aSetup() throws InterruptedException {
		System.out.println("before");
		ArrayList<TDI> tdis=new ArrayList<>();
		TDI t1=new TDI((byte) 49,100,100,100, new float[]{0,0,0});
		b=new BigLogic();
		tdis.add(t1);
		b.setTdis(tdis);
		b.splitIcons();
		Executor.saveBackground(b.getWallpaper().markArea(b.getTdis()));
		Thread.sleep(1000);
		t=new Thread(b);
		t.start();
	}
	
	@Test
	public void bRotateRightDesktop() {
		System.out.println("right");
		TDI t2=new TDI((byte) 49,100,100,100, new float[]{0,0,100});
		b.getCommands().add(t2);
	}
	
	@Test
	public void cRotateLeftDesktop() {
		System.out.println("left");
		TDI t2=new TDI((byte) 49,100,100,100, new float[]{0,0,-100});
		b.getCommands().add(t2);
	}
	
	@Test
	public void zTutorial1() throws InterruptedException {
		aSetup(); //begin the tutorial from scratch
		System.out.println("doubleRight");
		TDI t1=new TDI((byte) 49, 100, 100, 100, new float[]{0, 0, 50});
		TDI t2=new TDI((byte) 49, 100, 100, 100, new float[]{0, 0, 100});
		b.getCommands().add(t1);
		b.getCommands().add(t2);
	}
	
	@Ignore //Need other tests before this one, pls do :)
	@Test
	public void zTutorial8() {
		System.out.println("minimize");
		b.getTdis().get(0).setState(TDIState.window);
		TDI t1=new TDI((byte) 49, 100, 100, 100, new float[]{-45, 0, 0});
		TDI t2=new TDI((byte) 49, 100, 100, 100, new float[]{0, 0, 0});
		TDI t3=new TDI((byte) 49, 100, 100, 100, new float[]{-45, 0, 0});
		b.getCommands().add(t1);
		b.getCommands().add(t2);
		b.getCommands().add(t3);
	}
}
