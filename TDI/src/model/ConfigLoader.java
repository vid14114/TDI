package model;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import view.Icon;
import view.Wallpaper;
import controller.Executor;

/**
 * ConfigLoader ist dafuer zustaendig alle benoetigten Konfigs aus saemtlichen Datein zu laden und sie Bereit zu stellen
 * 
 */
public class ConfigLoader {
	private static File iconsRc = lastFileModified();	
	public static String imageType;
	private File oldWallpaper;
	/**
	 * Die Blocksize der Icons
	 */
	public static int blockSize;

	/**
	 * Sucht nach der zuletzt geaenderte xfce4 Datei fuer den Desktop
	 * Der wird dann als Konfig fuer die Icons benuetzt
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

	private static File[] returnDesktopFiles(File f) {
		return f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename.contains(".desktop");
			}
		});
	}
	
	private static File[] returnJarFiles(File f) {
		return f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".jar");
			}
		});
	}

	private static File[] returnDirectoriesAndFiles(File f) {
		return f.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (file.isDirectory() || file.getName().endsWith(".jar") || !file.getName().contains(
						".desktop")
						&& !file.getName().startsWith("."));
			}
		});
	}

	/**
	 * Berechnet die Blockgroeße der Icons auf dem Desktop
	 * 
	 * @return Die berechnete groeße
	 */
	public int getBlockSize() {
		String line;
		int margin = 0;
		int iconSize = 0;
		try {
			// First get the margin
			BufferedReader reader = new BufferedReader(
					new FileReader(
							System.getProperty("user.home")
									+ "/.config/xfce4/xfconf/xfce-perchannel-xml/xfwm4.xml"));
			while ((line = reader.readLine()) != null)
				if (line.contains("placement_ratio"))
					margin = Integer.parseInt(line.split("\"")[5]);
			reader.close();

			// Now get the icon size
			reader = new BufferedReader(
					new FileReader(
							System.getProperty("user.home")
									+ "/.config/xfce4/xfconf/xfce-perchannel-xml/xfce4-desktop.xml"));
			while ((line = reader.readLine()) != null)
				if (line.contains("icon-size"))
					iconSize = Integer.parseInt(line.split("\"")[5]);
			reader.close();
		} catch (IOException e) {

		}
		blockSize = iconSize + (2 * margin);
		return iconSize + (2 * margin);
	}

	/**
	 * Gibt den Panelsize zurueck
	 * @return The panel size
	 */
	public int getPanelSize() {
		return Integer.parseInt(Executor.getPanelSize());
	}

	/**
	 * Gibt den Placementratio der Icons zurueck
	 * @return The placementratio
	 */
	public int getPlacementRatio() {
		return Integer.parseInt(Executor.getPlacementRatio());
	}

	/**
	 * Laedt alle Plugins in der {@link TDIDirectories#TDI_PLUGINS} Verzeichnis und laedt sie ins Programm
	 * Laedt auch die {@link TDIDirectories#TDI_PREFERENCE} Datei und schaut welche Plugins das letzte Mal geladet wurden
	 * @return Alle Plugins als array
	 */
	public Object[][] getPlugins() {
		Object[][] plugins = null;
		try {
			BufferedReader preference = new BufferedReader(new FileReader(
					TDIDirectories.TDI_PREFERENCE));
			String line;
			ArrayList<String> jars = new ArrayList<String>(
					Arrays.asList(new File(TDIDirectories.TDI_PLUGINS)
							.list(new FilenameFilter() {
								@Override
								public boolean accept(File dir, String name) {
									return name.endsWith(".jar");
								}
							})));

			plugins = new Object[jars.size()][2];
			int i = 0;
			for (; (line = preference.readLine()) != null && jars.remove(line); i++) {
				plugins[i][0] = line;
				plugins[i][1] = true;
			}

			for (String jar : jars) {
				plugins[i][0] = jar;
				plugins[i][1] = false;
				i++;
			}
			preference.close();
		} catch (FileNotFoundException e) {
			TDILogger.logError("TDI preference text file not found, creating");
			TDIDirectories.createDirectories();
		} catch (IOException e) {
			TDILogger.logError("Couldn't read the preferences file");
		}
		return plugins;
	}

	/**
	 * Laedt alle Icons, von vielen Pfaden ins Program und gibt sie als {@link ArrayList} zurueck
	 * @return Eine ArrayListe aller Icons, die am Desktop sind
	 */
	public ArrayList<Icon> loadIcons() {
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
			File[] desktopDirectoriesAndFiles = returnDirectoriesAndFiles(new File(
					System.getProperty("user.home") + "/Desktop"));// directories
			
			File[] desktopJarFiles = returnJarFiles(new File(
					System.getProperty("user.home") + "/Desktop"));

			// xdg-open: everything inside ~/Desktop
			if (desktopDirectoriesAndFiles == null)
				desktopDirectoriesAndFiles = returnDirectoriesAndFiles(new File(
						System.getProperty("user.home") + "/Arbeitsflaeche"));// directories
			if (desktopShortcuts == null)
				desktopShortcuts = returnDesktopFiles(new File(
						System.getProperty("user.home") + "/Arbeitsflaeche")); // .desktop
			// The following code is split up into several parts --> .desktop
			// files, directories, default icons, removable,
			{// Parsing of .desktop files
				for (File file : desktopShortcuts) {
					// Parses through file until "Name=" is found
					for (String s : Files.readAllLines(file.toPath(),
							StandardCharsets.UTF_8))
						if ((line = s).contains("Name=")) {
							icons.get(
									icons.indexOf(new Icon(line.split("=")[1],
											null)))
									.setExecPath(
											new String[] { "xdg-open",
													file.getPath() });
							break;
						}
				}
			}

			// directories + NOT .desktop files
			for (File file : desktopDirectoriesAndFiles)				
				icons.get(icons.indexOf(new Icon(file.getName(), null)))
						.setExecPath(
								new String[] { "xdg-open", file.getPath() });
			
			//Jar files
			for (File file : desktopJarFiles)				
				icons.get(icons.indexOf(new Icon(file.getName(), null)))
						.setExecPath(
								new String[] { "java -jar", file.getPath() });
			
			// default icons (home & trash & file system)
			{
				int index;
				if ((index = icons.indexOf(new Icon("Home", null))) != -1)
					icons.get(index).setExecPath(
							new String[] { "xdg-open", "~" });
				if ((index = icons.indexOf(new Icon("Trash", null))) != -1
						|| (index = icons
								.indexOf(new Icon("Rubbish Bin", null))) != 1)
					icons.get(index).setExecPath(
							new String[] { "xgd-open", "trash:///" });
				if ((index = icons.indexOf(new Icon("File System", null))) != -1)
					icons.get(index).setExecPath(
							new String[] { "xdg-open", "/" });
			}

			{ // removable devices
				ArrayList<String> mounts = new ArrayList<String>();
				BufferedReader gvfsMount = Executor.getRemovableDiskList();
				line = null;
				for (int i = 0; (line = gvfsMount.readLine()) != null;) {
					if (line.contains("Volume("))
						mounts.add(line.split(":")[1].substring(1));
					if (line.contains("unix-device")) {
						line = line.split("'")[1];
						for (int j = 0; j < line.length(); j++)
							if (Character.isDigit(line.charAt(j))) {
								mounts.set(i, mounts.get(i) + "#" + line);
								i++;
								break;
							}
					}
				}
				for (String mount : mounts) {
					String[] s = mount.split("#");
					String[] mountCmd = { "udisks", "--mount", s[1] };
					icons.get(icons.indexOf(new Icon(s[0], null))).setMountCmd(
							mountCmd);
				}
			}
			icons.clone();
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

	/**
	 * Rechnet die aktuelle konfigurierte Bildschirmgroeße aus
	 * @return Die Groeße als Point
	 */
	public Point loadScreensize() {
		iconsRc = lastFileModified();
		// Split the name of the file to get only the dimension of the file
		String[] s = iconsRc.getName().split("-")[1].split("x");
		return new Point(Integer.parseInt(s[0]), Integer.parseInt(s[1]
				.split("\\.")[0]));
	}

	/**
	 * Laedt das Hintergrundbild ins Program, diese wird dann vom {@link Wallpaper} ueberschrieben
	 * Unterstuetzte Formate : GIF, JPEG, PNG, BMP
	 *
	 * @return Das Hintergrundbild als {@link BufferedImage}
	 */
	public BufferedImage loadWallpaper() {
		BufferedImage wallpaper = null;
		oldWallpaper = new File(Executor.getBackground());
		try {
			try {
				imageType = oldWallpaper.getName().split("\\.")[1];
				ImageIO.write((wallpaper = ImageIO.read(oldWallpaper)),
						imageType, new File(TDIDirectories.TDI_RESTORE + "/"
								+ oldWallpaper.getName()));
			} catch (Exception e1) {
				TDILogger
						.logError("Image cannot be saved, following formats are supported: GIF, JPEG, PNG, BMP");
				wallpaper = ImageIO.read(new File("images/image-blank.jpg"));
				imageType = "jpg";
			}
		} catch (IOException e) {
			TDILogger
					.logError("An error occured while trying to load the wallpaper");
		} finally {
			oldWallpaper.deleteOnExit();
		}
		return wallpaper;
	}

	/**
	 * Stellt das alte Hintergrundbild wieder her
	 */
	public void recoverWallpaper() {
		try {
			if (oldWallpaper.exists()) {
				Runtime.getRuntime().exec(
						new String[] { "xfconf-query", "-c", "xfce4-desktop",
								"-p", "/backdrop/screen0/monitor0/image-path",
								"-s", oldWallpaper.getAbsolutePath() });
				Runtime.getRuntime().exec(
						new String[] { "xfdesktop", "--reload" });
			} else {
				BufferedReader br = new BufferedReader(new FileReader(
						TDIDirectories.TDI_PREFERENCE));
				Runtime.getRuntime().exec(
						new String[] { "xfconf-query", "-c", "xfce4-desktop",
								"-p", "/backdrop/screen0/monitor0/image-path",
								"-s", br.readLine() });
				Runtime.getRuntime().exec(
						new String[] { "xfdesktop", "--reload" });
				br.close();
			}
		} catch (IOException e) {
			TDILogger.logError(e.getMessage());
		}
	}

	/**
	 * Speichert die ausgewaehlten Plugins in die {@link TDIDirectories#TDI_PREFERENCE} Datei
	 * @param plugins
	 */
	public void savePlugins(String[] plugins) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					TDIDirectories.TDI_PREFERENCE, false));
			if (oldWallpaper.exists())
				bw.write(oldWallpaper.getAbsolutePath() + "\n");
			for (String line : plugins)
				bw.write(line + "\n");
			bw.close();
		} catch (IOException | NullPointerException e) {
			TDILogger.logError("Error writing to "
					+ TDIDirectories.TDI_PREFERENCE);
		}
	}

	/**
	 * Greift auf den File System zu und veraendert die Positionen der Icons
	 * @param icons
	 */
	public void updateConfig(ArrayList<Icon> icons) {
		try {
			// Read all lines
			BufferedWriter bw = new BufferedWriter(new FileWriter(iconsRc));
			for (Icon icon : icons) {
				bw.write("[" + icon.getName() + "]");
				bw.newLine();
				bw.write("row=" + icon.getPosition().x);
				bw.newLine();
				bw.write("col=" + icon.getPosition().y);
				bw.newLine();
				bw.newLine();
			}
			bw.close();
			// refresh the desktop manager
			Runtime.getRuntime().exec("xfdesktop --reload").waitFor();
		} catch (IOException e) {
			TDILogger.logError("Couldn't save the configuration file");
		} catch (InterruptedException e) {
			TDILogger.logError(e.getMessage());
		}
	}

}
