package view;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Wallpaper {

	/**
	 * The image of the wallpaper
	 */
	private BufferedImage background;
	/**
	 * The block size of each icon
	 */	
	private int blockSize;
	
	public Wallpaper(BufferedImage background, int blocksize){
		this.background = background;
		this.setBlockSize(blocksize);
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
	/**
	 * @param the size of icon block
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

}