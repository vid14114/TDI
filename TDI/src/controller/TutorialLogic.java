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

	@Override
	public void run() {
		try {
			// First step
			tv.setText("Starten eines Programmes, Schritt 1\nBitte waehlen durch drehen des TDIs den Musikplayer aus");
			while (!tdis.get(0).getIcons().get(0).getName().equals("MPC"))
				Thread.sleep(50); // Wait period
			// Second Step
			tv.setText("Starten eines Programmes, Schritt 2\nKippen Sie das TDI nach rechts um den Musikplayer zu fixieren");
			while(!tdis.get(0).isLocked())
				Thread.sleep(50);
			// Third
			
			int i = ProgramHandler.getRunningPrograms().size();
			tv.setText("Starten eines Programmes, Schritt 3\nBewegen Sie das TDI an den oberen Rand des Tisches. Die Applikation wird gestartet.");		
			while (i == ProgramHandler.getRunningPrograms().size())
				Thread.sleep(50);
			
			// Fourth
			TDI tdi = tdis.get(0).getState().equals(TDIState.inapp)? tdis.get(0): tdis.get(1);
			float pos = tdi.getPosition()[0];			
			tv.setText("Steuern der Musikplayer App, Schritt 1\nBewegen Sie das TDI, das in der Mitte des Tisches ist, nach unten um ein Lied zu starten.");
			while(tdi.getPosition()[0]+20 > pos)
				Thread.sleep(50);
			
			pos = tdi.getPosition()[1];
			tv.setText("Steuern der Musikplayer App, Schritt 2\nBewegen Sie das selbe TDI nach rechts um das nächste Lied auszuwählen.");
			while (tdi.getPosition()[1]+20 < pos)
				Thread.sleep(50);
			
			tv.setText("Steuern der Musikplayer App, Schritt 3\nBewegen Sie das selbe TDI nach links um das vorherige Lied auszuwählen.");
			while (tdi.getPosition()[1]+20 > pos)
				Thread.sleep(50);
			
			tv.setText("Manipulieren des Fensters, Schritt 1\nKippen sie das TDI nach rechts um aus der Appsteuerung in die Fenstersteuerung zu gelangen.");
			while (!tdi.getState().equals(TDIState.window))
				Thread.sleep(50);
			
			tv.setText("Manipulieren des Fensters, Schritt 2\nKippen Sie das TDI nach oben um das Programm zu maximieren.");			
			Thread.sleep(400);
						
			tv.setText("Manipulieren des Fensters, Schritt 3\nKippen Sie das TDI nach oben um das Programm wiederherzustellen.");
			Thread.sleep(400);
				
			tv.setText("Manipulieren des Fensters, Schritt 4\nBewegen Sie das TDI um die Position des Fensters zu verändern.");
			Thread.sleep(3000);
			
			tv.setText("Manipulieren des Fensters, Schritt 4\nFühren Sie beide TDIs zusammen und trennen Sie wieder um das Fenster zu skalieren.");
			while(!tdi.isScale()) 
				Thread.sleep(50);
			
			tv.setText("Manipulieren des Fensters, Schritt 5\nKippen sie das TDI nach links um die Applikation zu schließen.");			
			while(!ProgramHandler.getRunningPrograms().isEmpty())
				Thread.sleep(50);
		} catch (Exception e) {
			tv.setText("Ein Fehler ist aufgetreten, das Tutorial wurde beendet");
		}
	}
}