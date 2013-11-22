package model;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import view.Icon;

public class ConfigLoaderTest {
	ArrayList<Icon> shouldIcons = new ArrayList<Icon>();
	BufferedImage shouldWallpaper;	
	//Point shouldScreensize = new Point(1366, 768); //Change the screen size, if it s different
//	String[] shouldPlugins = {};
	
	@Before
	public void loadEnvironment(){
		//Set the states the variables should be
		try {
			shouldWallpaper = ImageIO.read(new File("/home/abideen/Desktop/McDonalds-Monopoly-Gewinnspiel.png"));			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLoad() throws IOException {
		TDIDirectories.createDirectories(); //Creating the needed directories
		ConfigLoader cf = new ConfigLoader();
		//Testing whether the wallpaper is correct and if it is, whether it is backed up		
		Assert.assertEquals(shouldWallpaper.getGraphics(), cf.loadWallpaper().getGraphics());
		Assert.assertEquals(shouldWallpaper, ImageIO.read(new File(TDIDirectories.TDI_RESTORE).listFiles()[0]));
		//Testing whether the plugins are found
		Assert.assertArrayEquals(new String[0], cf.getPlugins());
	}	
	
	@Test
	public void testScreenSize(){
		ConfigLoader cf = new ConfigLoader();
		try {
			Runtime.getRuntime().exec(new String[] {"xrandr","-s","1366x768"});
			Thread.sleep(2000);
			Runtime.getRuntime().exec(new String[] {"xfdesktop","--reload"});
			Thread.sleep(2000);
			Assert.assertEquals(new Point(1366, 768), cf.loadScreensize());
			//Change to 1360x768
			Runtime.getRuntime().exec(new String[] {"xrandr","-s","1360x768"});
			Thread.sleep(2000);
			Runtime.getRuntime().exec(new String[] {"xfdesktop","--reload"});
			Thread.sleep(2000);
			Assert.assertEquals(new Point(1360, 768), cf.loadScreensize());
			//Change to 1024x768
			Runtime.getRuntime().exec(new String[] {"xrandr","-s","1028x768"});
			Thread.sleep(2000);
			Runtime.getRuntime().exec(new String[] {"xfdesktop","--reload"});
			Thread.sleep(2000);
			Assert.assertEquals(new Point(1024, 768), cf.loadScreensize());
			//
			Runtime.getRuntime().exec(new String[] {"xrandr","-s","800x600"});
			Thread.sleep(2000);
			Runtime.getRuntime().exec(new String[] {"xfdesktop","--reload"});
			Thread.sleep(2000);
			Assert.assertEquals(new Point(800, 600), cf.loadScreensize());
			//Change 
			Runtime.getRuntime().exec(new String[] {"xrandr","-s","1366x768"});
			Thread.sleep(2000);
			Runtime.getRuntime().exec(new String[] {"xfdesktop","--reload"});
			Thread.sleep(2000);
			Assert.assertEquals(new Point(1366, 768), cf.loadScreensize());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
