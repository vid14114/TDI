package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import model.ConfigLoader;
import model.Server;
import view.*;

/**
 * Implements Runnable interface. Is Master, is big.
 */
public class BigLogic implements Runnable{

	private ArrayList<TDI> tdis;
	private Server server;
	private ProgramHandler programHandler;
	
	/**
	 * The wallpaper
	 */
	private Wallpaper wallpaper;
	/**
	 * The commands that have to be executed.
	 */
	private ArrayList<String> commands;
	/**
	 * Lädt Dialog und Desktop configuration
	 * @param args
	 */
	public static void main(String[] args) {
		BigLogic bl=new BigLogic();
		ConfigLoader cl=new ConfigLoader();
		cl.loadIcons();		
		Server s=new Server();
		s.fullPose(bl.tdis);
	}
	
	/**
	 * The run method that is overriden
	 */
	public void run() {
		
	/*	while (true) {
			   if(commands.size() > 0){
			   
				   if(!tdis.contains(commands.get(0)));
			   
				   else{
			     
					   TDI temp = tdis.get(tdis.indexOf(commands.get(o)));
			     
					   if(temp.getPosition() != commands.get(0).getPosition()){
			      
						   
					   }
			    
					   if(temp.getRotation() != commands.get(0).getRotation()){
			      
						   if(programHandler.getRunningPrograms().isEmpty())
						   {
							   if(commands.get(0).getRotation()>0) // rotate clockwise
							   {
								   if(temp.getLocked()==false)
								   {
									   temp.setRotation(commands.get(0).getRotation());//new rotation
									   temp.rotateClockwise();								   
								   }
							   }
							   else //rotate counterwise
							   {
								   temp.setRotation(commands.get(0).getRotation());//new rotation
								   temp.rotateCounter();	
							   }
						   }
					   }
			  
				   }
			  
			   }
		}*/
	
	}

	/**
	 * 
	 * @param command
	 */
	public void addCommand(String command) {
		throw new UnsupportedOperationException();
	}
	
}
