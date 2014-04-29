/**
 *
 */
package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.TDIDirectories;

import org.junit.Before;
import org.junit.Test;

/**
 * @author abideen
 */
public class ExecutorTest {
    BufferedImage image;

    /**
     * Test method for {@link controller.Executor#getBackground()}.
     *
     * @throws IOException
     */
    @Before
    public void testGetBackground() throws IOException {
        TDIDirectories.createDirectories();
        String path = Executor.getBackground();
        image = ImageIO.read(new File(path));
    }

    /**
     * Test method for {@link controller.Executor#saveBackground(java.awt.image.BufferedImage)}.
     */
    @Test
    public void testSaveBackground() {
        Executor.saveBackground(image);
        image.getGraphics().drawString("IHPIHOIUHIOUH", 2, 120);
        Executor.saveBackground(image);
    }

}
