package model;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import controller.Executor;
import view.Icon;

public class ConfigLoaderTest {	
	@Before
	public void createDirectories(){
		TDIDirectories.createDirectories();
	}
	
//	@Test
//	public void testLoadWallpaper() throws IOException {
//		BufferedImage shouldWallpaper = ImageIO.read(new File(Executor.getBackground())); //Insert your wallpaper path here --> xfce
//		ConfigLoader cf = new ConfigLoader();
//		//Testing whether the wallpaper is correct and if it is, whether it is backed up		
//		Assert.assertEquals(shouldWallpaper, cf.loadWallpaper());
//		Assert.assertEquals(shouldWallpaper, ImageIO.read(new File(TDIDirectories.TDI_RESTORE).listFiles()[0]));
//		//Testing whether the plugins are found
//		Assert.assertArrayEquals(new String[0], cf.getPlugins());
//	}	
	
	@Test
	public void testLoadIcons(){
		ConfigLoader cf = new ConfigLoader();	
		ArrayList <Icon> icons = null;
		//Test for icons1
		ConfigLoader.setIconsRc(new File("resources/icons1"));
		icons = cf.loadIcons();
		//Asserting for each icon in the file
		Assert.assertEquals(19, icons.size());		
		Assert.assertSame("thunar ~", icons.get(icons.indexOf(new Icon("Home", null))).getExecPath());
		Assert.assertSame("thunar trash:///", icons.get(icons.indexOf(new Icon("Trash", null))).getExecPath());
		
		//Test for file icons2
		ConfigLoader.setIconsRc(new File("resoucres/icons2"));
		icons = cf.loadIcons();
		//Assertions begin
		Assert.assertEquals(15, icons.size());
		
		//Test for file icons3
		ConfigLoader.setIconsRc(new File("resoucres/icons3"));
		icons = cf.loadIcons();
		//Assertions begin
		Assert.assertEquals(16, icons.size());
	}
	
//	@Test
//	public void testScreenSize(){
//		ConfigLoader cf = new ConfigLoader();
//		try {
//			Runtime.getRuntime().exec(new String[] {"xrandr","-s","1366x768"});
//			Thread.sleep(2000);
//			Runtime.getRuntime().exec(new String[] {"xfdesktop","--reload"});
//			Thread.sleep(2000);
//			Assert.assertEquals(new Point(1366, 768), cf.loadScreensize());
//			//Change to 1360x768
//			Runtime.getRuntime().exec(new String[] {"xrandr","-s","1360x768"});
//			Thread.sleep(2000);
//			Runtime.getRuntime().exec(new String[] {"xfdesktop","--reload"});
//			Thread.sleep(2000);
//			Assert.assertEquals(new Point(1360, 768), cf.loadScreensize());
//			//Change to 1024x768
//			Runtime.getRuntime().exec(new String[] {"xrandr","-s","1028x768"});
//			Thread.sleep(2000);
//			Runtime.getRuntime().exec(new String[] {"xfdesktop","--reload"});
//			Thread.sleep(2000);
//			Assert.assertEquals(new Point(1024, 768), cf.loadScreensize());
//			//
//			Runtime.getRuntime().exec(new String[] {"xrandr","-s","800x600"});
//			Thread.sleep(2000);
//			Runtime.getRuntime().exec(new String[] {"xfdesktop","--reload"});
//			Thread.sleep(2000);
//			Assert.assertEquals(new Point(800, 600), cf.loadScreensize());
//			//Change 
//			Runtime.getRuntime().exec(new String[] {"xrandr","-s","1366x768"});
//			Thread.sleep(2000);
//			Runtime.getRuntime().exec(new String[] {"xfdesktop","--reload"});
//			Thread.sleep(2000);
//			Assert.assertEquals(new Point(1366, 768), cf.loadScreensize());
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}

}
