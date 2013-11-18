package view;

import static org.junit.Assert.*;

import org.junit.Test;

public class TDITest {

	@Test
	public void test() {
		TDI t=new TDI();
		
		float[] pos={22,20};
		
		int rot=327;
		int rot1=500;
		
		t.setPosition(pos);
		assertEquals(pos, t.getPosition());
		
		t.setRotation(rot);
		assertEquals(rot, t.getRotation());
		t.setRotation(rot1);
		assertNotEquals(rot1, t.getRotation()); //rotation can't be bigger than 359
	}

}
