package controller;

import java.util.ArrayList;
import java.util.Arrays;

import model.ConfigLoader;

import org.junit.Assert;
import org.junit.Test;

import view.Icon;
import view.TDI;
import view.TDI.TDIState;

class BigLogicTest {

    private BigLogic bl = new BigLogic();
    private ArrayList<Icon> icons = new ArrayList<Icon>();
    private ArrayList<TDI> tdis = new ArrayList<TDI>();
    private ArrayList<TDI> commands = new ArrayList<TDI>();
    private ConfigLoader cl = new ConfigLoader();

    /**
     * Test method for
     * {@link controller.BigLogic#run()}.
     */
    @Test
    public void testLinksDrehenD() {
        float[] rot1 = {100, 100, 100};
        float[] rot2 = {200, 100, 100}; //TODO: find right rot
        byte id = 49;
        TDI t1 = new TDI(id, 100, 100, 50, rot1);
        TDI t2 = new TDI(id, 100, 100, 50, rot2);
        ProgramHandler.closeAllPrograms();
        commands.add(t2);
        tdis.add(t1);

        icons = cl.loadIcons();
        bl.setCommands(commands);
        bl.setTdis(tdis);
        bl.splitIcons();
        bl.run();
        for (Icon i : t1.getIcons()) {
            System.out.println(i.getName());
        }
        Assert.assertEquals(t1.getIcons().get(0).getName(), "jre1.7.0_09");
        System.out.println(tdis.get(0).getRotation()[0]);
        Assert.assertTrue(Arrays.equals(tdis.get(0).getRotation(), t2.getRotation()));

    }

    /**
     * Test method for
     * {@link controller.BigLogic#run()}.
     */
    @Test
    public void testRechtsDrehenD() {
        float[] rot1 = {200, 100, 100};
        float[] rot2 = {100, 100, 100}; //TODO: find right rot
        byte id = 49;
        TDI t1 = new TDI(id, 100, 100, 50, rot1);
        TDI t2 = new TDI(id, 100, 100, 50, rot2);
        ProgramHandler.closeAllPrograms();
        commands.add(t2);
        tdis.add(t1);

        icons = cl.loadIcons();
        bl.setCommands(commands);
        bl.setTdis(tdis);
        bl.splitIcons();
        bl.run();

        Assert.assertEquals(t1.getIcons().get(0).getName(), "File System");
        Assert.assertTrue(Arrays.equals(tdis.get(0).getRotation(), t2.getRotation()));
    }

    /**
     * Test Links neigen im Window mode, es wird angenommen
     * dass nur das aktuell fokusierte Fenster offen ist (State => desktop)
     * {@link controller.BigLogic#run()}.
     */
    @Test
    public void testLinksNeigenW() {
        float[] rot1 = {100, 100, 100};
        float[] rot2 = {100, 200, 100}; //TODO: find right rot
        byte id = 49;
        TDI t1 = new TDI(id, 100, 100, 50, rot1);
        t1.setState(TDIState.window);
        TDI t2 = new TDI(id, 100, 100, 50, rot2);
        ProgramHandler.closeAllPrograms();
        commands.add(t2);
        tdis.add(t1);
        icons = cl.loadIcons();
        bl.setCommands(commands);
        bl.setTdis(tdis);
        bl.splitIcons();
        bl.run();
        System.out.println("tdis(0): " + tdis.get(0).getRotation()[1]);
        System.out.println("t2: " + t2.getRotation()[1]);
        System.out.println(ProgramHandler.getRunningPrograms().size());
        Assert.assertTrue(Arrays.equals(tdis.get(0).getRotation(), t2.getRotation()));
        Assert.assertEquals(tdis.get(0).getState(), TDIState.desktop);
        Assert.assertEquals(ProgramHandler.getRunningPrograms().size(), 0);
    }

    /**
     * Test method for
     * {@link controller.BigLogic#run()}.
     */
    @Test
    public void testRechtsNeigenNichtGelocktD() {
        float[] rot1 = {100, 200, 100};
        float[] rot2 = {100, 100, 100}; //TODO: find right rot
        byte id = 49;
        TDI t1 = new TDI(id, 100, 100, 50, rot1);
        t1.setState(TDIState.desktop);
        TDI t2 = new TDI(id, 100, 100, 50, rot2);
        ProgramHandler.closeAllPrograms();
        commands.add(t2);
        tdis.add(t1);
        if (t1.getLocked()) {
            t1.toggleLock();
        }

        icons = cl.loadIcons();
        bl.setCommands(commands);
        bl.setTdis(tdis);
        bl.splitIcons();
        bl.run();
        Assert.assertTrue(tdis.get(0).getLocked());
        //TODO check if green led is on
        Assert.assertEquals(tdis.get(0).getState(), TDIState.desktop);
        Assert.assertTrue(Arrays.equals(tdis.get(0).getRotation(), t2.getRotation()));
    }

    /**
     * Test method for
     * {@link controller.BigLogic#run()}.
     */
    @Test
    public void testRechtsNeigenGelocktD() {
        float[] rot1 = {100, 200, 100};
        float[] rot2 = {100, 100, 100}; //TODO: find right rot
        byte id = 49;
        TDI t1 = new TDI(id, 100, 100, 50, rot1);
        t1.setState(TDIState.desktop);
        TDI t2 = new TDI(id, 100, 100, 50, rot2);
        ProgramHandler.closeAllPrograms();
        commands.add(t2);
        tdis.add(t1);
        if (!t1.getLocked()) {
            t1.toggleLock();
        }

        icons = cl.loadIcons();
        bl.setCommands(commands);
        bl.setTdis(tdis);
        bl.splitIcons();
        bl.run();
        Assert.assertFalse(tdis.get(0).getLocked());
        //TODO check if green led is on
        Assert.assertEquals(tdis.get(0).getState(), TDIState.desktop);
        Assert.assertTrue(Arrays.equals(tdis.get(0).getRotation(), t2.getRotation()));
    }

    /**
     * Test method for
     * {@link controller.BigLogic#run()}.
     */
    @Test
    public void testObenNeigenNichtMaximiertW() {
        float[] rot1 = {100, 100, 200};
        float[] rot2 = {100, 100, 100}; //TODO: find right rot
        byte id = 49;
        TDI t1 = new TDI(id, 100, 100, 50, rot1);
        t1.setState(TDIState.window);
        TDI t2 = new TDI(id, 100, 100, 50, rot2);
        commands.add(t2);
        tdis.add(t1);

        icons = cl.loadIcons();
        bl.setCommands(commands);
        bl.setTdis(tdis);
        bl.splitIcons();
        bl.run();

        //Assert.assertEquals(ProgramHandler.); TODO get Maximised Programs
        Assert.assertEquals(ProgramHandler.getNonMinimized(), 1);
        Assert.assertTrue(Arrays.equals(tdis.get(0).getRotation(), t2.getRotation()));
    }

}
