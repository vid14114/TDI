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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import controller.Executor;
import view.Icon;

/**
 * Make a backup of wallpaper. Load the configs.
 */
public class ConfigLoader {
	private static File iconsRc = lastFileModified();

	public ArrayList<Icon> loadIcons() {
		// What happens to files like blabla.pdf or bla.doc on the desktop
		ArrayList<Icon> icons = new ArrayList<Icon>();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					iconsRc)));
			String line;
			while (br.ready())
				if ((line = br.readLine()).contains("["))
					// Construct a new Icon which needs a name and a
					// position(point) and adds to arraylist
					icons.add(new Icon(
							line.substring(1, line.length() - 1),
							new Point(
									Integer.parseInt(br.readLine().split("=")[1]),
									Integer.parseInt(br.readLine().split("=")[1]))));
			br.close();
			// Above we got all icons --> now we need to split them into
			// directories, shortcuts and default icons
			File[] desktopShortcuts = returnDesktopFiles(new File(
					System.getProperty("user.home") + "/Desktop")); // .desktop
																	// files
			File[] desktopDirectories = returnDirectories(new File(
					System.getProperty("user.home") + "/Desktop"));// directories
			// File[] otherFiles = returnOthers(new
			// File(System.getProperty("user.home")+ "/Desktop"));

			// xdg-open: everything inside ~/Desktop
			
			// The following code is split up into several parts --> .desktop
			// files, directories, default icons, removable,
			{// Parsing of .desktop files
				for (File file : desktopShortcuts) {
					Iterator<String> iterator = Files.readAllLines(
							file.toPath(), StandardCharsets.UTF_8).iterator();
					String name = "";
					String execPath[] = new String[2];
					while (iterator.hasNext()) {
						String s = iterator.next();
						if (s.contains("Name=") && name.equals("")) // we still
																	// need this
																	// for
																	// association
																	// with
																	// iconsRc
						{
							name = s.split("=")[1];
							execPath[0] = "xdg-open";
							execPath[1] = file.getPath();
							break;
						}
					}
					icons.get(icons.indexOf(new Icon(name, null))).setExecPath(
							execPath); // Replaced for loop with this --> :)
				}
			}
			// directories
			for (File file : desktopDirectories) {
				String[] execPath = { "xdg-open", file.getPath() };
				icons.get(icons.indexOf(new Icon(file.getName(), null)))
						.setExecPath(execPath);
			}
			// default icons (home & trash & file system)
			{
				String[] home = { "thunar", "~" };
				String[] trash = { "thunar", "trash:///" };
				String[] fileSystem = { "thunar", "/" };
				icons.get(icons.indexOf(new Icon("Home", null))).setExecPath(
						home);
				icons.get(icons.indexOf(new Icon("Trash", null))).setExecPath(
						trash);
				icons.get(icons.indexOf(new Icon("File System", null)))
						.setExecPath(fileSystem);
			}

			{ // removable devices
				ArrayList<String> mounts = new ArrayList<String>();
				BufferedReader gvfsMount = Executor.getRemovableDiskList();
				for (int i = 0; gvfsMount.ready();) {
					String s = gvfsMount.readLine();
					if (s.contains("Volume("))
						mounts.add(s.split(":")[1].substring(1));
					if (s.contains("unix-device")) {
						s = s.split("'")[1];
						for (int j = 0; j < s.length(); j++)
							if (Character.isDigit(s.charAt(j))) {
								mounts.set(i, mounts.get(i) + "#" + s);
								i++;
								break;
							}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// Happens when name is not found
		} catch (FileNotFoundException e) {
			// dialog -> config file not found
		} catch (NumberFormatException e) {
			// dialog -> config file destroyed
		} catch (IOException e) {
			// ka...
		}
		return icons;
	}

	private static File[] returnOthers(File file) {
		return file.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory() || file.getName().contains(".desktop"))
					return false;
				return true;
			}
		});
	}

	/**
	 * Loads the Wallpaper into the program and saves it into the
	 * {@link TDIDirectories.TDI_RESTORE} directory
	 * 
	 * @return The Wallpaper of the user
	 */
	public BufferedImage loadWallpaper() {
		// xfconf-query -c xfce4-desktop -p
		// /backdrop/screen0/monitor0/image-path -s
		// ~/Desktop/McDonalds-Monopoly-Gewinnspiel.png
		BufferedImage wallpaper = null;
		File wallpaperFile = new File(Executor.getBackground());
		File restore;
		try {
			ImageIO.write((wallpaper = ImageIO.read(wallpaperFile)),
					wallpaperFile.getName().split("\\.")[1],
					restore = new File(TDIDirectories.TDI_RESTORE + "/"
							+ wallpaperFile.getName()));
			restore.deleteOnExit();
		} catch (IOException e) {
			TDILogger
					.logError("An error occured while trying to load the wallpaper");
		}
		return wallpaper;
	}

	/**
	 * @return The currently configured screen size
	 */
	public Point loadScreensize() {
		iconsRc = lastFileModified();
		// Split the name of the file to get only the dimension of the file
		String[] s = iconsRc.getName().split("-")[1].split("x");
		return new Point(Integer.parseInt(s[0]), Integer.parseInt(s[1]
				.split("\\.")[0]));
	}

	/**
	 * Accesses the ~/.tdi/plugins/ folder to return all available plugins
	 * 
	 * @return An array of plugins
	 */
	public String[] getPlugins() {
		return new File(TDIDirectories.TDI_PLUGINS).list(new FilenameFilter() {
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

	private static File[] returnDirectories(File f) {
		return f.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
	}

	private static File[] returnDesktopFiles(File f) {
		return f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.contains(".desktop"))
					return true;
				return false;
			}
		});
	}

	/**
	 * This method is implemented for testing purposes
	 * 
	 * @param iconsRc
	 *            the iconsRc to set
	 */
	public static void setIconsRc(File iconsRc) {
		ConfigLoader.iconsRc = iconsRc;
	}

}
