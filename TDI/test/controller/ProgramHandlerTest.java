/**
 * 
 */
package controller;

import java.util.ArrayList;

import model.ConfigLoader;

import org.junit.Assert;
import org.junit.Test;

import view.Icon;

/**
 * @author abideen
 * 
 */
public class ProgramHandlerTest {

	@Test
	public void testAll() {
		testOpenProgram();
		// Should be three
		Assert.assertEquals(3, ProgramHandler.getRunningPrograms().size());
		testCloseProgram();
		testRestoreLeft();
		testRestoreRight();
		testRestoreRight();
		testRestoreLeft();
		testRestoreRight();
		testMinimizeAllPrograms();
		testRestoreAllPrograms();
		testMinimizeAllPrograms();
		testRestoreAllPrograms();
		Assert.assertEquals(2, ProgramHandler.getRunningPrograms().size());
		testCloseProgram();
		Assert.assertEquals(0, ProgramHandler.getRunningPrograms().size());
		testRestoreLeft();
	}

	/**
	 * Test method for
	 * {@link controller.ProgramHandler#openProgram(java.lang.String[])}.
	 */
	public void testOpenProgram() {
		ArrayList<Icon> icons = new ConfigLoader().loadIcons();
		// Execute three programs and see whether they were started
		System.out.println("Starting programs with exec:");
		System.out.println(icons.get(1).getExecPath()[1]);
		System.out.println(icons.get(4).getExecPath()[1]);
		System.out.println(icons.size());
		System.out.println(icons.get(5).getExecPath()[1]);
		ProgramHandler.openProgram(icons.get(1));
		ProgramHandler.openProgram(icons.get(4));
		ProgramHandler.openProgram(icons.get(5));
	}

	/**
	 * Test method for {@link controller.ProgramHandler#closeProgram()}.
	 */

	public void testCloseProgram() {
		ProgramHandler.closeProgram();
	}

	/**
	 * Test method for {@link controller.ProgramHandler#closeAllPrograms()}.
	 */

	public void testCloseAllPrograms() {
		ProgramHandler.closeAllPrograms();
	}

	/**
	 * Test method for {@link controller.ProgramHandler#restoreRight()}.
	 */

	public void testRestoreRight() {
		ProgramHandler.restoreRight();
	}

	/**
	 * Test method for {@link controller.ProgramHandler#restoreLeft()}.
	 */

	public void testRestoreLeft() {
		ProgramHandler.restoreLeft();
	}

	/**
	 * Test method for {@link controller.ProgramHandler#minimize()}.
	 */

	public void testMinimize() {
		ProgramHandler.minimize();
	}

	/**
	 * Test method for
	 * {@link controller.ProgramHandler#moveProgram(int, int, int, int)}.
	 */

	public void testMoveProgram() {
		ProgramHandler.moveProgram(10, 30);
	}

	/**
	 * Test method for {@link controller.ProgramHandler#minimizeAllPrograms()}.
	 */

	public void testMinimizeAllPrograms() {
		ProgramHandler.minimizeAllPrograms();
	}

	/**
	 * Test method for {@link controller.ProgramHandler#restoreAllPrograms()}.
	 */

	public void testRestoreAllPrograms() {
		ProgramHandler.restoreAllPrograms();
	}

	/**
	 * Test method for {@link controller.ProgramHandler#toggleMaximization()}.
	 */

	public void testToggleMaximization() {
		ProgramHandler.toggleMaximization();
	}

}
