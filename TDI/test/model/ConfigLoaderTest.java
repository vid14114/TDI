package model;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;

import controller.Executor;

public class ConfigLoaderTest {

    // @Before
    // public void prepareEnvironment() throws IOException{
    // /* Creates the TDI directory and copies all files in the desktop folder
    // to a backup folder in TDI
    // * This can then be used for the test
    // */
    // TDIDirectories.createDirectories();
    // new File(TDIDirectories.TDI_HOME+"/Desktop").mkdir();
    // Runtime.getRuntime().exec(new String []{"cp","-r",
    // System.getProperty("user.home")+"/Desktop/*",TDIDirectories.TDI_HOME+"/Desktop/"});
    // deleteDesktop();
    // }
    //
    // private void deleteDesktop() throws IOException{
    // Runtime.getRuntime().exec(new String[]{"rm","-r",
    // System.getProperty("user.home")+"/Desktop/*"});
    // }
    //
    // @After
    // public void closeEnviroment() throws IOException{
    // /*
    // * Reverts the desktop back to its usual form and deletes the backup
    // folder
    // */
    // deleteDesktop();
    // Runtime.getRuntime().exec(new String
    // []{"cp","-r",TDIDirectories.TDI_HOME+"/Desktop/*",System.getProperty("user.home")+"/Desktop/"});
    // Runtime.getRuntime().exec(new String[]{"rm","-r",
    // TDIDirectories.TDI_HOME+"/Desktop/*"});
    // }

    @Test
    public void testLoadWallpaper() throws IOException {
        BufferedImage shouldWallpaper = ImageIO.read(new File(Executor
                .getBackground())); // Insert your wallpaper path here --> xfce
        ConfigLoader cf = new ConfigLoader();
        // Testing whether the wallpaper is correct and if it is, whether it is
        // backed up
        Assert.assertEquals(shouldWallpaper, cf.loadWallpaper());
        Assert.assertEquals(shouldWallpaper, ImageIO.read(new File(
                TDIDirectories.TDI_RESTORE).listFiles()[0]));
    }

    // @Test
    // public void testLoadIcons() throws IOException{
    // ConfigLoader cf = new ConfigLoader();
    // ArrayList <Icon> icons = null;
    //
    // //building the environment for the first test
    // ConfigLoader.setIconsRc(new File("resources/ConfigLoaderTest/icons1"));
    // Runtime.getRuntime().exec(new String
    // []{"cp","-r","resources/ConfigLoaderTest/desktop1/*",System.getProperty("user.home")+"/Desktop/"});
    // icons = cf.loadIcons();
    //
    // //Asserting for each icon in the file
    // Assert.assertEquals(15, icons.size());
    // Assert.assertSame("xdg-open ~", icons.get(icons.indexOf(new Icon("Home",
    // null))).getExecPath());
    // Assert.assertSame("xdg-open trash:///", icons.get(icons.indexOf(new
    // Icon("Rubbish Bin", null))).getExecPath());
    //
    // //Building and testing second icon test
    // deleteDesktop();
    // ConfigLoader.setIconsRc(new File("resources/ConfigLoaderTest/icons2"));
    // Runtime.getRuntime().exec(new String
    // []{"cp","-r","resources/ConfigLoaderTest/desktop2/*",System.getProperty("user.home")+"/Desktop/"});
    // icons = cf.loadIcons();
    //
    // //Assertions begin
    // Assert.assertEquals(12, icons.size());
    //
    // //Building and testing third icon test
    // deleteDesktop();
    // ConfigLoader.setIconsRc(new File("resources/ConfigLoaderTest/icons3"));
    // Runtime.getRuntime().exec(new String
    // []{"cp","-r","resources/ConfigLoaderTest/desktop3/*",System.getProperty("user.home")+"/Desktop/"});
    // icons = cf.loadIcons();
    //
    // //Assertions begin
    // Assert.assertEquals(11, icons.size());
    // }

    // @Test
    public void testScreenSize() {
        ConfigLoader cf = new ConfigLoader();
        try {
            Runtime.getRuntime().exec(
                    new String[]{"xrandr", "-s", "1366x768"});
            Thread.sleep(2000);
            Runtime.getRuntime().exec(new String[]{"xfdesktop", "--reload"});
            Thread.sleep(2000);
            Assert.assertEquals(new Point(1366, 768), cf.loadScreensize());
            // Change to 1360x768
            Runtime.getRuntime().exec(
                    new String[]{"xrandr", "-s", "1360x768"});
            Thread.sleep(2000);
            Runtime.getRuntime().exec(new String[]{"xfdesktop", "--reload"});
            Thread.sleep(2000);
            Assert.assertEquals(new Point(1360, 768), cf.loadScreensize());
            // Change to 1024x768
            Runtime.getRuntime().exec(
                    new String[]{"xrandr", "-s", "1028x768"});
            Thread.sleep(2000);
            Runtime.getRuntime().exec(new String[]{"xfdesktop", "--reload"});
            Thread.sleep(2000);
            Assert.assertEquals(new Point(1024, 768), cf.loadScreensize());
            //
            Runtime.getRuntime().exec(
                    new String[]{"xrandr", "-s", "800x600"});
            Thread.sleep(2000);
            Runtime.getRuntime().exec(new String[]{"xfdesktop", "--reload"});
            Thread.sleep(2000);
            Assert.assertEquals(new Point(800, 600), cf.loadScreensize());
            // Change
            Runtime.getRuntime().exec(
                    new String[]{"xrandr", "-s", "1366x768"});
            Thread.sleep(2000);
            Runtime.getRuntime().exec(new String[]{"xfdesktop", "--reload"});
            Thread.sleep(2000);
            Assert.assertEquals(new Point(1366, 768), cf.loadScreensize());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testPluginMethods() throws IOException {
        new BufferedWriter(new FileWriter(TDIDirectories.TDI_PREFERENCE, false))
                .close(); // Here I make sure the preference file is empty
        // First I get the plugins
        Object res[][] = new ConfigLoader().getPlugins();
        // Assertions
        Assert.assertEquals(5, res.length);
        Assert.assertEquals("lo.jar", (String) res[0][0]);
        Assert.assertEquals(false, (Boolean) res[0][1]);
        int falses = 0;
        for (Object[] plugin : res)
            if (!(boolean) plugin[1])
                falses++;
        Assert.assertEquals(5, falses);

        // Select 4 Plugins and save them to the preference file
        String[] savedPlugins = new String[4];
        for (int i = 0; i < 4; i++)
            savedPlugins[i] = (String) res[i][0];
        new ConfigLoader().savePlugins(savedPlugins);

        // Get the plugins angain and check if the plugins were saved
        res = new ConfigLoader().getPlugins();
        Assert.assertEquals(5, res.length);
        Assert.assertEquals("lo.jar", (String) res[0][0]);
        Assert.assertEquals(true, (Boolean) res[0][1]);
        int trues = 0;
        for (Object[] plugin : res)
            if ((boolean) plugin[1])
                trues++;
        Assert.assertEquals(4, trues);
    }

}
