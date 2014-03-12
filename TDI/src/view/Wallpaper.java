package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Funktioniert nur, wenn das Hintergrundbild gleich der Monitorauflösung ist
public class Wallpaper {

	/**
	 * The image of the wallpaper
	 */
	private BufferedImage background;
	/**
	 * The block size of each icon
	 */	
	private int blockSize;
	private int panelSize;
	private int ratio;
	
	private int realWidth;
	private int realHeight;
	
	public Wallpaper(BufferedImage background, int blockSize, int panelSize, int ratio){
		this.background = background;
		this.blockSize = blockSize;
		this.panelSize = panelSize;
		this.ratio = ratio;
		realWidth=blockSize+(4*ratio);
		realHeight=blockSize+(3*ratio);
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
		Graphics2D g2 = b.createGraphics();
		for(int i=0; i<colorAreas.size(); i++) {
			Icon first=colorAreas.get(i).getIcons().get(0);
			Icon last=colorAreas.get(i).getIcons().get(colorAreas.get(i).getIcons().size()-1);
			if(i==0)
				g2.setColor(Color.blue);
			if(i==1)
				g2.setColor(Color.red);
			int firstX=first.getPosition().y*realWidth;
			int firstY=first.getPosition().x*realHeight+ratio;
			if(first.getPosition().x==0)
				firstY+=panelSize;
			int lastX=last.getPosition().y*realWidth-firstX;
			int lastY=(last.getPosition().x+1)*realHeight-firstY;
			if(first.getPosition().x!=0)
				lastY-=panelSize;
			g2.fill(new Rectangle2D.Float(firstX, firstY, lastX, lastY));
			g2.drawImage(b, 0, 0, null);
		}
		return b;
	}
}