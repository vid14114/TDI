package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class Wallpaper {

    /**
     * The image of the wallpaper
     */
    private final BufferedImage background;
    /**
     * The block size of each icon
     */
    private float panelSize;
    private int ratio;

    private int realWidth;
    private int realHeight;

    private float scalingX;
    private float scalingY;
    
    private int blockSize;

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
     * Marks the area of the tdi with the specified color.
     * <p>
     * As parameters we give the array of ids (for the tdis) and the hashtable
     * of selected icons.
     *
     * @param colorAreas The TDIs with their icons, and we will generate the markedArea
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
    
    static BufferedImage deepCopy(BufferedImage bi) {
    	 ColorModel cm = bi.getColorModel();
    	 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    	 WritableRaster raster = bi.copyData(null);
    	 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    	}
}