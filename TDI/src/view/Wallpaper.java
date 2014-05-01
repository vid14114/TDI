package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Die Wallpaper Klasse repr�sentiert das Wallpaper
 * 
 * @author TDI Team
 */

public class Wallpaper {

    /**
     * Das Bild des Wallpapers
     */
    private final BufferedImage background;
    
    /**
     * Die groeße des Panels
     */
    private float panelSize;
    
    /**
     * Das Verhaeltnis des Bildes zum Wallpaper etc.
     */
    private int ratio;

    /**
     * Die wahre Breite
     */
    private int realWidth;
    
    /**
     * Die wahre Hoehe
     */
    private int realHeight;

    /**
     * Die Skalierung der X-Achse
     */
    private float scalingX;
    
    /**
     * Die Skalierung der Y-Achse
     */
    private float scalingY;
    
    /**
     * Die groeße des Blocks der gezeichnet wird fuer jeden Icon
     */
    private int blockSize;

    /**
	 * Konstruktor von TutorialView
	 * @param background das Hintergrundbild
	 * @param blockSize die Blockgroeße
	 * @param panelSize die Panelgroeße
	 * @param ratio das Verhaeltniss
	 * @param screensize die groeße des Bildschirms
	 */
    public Wallpaper(BufferedImage background, int blockSize, int panelSize, int ratio, Point screensize) {
        this.background = background;
        this.ratio = ratio;
        realWidth = blockSize + (2 * ratio);
        realHeight = blockSize + (3 * ratio);
        scalingX = (float)background.getWidth() / (float)screensize.x;
        scalingY = (float)background.getHeight() / (float)screensize.y;
        this.panelSize = panelSize*scalingY;
        this.blockSize=blockSize;
    }

    /**
     * @param colorAreas Die TDIs die markiert werden
     */
    public BufferedImage markArea(ArrayList<TDI> colorAreas) {
        BufferedImage b = deepCopy(background);
        Graphics2D g2 = b.createGraphics();
        for (int i = 0; i < colorAreas.size(); i++) {
            int firstPosX = 100;
            int firstPosY = 100;
            int lastPosX = 0;
            int lastPosY = 0;
            for (int j = 0; j < colorAreas.get(i).getIcons().size(); j++) {
                if (colorAreas.get(i).getIcons().get(j).getPosition().y < firstPosX)
                    firstPosX = colorAreas.get(i).getIcons().get(j).getPosition().y;
                if (colorAreas.get(i).getIcons().get(j).getPosition().x < firstPosY)
                    firstPosY = colorAreas.get(i).getIcons().get(j).getPosition().x;
                if (colorAreas.get(i).getIcons().get(j).getPosition().y > lastPosX)
                    lastPosX = colorAreas.get(i).getIcons().get(j).getPosition().y;
                if (colorAreas.get(i).getIcons().get(j).getPosition().x > lastPosY)
                    lastPosY = colorAreas.get(i).getIcons().get(j).getPosition().x;
            }
            if (i == 0)
                g2.setColor(Color.blue);
            if (i == 1)
                g2.setColor(Color.red);
            int firstX = firstPosX * realWidth;
            int firstY = firstPosY * realHeight + ratio;
            if (firstPosY == 0)
                firstY += panelSize;
            int lastX = (lastPosX) * realWidth - firstX;
            int lastY = (lastPosY + 1) * realHeight - firstY;
            if (firstPosY != 0)
                lastY -= panelSize;
            g2.fill(new Rectangle2D.Float(firstX * scalingX, firstY * scalingY, (lastX+2*ratio) * scalingX, lastY * scalingY));

            //mark the selected Icon
            int selectedPosX = colorAreas.get(i).getIcons().get(0).getPosition().y;
            int selectedPosY = colorAreas.get(i).getIcons().get(0).getPosition().x;

            int posX = selectedPosX * realWidth;
            int posY = selectedPosY * realHeight + ratio;
            if(selectedPosY==0)
            	posY+=panelSize;
            if (i == 0)
                g2.setColor(Color.cyan);
            if (i == 1)
                g2.setColor(Color.magenta);
            if(posX!=firstX)
            	posX-=ratio;
            g2.fill(new Rectangle2D.Float(posX * scalingX, posY * scalingY, (realWidth-blockSize+2*ratio) * scalingX, (realHeight-blockSize) * scalingY));
            if(colorAreas.get(i).isLocked())
            {
            	g2.setColor(Color.black);
            	g2.fill(new Rectangle2D.Float(colorAreas.get(i).getIcons().get(0).getPosition().x * scalingX, colorAreas.get(i).getIcons().get(0).getPosition().y * scalingY, 10 * scalingX, 10 * scalingY));
            }
            g2.drawImage(b, 0, 0, null);
        }
        return b;
    }
    
    /**
     * 
     * @param bi das Bild
     * @return macht eine deep copy
     */
    static BufferedImage deepCopy(BufferedImage bi) {
    	 ColorModel cm = bi.getColorModel();
    	 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    	 WritableRaster raster = bi.copyData(null);
    	 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    	}
}