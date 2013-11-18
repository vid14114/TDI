package model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.image.BufferedImage;

import org.junit.Test;

public class ConfigLoaderTest {
	ConfigLoader c = new ConfigLoader();
	public void test() {
		loadIconsTest();
		loadWallpaperTest();
		loadScreensizeTest();
	}
	
	//Should check the structure of the String
	@Test
	public void loadIconsTest(){
		assertTrue(ConfigLoader.loadIcons() instanceof String);
	}
	
	@Test
	public void loadWallpaperTest(){
		assertTrue(ConfigLoader.loadWallpaper() instanceof BufferedImage);
	}
	
	@Test
	public void loadScreensizeTest(){
		assertTrue(c.loadScreensize() instanceof Point);
	}
	
	

}
