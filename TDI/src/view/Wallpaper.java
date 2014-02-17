package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
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
	public void markArea(TDI[] colorAreas) {
		int[][] d = new int[10][10];
		int tId = 1;
		
		//Fill array with ids
		for(TDI t : colorAreas){
			for(Icon i : t.getIcons()){
				d[i.getPosition().x][i.getPosition().y]=tId;
			}
			tId++;
		}
		
		int imageWidth = background.getWidth();
		int imageHeight = background.getHeight();
		
		int column=0;
		for(int[] i : d){
			int row=0;
			for(int x : i){
				Graphics2D g2 = background.createGraphics();
				//Only draw if the is is not 0 - 0 = no icon
				if(x==0){
				}
				else{
					g2.setColor(Color.blue);
					//First 2 values define the position of the icon, the other 2 define the size
					g2.fill(new Rectangle2D.Float(blockSize*row,blockSize*column,blockSize,blockSize));
					g2.drawImage(background, 0, 0, null);
				}
				row++;
			}
			column++;
		}
	}
	/**
	 * @param the size of icon block
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

}