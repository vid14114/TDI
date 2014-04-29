package view;

import java.util.ArrayList;

import org.junit.Test;

import controller.BigLogic;

public class WallpaperTest {

	@Test
	public void test() throws InterruptedException {
		final BigLogic b = new BigLogic();
		final TDI t1 = new TDI((byte) 49, 124, 357, 763, null);
		final TDI t2 = new TDI((byte) 50, 189, 65, 34, null);
		final ArrayList<TDI> tdis = new ArrayList<>();
		tdis.add(t1);
		tdis.add(t2);
		b.setTdis(tdis);
		b.splitIcons();
		b.getTdis().get(0).rotateIconsClockwise();
		b.getTdis().get(0).rotateIconsClockwise();
		b.refreshBackground();
		Thread.sleep(3000);
		b.getTdis().get(0).rotateIconsCounterClockwise();
		b.getTdis().get(1).rotateIconsClockwise();
		b.refreshBackground();
		while (true) {
			;
		}
	}

}
