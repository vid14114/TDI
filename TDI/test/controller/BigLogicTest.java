package controller;

import model.ConfigLoader;
import view.Icon;
import view.TDI;
import view.TDI.TDIState;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

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

    /**
     * Test method for
     * {@link controller.BigLogic#run()}.
     */
    @Test
    public void testObenNeigenMaximiertW() {
        float[] rot1 = {1, 1, 1};
        float[] rot2 = {1, 1, 2}; //TODO: find right rot
        byte id = 0;
        TDI t1 = new TDI(id, 1, 1, 0, rot1);
        TDI t2 = new TDI(id, 1, 1, 0, rot2);
        Point p1 = new Point(5, 3);
        Icon i1 = new Icon("mozilla", p1);

        ProgramHandler.closeAllPrograms();
        ProgramHandler.openProgram(i1);
        ProgramHandler.toggleMaximization();//must maximise it!
        t1.setState(TDIState.window);
        tdis.add(t1);
        icons.add(i1);
        commands.add(t2);

	/*	bl.tdis=tdis;
        bl.commands=commands;
		bl.icons=icons;*/

        bl.run();

        //Assert.assertEquals(ProgramHandler.); TODO get "wiederhergestellte" Programs(?)
        Assert.assertEquals(ProgramHandler.getNonMinimized(), 1);
        Assert.assertEquals(tdis.get(0).getState(), TDIState.window);
        Assert.assertEquals(tdis.get(0).getRotation(), t2.getRotation());
    }

    /**
     * Test method for
     * {@link controller.BigLogic#run()}.
     */
    @Test
    public void testUntenNeigenMehrereFensterOffenW() {
        float[] rot1 = {1, 1, 1};
        float[] rot2 = {1, 1, 2}; //TODO: find right rot
        byte id = 0;
        TDI t1 = new TDI(id, 1, 1, 0, rot1);
        TDI t2 = new TDI(id, 1, 1, 0, rot2);
        Point p1 = new Point(5, 3);
        Icon i1 = new Icon("mozilla", p1);
        Point p2 = new Point(5, 3);
        Icon i2 = new Icon("explorer", p1);

        ProgramHandler.closeAllPrograms();
        ProgramHandler.openProgram(i1);
        ProgramHandler.openProgram(i2);
        icons.add(i1);
        icons.add(i2);
        t1.setState(TDIState.window);
        tdis.add(t1);
        commands.add(t2);

	/*	bl.tdis=tdis;
		bl.commands=commands;
		bl.icons=icons;*/

        bl.run();

        Assert.assertEquals(ProgramHandler.getNonMinimized(), 1);
        Assert.assertEquals(tdis.get(0).getRotation(), t2.getRotation());
        Assert.assertEquals(tdis.get(0).getState(), TDIState.desktop);
    }

    /**
     * Test method for
     * {@link controller.BigLogic#run()}.
     */
    @Test
    public void testUntenNeigenEinFensterOffenW() {
        float[] rot1 = {1, 1, 1};
        float[] rot2 = {1, 1, 2}; //TODO: find right rot
        byte id = 0;
        TDI t1 = new TDI(id, 1, 1, 0, rot1);
        TDI t2 = new TDI(id, 1, 1, 0, rot2);
        Point p1 = new Point(5, 3);
        Icon i1 = new Icon("mozilla", p1);

        ProgramHandler.closeAllPrograms();
        ProgramHandler.openProgram(i1);
        icons.add(i1);
        t1.setState(TDIState.window);
        tdis.add(t1);
        commands.add(t2);
		
	/*	bl.tdis=tdis;
		bl.commands=commands;
		bl.icons=icons;*/

        bl.run();

        float[] newPos = {1, 1, 1}; //TODO find right position
        Assert.assertEquals(ProgramHandler.getNonMinimized(), 0);
        Assert.assertEquals(tdis.get(0).getPosition(), newPos);
        Assert.assertEquals(tdis.get(0).getRotation(), t2.getRotation());
        Assert.assertEquals(tdis.get(0).getState(), TDIState.window);
    }

    /**
     * Test method for
     * {@link controller.BigLogic#run()}.
     */
    @Test
    public void testBewegenXNichtTBNichtLockKeinFensterD() {
        float[] rot1 = {1, 1, 1};
        float[] rot2 = {1, 1, 2}; //TODO: find right rot
        byte id = 0;
        TDI t1 = new TDI(id, 1, 1, 0, rot1);
        TDI t2 = new TDI(id, 5, 1, 0, rot2);
        Point p1 = new Point(5, 3);
        Icon i1 = new Icon("mozilla", p1);

        ProgramHandler.closeAllPrograms();
        ProgramHandler.openProgram(i1);
        icons.add(i1);
        t1.setState(TDIState.desktop);
        tdis.add(t1);
        commands.add(t2);
		
	/*	bl.tdis=tdis;
		bl.commands=commands;
		bl.icons=icons;*/

        bl.run();

        float[] newPos = {1, 1, 1}; //TODO find right position
        Assert.assertEquals(ProgramHandler.getNonMinimized(), 0);
        Assert.assertEquals(tdis.get(0).getPosition(), newPos);
        Assert.assertEquals(tdis.get(0).getRotation(), t2.getRotation());
        Assert.assertEquals(tdis.get(0).getState(), TDIState.window);
    }

}
