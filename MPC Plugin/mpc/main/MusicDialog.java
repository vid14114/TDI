package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MusicDialog extends JDialog implements ActionListener{
	/**
	 * Der Knopf um das vorherige Lied abzuspielen
	 */
	JButton previousButton = new JButton();
	
	/**
	 * Der Knopf um das nächste Lied abzuspielen
	 */
	JButton nextButton = new JButton();
	
	/**
	 * Der Knopf um zu pausieren und weiter zu spielen
	 */
	JButton togglePauseButton = new JButton();
	
	/**
	 * Das Lied das gerade spielt
	 */
	JLabel musicLabel = new JLabel();
	
	/**
	 * Die ID des TDIs
	 */
	float id;
	
	/**
	 * X-Positon des TDIs
	 */
	float x;
	
	/**
	 * Y-Position des TDIs
	 */
	float y;
	
	/**
	 * Z-Position des TDIs
	 */
	float z;
	
	/**
	 * Die Kompensation damit nicht jede kleinste Bewegung als Befehl zählt
	 */
	int compensation;
	
	/**
	 * Der Musikdialog
	 */
	public MusicDialog(){
		super();
		id=-1;
		x=-1;
		y=-1;
		z=-1;
		compensation = 5;
		setTitle("Tangible Musicplayer");
		setSize(600,600);
		setLocation(400, 200);
		setContentPane(musicPane());
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
	}
	
	/**
	 * @return der Inhalt das MusikDialogs
	 */
	public JPanel musicPane(){
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());
		
		previousButton.setText("Previous");
		nextButton.setText("Next");
		togglePauseButton.setText("Pause/Play");
		musicLabel.setText("DJ Anil - Anihilate them!");
		
		musicLabel.setHorizontalAlignment(JLabel.CENTER);
		
		previousButton.setFocusPainted(false);
		nextButton.setFocusPainted(false);
		togglePauseButton.setFocusPainted(false);
		
		previousButton.addActionListener(this);
		nextButton.addActionListener(this);
		togglePauseButton.addActionListener(this);
		
		main.add(previousButton,BorderLayout.EAST);
		main.add(nextButton,BorderLayout.WEST);
		main.add(musicLabel,BorderLayout.CENTER);
		main.add(togglePauseButton,BorderLayout.SOUTH);
		return main;
	}
	
	/**
	 * Schaut ob sich das TDI bewegt
	 * @param id um das TDi zu identifizieren
	 * @param x die neue X-Position
	 * @param y die neue Y-Position
	 * @param z die neue Z-Position
	 */
	public void TDIMoved(float id,float x,float y,float z){
		if(this.id==-1){
			this.id=id;
			this.x = x;
			this.y = y;
			this.z = z;
			return; 
		}
		try {
		if(this.id==id){
			//move right
			if(x>this.x+compensation){
				this.x=x;
				System.out.println("mpc next");
				Runtime.getRuntime().exec("mpc next");
			}
			//move left
			if(x<this.x-compensation){
				this.x=x;
				System.out.println("mpc prev");
				Runtime.getRuntime().exec("mpc prev");
			}
			//toggle
			if(y>this.y+compensation){
				this.y=y;
				System.out.println("mpc toggle");
				Runtime.getRuntime().exec("mpc toggle");
			}
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Falls ein Knopf gedrückt wird
	 */
	@Override
	public void actionPerformed(ActionEvent actionPerformed) {
		try {
		if(actionPerformed.getSource() == previousButton){
				System.out.println("mpc prev");
				Runtime.getRuntime().exec("mpc prev");
		}
		if(actionPerformed.getSource() == nextButton){
			System.out.println("mpc next");
			Runtime.getRuntime().exec("mpc next");
		}
		if(actionPerformed.getSource() == togglePauseButton){
			System.out.println("mpc toggle");
			Runtime.getRuntime().exec("mpc toggle");
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
