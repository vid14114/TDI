package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
	public BufferedImage markArea(ArrayList<TDI> colorAreas) {
		BufferedImage b = background;
		
		for(TDI t : colorAreas){
			Graphics2D g2 = b.createGraphics();
			for(Icon i : t.getIcons()){
				if(t.getId()==49){
					g2.setColor(Color.blue);
					//First 2 values define the position of the icon, the other 2 define the size
					g2.fill(new Rectangle2D.Float(blockSize*i.getPosition().y,blockSize*i.getPosition().x,blockSize,blockSize));
					g2.drawImage(b, 0, 0, null);
				}
				else{
					g2.setColor(Color.red);
					//First 2 values define the position of the icon, the other 2 define the size
					g2.fill(new Rectangle2D.Float(blockSize*i.getPosition().y,blockSize*i.getPosition().x,blockSize,blockSize));
					g2.drawImage(b, 0, 0, null);
				}
			}
		}
		return b;
	}
	/**
	 * @param the size of icon block
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

}