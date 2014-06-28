package controller;

import java.util.ArrayList;
import java.util.Arrays;

import view.Icon;
import view.TDI;
import view.TDI.TDIState;
import view.TutorialView;

public class TutorialLogic implements Runnable {
	private final TutorialView tv = new TutorialView();
	private ArrayList<TDI> tdis;

	public TutorialLogic(ArrayList<TDI> tdis) {
		this.tdis = tdis;
	}
	
	private void firstStep() throws InterruptedException{
		tv.setText("Starten eines Programmes, Schritt 1\nBitte waehlen durch drehen des TDIs den Musikplayer aus");
		while (!tdis.get(0).getIcons().get(0).getName().equals("mpc.jar"))
			Thread.sleep(50); // Wait period
		tv.setText("Starten eines Programmes, Schritt 2\nKippen Sie das TDI nach rechts um den Musikplayer zu fixieren");
	}

	@Override
	public void run() {
		try {
			// First step
			firstStep();

			// Second Step			
			while(!tdis.get(0).isLocked()){
				if(!tdis.get(0).getIcons().get(0).getName().equals("mpc.jar")) 
					firstStep();
				Thread.sleep(50);
			}
			
			// Third			
			int i = ProgramHandler.getRunningPrograms().size();
			tv.setText("Starten eines Programmes, Schritt 3\nBewegen Sie das TDI an den oberen Rand des Tisches. Die Applikation wird gestartet.");		
			while (i == ProgramHandler.getRunningPrograms().size())
				Thread.sleep(50);
			tv.exit();
		} catch (Exception e) {
			tv.setText("Ein Fehler ist aufgetreten, das Tutorial wurde beendet");
		}
	}
}