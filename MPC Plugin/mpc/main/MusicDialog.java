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
	JButton previousButton = new JButton();
	JButton nextButton = new JButton();
	JButton togglePauseButton = new JButton();
	JLabel musicLabel = new JLabel();
	
	float id;
	float x;
	float y;
	float z;
	int compensation;
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
				Runtime.getRuntime().exec("mpc next");
			}
			//move left
			if(x<this.x-compensation){
				this.x=x;
				Runtime.getRuntime().exec("mpc prev");
			}
			//toggle
			if(y>this.y+compensation){
				this.y=y;
				Runtime.getRuntime().exec("mpc toggle");
			}
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent actionPerformed) {
		try {
		if(actionPerformed.getSource() == previousButton){
				Runtime.getRuntime().exec("mpc prev");
		}
		if(actionPerformed.getSource() == nextButton){
			Runtime.getRuntime().exec("mpc next");
		}
		if(actionPerformed.getSource() == togglePauseButton){
			Runtime.getRuntime().exec("mpc toggle");
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
