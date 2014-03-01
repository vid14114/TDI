package controller;

import java.util.ArrayList;
import view.Icon;
import view.TDI;
import view.TDI.TDIState;
import view.TutorialView;

public class TutorialLogic implements Runnable{
	private volatile ArrayList<TDI> tdis;	
	TutorialView tv = new TutorialView();	
		
	public TutorialLogic(ArrayList<TDI> tdis){
		this.tdis = tdis;		
	}
	
	@Override
	public void run() {
		//First Step
		Icon temp = tdis.get(0).getIcons().get((tdis.get(0).getIcons().size()-1));
		//First step
		tv.setText("1/11 - Desktop Mode\nPlease select Icon "+temp.getName()+ " at position "+temp.getPosition().x+","
				+temp.getPosition().y+" by turning assigned TDI until the icon is marked. ");
		while(!tdis.get(0).getIcons().get(0).equals(temp)); //Wait period
		//Second Step
		tv.setText("2/11 - Desktop Mode\nSelect the Icon by tilting the TDI to the right");
		while(!tdis.get(0).getIcons().get(0).equals(temp) && !tdis.get(0).getLocked()); //Wait period
		//Third
		tv.setText("3/11 - Desktop Mode, Locked\nMove the Icon to another position");
		while(tdis.get(0).getIcons().get(0).getPosition() == temp.getPosition());
		//Fourth
		tv.setText("4/11 Desktop Mode, Locked\nVery Good, you have successfully moved the TDI. TDIs can take on many different modes. As you might habe noticed on the "
				+ "progress that we are currently teaching you Desktop Mode. When you tilt the TDI to the right it becomes locked. When the TDI becomes locked "
				+ "you are able to move icons to other positions and if a program is open, you are able to close, minimize and maximize it. Now unlock the TDI by tilting it to the right again");
		while(tdis.get(0).getLocked());
		int i = ProgramHandler.getRunningPrograms().size();
		tv.setText("5/11 Desktop Mode\nWell done! The greed LED turns on when the TDI is locked and turn back off when the TDI is not locked. Now move the TDI to the bottom/top of your working platform to start a program");
		while(i == ProgramHandler.getRunningPrograms().size());
		tv.setText("6/11 In-App Mode\nAs you started the program you might have noticed that a one TDI stayed on the bottom/top of the table, this TDI is now in Taskbar Mode. The TDI which moved to the middle of your working platform"
				+ "is in In-App Mode. In-App mode is a featured mode where developers can write \"plugins\" to support TDI funtionalities in various programs. Note that this program only manipulates windows but to be able to manipulate the "
				+ "programs themselves, you will need a so called plugin. To exit In-App Mode tilt the TDI to the right");//TODO check correctness
		for(i = 0; i < tdis.size() && !tdis.get(i).getState().equals(TDIState.inapp); i++);
		while(tdis.get(i).getState().equals(TDIState.inapp));
		tv.setText("7/11 Window Mode\nThe TDI is now in Window Mode. In Window Mode you can manipulate the window itself: move, minimize --> tilt down, maximize --> tilt up and close --> tilt down. "
				+ "Let us try a few functionalities. Move the window by moving the TDI");
		TDI tdi = tdis.get(i);
		while(tdi.getPosition().equals(tdis.get(i).getPosition()));
		tv.setText("8/11 Window Mode\nNice move! Tilt the TDI to the bottom to minimize the program");
		if(ProgramHandler.isDesktopMode())
			tv.setText("9/11 Desktop Mode\nThe TDI is now back in Desktop Mode. If all opened programs are minimized then the TDI goes back to Desktop Mode. Now it is responsible for the icons again."
					+ "Titl the TDI which is in Taskbar Mode(Red LED on) up to restore all minimized programs");
		else
			tv.setText("9/11 In-App Mode\nThe TDI is now back in In-App Mode. If you minimize a program and the next opened program at the background is automatically focused, your TDI enters In-App Mode."
					+ "Titl the TDI which is in Taskbar Mode(Red LED on) up to restore all minimized programs");
		while(ProgramHandler.getNonMinimized() > 0);
		tv.setText("10/11 In-App Mode\nVery good! To switch between indivdual programs, all you have to do is turn the Taskbar-TDI right or left "
				+ "and to minimize all programs you tilt the Taskbar-TDI down. Let us try to resize the currently focused window, this is quite complicate so read well."
				+ "To do that, move the Taskbar TDI close the In-App Mode TDI and wait for a second. The TDIs will enter into scale mode.");
		while(!tdi.getIsScale());
		tv.setText("11/11 Scale Mode\nTo resize the program, all you have to do is move the TDIs. Play around with resize mode a bit");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tv.setText("Well done! You have finished the tutorial. Now you know all you need to be able to use the TDIs. Plugins can be downloaded, if available to manipulate more programs in In-App Mode. Read the manual for more info");
	}

}