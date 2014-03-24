package view;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Wallpaper {

    /**
     * The image of the wallpaper
     */
    private final BufferedImage background;
    /**
     * The block size of each icon
     */
    private final int panelSize;
    private final int ratio;

    private final int realWidth;
    private final int realHeight;

    private final int scalingX;
    private final int scalingY;

    public Wallpaper(BufferedImage background, int blockSize, int panelSize, int ratio, Point screensize) {
        this.background = background;
        this.panelSize = panelSize;
        this.ratio = ratio;
        realWidth = blockSize + (4 * ratio);
        realHeight = blockSize + (3 * ratio);
        scalingX = background.getWidth() / screensize.x;
        scalingY = background.getHeight() / screensize.y;
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
        BufferedImage b = background;
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
            int lastX = lastPosX * realWidth - firstX;
            int lastY = (lastPosY + 1) * realHeight - firstY;
            if (firstPosY != 0)
                lastY -= panelSize;
            g2.fill(new Rectangle2D.Float(firstX * scalingX, firstY * scalingY, lastX * scalingX, lastY * scalingY));

            //mark the selected Icon
            int selectedPosX = colorAreas.get(i).getIcons().get(0).getPosition().y;
            int selectedPosY = colorAreas.get(i).getIcons().get(0).getPosition().x;

            int posX = selectedPosX * realWidth;
            int posY = selectedPosY * realHeight + ratio;
            if (i == 0)
                g2.setColor(Color.cyan);
            if (i == 1)
                g2.setColor(Color.magenta);
            g2.fill(new Rectangle2D.Float(posX * scalingX, posY * scalingY, realWidth * scalingX, realHeight * scalingY));
            g2.drawImage(b, 0, 0, null);
        }
        return b;
    }
}