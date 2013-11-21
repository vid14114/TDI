package model;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import view.Icon;

/**
 * Make a backup of wallpaper.
 * Load the configs.
 */
public class ConfigLoader {
	
	private static final File iconsRc=lastFileModified(System.getProperty("user.home")+"/.config/xfce4/desktop");
	
	public static ArrayList<Icon> loadIcons() {
		ArrayList<Icon> icons=new ArrayList<Icon>();
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(iconsRc)));
				for(Icon i; br.ready(); )
				{
					String line = br.readLine();
	                if(line.contains("[")){
	                	i=new Icon(line.substring(1, line.length()-1), new Point(Integer.parseInt(br.readLine().split("=")[1]), Integer.parseInt(br.readLine().split("=")[1])));
	                	icons.add(i);
	                }
				}
				br.close();
				File[] files=new File(System.getProperty("user.home")+"/Desktop").listFiles();
				for(File file : files)
				{
					if(file.getName().contains(".desktop"))
					{
						BufferedReader brf=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
						String name="";
						String execPath="";
						while(brf.ready())
						{
							String s=brf.readLine();
							if(s.contains("Name="))
								name=s.split("=")[1];
							if(s.contains("Exec="))
								execPath=s.split("=")[1];
						}
						brf.close();
						for(Icon i : icons)
						{
							if(i.getName().equals(name))
								i.setExecPath(execPath);
						}
					}
				}
			} catch (FileNotFoundException e) {
				// dialog -> config file not found
			} catch (NumberFormatException e) {
				// dialog -> config file destroyed
			} catch (IOException e) {
				// ka...
			}
			return icons;
		}
	

	/**
	 * Returns the wallpaper in form of a buffered image
	 */
	public static BufferedImage loadWallpaper() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the screensize
	 */
	public Point loadScreensize() {
		throw new UnsupportedOperationException();
	}
	
	public static File lastFileModified(String dir) {           
        File[] files = new File(dir).listFiles(new FileFilter() {
                @Override
                                public boolean accept(File file) {
                        return file.isFile();
                }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
                if (file.lastModified() > lastMod) {
                        choice = file;
                        lastMod = file.lastModified();
                }
        }
        return choice;
        }

}