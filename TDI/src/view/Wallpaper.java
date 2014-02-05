package view;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Wallpaper {

	/**
	 * The image of the wallpaper
	 */
	private static BufferedImage background;
	/**
	 * The resolution as a point yo.
	 */
	private static Point resolution;

	public static Point getResolution() {
		return resolution;
	}

	public void setResolution(Point resolution) {
		Wallpaper.resolution = resolution;
	}

	public void setBackground(BufferedImage background) {
		this.background = background;
	}

	/**
	 * Marks the area of the tdi with the specified color.
	 * 
	 * As parameters we give the array of ids (for the tdis) and the hashtable
	 * of selected icons.
	 * 
	 * @param colorAreas
	 *            The TDIs with their icons, and we will generate the markedArea
	 */
	public static void markArea(TDI[] colorAreas) {
		throw new UnsupportedOperationException();
	}

}