package controller;

import static org.junit.Assert.*;

import org.junit.Test;


public class ProgramHandlerTest {
		 
		 private static String[] runningPrograms = {"1","2","3","4"};
		 String programPath="/usr/bin/firefox"; //not sure if actual path
         
		 public void test() {
			 
			 openProgramTest(programPath);
			 closeProgramTest();
			 closeAllProgramsTest();
			 toggleMinimizationTest();
			 minimizeAllProgramsTest();
			 restoreAllProgramsTest();
			 toggleMaximizationTest();	 
				 
			}
		 
        @Test
		public static void openProgramTest(String programPath) {
			
        	 assertTrue(programPath instanceof String);
		}

		/**
		 * Closes the program and removes it from the programs ArrayList
		 */
         @Test
		public static void closeProgramTest() {
			
		}

		/**
		 * Closes all programs
		 */
         @Test
		public static void closeAllProgramsTest() {
			
		}

		/**
		 * Toggles between minimize and maximize (gets the program from the programs array)
		 */
         @Test
		public static void toggleMinimizationTest() {
			
		}

		/**
		 * Minimizes all programs
		 */
         @Test
		public static void minimizeAllProgramsTest() {
			
		}

		/**
		 * Restores all programs
		 */
         @Test
		public static void restoreAllProgramsTest() {
			
		}

		/**
		 * Toggles between maximize and non-maximized
		 */
         @Test
		public static void toggleMaximizationTest() {
			
		}



}
