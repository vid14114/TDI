package view;

import org.junit.Test;

public class TutorialViewTest {

	@Test
	public void testTutorialView() throws InterruptedException {
		TutorialView th = new TutorialView();
		Thread.sleep(2000);
		th.setText("Helloe");
		Thread.sleep(2000);
		th.setText("Helloe2");
	}

}
