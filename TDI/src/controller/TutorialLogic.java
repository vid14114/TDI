package controller;

import java.util.ArrayList;
import java.util.Random;

import view.Icon;
import view.TDI;
import view.TutorialView;

public class TutorialLogic implements Runnable{
	private ArrayList<Icon> icons;
	private ArrayList<TDI> tdis;	
	TutorialView tv = new TutorialView();	
		
	public TutorialLogic(ArrayList<Icon> icons, ArrayList<TDI> tdis){
		this.icons = icons;
		this.tdis = tdis;		
	}
	
	@Override
	public void run() {
		//First Step
		Icon temp = tdis.get(0).getIcons().get(0);
		tv.setText("1/7\nPlease select Icon "+temp.getName()+ " at position "+temp.getPosition().x+","+temp.getPosition().y+" by turning assigned TDI until the icon is marked. "
				+"Select the icon tilting the TDI to the right");
	}

}