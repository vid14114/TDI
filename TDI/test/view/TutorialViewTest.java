package view;

import java.io.IOException;
import java.util.ArrayList;

import model.ConfigLoader;

import org.junit.Test;

import controller.TutorialLogic;

;

public class TutorialViewTest {

	@Test
	public void testTutorialView() throws InterruptedException, IOException {
		ArrayList<TDI> tdis = new ArrayList<>();
		tdis.add(new TDI((byte) 50, 200, 300, 300, new float[] { 3, 2, 2 }));
		tdis.get(0).setIcons(new ConfigLoader().loadIcons());
		TutorialLogic g = new TutorialLogic(tdis);
		g.run();
		System.in.read();
	}

}
