package view;

import org.junit.Test;

class TutorialViewTest {

	@Test
	public void testTutorialView() throws InterruptedException {
		final TutorialView th = new TutorialView();
		Thread.sleep(2000);
		th.setText("Helloe");
		Thread.sleep(2000);
		th.setText("Helloe2");
	}

}
