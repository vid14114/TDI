package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import model.ConfigLoader;
import model.PluginTableModel;

import org.junit.Assert;
import org.junit.Test;

import view.Icon;
import view.TDI;
import view.TDI.TDIState;
import view.TDIDialog;
import view.Wallpaper;

public class BigLogicTest {

	BigLogic bl= new BigLogic();
	ArrayList<Icon> icons=new ArrayList <Icon>();
	ArrayList<TDI> tdis = new ArrayList <TDI>();
	ArrayList<TDI> commands= new ArrayList <TDI>();
	ConfigLoader cl = new ConfigLoader();
	/**
	 * Test method for
	 * {@link controller.BigLogic#run()}.
	 */
	@Test
	public void testLinksDrehenD()
	{
		float[] rot1 = {1,1,1};
		float[] rot2 = {1,2,1}; //TODO: find right rot 
		byte id= 49;
		TDI t1 = new TDI(id, 1, 1, 0, rot1);	
		TDI t2 = new TDI(id, 1, 1, 0, rot2);
		ProgramHandler.closeAllPrograms();
		commands.add(t2);
		tdis.add(t1);
		
		icons = cl.loadIcons();
		bl.setCommands(commands);
		bl.setTdis(tdis);
		bl.splitIcons();
		bl.run();
		Assert.assertEquals(t1.getIcons().get(0).getName(),"gimp");
		Assert.assertEquals(tdis.get(0).getRotation(),t2.getRotation());
	}
	/**
	 * Test method for
	 * {@link controller.BigLogic#run()}.
	 */
	@Test
	public void testRechtsDrehenD()
	{
		float[] rot1 = {1,1,1};
		float[] rot2 = {1,-1,1}; //TODO: find right rot 
		byte id= 0;
		TDI t1 = bl.getTdis().get(0);	
		TDI t2 = bl.getTdis().get(1);	
		t1.setRotation(rot1);
		t2.setRotation(rot2);
		Point p1 = new Point (5,3);
		Icon i1 = new Icon("mozilla",p1);
		Point p2 = new Point (6,1);
		Icon i2 = new Icon("gimp",p2);
		ProgramHandler.closeAllPrograms();
		tdis.add(t1);
		commands.add(t2);
		
	/*	bl.icons=icons;
		bl.tdis=tdis;
		bl.commands=commands;
		*/
		bl.run();
		Assert.assertEquals(t1.getIcons().get(0).getName(),"mozilla");
		Assert.assertEquals(tdis.get(0).getRotation(),t2.getRotation());
	}
	/**
	 * Test method for
	 * {@link controller.BigLogic#run()}.
	 */
	@Test
	public void testLinksNeigenW()
	{
		float[] rot1 = {1,1,1};
		float[] rot2 = {-1,1,1}; //TODO: find right rot 
		byte id= 0;
		TDI t1 = new TDI(id, 1, 1, 0, rot1);	
		TDI t2 = new TDI(id, 1, 1, 0, rot2);	
		Point p1 = new Point (5,3);
		Icon i1 = new Icon("mozilla",p1);
		
		ProgramHandler.closeAllPrograms();
		ProgramHandler.openProgram(i1);
		t1.setState(TDIState.window);
		tdis.add(t1);
		commands.add(t2);
		icons.add(i1);
		
	/*	bl.tdis=tdis;
		bl.commands=commands;
		bl.icons=icons;
		*/
		bl.run();
		Assert.assertEquals(ProgramHandler.getRunningPrograms(),0);
		Assert.assertEquals(tdis.get(0).getRotation(),t2.getRotation());
		Assert.assertEquals(tdis.get(0).getState(),TDIState.desktop);
	}
	/**
	 * Test method for
	 * {@link controller.BigLogic#run()}.
	 */
	@Test
	public void testRechtsNeigenNichtGelocktD()
	{
		float[] rot1 = {1,1,1};
		float[] rot2 = {2,1,1}; //TODO: find right rot 
		byte id= 0;
		TDI t1 = new TDI(id, 1, 1, 0, rot1);	
		TDI t2 = new TDI(id, 1, 1, 0, rot2);	
		Point p1 = new Point (5,3);
		Icon i1 = new Icon("mozilla",p1);
		
		ProgramHandler.closeAllPrograms();
		if(t1.getLocked())
		{
			t1.toggleLock();
		}
		tdis.add(t1);
		icons.add(i1);
		commands.add(t2);
		
	/*	bl.tdis=tdis;
		bl.commands=commands;
		bl.icons=icons;*/
		
		bl.run();
		Assert.assertTrue(tdis.get(0).getLocked());
		//TODO check if green led is on
		Assert.assertEquals(tdis.get(0).getState(),TDIState.desktop);
		Assert.assertEquals(tdis.get(0).getRotation(),t2.getRotation());
	}
	/**
	 * Test method for
	 * {@link controller.BigLogic#run()}.
	 */
	@Test
	public void testRechtsNeigenGelocktD()
	{
		float[] rot1 = {1,1,1};
		float[] rot2 = {2,1,1}; //TODO: find right rot 
		byte id= 0;
		TDI t1 = new TDI(id, 1, 1, 0, rot1);	
		TDI t2 = new TDI(id, 1, 1, 0, rot2);	
		Point p1 = new Point (5,3);
		Icon i1 = new Icon("mozilla",p1);
		
		ProgramHandler.closeAllPrograms();
		if(t1.getLocked()==false)
		{
			t1.toggleLock();
		}
		icons.add(i1);
		tdis.add(t1);
		commands.add(t2);
		
/*		bl.tdis=tdis;
		bl.commands=commands;
		bl.icons=icons;*/
		
		bl.run();
		Assert.assertTrue(!tdis.get(0).getLocked());
		//TODO check if green led is not on
		Assert.assertEquals(tdis.get(0).getState(),TDIState.desktop);
		Assert.assertEquals(tdis.get(0).getRotation(),t2.getRotation());
	}
	/**
	 * Test method for
	 * {@link controller.BigLogic#run()}.
	 */
	@Test
	public void testObenNeigenNichtMaximiertW()
	{
		float[] rot1 = {1,1,1};
		float[] rot2 = {1,1,2}; //TODO: find right rot 
		byte id= 0;
		TDI t1 = new TDI(id, 1, 1, 0, rot1);	
		TDI t2 = new TDI(id, 1, 1, 0, rot2);	
		Point p1 = new Point (5,3);
		Icon i1 = new Icon("mozilla",p1);
		
		ProgramHandler.closeAllPrograms();
		ProgramHandler.openProgram(i1);
		t1.setState(TDIState.window);
		icons.add(i1);
		tdis.add(t1);
		commands.add(t2);
		
	/*	bl.tdis=tdis;
		bl.commands=commands;
		bl.icons=icons;*/
		
		bl.run();
		//Assert.assertEquals(ProgramHandler.); TODO get Maximised Programs
		Assert.assertEquals(ProgramHandler.getNonMinimized(),1);
		Assert.assertEquals(tdis.get(0).getRotation(),t2.getRotation());
	}
	/**
	 * Test method for
	 * {@link controller.BigLogic#run()}.
	 */
	@Test
	public void testObenNeigenMaximiertW()
	{
		float[] rot1 = {1,1,1};
		float[] rot2 = {1,1,2}; //TODO: find right rot 
		byte id= 0;
		TDI t1 = new TDI(id, 1, 1, 0, rot1);	
		TDI t2 = new TDI(id, 1, 1, 0, rot2);	
		Point p1 = new Point (5,3);
		Icon i1 = new Icon("mozilla",p1);
		
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
		Assert.assertEquals(ProgramHandler.getNonMinimized(),1);
		Assert.assertEquals(tdis.get(0).getState(), TDIState.window);
		Assert.assertEquals(tdis.get(0).getRotation(),t2.getRotation());
	}
	/**
	 * Test method for
	 * {@link controller.BigLogic#run()}.
	 */
	@Test
	public void testUntenNeigenMehrereFensterOffenW()
	{
		float[] rot1 = {1,1,1};
		float[] rot2 = {1,1,2}; //TODO: find right rot 
		byte id= 0;
		TDI t1 = new TDI(id, 1, 1, 0, rot1);	
		TDI t2 = new TDI(id, 1, 1, 0, rot2);	
		Point p1 = new Point (5,3);
		Icon i1 = new Icon("mozilla",p1);
		Point p2 = new Point (5,3);
		Icon i2 = new Icon("explorer",p1);
		
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
		
		Assert.assertEquals(ProgramHandler.getNonMinimized(),1);
		Assert.assertEquals(tdis.get(0).getRotation(),t2.getRotation());
		Assert.assertEquals(tdis.get(0).getState(),TDIState.desktop);
	}
	/**
	 * Test method for
	 * {@link controller.BigLogic#run()}.
	 */
	@Test
	public void testUntenNeigenEinFensterOffenW()
	{
		float[] rot1 = {1,1,1};
		float[] rot2 = {1,1,2}; //TODO: find right rot 
		byte id= 0;
		TDI t1 = new TDI(id, 1, 1, 0, rot1);	
		TDI t2 = new TDI(id, 1, 1, 0, rot2);	
		Point p1 = new Point (5,3);
		Icon i1 = new Icon("mozilla",p1);
		
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
		
		float[] newPos={1,1,1}; //TODO find right position
		Assert.assertEquals(ProgramHandler.getNonMinimized(),0);
		Assert.assertEquals(tdis.get(0).getPosition(), newPos);
		Assert.assertEquals(tdis.get(0).getRotation(),t2.getRotation());
		Assert.assertEquals(tdis.get(0).getState(),TDIState.window);
	}
	/**
	 * Test method for
	 * {@link controller.BigLogic#run()}.
	 */
	@Test
	public void testBewegenXNichtTBNichtLockKeinFensterD()
	{
		float[] rot1 = {1,1,1};
		float[] rot2 = {1,1,2}; //TODO: find right rot 
		byte id= 0;
		TDI t1 = new TDI(id, 1, 1, 0, rot1);	
		TDI t2 = new TDI(id, 5, 1, 0, rot2);	
		Point p1 = new Point (5,3);
		Icon i1 = new Icon("mozilla",p1);
		
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
		
		float[] newPos={1,1,1}; //TODO find right position
		Assert.assertEquals(ProgramHandler.getNonMinimized(),0);
		Assert.assertEquals(tdis.get(0).getPosition(), newPos);
		Assert.assertEquals(tdis.get(0).getRotation(),t2.getRotation());
		Assert.assertEquals(tdis.get(0).getState(),TDIState.window);
	}

}
