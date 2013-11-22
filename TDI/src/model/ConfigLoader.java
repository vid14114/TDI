package model;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import view.Icon;

/**
 * Make a backup of wallpaper. Load the configs.
 */
public class ConfigLoader {

	private static final File iconsRc = lastFileModified();
	public static final String PLUGINS_LOC = System.getProperty("user.home")
			.concat("/.tdi/plugins");

	public static ArrayList<Icon> loadIcons() {
		ArrayList<Icon> icons = new ArrayList<Icon>();	
		BufferedReader br;		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					iconsRc)));
			String line;
			while (br.ready()) 
				if ((line = br.readLine()).contains("[")) 
					//Construct a new Icon which needs a name and a position(point)	and adds to arraylist									 
					icons.add(
							new Icon(
							line.substring(1, line.length() - 1),
							new Point(
									Integer.parseInt(br.readLine().split("=")[1]),
									Integer.parseInt(br.readLine().split("=")[1]))));
			br.close();
			File[] files = new File(System.getProperty("user.home")
					+ "/Desktop").listFiles();
			for (File file : files) {
				String name = "";
				String execPath = "";
				if (file.getName().contains(".desktop")) {
					BufferedReader brf = new BufferedReader(
							new InputStreamReader(new FileInputStream(file)));
					while (brf.ready()) {
						String s = brf.readLine();
						if (s.contains("Name="))
							name = s.split("=")[1];
						if (s.contains("Exec="))
							execPath = s.split("=")[1];
					}
					brf.close();
					for (Icon i : icons) {
						if (i.getName().equals(name))
						{
							i.setExecPath(execPath);
							break;
						}
					}
				}
				else if(file.isDirectory())
				{
					String s=file.getName();
					for(Icon i : icons)
						if(i.getName().equals(s))
						{
							i.setExecPath("thunar "+file.getPath());
							break;
						}
				}
			}
			for(Icon i : icons)
			{
				if(i.getExecPath()==null)
				{
					if(i.getName().equals("Home"))
						i.setExecPath("thunar ~");
					if(i.getName().equals("Trash"))
						i.setExecPath("thunar trash:///");
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
	 * @return The currently configured screen size
	 */
	public Point loadScreensize() {
		// Split the name of the file to get only the dimension of the file
		String[] s = iconsRc.getName().split("-")[1].split("x");
		return new Point(Integer.parseInt(s[0]), Integer.parseInt(s[1]
				.split(".")[0]));
	}

	/**
	 * Accesses the ~/.tdi/plugins/ folder to return all available plugins
	 * 
	 * @return An array of plugins
	 */
	public String[] getPlugins() {
		return new File(PLUGINS_LOC).list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".jar"))
					return true;
				return false;
			}
		});
	}

	/**
	 * Searches for the last modified file in the xfce4 desktop folder
	 * 
	 * @return The last modified file
	 */
	private static File lastFileModified() {
		File[] files = new File(System.getProperty("user.home")
				+ "/.config/xfce4/desktop").listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		long lastModified = Long.MIN_VALUE;
		File choice = null;
		for (File file : files) {
			if (file.lastModified() > lastModified) {
				choice = file;
				lastModified = file.lastModified();
			}
		}
		return choice;
	}

}