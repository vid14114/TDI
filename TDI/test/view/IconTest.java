package view;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

public class IconTest {

	@Test
	public void test() {
		String name="Firefox";
		String execPath="/usr/bin/firefox";
		Point position=new Point(0,0);
		
		Icon i=new Icon(name, execPath, position);
		
		assertEquals(name, i.getName());
		assertEquals(execPath, i.getExecPath());
		assertEquals(position, i.getPosition());
	}

}
